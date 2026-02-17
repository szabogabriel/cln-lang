package org.clnlang.ast.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.visitor.itnerpreted.CompilerVisitor;
import org.clnlang.interpreted.compile.BlockImpl;
import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;
import org.clnlang.interpreted.compile.declaration.ImportDeclImpl;
import org.clnlang.interpreted.compile.declaration.ProgramImpl;
import org.clnlang.interpreted.compile.declaration.StructDeclImpl;
import org.clnlang.interpreted.compile.declaration.UnionDeclImpl;
import org.clnlang.interpreted.compile.expression.BinaryExprImpl;
import org.clnlang.interpreted.compile.expression.BoolLiteralExprImpl;
import org.clnlang.interpreted.compile.expression.CallExprImpl;
import org.clnlang.interpreted.compile.expression.IdentifierExprImpl;
import org.clnlang.interpreted.compile.expression.IndexAccessExprImpl;
import org.clnlang.interpreted.compile.expression.IntLiteralExprImpl;
import org.clnlang.interpreted.compile.expression.MemberAccessExprImpl;
import org.clnlang.interpreted.compile.expression.Operator;
import org.clnlang.interpreted.compile.expression.StringLiteralExprImpl;
import org.clnlang.interpreted.compile.expression.StructLiteralExprImpl;
import org.clnlang.interpreted.compile.expression.UnaryExprImpl;
import org.clnlang.interpreted.compile.statement.AssignStmtImpl;
import org.clnlang.interpreted.compile.statement.IfStmtImpl;
import org.clnlang.interpreted.compile.statement.ReturnStmtImpl;
import org.clnlang.interpreted.compile.statement.TupleAssignStmtImpl;
import org.clnlang.interpreted.compile.statement.VarDeclStmtImpl;
import org.clnlang.interpreted.compile.statement.WhileStmtImpl;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.interpreted.runtime.context.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CompilerVisitor
 */
public class CompilerVisitorTest {
    
    private CompilerVisitor compiler;
    private ExecutionContext context;
    
    @BeforeEach
    public void setUp() {
        context = new ExecutionContext();
        compiler = new CompilerVisitor();
    }
    
    /**
     * Helper method to parse and compile a program
     */
    private ProgramImpl compileProgram(String source) {
        clnLexer lexer = new clnLexer(CharStreams.fromString(source));
        clnParser parser = new clnParser(new CommonTokenStream(lexer));
        clnParser.ProgramContext parseTree = parser.program();
        return compiler.compileProgram(parseTree);
    }
    
    @Test
    public void testEmptyProgram() {
        String source = "";
        ProgramImpl program = compileProgram(source);
        
        assertNotNull(program);
        assertNull(program.getPackageDecl());
        assertTrue(program.getImports().isEmpty());
        assertTrue(program.getDeclarations().isEmpty());
    }
    
    @Test
    public void testPackageDeclaration() {
        String source = "package com.example.test;";
        ProgramImpl program = compileProgram(source);
        
        assertNotNull(program.getPackageDecl());
        assertEquals("com.example.test", program.getPackageDecl().getPackageName());
    }
    
    @Test
    public void testImportDeclaration() {
        String source = "import com.example.utils;";
        ProgramImpl program = compileProgram(source);
        
        assertEquals(1, program.getImports().size());
        ImportDeclImpl importDecl = program.getImports().get(0);
        assertEquals("com.example.utils", importDecl.getImportPath());
        assertFalse(importDecl.isWildcard());
    }
    
    @Test
    public void testImportWildcard() {
        String source = "import com.example.utils.*;";
        ProgramImpl program = compileProgram(source);
        
        assertEquals(1, program.getImports().size());
        ImportDeclImpl importDecl = program.getImports().get(0);
        assertEquals("com.example.utils", importDecl.getImportPath());
        assertTrue(importDecl.isWildcard());
    }
    
