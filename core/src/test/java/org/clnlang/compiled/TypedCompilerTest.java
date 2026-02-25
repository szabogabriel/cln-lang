package org.clnlang.compiled;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.visitor.compiled.CompilerContext;
import org.clnlang.ast.visitor.compiled.TypecheckCompilerVisitor;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.compiled.register.elements.FunctionSignature;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.junit.jupiter.api.Test;

public class TypedCompilerTest {

    /**
     * Parse a Clean source code string and build an AST.
     */
    private clnParser.ProgramContext parseString(String code) {
        CharStream input = CharStreams.fromString(code);
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        
        return parser.program();
    }

    @Test
    void testFunctionWithReturn() {
        String code = """
                package main;
                
                int add(int a, int b) {
                    return a + b;
                }
                """;

        // Run the compiler visitor
        GlobalRegistry globalRegistry = new GlobalRegistry();
        FunctionSignature function = new FunctionSignature("main", "add", new Types[] {Types.INT, Types.INT}, new Types[] {Types.INT}, new String[] {"a", "b"}, true);
        globalRegistry.registerFunction(function, null);
        
        // Parse the code
        clnParser.ProgramContext program = parseString(code);
        assertNotNull(program, "Parsed program should not be null");
        
        TypecheckCompilerVisitor visitor = new TypecheckCompilerVisitor(globalRegistry);
        visitor.compileProgram(program, null);
        
        CompilerContext context = visitor.getCompilerContext();
        assertNotNull(context);
        // TODO: Add assertions once visitor implementation is complete
    }
    
}
