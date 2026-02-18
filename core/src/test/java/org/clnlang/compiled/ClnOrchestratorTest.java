package org.clnlang.compiled;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.clnlang.RuntimeConfiguration;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.compiled.register.elements.FunctionSignature;
import org.junit.jupiter.api.Test;

public class ClnOrchestratorTest {

    @Test
    void testGlobalRegistryFilled() throws IOException {
        File sourceFile = File.createTempFile("test_file", ".cln");
        sourceFile.deleteOnExit();

        String sourceCode = """
            package geo;

            import std.console.writeLine;

            string globalMessage = "Global variables work!";

            expose struct Point {
                int x;
                int y;
            };

            struct Circle {
                Point center;
                int radius;
            };

            union Shape {
                Point;
                Circle;
            };

            expose int printer(string message) {
                console.writeLine(message);
                return 0;
            }
            
            int main() {
                printer("Hello, World!");
                return 0;
            }
        """;

        Files.writeString(sourceFile.toPath(), sourceCode);

        String[] args = new String[] { sourceFile.getAbsolutePath() };

        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(args);

        assertTrue(config.isSourceFileExecution());
        
        ClnOrchestrator orchestrator = new ClnOrchestrator(config);
        orchestrator.orchestrate();

        GlobalRegistry globalRegistry = orchestrator.globalRegistry;
        assertNotNull(globalRegistry);

        Map<FunctionSignature, File> functions = globalRegistry.getFunctions();
        assertNotNull(functions);
        assertTrue(functions.keySet().size() == 2);
        for (FunctionSignature signature : functions.keySet()) {
            System.out.println("Function: " + signature.getFullyQualifiedName());
            assertTrue(signature.getFullyQualifiedName().equals("geo.printer") || signature.getFullyQualifiedName().equals("geo.main"));
        }
    }
    
}
