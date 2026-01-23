package org.clnlang;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.clnlang.ast.visitor.ASTPrinterVisitor;
import org.clnlang.ast.visitor.DetailedASTPrinter;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String fileName;
        if (args.length < 1) {
            fileName = "./core/src/test/resources/test_program.cln";
            System.out.println("No file specified, using default: " + fileName);
        } else {
            fileName = args[0];
        }

        // Create a CharStream from the input file
        CharStream input = CharStreams.fromFileName(fileName);
        
        // Create a lexer that feeds off of input CharStream
        clnLexer lexer = new clnLexer(input);
        
        // Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Create a parser that feeds off the tokens buffer
        clnParser parser = new clnParser(tokens);
        
        // Begin parsing at the program rule
        ParseTree tree = parser.program();
        
        // Check for syntax errors
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Parsing failed with " + parser.getNumberOfSyntaxErrors() + " errors.");
            System.exit(1);
        }
        
        // Build AST using the visitor pattern
        ClnASTBuilder astBuilder = new ClnASTBuilder();
        ProgramNode ast = (ProgramNode) astBuilder.visit(tree);
        
        // Print the AST using toString()
        System.out.println("=== Abstract Syntax Tree (toString) ===");
        System.out.println(ast);
        
        // Print the AST using visitor pattern
        System.out.println("\n=== Abstract Syntax Tree (Visitor Pattern) ===");
        ASTPrinterVisitor printer = new ASTPrinterVisitor();
        ast.accept(printer);
        
        // Print detailed AST with statements and expressions
        System.out.println("\n=== Detailed AST (Statements & Expressions) ===");
        DetailedASTPrinter detailedPrinter = new DetailedASTPrinter();
        ast.accept(detailedPrinter);
    }
}