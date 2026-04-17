package org.clnlang.startup;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.clnlang.RuntimeConfiguration;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.exception.ClnException;
import org.clnlang.lib.StandardLibrary;
import org.clnlang.persistance.ClnLoader;
import org.clnlang.runtime.execution.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for StartupContext with package-based startup mode.
 */
class PackageBasedStartupTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    /**
     * Helper method to create a .cln file in a package.
     */
    private Path createPackageFile(Path rootDir, String packagePath, String filename, 
                                   String packageName, boolean includeMain) throws IOException {
        // Create package directory
        Path packageDir = rootDir.resolve(packagePath);
        Files.createDirectories(packageDir);
        
        // Use filename to make helper function names unique
        String baseName = filename.replace(".cln", "");
        
        StringBuilder content = new StringBuilder();
        content.append("package ").append(packageName).append(";\n\n");
        
        if (includeMain) {
            content.append("(var int result = 42) main() {\n");
            content.append("    return;\n");
            content.append("}\n\n");
        }
        
        content.append("(var int value = 10) helper").append(baseName).append("() {\n");
        content.append("    return;\n");
        content.append("}\n");
        
        Path file = packageDir.resolve(filename);
        Files.writeString(file, content.toString());
        return file;
    }
    
    @Test
    void testPackageWithSingleMainFunction(@TempDir Path tempDir) throws Exception {
        // Create package with main function
        createPackageFile(tempDir, "myapp", "Main.cln", "myapp", true);
        createPackageFile(tempDir, "myapp", "Helper.cln", "myapp", false);
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"myapp"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Verify mode
        assertEquals(ClnLoader.StartupMode.PACKAGE, startupContext.getMode());
        
        // Prepare execution context
        startupContext.prepareExecutionContext();
        
        // Find main function
        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        assertNotNull(mainFunction);
        assertEquals("myapp", mainFunction.getPackageName());
    }
    
    @Test
    void testNestedPackageWithMainFunction(@TempDir Path tempDir) throws Exception {
        // Create nested package with main function
        createPackageFile(tempDir, "com/example/app", "Main.cln", "com.example.app", true);
        createPackageFile(tempDir, "com/example/app", "Helper.cln", "com.example.app", false);
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"com.example.app"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Verify mode
        assertEquals(ClnLoader.StartupMode.PACKAGE, startupContext.getMode());
        
        // Prepare execution context
        startupContext.prepareExecutionContext();
        
        // Find main function
        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        assertNotNull(mainFunction);
    }
    
    @Test
    void testPackageNotFound(@TempDir Path tempDir) throws Exception {
        // Create a package but request a different one
        createPackageFile(tempDir, "myapp", "Main.cln", "myapp", true);
        
        // Setup configuration with wrong package name
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"nonexistent"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context - should fail
        StartupContext startupContext = new StartupContext(config, registry, false);
        
        ClnException exception = assertThrows(ClnException.class, () -> {
            startupContext.initialize();
        });
        
        assertTrue(exception.getMessage().contains("Package not found"));
    }
    
    @Test
    void testPackageWithNoMainFunction(@TempDir Path tempDir) throws Exception {
        // Create package without main function
        createPackageFile(tempDir, "myapp", "Helper1.cln", "myapp", false);
        createPackageFile(tempDir, "myapp", "Helper2.cln", "myapp", false);
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"myapp"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        startupContext.prepareExecutionContext();
        
        // Should fail to find main function
        ClnException exception = assertThrows(ClnException.class, () -> {
            startupContext.findMainFunction();
        });
        
        assertTrue(exception.getMessage().contains("No 'main' function found"));
        assertTrue(exception.getMessage().contains("myapp"));
    }
    
    @Test
    void testPackageWithMultipleMainFunctions(@TempDir Path tempDir) throws Exception {
        // Create package with multiple main functions
        createPackageFile(tempDir, "myapp", "Main1.cln", "myapp", true);
        createPackageFile(tempDir, "myapp", "Main2.cln", "myapp", true);
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"myapp"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Should fail because multiple main functions when trying to load
        Exception exception = assertThrows(Exception.class, () -> {
            startupContext.prepareExecutionContext();
        });
        
        assertTrue(exception.getMessage().contains("main is already defined") || 
                   exception.getMessage().contains("Multiple 'main' functions"));
    }
    
    @Test
    void testMultiplePackagesSpecifiedFails(@TempDir Path tempDir) throws Exception {
        // Create two packages
        createPackageFile(tempDir, "app1", "Main.cln", "app1", true);
        createPackageFile(tempDir, "app2", "Main.cln", "app2", true);
        
        // Setup configuration with multiple packages
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        config.parse(new String[]{"app1", "app2"});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Should fail during initialization
        StartupContext startupContext = new StartupContext(config, registry, false);
        
        ClnException exception = assertThrows(ClnException.class, () -> {
            startupContext.initialize();
        });
        
        assertTrue(exception.getMessage().contains("Only one package"));
    }
    
    @Test
    void testMixedFileAndPackageArgumentsFails(@TempDir Path tempDir) throws Exception {
        // Create package and file
        createPackageFile(tempDir, "myapp", "Main.cln", "myapp", true);
        Path file = tempDir.resolve("test.cln");
        Files.writeString(file, "(var int result = 0) main() { return; }");
        
        // Setup configuration with mixed arguments
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", tempDir.toString()});
        
        ClnException exception = assertThrows(ClnException.class, () -> {
            config.parse(new String[]{file.toString(), "myapp"});
            
            Registry registry = new Registry();
            new StandardLibrary().registerAll(registry);
            
            StartupContext startupContext = new StartupContext(config, registry, false);
            startupContext.initialize();
        });
        
        assertTrue(exception.getMessage().contains("Mixed file and package arguments"));
    }
}
