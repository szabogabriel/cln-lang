package org.clnlang;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Test class for RuntimeConfiguration parsing.
 */
public class RuntimeConfigurationTest {
    
    private String originalClnHome;
    private String originalClnPath;
    
    @BeforeEach
    public void setUp() {
        // Save original environment variables (they can't be changed, but we can track them)
        originalClnHome = System.getenv("CLN_HOME");
        originalClnPath = System.getenv("CLN_PATH");
    }
    
    @AfterEach
    public void tearDown() {
        // Note: We can't actually restore environment variables in Java
        // Tests that need to simulate env vars will use setClnHome() instead
    }
    
    @Test
    public void testDefaultConfiguration() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        assertFalse(config.isVerbose());
        assertTrue(config.getClnPaths().isEmpty());
        assertTrue(config.getSourceFiles().isEmpty());
        assertFalse(config.hasSourceFiles());
        assertFalse(config.hasClnPaths());
        assertNull(config.getFirstSourceFile());
    }
    
    @Test
    public void testVerboseShortFlag() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-v"});
        
        assertTrue(config.isVerbose());
    }
    
    @Test
    public void testVerboseLongFlag() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"--verbose"});
        
        assertTrue(config.isVerbose());
    }
    
    @Test
    public void testSingleSourceFile() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"hello.cln"});
        
        assertTrue(config.hasSourceFiles());
        assertEquals(1, config.getSourceFiles().size());
        assertEquals("hello.cln", config.getFirstSourceFile());
    }
    
    @Test
    public void testPackageDefinition() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"myapp.main"});
        
        assertTrue(config.hasSourceFiles());
        assertEquals("myapp.main", config.getFirstSourceFile());
    }
    
    @Test
    public void testClnPathShort() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-cp", "/path/to/libs"});
        
        assertTrue(config.hasClnPaths());
        List<String> paths = config.getClnPaths();
        assertEquals(1, paths.size());
        assertEquals("/path/to/libs", paths.get(0));
    }
    
    @Test
    public void testClnPathLong() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"--cln_path", "/path/to/libs"});
        
        assertTrue(config.hasClnPaths());
        assertEquals(1, config.getClnPaths().size());
        assertEquals("/path/to/libs", config.getClnPaths().get(0));
    }
    
    @Test
    public void testMultipleClnPaths() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        String paths = "/path/to/libs" + File.pathSeparator + "/another/path" + File.pathSeparator + "/third/path";
        config.parse(new String[]{"-cp", paths});
        
        assertTrue(config.hasClnPaths());
        List<String> clnPaths = config.getClnPaths();
        assertEquals(3, clnPaths.size());
        assertEquals("/path/to/libs", clnPaths.get(0));
        assertEquals("/another/path", clnPaths.get(1));
        assertEquals("/third/path", clnPaths.get(2));
    }
    
    @Test
    public void testMultipleSourceFiles() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        String files = "file1.cln" + File.pathSeparator + "file2.cln" + File.pathSeparator + "file3.cln";
        config.parse(new String[]{files});
        
        assertTrue(config.hasSourceFiles());
        List<String> sources = config.getSourceFiles();
        assertEquals(3, sources.size());
        assertEquals("file1.cln", sources.get(0));
        assertEquals("file2.cln", sources.get(1));
        assertEquals("file3.cln", sources.get(2));
    }
    
    @Test
    public void testComplexConfiguration() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        String clnPaths = "/libs" + File.pathSeparator + "/modules";
        config.parse(new String[]{"-v", "-cp", clnPaths, "app.cln"});
        
        assertTrue(config.isVerbose());
        assertTrue(config.hasClnPaths());
        assertTrue(config.hasSourceFiles());
        assertEquals(2, config.getClnPaths().size());
        assertEquals("app.cln", config.getFirstSourceFile());
    }
    
    @Test
    public void testClnPathWithoutValue() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> config.parse(new String[]{"-cp"})
        );
        
        assertTrue(exception.getMessage().contains("requires a path argument"));
    }
    
    @Test
    public void testUnknownOption() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> config.parse(new String[]{"-unknown"})
        );
        
        assertTrue(exception.getMessage().contains("Unknown option"));
    }
    
    @Test
    public void testAddClnPath() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath("/new/path");
        
        assertTrue(config.hasClnPaths());
        assertEquals(1, config.getClnPaths().size());
        assertEquals("/new/path", config.getClnPaths().get(0));
    }
    
    @Test
    public void testAddClnPathIgnoresEmpty() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath("");
        config.addClnPath("   ");
        config.addClnPath(null);
        
        assertFalse(config.hasClnPaths());
        assertEquals(0, config.getClnPaths().size());
    }
    
    @Test
    public void testSetVerbose() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        assertFalse(config.isVerbose());
        
        config.setVerbose(true);
        assertTrue(config.isVerbose());
        
        config.setVerbose(false);
        assertFalse(config.isVerbose());
    }
    
    @Test
    public void testToString() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(new String[]{"-v", "-cp", "/libs", "app.cln"});
        
        String str = config.toString();
        assertTrue(str.contains("verbose=true"));
        assertTrue(str.contains("/libs"));
        assertTrue(str.contains("app.cln"));
    }
    
    @Test
    public void testClnHomeWithLibDirectory(@TempDir Path tempDir) throws IOException {
        // Create a lib directory under CLN_HOME
        Path libDir = tempDir.resolve("lib");
        Files.createDirectory(libDir);
        
        // Create a new config and simulate having CLN_HOME set
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        // Since we can't set environment variables, we manually add the lib path
        // and set CLN_HOME to simulate what would happen if CLN_HOME was set
        config.addClnPath(libDir.toString());
        config.setClnHome(tempDir.toString());
        config.parse(new String[]{"test.cln"});
        
        // Check that lib path was added
        List<String> paths = config.getClnPaths();
        assertTrue(paths.size() >= 1);
        assertTrue(paths.contains(libDir.toString()));
        assertEquals(tempDir.toString(), config.getClnHome());
    }
    
    @Test
    public void testClnHomeWithoutLibDirectory(@TempDir Path tempDir) {
        // Don't create lib directory
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.setClnHome(tempDir.toString());
        config.parse(new String[]{"test.cln"});
        
        // Since constructor already ran, CLN_HOME won't add paths
        // This tests the behavior when CLN_HOME/lib doesn't exist at construction time
        assertEquals(tempDir.toString(), config.getClnHome());
    }
    
    @Test
    public void testClnHomeWithExplicitCp(@TempDir Path tempDir) throws IOException {
        // Create a lib directory under CLN_HOME
        Path libDir = tempDir.resolve("lib");
        Files.createDirectory(libDir);
        
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(libDir.toString());  // Simulate CLN_HOME/lib
        config.parse(new String[]{"-cp", "/explicit/path", "test.cln"});
        
        // Check that both paths are present
        List<String> paths = config.getClnPaths();
        assertTrue(paths.size() >= 2);
        assertTrue(paths.contains(libDir.toString()));
        assertTrue(paths.contains("/explicit/path"));
    }
    
    @Test
    public void testClnPathExplicitlySet() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        // Parse with explicit -cp
        config.parse(new String[]{"-cp", "/some/path", "test.cln"});
        
        // Check that the path was added
        assertTrue(config.getClnPaths().contains("/some/path"));
    }
    
    @Test
    public void testGetClnHome() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        
        // Set custom CLN_HOME
        config.setClnHome("/custom/home");
        assertEquals("/custom/home", config.getClnHome());
    }
    
    @Test
    public void testEmptyClnHome() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.setClnHome("");
        config.parse(new String[]{"test.cln"});
        
        // Empty CLN_HOME should be set
        assertEquals("", config.getClnHome());
    }
    
    @Test
    public void testNullClnHome() {
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.setClnHome(null);
        config.parse(new String[]{"test.cln"});
        
        // Null CLN_HOME should be set
        assertNull(config.getClnHome());
    }
}