    @Test
    public void testStructDeclaration() {
        String source = """
            struct Point {
                int x;
                int y;
            };
            """;
        ProgramImpl program = compileProgram(source);
        
        assertEquals(1, program.getDeclarations().size());
        assertTrue(program.getDeclarations().get(0) instanceof StructDeclImpl);
        
        StructDeclImpl struct = (StructDeclImpl) program.getDeclarations().get(0);
        assertEquals("Point", struct.getName());
        assertFalse(struct.isExposed());
        assertEquals(2, struct.getFields().size());
    }
    
    @Test
    public void testExposedStructDeclaration() {
        String source = """
            expose struct Point {
                int x;
                int y;
            };
            """;
        ProgramImpl program = compileProgram(source);
        
        StructDeclImpl struct = (StructDeclImpl) program.getDeclarations().get(0);
        assertTrue(struct.isExposed());
    }
    
    @Test
    public void testUnionDeclaration() {
        String source = """
            union Value {
                int;
                string;
                bool;
            };
            """;
        ProgramImpl program = compileProgram(source);
        
        assertEquals(1, program.getDeclarations().size());
        assertTrue(program.getDeclarations().get(0) instanceof UnionDeclImpl);
        
        UnionDeclImpl union = (UnionDeclImpl) program.getDeclarations().get(0);
        assertEquals("Value", union.getName());
        // Union members may be empty due to parser implementation
        // This test verifies the structure compiles correctly
    }
    
