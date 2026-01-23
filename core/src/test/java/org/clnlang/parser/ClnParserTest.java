package org.clnlang.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.*;
import org.clnlang.ast.expression.*;
import org.clnlang.ast.statement.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Clean language parser and AST builder.
 */
class ClnParserTest {

    /**
     * Parse a Clean source file from test resources.
     */
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
    void testParseTestProgram() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        assertNotNull(program, "Program should not be null");
        
        // Verify package declaration
        assertNotNull(program.getPackageDecl(), "Package declaration should exist");
        assertEquals("main", program.getPackageDecl().getPackageName());
        
        // Verify imports
        assertEquals(1, program.getImports().size(), "Should have 1 import");
        ImportDeclNode importNode = program.getImports().get(0);
        assertEquals("utils.helpers", importNode.getImportPath());
        assertTrue(importNode.isWildcard(), "Import should be a wildcard");
        
        // Verify declarations (2 structs + 1 union + 2 functions = 5 total)
        assertEquals(5, program.getDeclarations().size(), "Should have 5 declarations");
        
        // Test struct declarations
        assertTrue(program.getDeclarations().get(0) instanceof StructDeclNode);
        StructDeclNode pointStruct = (StructDeclNode) program.getDeclarations().get(0);
        assertEquals("Point", pointStruct.getName());
        assertEquals(2, pointStruct.getFields().size());
        assertEquals("int", pointStruct.getFields().get(0).getType());
        assertEquals("x", pointStruct.getFields().get(0).getName());
        
        assertTrue(program.getDeclarations().get(1) instanceof StructDeclNode);
        StructDeclNode circleStruct = (StructDeclNode) program.getDeclarations().get(1);
        assertEquals("Circle", circleStruct.getName());
        assertEquals(3, circleStruct.getFields().size());
        
        // Test union declaration
        assertTrue(program.getDeclarations().get(2) instanceof UnionDeclNode);
        UnionDeclNode shapesUnion = (UnionDeclNode) program.getDeclarations().get(2);
        assertEquals("Shapes", shapesUnion.getName());
        assertEquals(2, shapesUnion.getMembers().size());
        assertTrue(shapesUnion.getMembers().contains("Point"));
        assertTrue(shapesUnion.getMembers().contains("Circle"));
        
        // Test function declarations
        assertTrue(program.getDeclarations().get(3) instanceof FunctionDeclNode);
        FunctionDeclNode addFunc = (FunctionDeclNode) program.getDeclarations().get(3);
        assertEquals("add", addFunc.getName());
        assertEquals(2, addFunc.getParameters().size());
        assertEquals(1, addFunc.getReturnVars().size());
        assertEquals("result", addFunc.getReturnVars().get(0).getName());
        assertEquals("int", addFunc.getReturnVars().get(0).getType());
        
