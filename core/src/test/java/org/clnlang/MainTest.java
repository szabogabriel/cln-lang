package org.clnlang;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Main class command-line interface.
 */
class MainTest {
    
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
     * Helper method to create a simple valid .cln file with main function
     */
    private Path createValidClnFile(Path dir, String filename) throws IOException {
        String content = """
            package test;
            
            (var int result = 0) main() {
                result = 42;
                return;
            }
            """;
        Path file = dir.resolve(filename);
        Files.writeString(file, content);
        return file;
    }
    
    /**
     * Helper method to create a .cln file without main function
     */
    private Path createClnFileWithoutMain(Path dir, String filename) throws IOException {
        String content = """
            package test;
            
            struct Point {
                var int x;
                var int y;
            };
            """;
        Path file = dir.resolve(filename);
        Files.writeString(file, content);
        return file;
    }
    
    @Test
    void testExplicitFileWithMainFunction(@TempDir Path tempDir) throws Exception {
        Path clnFile = createValidClnFile(tempDir, "test.cln");
        
        String[] args = {clnFile.toString()};
        Main.run(args);
        
        // Should execute without errors
        assertEquals("", errContent.toString());
    }
    
    @Test
    void testExplicitFileWithVerboseShortFlag(@TempDir Path tempDir) throws Exception {
        Path clnFile = createValidClnFile(tempDir, "test.cln");
        
        String[] args = {"-v", clnFile.toString()};
        Main.run(args);
        
        String output = outContent.toString();
        assertTrue(output.contains("Loading file: " + clnFile.toString()));
        assertTrue(output.contains("Parsing completed successfully."));
        assertTrue(output.contains("Compilation completed successfully."));
        assertTrue(output.contains("Program executed, context populated."));
        assertTrue(output.contains("Found 'main' function."));
        assertTrue(output.contains("=== Abstract Syntax Tree"));
    }
    
    @Test
    void testExplicitFileWithVerboseLongFlag(@TempDir Path tempDir) throws Exception {
        Path clnFile = createValidClnFile(tempDir, "test.cln");
        
        String[] args = {"--verbose", clnFile.toString()};
        Main.run(args);
        
        String output = outContent.toString();
        assertTrue(output.contains("Loading file: " + clnFile.toString()));
        assertTrue(output.contains("Parsing completed successfully."));
        assertTrue(output.contains("Compilation completed successfully."));
        assertTrue(output.contains("Found 'main' function."));
    }
    
    @Test
    void testFileNotFound() {
        String[] args = {"nonexistent.cln"};
        
        Main.ClnException exception = assertThrows(Main.ClnException.class, () -> {
            Main.run(args);
        });
        
        assertEquals("File not found: nonexistent.cln", exception.getMessage());
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Error: File not found: nonexistent.cln"));
    }
    
    @Test
    void testNoMainFunctionFound(@TempDir Path tempDir) throws Exception {
        Path clnFile = createClnFileWithoutMain(tempDir, "test.cln");
        
        String[] args = {clnFile.toString()};
        
        Main.ClnException exception = assertThrows(Main.ClnException.class, () -> {
            Main.run(args);
        });
        
        assertEquals("No 'main' function found in the program.", exception.getMessage());
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Error: No 'main' function found in the program."));
    }
    
    @Test
    void testNoMainFunctionWithVerbose(@TempDir Path tempDir) throws Exception {
        Path clnFile = createClnFileWithoutMain(tempDir, "test.cln");
        
        String[] args = {"-v", clnFile.toString()};
        
        Main.ClnException exception = assertThrows(Main.ClnException.class, () -> {
            Main.run(args);
        });
        
        assertEquals("No 'main' function found in the program.", exception.getMessage());
        String output = outContent.toString();
        String errorOutput = errContent.toString();
        
        // Verbose messages should appear before the error
        assertTrue(output.contains("Loading file:"));
        assertTrue(output.contains("Parsing completed successfully."));
        assertTrue(errorOutput.contains("Error: No 'main' function found in the program."));
    }
    
    @Test
    void testAutoDetectClnFile(@TempDir Path tempDir) throws Exception {
        // Change to temp directory and create a .cln file
        String originalDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tempDir.toString());
            Path clnFile = createValidClnFile(tempDir, "auto.cln");
            