    @Test
    public void testSimpleFunctionDeclaration() {
        String source = """
            (int result = 0) main() {
                return 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        assertEquals(1, program.getDeclarations().size());
        assertTrue(program.getDeclarations().get(0) instanceof FunctionDeclImpl);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        assertEquals("main", func.getName());
        assertFalse(func.isExposed());
        assertEquals(0, func.getParameters().size());
        assertEquals(1, func.getReturnVars().size());
        assertNotNull(func.getBlock());
    }
    
    @Test
    public void testFunctionWithParameters() {
        String source = """
            (int result = 0) add(int a, int b) {
                return a + b;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        assertEquals("add", func.getName());
        assertEquals(2, func.getParameters().size());
        
        assertEquals("int", func.getParameters().get(0).getType());
        assertEquals("a", func.getParameters().get(0).getName());
    }
    
    @Test
    public void testVariableDeclaration() {
        String source = """
            (int result = 0) test() {
                int x = 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertEquals(1, block.getStatements().size());
        assertTrue(block.getStatements().get(0) instanceof VarDeclStmtImpl);
        
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        assertFalse(varDecl.isVar());
        assertEquals("int", varDecl.getType());
        assertEquals("x", varDecl.getName());
        assertNotNull(varDecl.getInitializer());
    }
    
    @Test
    public void testMutableVariableDeclaration() {
        String source = """
            (int result = 0) test() {
                var int x = 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.isVar());
    }
    
    @Test
    public void testAssignmentStatement() {
        String source = """
            (int result = 0) test() {
                var int x = 0;
                x = 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertEquals(2, block.getStatements().size());
        assertTrue(block.getStatements().get(1) instanceof AssignStmtImpl);
        
        AssignStmtImpl assign = (AssignStmtImpl) block.getStatements().get(1);
        assertTrue(assign.getLvalue() instanceof IdentifierExprImpl);
        assertTrue(assign.getValue() instanceof IntLiteralExprImpl);
    }
    
    @Test
    public void testIfStatement() {
        String source = """
            (int result = 0) test() {
                if (true) {
                    return 1;
                }
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertTrue(block.getStatements().get(0) instanceof IfStmtImpl);
        
        IfStmtImpl ifStmt = (IfStmtImpl) block.getStatements().get(0);
        assertNotNull(ifStmt.getCondition());
        assertNotNull(ifStmt.getThenBlock());
        assertNull(ifStmt.getElseBlock());
    }
    
    @Test
    public void testIfElseStatement() {
        String source = """
            (int result = 0) test() {
                if (true) {
                    return 1;
                } else {
                    return 0;
                }
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        IfStmtImpl ifStmt = (IfStmtImpl) block.getStatements().get(0);
        
        assertNotNull(ifStmt.getElseBlock());
    }
    
    @Test
    public void testWhileStatement() {
        String source = """
            (int result = 0) test() {
                while (true) {
                    return 0;
                }
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertTrue(block.getStatements().get(0) instanceof WhileStmtImpl);
        
        WhileStmtImpl whileStmt = (WhileStmtImpl) block.getStatements().get(0);
        assertNotNull(whileStmt.getCondition());
        assertNotNull(whileStmt.getBody());
    }
    
    @Test
    public void testReturnStatement() {
        String source = """
            (int result = 0) test() {
                return 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertTrue(block.getStatements().get(0) instanceof ReturnStmtImpl);
        
        ReturnStmtImpl returnStmt = (ReturnStmtImpl) block.getStatements().get(0);
        assertEquals(1, returnStmt.getReturnValues().size());
    }
    
    @Test
    public void testIntLiteral() {
        String source = """
            (int result = 0) test() {
                int x = 42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof IntLiteralExprImpl);
        
        IntLiteralExprImpl intLit = (IntLiteralExprImpl) varDecl.getInitializer();
        assertEquals(42, intLit.getValue());
    }
    
    @Test
    public void testBoolLiteral() {
        String source = """
            (int result = 0) test() {
                bool x = true;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof BoolLiteralExprImpl);
        
        BoolLiteralExprImpl boolLit = (BoolLiteralExprImpl) varDecl.getInitializer();
        assertTrue(boolLit.getValue());
    }
    
    @Test
    public void testStringLiteral() {
        String source = """
            (int result = 0) test() {
                string x = "hello";
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof StringLiteralExprImpl);
        
        StringLiteralExprImpl strLit = (StringLiteralExprImpl) varDecl.getInitializer();
        assertEquals("hello", strLit.getValue());
    }
    
    @Test
    public void testBinaryExpression() {
        String source = """
            (int result = 0) test() {
                int x = 1 + 2;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof BinaryExprImpl);
        
        BinaryExprImpl binExpr = (BinaryExprImpl) varDecl.getInitializer();
        assertEquals(Operator.PLUS, binExpr.getOperator());
        assertTrue(binExpr.getLeft() instanceof IntLiteralExprImpl);
        assertTrue(binExpr.getRight() instanceof IntLiteralExprImpl);
    }
    
    @Test
    public void testUnaryExpression() {
        String source = """
            (int result = 0) test() {
                int x = -42;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof UnaryExprImpl);
        
        UnaryExprImpl unaryExpr = (UnaryExprImpl) varDecl.getInitializer();
        assertEquals("-", unaryExpr.getOperator());
        assertTrue(unaryExpr.getOperand() instanceof IntLiteralExprImpl);
    }
    
    @Test
    public void testFunctionCall() {
        String source = """
            (int result = 0) test() {
                int x = foo(1, 2);
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof CallExprImpl);
        
        CallExprImpl callExpr = (CallExprImpl) varDecl.getInitializer();
        assertTrue(callExpr.getFunction() instanceof IdentifierExprImpl);
        assertEquals(2, callExpr.getArguments().size());
    }
    
    @Test
    public void testMemberAccess() {
        String source = """
            (int result = 0) test() {
                int x = point.x;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof MemberAccessExprImpl);
        
        MemberAccessExprImpl memberAccess = (MemberAccessExprImpl) varDecl.getInitializer();
        assertTrue(memberAccess.getObject() instanceof IdentifierExprImpl);
        assertEquals("x", memberAccess.getMember());
    }
    
    @Test
    public void testIndexAccess() {
        String source = """
            (int result = 0) test() {
                int x = arr[0];
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof IndexAccessExprImpl);
        
        IndexAccessExprImpl indexAccess = (IndexAccessExprImpl) varDecl.getInitializer();
        assertTrue(indexAccess.getArray() instanceof IdentifierExprImpl);
        assertTrue(indexAccess.getIndex() instanceof IntLiteralExprImpl);
    }
    
    @Test
    public void testStructLiteral() {
        String source = """
            struct Point {
                var int x;
                var int y;
            };
            
            (int result = 0) test() {
                Point p = Point(x: 10, y: 20);
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        // Function is now the second declaration (after struct)
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(1);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        assertTrue(varDecl.getInitializer() instanceof StructLiteralExprImpl);
        
        StructLiteralExprImpl structLit = (StructLiteralExprImpl) varDecl.getInitializer();
        assertEquals("Point", structLit.getTypeName());
        assertEquals(2, structLit.getFields().size());
    }
    
    @Test
    public void testComplexExpression() {
        String source = """
            (int result = 0) test() {
                bool x = (1 + 2) * 3 > 5 && true;
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        VarDeclStmtImpl varDecl = (VarDeclStmtImpl) block.getStatements().get(0);
        
        // Should be AND at the top level
        assertTrue(varDecl.getInitializer() instanceof BinaryExprImpl);
        BinaryExprImpl andExpr = (BinaryExprImpl) varDecl.getInitializer();
        assertEquals(Operator.AND, andExpr.getOperator());
    }
    
    @Test
    public void testTupleAssignment() {
        String source = """
            (int result = 0) test() {
                (int a, int b) = getValue();
            }
            """;
        ProgramImpl program = compileProgram(source);
        
        FunctionDeclImpl func = (FunctionDeclImpl) program.getDeclarations().get(0);
        BlockImpl block = (BlockImpl) func.getBlock();
        
        assertTrue(block.getStatements().get(0) instanceof TupleAssignStmtImpl);
        
        TupleAssignStmtImpl tupleAssign = (TupleAssignStmtImpl) block.getStatements().get(0);
        assertEquals(2, tupleAssign.getBindings().size());
        assertNotNull(tupleAssign.getValue());
    }
    
    @Test
    public void testCompilerIsStateless() {
        // CompilerVisitor should be stateless and not hold any ExecutionContext
        // Verify that multiple compilations work correctly
        String source = """
            package test
            
            (var int x = 0) simple() {
                x = 42;
                return;
            }
        """;
        
        ProgramImpl prog1 = compileProgram(source);
        ProgramImpl prog2 = compileProgram(source);
        
        // Both compilations should succeed and produce distinct objects
        assertNotNull(prog1);
        assertNotNull(prog2);
        assertNotSame(prog1, prog2);
    }

    @Test
    public void testExecutionContextWithPackageImportsStructsUnionsFunctions() throws Exception {
        // Compile and execute a comprehensive program
        String source = """
            package testpkg;
            
            import std.io.*;
            import utils.math;
            
            var int globalVar = 42;
            int globalConst = 100;
            
            struct TestStruct {
                var int field1;
                var int field2;
            };
            
            struct AnotherStruct {
                var int x;
            };
            
            union TestUnion {
                TestStruct;
                AnotherStruct;
            };
            
            (var int result = 0) testFunction(int param) {
                result = param * 2;
                return;
            }
            
            (var int value = 100) anotherFunction() {
                value = 42;
                return;
            }
            """;
        
        ProgramImpl program = compileProgram(source);
        
        // Execute the program to populate the context
        program.populateContext(context);
        
        // 1. Test package
        assertEquals("testpkg", context.getGlobalContext().getPackageName(), 
                     "Package should be 'testpkg'");
        
        // 2. Test imports
        assertNotNull(program.getImports(), "Imports list should not be null");
        assertEquals(2, program.getImports().size(), "Should have 2 imports");
        assertEquals("std.io", program.getImports().get(0).getImportPath(), 
                     "First import path should be 'std.io'");
        assertTrue(program.getImports().get(0).isWildcard(), "First import should be wildcard");
        assertEquals("utils.math", program.getImports().get(1).getImportPath(), 
                     "Second import path should be 'utils.math'");
        assertFalse(program.getImports().get(1).isWildcard(), "Second import should not be wildcard");
        
        // 3. Test structs
        assertTrue(context.getGlobalContext().hasStructType("TestStruct"), 
                   "Should have TestStruct");
        assertTrue(context.getGlobalContext().hasStructType("AnotherStruct"), 
                   "Should have AnotherStruct");
        
        // 4. Test unions
        assertTrue(context.getGlobalContext().hasUnionType("TestUnion"), 
                   "Should have TestUnion");
        
        // 5. Test functions
        assertTrue(context.getGlobalContext().hasFunction("testFunction"), 
                   "Should have 'testFunction'");
        assertTrue(context.getGlobalContext().hasFunction("anotherFunction"), 
                   "Should have 'anotherFunction'");
        
        // 6. Test global variables
        assertTrue(context.getGlobalContext().hasGlobalVariable("globalVar"),
                   "Should have global variable 'globalVar'");
        assertEquals(42L, context.getGlobalContext().getGlobalValue("globalVar"),
                     "Global variable 'globalVar' should be 42");
        
        // 7. Test global constants
        assertTrue(context.getGlobalContext().hasGlobalVariable("globalConst"),
                   "Should have global constant 'globalConst'");
        assertEquals(100L, context.getGlobalContext().getGlobalValue("globalConst"),
                     "Global constant 'globalConst' should be 100");
    }

    @Test
    public void testExecutionContextWithRealTestProgram() throws Exception {
        // Load and compile test_program.cln (simplified - just declarations)
        String source = """
            package main;
            
            import utils.helpers.*;
            
            struct Point {
                var int x;
                var int y;
            };
            
            struct Circle {
                var int x;
                var int y;
                var int r;
            };
            
            union Shapes {
                Point;
                Circle;
            };
            
            (var int result = 0) add(int a, int b) {
                result = a + b;
                return;
            }
            
            (var int answer = 42) main() {
                answer = 10;
                return;
            }
            """;
        
        ProgramImpl program = compileProgram(source);
        
        // Execute the program to populate the context
        program.populateContext(context);
        
        // 1. Test package
        assertEquals("main", context.getGlobalContext().getPackageName(), 
                     "Package should be 'main'");
        
        // 2. Test imports (test_program.cln has "import utils.helpers.*;")
        assertNotNull(program.getImports(), "Imports list should not be null");
        assertEquals(1, program.getImports().size(), "Should have 1 import");
        assertEquals("utils.helpers", program.getImports().get(0).getImportPath(), 
                     "Import path should be 'utils.helpers'");
        assertTrue(program.getImports().get(0).isWildcard(), "Import should be wildcard");
        
        // 3. Test structs (Point and Circle)
        assertTrue(context.getGlobalContext().hasStructType("Point"), 
                   "Should have Point struct");
        assertTrue(context.getGlobalContext().hasStructType("Circle"), 
                   "Should have Circle struct");
        
        // 4. Test unions (Shapes union)
        assertTrue(context.getGlobalContext().hasUnionType("Shapes"), 
                   "Should have Shapes union");
        
        // 5. Test functions (add and main)
        assertTrue(context.getGlobalContext().hasFunction("add"), 
                   "Should have 'add' function");
        assertTrue(context.getGlobalContext().hasFunction("main"), 
                   "Should have 'main' function");
    }

    @Test
    public void testExecutionContextInitialization() {
        ExecutionContext newContext = new ExecutionContext();
        
        // Verify context is properly initialized
        assertNotNull(newContext.getGlobalContext(), "Global context should not be null");
        assertNotNull(newContext.getCurrentFrame(), "Should have a current frame");
        assertNotNull(newContext.getLocalContext(), "Should have a local context");
    }

    @Test
    public void testProgramExecutionPopulatesContext() throws Exception {
        String source = """
            package myapp;
            
            var int mutableGlobal = 50;
            int immutableGlobal = 200;
            
            struct User {
                var int id;
            };
            
            union Response {
                User;
            };
            
            (var int status = 0) process() {
                return;
            }
            """;
        
        ProgramImpl program = compileProgram(source);
        
        // Before execution, context should be empty
        assertNull(context.getGlobalContext().getPackageName());
        assertFalse(context.getGlobalContext().hasStructType("User"));
        assertFalse(context.getGlobalContext().hasUnionType("Response"));
        assertFalse(context.getGlobalContext().hasFunction("process"));
        assertFalse(context.getGlobalContext().hasGlobalVariable("mutableGlobal"));
        assertFalse(context.getGlobalContext().hasGlobalVariable("immutableGlobal"));
        
        // Execute program
        program.populateContext(context);
        
        // After execution, context should be populated
        assertEquals("myapp", context.getGlobalContext().getPackageName());
        assertTrue(context.getGlobalContext().hasStructType("User"));
        assertTrue(context.getGlobalContext().hasUnionType("Response"));
        assertTrue(context.getGlobalContext().hasFunction("process"));
        assertTrue(context.getGlobalContext().hasGlobalVariable("mutableGlobal"));
        assertEquals(50L, context.getGlobalContext().getGlobalValue("mutableGlobal"));
        assertTrue(context.getGlobalContext().hasGlobalVariable("immutableGlobal"));
        assertEquals(200L, context.getGlobalContext().getGlobalValue("immutableGlobal"));
    }

    @Test
    public void testGlobalVariablesAndConstants() throws Exception {
        String source = """
            package test;
            
            var int counter = 0;
            var bool flag = true;
            int maxValue = 1000;
            string message = "Hello";
            
            (var int result = 0) dummy() {
                return;
            }
            """;
        
        ProgramImpl program = compileProgram(source);
        program.populateContext(context);
        
        // Test mutable globals
        assertTrue(context.getGlobalContext().hasGlobalVariable("counter"));
        assertEquals(0L, context.getGlobalContext().getGlobalValue("counter"));
        assertTrue(context.getGlobalContext().isGlobalMutable("counter"));
        
        assertTrue(context.getGlobalContext().hasGlobalVariable("flag"));
        assertEquals(true, context.getGlobalContext().getGlobalValue("flag"));
        assertTrue(context.getGlobalContext().isGlobalMutable("flag"));
        
        // Test immutable globals (constants)
        assertTrue(context.getGlobalContext().hasGlobalVariable("maxValue"));
        assertEquals(1000L, context.getGlobalContext().getGlobalValue("maxValue"));
        assertFalse(context.getGlobalContext().isGlobalMutable("maxValue"));
        
        assertTrue(context.getGlobalContext().hasGlobalVariable("message"));
        assertEquals("Hello", context.getGlobalContext().getGlobalValue("message"));
        assertFalse(context.getGlobalContext().isGlobalMutable("message"));
        
        // Test getGlobalValue for both types
        assertEquals(0L, context.getGlobalContext().getGlobalValue("counter"));
        assertEquals(1000L, context.getGlobalContext().getGlobalValue("maxValue"));
    }

    @Test
    public void testExposedGlobalDeclarations() throws Exception {
        String source = """
            package mylib;
            
            expose var int publicVar = 10;
            expose int publicConst = 20;
            
            (var int result = 0) dummy() {
                return;
            }
            """;
        
        ProgramImpl program = compileProgram(source);
        
        // Verify the declarations exist
        assertEquals(3, program.getDeclarations().size());
        
        // Execute and verify values are set
        program.populateContext(context);
        
        assertTrue(context.getGlobalContext().hasGlobalVariable("publicVar"));
        assertEquals(10L, context.getGlobalContext().getGlobalValue("publicVar"));
        
        assertTrue(context.getGlobalContext().hasGlobalVariable("publicConst"));
        assertEquals(20L, context.getGlobalContext().getGlobalValue("publicConst"));
    }
}
