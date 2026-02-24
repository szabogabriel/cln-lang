package org.clnlang.compiled;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.visitor.compiled.TypecheckCompilerVisitor;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.junit.jupiter.api.Test;

public class TypedCompilerTest {

    /**
     * Parse a Clean source code string and build an AST.
     */
    private ProgramNode parseString(String code) {
        CharStream input = CharStreams.fromString(code);
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        
        ClnASTBuilder builder = new ClnASTBuilder();
        Object result = builder.visitProgram(parser.program());
        return (ProgramNode) result;
    }

    @Test
    void testFunctionWithReturn() {
        String code = """
                package main;
                
                int add(int a, int b) {
                    return a + b;
                }
                """;
        
        // Parse the code
        ProgramNode program = parseString(code);
        assertNotNull(program, "Parsed program should not be null");
        
        // Run the compiler visitor
        TypecheckCompilerVisitor visitor = new TypecheckCompilerVisitor();
        program.accept(visitor);
        
        // TODO: Add assertions once visitor implementation is complete
    }
    
}
