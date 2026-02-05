package org.clnlang.startup;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.clnlang.RuntimeConfiguration;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.exception.ClnException;
import org.clnlang.lib.StandardLibrary;
import org.clnlang.runtime.execution.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for StartupContext with file-based startup mode.
 */
class FileBasedStartupTest {
    
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
     * Helper method to create a simple .cln file with main function in default package.
     */
    private Path createDefaultPackageFile(Path dir, String filename, String functionBody) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("(var int result = 0) main() {\n");
        content.append("    ").append(functionBody).append("\n");
        content.append("    return;\n");
        content.append("}\n");
        
        Path file = dir.resolve(filename);
        Files.writeString(file, content.toString());
        return file;
    }
    
    /**
     * Helper method to create a .cln file with a package declaration.
     */
    private Path createPackagedFile(Path dir, String filename, String packageName) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("package ").append(packageName).append(";\n\n");
        content.append("(var int result = 0) main() {\n");
        content.append("    result = 42;\n");
        content.append("    return;\n");
        content.append("}\n");
        
        Path file = dir.resolve(filename);
        Files.writeString(file, content.toString());
        return file;
    }
    
    /**
     * Helper method to create a file without main function.
     */
    private Path createHelperFile(Path dir, String filename) throws IOException {
        // Use filename to make function names unique
        String baseName = filename.replace(".cln", "");
        StringBuilder content = new StringBuilder();
        content.append("(var int result = 42) ").append(baseName).append("() {\n");
        content.append("    return;\n");
        content.append("}\n");
        
        Path file = dir.resolve(filename);
        Files.writeString(file, content.toString());
        return file;
    }
    
    @Test
    void testSingleFileWithMainInDefaultPackage(@TempDir Path tempDir) throws Exception {
        // Create a file with main function in default package
        Path file1 = createDefaultPackageFile(tempDir, "test.cln", "result = 42;");
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        config.parse(new String[]{file1.toString()});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Verify mode
        assertEquals(StartupContext.StartupMode.FILES, startupContext.getMode());
        
        // Prepare execution context
        startupContext.prepareExecutionContext();
        
        // Find main function
        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        assertNotNull(mainFunction);
    }
    
    @Test
    void testMultipleFilesWithSingleMainInDefaultPackage(@TempDir Path tempDir) throws Exception {
        // Create multiple files, only one with main
        Path file1 = createDefaultPackageFile(tempDir, "main.cln", "result = 42;");
        Path file2 = createHelperFile(tempDir, "helper.cln");
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        config.parse(new String[]{file1.toString(), file2.toString()});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Verify mode
        assertEquals(StartupContext.StartupMode.FILES, startupContext.getMode());
        
        // Prepare execution context
        startupContext.prepareExecutionContext();
        
        // Find main function
        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        assertNotNull(mainFunction);
    }
    
    @Test
    void testFileWithPackageDeclarationFails(@TempDir Path tempDir) throws Exception {
        // Create a file with package declaration
        Path file1 = createPackagedFile(tempDir, "test.cln", "mypackage");
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        config.parse(new String[]{file1.toString()});
        
        Registry registry = new Registry();
        new StandardLibrary().registerAll(registry);
        
        // Create and initialize startup context
        StartupContext startupContext = new StartupContext(config, registry, false);
        startupContext.initialize();
        
        // Should fail during prepare because file has package declaration
        ClnException exception = assertThrows(ClnException.class, () -> {
            startupContext.prepareExecutionContext();
        });
        
        assertTrue(exception.getMessage().contains("default package"));
        assertTrue(exception.getMessage().contains("mypackage"));
    }
    
    @Test
    void testNoMainFunctionInFiles(@TempDir Path tempDir) throws Exception {
        // Create files without main function
        Path file1 = createHelperFile(tempDir, "helper1.cln");
        Path file2 = createHelperFile(tempDir, "helper2.cln");
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        config.parse(new String[]{file1.toString(), file2.toString()});
        
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
    }
    
    @Test
    void testMultipleMainFunctionsInFiles(@TempDir Path tempDir) throws Exception {
        // Create multiple files with main function
        Path file1 = createDefaultPackageFile(tempDir, "main1.cln", "result = 42;");
        Path file2 = createDefaultPackageFile(tempDir, "main2.cln", "result = 100;");
        
        // Setup configuration
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        config.parse(new String[]{file1.toString(), file2.toString()});
        
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
    void testFileNotFound(@TempDir Path tempDir) {
        // Setup configuration with non-existent file
        RuntimeConfiguration config = new RuntimeConfiguration();
        config.addClnPath(tempDir.toString());
        
        ClnException exception = assertThrows(ClnException.class, () -> {
            config.parse(new String[]{tempDir.resolve("nonexistent.cln").toString()});
            
            Registry registry = new Registry();
            new StandardLibrary().registerAll(registry);
            
            StartupContext startupContext = new StartupContext(config, registry, false);
            startupContext.initialize();
        });
        
        assertTrue(exception.getMessage().contains("File not found"));
    }
}
