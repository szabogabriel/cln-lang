package org.clnlang.compiled;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.clnlang.RuntimeConfiguration;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.compiled.register.elements.FunctionSignature;
import org.clnlang.compiled.register.elements.StructSignature;
import org.clnlang.compiled.register.elements.UnionSignature;
import org.clnlang.compiled.register.elements.VariableSignature;
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
            if (signature.getFullyQualifiedName().equals("geo.printer")) {
                assertTrue(signature.getParameterTypes().length == 1);
                assertTrue(signature.getParameterTypes()[0].equals(Types.STRING));
                assertTrue(signature.getReturnTypes()[0].equals(Types.INT));
            } else if (signature.getFullyQualifiedName().equals("geo.main")) {
                assertTrue(signature.getParameterTypes().length == 0);
                assertTrue(signature.getReturnTypes()[0].equals(Types.INT));
            }
        }

        // Test structs
        Map<StructSignature, File> structs = globalRegistry.getStructs();
        assertNotNull(structs);
        assertTrue(structs.keySet().size() == 2);
        for (StructSignature signature : structs.keySet()) {
            System.out.println("Struct: " + signature.getFullyQualifiedName() + ", exposed: " + signature.isExposed());
            assertTrue(signature.getFullyQualifiedName().equals("geo.Point") || signature.getFullyQualifiedName().equals("geo.Circle"));
            if (signature.getFullyQualifiedName().equals("geo.Point")) {
                assertTrue(signature.isExposed());
            } else if (signature.getFullyQualifiedName().equals("geo.Circle")) {
                assertTrue(!signature.isExposed());
            }
        }

        // Test unions
        Map<UnionSignature, File> unions = globalRegistry.getUnions();
        assertNotNull(unions);
        assertTrue(unions.keySet().size() == 1);
        for (UnionSignature signature : unions.keySet()) {
            System.out.println("Union: " + signature.getFullyQualifiedName() + ", exposed: " + signature.isExposed());
            assertTrue(signature.getFullyQualifiedName().equals("geo.Shape"));
            assertTrue(!signature.isExposed());
        }

        // Test global variables
        Map<VariableSignature, File> variables = globalRegistry.getVariables();
        assertNotNull(variables);
        assertTrue(variables.keySet().size() == 1);
        for (VariableSignature signature : variables.keySet()) {
            System.out.println("Variable: " + signature.getFullyQualifiedName() + ", type: " + signature.getTypeName() + ", mutable: " + signature.isMutable());
            assertTrue(signature.getFullyQualifiedName().equals("geo.globalMessage"));
            assertTrue(signature.getTypeName().equals(Types.STRING));
            assertTrue(!signature.isMutable()); // Not declared with 'var', so it's immutable
            assertTrue(!signature.isExposed());
        }
    }
    
}
