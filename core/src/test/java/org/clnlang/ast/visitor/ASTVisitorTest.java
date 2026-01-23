package org.clnlang.ast.visitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.declaration.*;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for AST visitors.
 */
class ASTVisitorTest {

    private ProgramNode parseResource(String resourcePath) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        assertNotNull(is, "Test resource not found: " + resourcePath);
        
        CharStream input = CharStreams.fromStream(is);
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        
        ClnASTBuilder builder = new ClnASTBuilder();
        return (ProgramNode) builder.visitProgram(parser.program());
    }

    @Test
    void testASTPrinterVisitor() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        ASTPrinterVisitor printer = new ASTPrinterVisitor();
        
        // Should not throw any exceptions
        assertDoesNotThrow(() -> program.accept(printer));
    }

    @Test
    void testDetailedASTPrinter() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        DetailedASTPrinter printer = new DetailedASTPrinter();
        
        // Should not throw any exceptions
        assertDoesNotThrow(() -> program.accept(printer));
    }

    @Test
    void testVisitorOnAllDeclarations() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        // Custom visitor to count declarations
        class CountingVisitor implements ASTVisitor {
            int structCount = 0;
            int unionCount = 0;
            int functionCount = 0;

            @Override
            public void visit(ProgramNode node) {
                if (node.getPackageDecl() != null) {
                    node.getPackageDecl().accept(this);
                }
                for (ImportDeclNode imp : node.getImports()) {
                    imp.accept(this);
                }
                for (org.clnlang.ast.ASTNode decl : node.getDeclarations()) {
                    decl.accept(this);
                }
            }

            @Override
            public void visit(PackageDeclNode node) {
                // Count visited
            }

            @Override
            public void visit(ImportDeclNode node) {
                // Count visited
            }

            @Override
            public void visit(StructDeclNode node) {
                structCount++;
            }

            @Override
            public void visit(UnionDeclNode node) {
                unionCount++;
            }

            @Override
            public void visit(FunctionDeclNode node) {
                functionCount++;
            }
        }

        CountingVisitor counter = new CountingVisitor();
        program.accept(counter);

        assertEquals(2, counter.structCount, "Should have 2 structs");
        assertEquals(1, counter.unionCount, "Should have 1 union");
        assertEquals(2, counter.functionCount, "Should have 2 functions");
    }

    @Test
    void testVisitorOnTestUnion() throws IOException {
        ProgramNode program = parseResource("/test_union.cln");
        
        ASTPrinterVisitor printer = new ASTPrinterVisitor();
        assertDoesNotThrow(() -> program.accept(printer));
        
        // Verify program structure through visitor
        assertNotNull(program.getPackageDecl());
        assertEquals("shapes", program.getPackageDecl().getPackageName());
    }

    @Test
    void testAcceptMethodsExist() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        // Verify all node types have accept methods
        assertNotNull(program);
        program.getPackageDecl().accept(new ASTPrinterVisitor());
        
        for (ImportDeclNode imp : program.getImports()) {
            imp.accept(new ASTPrinterVisitor());
        }
        
        for (org.clnlang.ast.ASTNode decl : program.getDeclarations()) {
            decl.accept(new ASTPrinterVisitor());
        }
    }
}