            String[] args = {};
            Main.run(args);
            
            // Should execute without errors
            assertEquals("", errContent.toString());
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
    
    @Test
    void testAutoDetectClnFileWithVerbose(@TempDir Path tempDir) throws Exception {
        // Change to temp directory and create a .cln file
        String originalDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tempDir.toString());
            Path clnFile = createValidClnFile(tempDir, "auto.cln");
            
            String[] args = {"-v"};
            Main.run(args);
            
            String output = outContent.toString();
            assertTrue(output.contains("No file specified, using:"));
            assertTrue(output.contains("auto.cln"));
            assertTrue(output.contains("Found 'main' function."));
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
    
    @Test
    void testNoClnFileFoundInDirectory(@TempDir Path tempDir) {
        // Change to temp directory without any .cln files
        String originalDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tempDir.toString());
            
            String[] args = {};
            
            Main.ClnException exception = assertThrows(Main.ClnException.class, () -> {
                Main.run(args);
            });
            
            assertEquals("No .cln file found in current directory.", exception.getMessage());
            String errorOutput = errContent.toString();
            assertTrue(errorOutput.contains("Error: No .cln file found in current directory."));
            assertTrue(errorOutput.contains("Usage: java -jar cln.jar"));
            assertTrue(errorOutput.contains("-v, --verbose"));
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
    
    @Test
    void testVerboseFlagPosition(@TempDir Path tempDir) throws Exception {
        // Test that verbose flag works in different positions
        Path clnFile = createValidClnFile(tempDir, "test.cln");
        
        // Reset streams
        outContent.reset();
        errContent.reset();
        
        // Test with -v before filename
        String[] args1 = {"-v", clnFile.toString()};
        Main.run(args1);
        assertTrue(outContent.toString().contains("Loading file:"));
        
        // Reset streams
        outContent.reset();
        errContent.reset();
        
        // Test with --verbose before filename
        String[] args2 = {"--verbose", clnFile.toString()};
        Main.run(args2);
        assertTrue(outContent.toString().contains("Loading file:"));
        
        // Reset streams
        outContent.reset();
        errContent.reset();
        
        // Test with -v after filename
        String[] args3 = {clnFile.toString(), "-v"};
        Main.run(args3);
        assertTrue(outContent.toString().contains("Loading file:"));
    }
    
    @Test
    void testNonVerboseModeIsQuiet(@TempDir Path tempDir) throws Exception {
        Path clnFile = createValidClnFile(tempDir, "test.cln");
        
        String[] args = {clnFile.toString()};
        Main.run(args);
        
        // In non-verbose mode, there should be no output
        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
    }
    
    @Test
    void testParsingErrorHandling(@TempDir Path tempDir) throws Exception {
        // Create a .cln file with syntax errors
        String invalidContent = """
            package test;
            
            this is not valid syntax
            """;
        Path clnFile = tempDir.resolve("invalid.cln");
        Files.writeString(clnFile, invalidContent);
        
        String[] args = {clnFile.toString()};
        
        Main.ClnException exception = assertThrows(Main.ClnException.class, () -> {
            Main.run(args);
        });
        
        assertTrue(exception.getMessage().contains("Parsing failed") || exception.getMessage().contains("errors"));
        String errorOutput = errContent.toString();
        assertTrue(errorOutput.contains("Parsing failed") || errorOutput.contains("errors"));
    }
    
    @Test
    void testRealTestProgramFile() throws Exception {
        // Test with the actual test_program.cln if it exists
        File testFile = new File("src/test/resources/test_program.cln");
        if (testFile.exists()) {
            String[] args = {testFile.getPath()};
            Main.run(args);
            
            // Should execute without errors
            assertEquals("", errContent.toString());
        }
    }
    
    @Test
    void testRealTestProgramFileWithVerbose() throws Exception {
        // Test with the actual test_program.cln if it exists
        File testFile = new File("src/test/resources/test_program.cln");
        if (testFile.exists()) {
            String[] args = {"-v", testFile.getPath()};
            Main.run(args);
            
            String output = outContent.toString();
            assertTrue(output.contains("Found 'main' function."));
            assertTrue(output.contains("Package: main"));
        }
    }
}
