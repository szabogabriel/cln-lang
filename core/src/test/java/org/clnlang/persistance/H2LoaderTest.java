package org.clnlang.persistance;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.clnlang.ClnRuntime;
import org.clnlang.RuntimeConfiguration;
import org.clnlang.persistance.ClnLoader.StartupMode;
import org.clnlang.runtime.execution.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class H2LoaderTest {

    // Each test gets an isolated in-memory DB via a unique name + DB_CLOSE_DELAY=-1
    private static final String JDBC_URL = "jdbc:h2:mem:h2loadertest;DB_CLOSE_DELAY=-1";
    private static final String DRIVER   = "org.h2.Driver";

    @BeforeEach
    void setUp() throws Exception {
        Class.forName(DRIVER);
        dropSchema();
    }

    @AfterEach
    void tearDown() throws Exception {
        dropSchema();
    }

    private void dropSchema() throws Exception {
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS CLN_SOURCE");
        }
    }

    // -------------------------------------------------------------------------
    // Schema
    // -------------------------------------------------------------------------

    @Test
    void schemaIsCreatedOnFirstLoadSources() throws Exception {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        loader.loadSources(new Registry());

        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT id, package, source, createdAt, updatedAt, version FROM CLN_SOURCE")) {
            assertFalse(rs.next(), "Table should be empty after loading an empty DB");
        }
    }

    @Test
    void schemaCreationIsIdempotent() throws Exception {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        // Two calls must not throw
        assertDoesNotThrow(() -> {
            loader.loadSources(new Registry());
            loader.loadSources(new Registry());
        });
    }

    // -------------------------------------------------------------------------
    // loadSources
    // -------------------------------------------------------------------------

    @Test
    void loadSourcesReturnsZeroForEmptyDatabase() throws Exception {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        int count = loader.loadSources(new Registry());
        assertEquals(0, count);
    }

    @Test
    void loadSourcesCompilesAndRegistersValidSource() throws Exception {
        String clnSource = """
                package myapp;

                (var int result = 0) main() {
                    result = 42;
                    return;
                }
                """;
        insertSource("myapp", clnSource);

        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        Registry registry = new Registry();
        int count = loader.loadSources(registry);

        assertEquals(1, count);
        assertTrue(registry.hasPackage("myapp"));
    }

    @Test
    void loadSourcesLoadsMultipleRows() throws Exception {
        String src1 = "package pkg1;\n\nhelper1() { return; }\n";
        String src2 = "package pkg2;\n\nhelper2() { return; }\n";
        insertSource("pkg1", src1);
        insertSource("pkg2", src2);

        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("pkg1"), false);
        Registry registry = new Registry();
        int count = loader.loadSources(registry);

        assertEquals(2, count);
        assertTrue(registry.hasPackage("pkg1"));
        assertTrue(registry.hasPackage("pkg2"));
    }

    @Test
    void loadSourcesSkipsInvalidSourceWithoutThrowing() throws Exception {
        insertSource("broken", "this is @@ not valid CLN source");

        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("broken"), false);
        Registry registry = new Registry();
        int count = loader.loadSources(registry);

        assertEquals(0, count);
    }

    @Test
    void loadSourcesSkipsBrokenRowButContinuesWithValid() throws Exception {
        insertSource("broken", "not valid @@@@");
        insertSource("myapp", "package myapp;\nhelper() { return; }\n");

        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        Registry registry = new Registry();
        int count = loader.loadSources(registry);

        assertEquals(1, count);
        assertTrue(registry.hasPackage("myapp"));
    }

    // -------------------------------------------------------------------------
    // getSourceFiles / getSupportedStartupMode
    // -------------------------------------------------------------------------

    @Test
    void getSourceFilesReturnsPackageReferencesForEachArg() {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp", "util.helper"), false);
        List<ClnSourceFile> files = loader.getSourceFiles();

        assertEquals(2, files.size());
        assertTrue(files.get(0).isPackage());
        assertEquals("myapp", files.get(0).getName());
        assertTrue(files.get(1).isPackage());
        assertEquals("util.helper", files.get(1).getName());
    }

    @Test
    void getSourceFilesStripsClnExtension() {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("hello.cln"), false);
        List<ClnSourceFile> files = loader.getSourceFiles();

        assertEquals(1, files.size());
        assertTrue(files.get(0).isPackage());
        assertEquals("hello", files.get(0).getName());
    }

    @Test
    void startupModeIsPackageForPackageArg() {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of("myapp"), false);
        assertEquals(StartupMode.PACKAGE, loader.getSupportedStartupMode());
    }

    @Test
    void startupModeThrowsWhenNoSourceArgsProvided() {
        H2Loader loader = new H2Loader(JDBC_URL, DRIVER, List.of(), false);
        assertThrows(RuntimeException.class, loader::getSupportedStartupMode);
    }

    // -------------------------------------------------------------------------
    // Driver loading
    // -------------------------------------------------------------------------

    @Test
    void nullDriverFallsBackToH2DefaultAndLoadsSuccessfully() throws Exception {
        // null driver → H2Loader uses DEFAULT_DRIVER (org.h2.Driver) which is on classpath
        H2Loader loader = new H2Loader(JDBC_URL, null, List.of("myapp"), false);
        assertDoesNotThrow(() -> loader.loadSources(new Registry()));
    }

    @Test
    void blankDriverFallsBackToH2Default() throws Exception {
        H2Loader loader = new H2Loader(JDBC_URL, "  ", List.of("myapp"), false);
        assertDoesNotThrow(() -> loader.loadSources(new Registry()));
    }

    @Test
    void unknownDriverClassThrowsDescriptiveException() {
        H2Loader loader = new H2Loader(JDBC_URL, "com.nonexistent.Driver", List.of("myapp"), false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> loader.loadSources(new Registry()));
        assertTrue(ex.getMessage().contains("com.nonexistent.Driver"));
    }

    // -------------------------------------------------------------------------
    // ClnLoaderFactory integration
    // -------------------------------------------------------------------------

    @Test
    void factoryCreatesH2LoaderWhenClnPathEnvIsJdbcUrl() {
        ClnLoader loader = ClnLoaderFactory.fromEnvironment(null, JDBC_URL, null,
                List.of("myapp"), false, DRIVER);
        assertInstanceOf(H2Loader.class, loader);
    }

    @Test
    void factoryCreatesH2LoaderWhenCpArgIsJdbcUrl() {
        ClnLoader loader = ClnLoaderFactory.fromEnvironment(null, null, JDBC_URL,
                List.of("myapp"), false, DRIVER);
        assertInstanceOf(H2Loader.class, loader);
    }

    @Test
    void factoryPrefersClnPathEnvOverCpArgForJdbcDetection() {
        ClnLoader loader = ClnLoaderFactory.fromEnvironment(null, JDBC_URL, "/some/path",
                List.of("myapp"), false, DRIVER);
        assertInstanceOf(H2Loader.class, loader);
    }

    @Test
    void factoryCreatesFileSystemLoaderForRegularPath() {
        ClnLoader loader = ClnLoaderFactory.fromEnvironment(null, "/some/path", null,
                List.of("hello.cln"), false, null);
        assertInstanceOf(FileSystemLoader.class, loader);
    }

    // -------------------------------------------------------------------------
    // RuntimeConfiguration integration
    // -------------------------------------------------------------------------

    @Test
    void runtimeConfigParsesDbDriverArgAndCreatesH2Loader() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cdd", DRIVER, "-cp", JDBC_URL, "myapp"});

        assertInstanceOf(H2Loader.class, config.getClnLoader());
    }

    @Test
    void runtimeConfigLongDbDriverArgCreatesH2Loader() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"--cln-db-driver", DRIVER, "-cp", JDBC_URL, "myapp"});

        assertInstanceOf(H2Loader.class, config.getClnLoader());
    }

    @Test
    void runtimeConfigMissingDriverArgThrows() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        assertThrows(IllegalArgumentException.class,
                () -> config.parse(new String[]{"-cdd"}));
    }

    // -------------------------------------------------------------------------
    // End-to-end: execute a CLN program stored in H2
    // -------------------------------------------------------------------------

    @Test
    void endToEndExecutesProgramFromDatabase() throws Exception {
        String clnSource = """
                package myapp;

                (var int result = 0) main() {
                    result = 7;
                    return;
                }
                """;
        insertSource("myapp", clnSource);

        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cdd", DRIVER, "-cp", JDBC_URL, "myapp"});

        int exitCode = ClnRuntime.execute(config);
        assertEquals(7, exitCode);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Bootstraps the schema (via a dummy load) and inserts a source row.
     */
    private void insertSource(String pkg, String source) throws Exception {
        // Ensure table exists
        new H2Loader(JDBC_URL, DRIVER, List.of(), false).loadSources(new Registry());

        try (Connection conn = DriverManager.getConnection(JDBC_URL);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO CLN_SOURCE (package, source) VALUES (?, ?)")) {
            ps.setString(1, pkg);
            ps.setString(2, source);
            ps.executeUpdate();
        }
    }
}