        assertTrue(program.getDeclarations().get(4) instanceof FunctionDeclNode);
        FunctionDeclNode mainFunc = (FunctionDeclNode) program.getDeclarations().get(4);
        assertEquals("main", mainFunc.getName());
        assertEquals(0, mainFunc.getParameters().size());
        assertEquals(1, mainFunc.getReturnVars().size());
    }

    @Test
    void testParseTestUnion() throws IOException {
        ProgramNode program = parseResource("/test_union.cln");
        
        assertNotNull(program);
        assertEquals("shapes", program.getPackageDecl().getPackageName());
        
        // Verify imports
        assertEquals(1, program.getImports().size());
        assertEquals("std.io", program.getImports().get(0).getImportPath());
        
        // Verify declarations (2 structs + 1 union + 2 functions = 5 total)
        assertEquals(5, program.getDeclarations().size());
        
        // Verify struct with single field
        StructDeclNode circleStruct = (StructDeclNode) program.getDeclarations().get(0);
        assertEquals("Circle", circleStruct.getName());
        assertEquals(1, circleStruct.getFields().size());
        assertEquals("radius", circleStruct.getFields().get(0).getName());
    }

    @Test
    void testFunctionStatements() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        // Get the 'add' function (4th declaration, index 3)
        FunctionDeclNode addFunc = (FunctionDeclNode) program.getDeclarations().get(3);
        BlockNode block = addFunc.getBlock();
        
        assertNotNull(block, "Function should have a block");
        assertEquals(2, block.getStatements().size(), "Add function should have 2 statements");
        
        // First statement: assignment
        assertTrue(block.getStatements().get(0) instanceof AssignStmt);
        AssignStmt assignStmt = (AssignStmt) block.getStatements().get(0);
        assertTrue(assignStmt.getLvalue() instanceof IdentifierExpr);
        assertTrue(assignStmt.getValue() instanceof BinaryExpr);
        
        BinaryExpr binaryExpr = (BinaryExpr) assignStmt.getValue();
        assertEquals("+", binaryExpr.getOperator());
        
        // Second statement: return
        assertTrue(block.getStatements().get(1) instanceof ReturnStmt);
    }

    @Test
    void testMainFunctionStatements() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        // Get the 'main' function (5th declaration, index 4)
        FunctionDeclNode mainFunc = (FunctionDeclNode) program.getDeclarations().get(4);
        BlockNode block = mainFunc.getBlock();
        
        assertNotNull(block);
        assertEquals(4, block.getStatements().size(), "Main function should have 4 statements");
        
        // First statement: var Point p = Point(...)
        assertTrue(block.getStatements().get(0) instanceof VarDeclStmt);
        VarDeclStmt varDecl = (VarDeclStmt) block.getStatements().get(0);
        assertEquals("Point", varDecl.getType());
        assertEquals("p", varDecl.getName());
        assertTrue(varDecl.getInitializer() instanceof StructLiteralExpr);
        
        StructLiteralExpr structLiteral = (StructLiteralExpr) varDecl.getInitializer();
        assertEquals("Point", structLiteral.getTypeName());
        assertEquals(2, structLiteral.getFields().size());
        
        // Second statement: var int sum = add(...)
        assertTrue(block.getStatements().get(1) instanceof VarDeclStmt);
        VarDeclStmt sumDecl = (VarDeclStmt) block.getStatements().get(1);
        assertEquals("int", sumDecl.getType());
        assertEquals("sum", sumDecl.getName());
        assertTrue(sumDecl.getInitializer() instanceof CallExpr);
        
        CallExpr callExpr = (CallExpr) sumDecl.getInitializer();
        assertTrue(callExpr.getFunction() instanceof IdentifierExpr);
        assertEquals(2, callExpr.getArguments().size());
        
        // Third statement: if statement
        assertTrue(block.getStatements().get(2) instanceof IfStmt);
        IfStmt ifStmt = (IfStmt) block.getStatements().get(2);
        assertTrue(ifStmt.getCondition() instanceof BinaryExpr);
        
        BinaryExpr condition = (BinaryExpr) ifStmt.getCondition();
        assertEquals(">", condition.getOperator());
        
        assertNotNull(ifStmt.getThenBlock());
        assertNotNull(ifStmt.getElseBlock());
        
        // Fourth statement: return
        assertTrue(block.getStatements().get(3) instanceof ReturnStmt);
    }

    @Test
    void testExpressionParsing() throws IOException {
        ProgramNode program = parseResource("/test_union.cln");
        
        // Get the calculateCircleArea function
        FunctionDeclNode calcFunc = (FunctionDeclNode) program.getDeclarations().get(3);
        BlockNode block = calcFunc.getBlock();
        
        // First statement: area = radius * radius * 3
        AssignStmt assignStmt = (AssignStmt) block.getStatements().get(0);
        assertTrue(assignStmt.getValue() instanceof BinaryExpr);
        
        // Verify binary expression structure: ((radius * radius) * 3)
        BinaryExpr outerMultiply = (BinaryExpr) assignStmt.getValue();
        assertEquals("*", outerMultiply.getOperator());
        assertTrue(outerMultiply.getLeft() instanceof BinaryExpr);
        assertTrue(outerMultiply.getRight() instanceof IntLiteralExpr);
        
        BinaryExpr innerMultiply = (BinaryExpr) outerMultiply.getLeft();
        assertEquals("*", innerMultiply.getOperator());
        assertTrue(innerMultiply.getLeft() instanceof IdentifierExpr);
        assertTrue(innerMultiply.getRight() instanceof IdentifierExpr);
    }

    @Test
    void testMemberAccessExpression() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        FunctionDeclNode mainFunc = (FunctionDeclNode) program.getDeclarations().get(4);
        VarDeclStmt sumDecl = (VarDeclStmt) mainFunc.getBlock().getStatements().get(1);
        CallExpr callExpr = (CallExpr) sumDecl.getInitializer();
        
        // First argument: p.x
        assertTrue(callExpr.getArguments().get(0) instanceof MemberAccessExpr);
        MemberAccessExpr memberAccess1 = (MemberAccessExpr) callExpr.getArguments().get(0);
        assertTrue(memberAccess1.getObject() instanceof IdentifierExpr);
        assertEquals("x", memberAccess1.getMember());
        
        // Second argument: p.y
        assertTrue(callExpr.getArguments().get(1) instanceof MemberAccessExpr);
        MemberAccessExpr memberAccess2 = (MemberAccessExpr) callExpr.getArguments().get(1);
        assertEquals("y", memberAccess2.getMember());
    }

    @Test
    void testStructLiteralFieldInitializers() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        FunctionDeclNode mainFunc = (FunctionDeclNode) program.getDeclarations().get(4);
        VarDeclStmt varDecl = (VarDeclStmt) mainFunc.getBlock().getStatements().get(0);
        StructLiteralExpr structLiteral = (StructLiteralExpr) varDecl.getInitializer();
        
        assertEquals(2, structLiteral.getFields().size());
        
        StructLiteralExpr.FieldInit field1 = structLiteral.getFields().get(0);
        assertEquals("x", field1.getFieldName());
        assertTrue(field1.getValue() instanceof IntLiteralExpr);
        assertEquals(10, ((IntLiteralExpr) field1.getValue()).getIntValue());
        
        StructLiteralExpr.FieldInit field2 = structLiteral.getFields().get(1);
        assertEquals("y", field2.getFieldName());
        assertTrue(field2.getValue() instanceof IntLiteralExpr);
        assertEquals(20, ((IntLiteralExpr) field2.getValue()).getIntValue());
    }

    @Test
    void testReturnStatement() throws IOException {
        ProgramNode program = parseResource("/test_union.cln");
        
        // Get createDefaultShape function which has 'return shape;'
        FunctionDeclNode func = (FunctionDeclNode) program.getDeclarations().get(4);
        BlockNode block = func.getBlock();
        
        assertTrue(block.getStatements().get(0) instanceof ReturnStmt);
        ReturnStmt returnStmt = (ReturnStmt) block.getStatements().get(0);
        
        assertEquals(1, returnStmt.getReturnValues().size());
        assertTrue(returnStmt.getReturnValues().get(0) instanceof IdentifierExpr);
    }

    @Test
    void testToStringMethods() throws IOException {
        ProgramNode program = parseResource("/test_program.cln");
        
        // Test that toString methods work without throwing exceptions
        assertNotNull(program.toString());
        assertTrue(program.toString().contains("Program"));
        
        PackageDeclNode pkg = program.getPackageDecl();
        assertNotNull(pkg.toString());
        assertTrue(pkg.toString().contains("main"));
        
        StructDeclNode struct = (StructDeclNode) program.getDeclarations().get(0);
        assertNotNull(struct.toString());
        assertTrue(struct.toString().contains("Point"));
        
        UnionDeclNode union = (UnionDeclNode) program.getDeclarations().get(2);
        assertNotNull(union.toString());
        assertTrue(union.toString().contains("Shapes"));
        
        FunctionDeclNode func = (FunctionDeclNode) program.getDeclarations().get(3);
        assertNotNull(func.toString());
        assertTrue(func.toString().contains("add"));
    }
}
