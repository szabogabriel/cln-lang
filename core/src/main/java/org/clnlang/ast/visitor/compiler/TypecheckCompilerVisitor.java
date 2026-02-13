package org.clnlang.ast.visitor.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.FunctionDeclNode.ReturnVar;
import org.clnlang.ast.declaration.ImportDeclNode;
import org.clnlang.ast.declaration.PackageDeclNode;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.declaration.StructDeclNode;
import org.clnlang.ast.declaration.UnionDeclNode;
import org.clnlang.ast.expression.BinaryExpr;
import org.clnlang.ast.expression.BoolLiteralExpr;
import org.clnlang.ast.expression.CallExpr;
import org.clnlang.ast.expression.IdentifierExpr;
import org.clnlang.ast.expression.IndexAccessExpr;
import org.clnlang.ast.expression.IntLiteralExpr;
import org.clnlang.ast.expression.MemberAccessExpr;
import org.clnlang.ast.expression.StringLiteralExpr;
import org.clnlang.ast.expression.StructLiteralExpr;
import org.clnlang.ast.expression.UnaryExpr;
import org.clnlang.ast.statement.AssignStmt;
import org.clnlang.ast.statement.EmptyStmt;
import org.clnlang.ast.statement.ExprStmt;
import org.clnlang.ast.statement.IfStmt;
import org.clnlang.ast.statement.ReturnStmt;
import org.clnlang.ast.statement.SwitchStmt;
import org.clnlang.ast.statement.TupleAssignStmt;
import org.clnlang.ast.statement.VarDeclStmt;
import org.clnlang.ast.statement.WhileStmt;
import org.clnlang.ast.visitor.ASTVisitor;
import org.clnlang.compiled.types.Types;

public class TypecheckCompilerVisitor implements ASTVisitor {

    private static enum Variable { INT, DEC, BOOL, STRING, STRUCT, UNION; };

    private String packageName = null;

    private Set<String> imports = new HashSet<>();
    
    private List<String> structNames = new ArrayList<>();
    private List<String> unionNames = new ArrayList<>();
    private List<String> functionNames = new ArrayList<>();
    private List<String> globalVariableNames = new ArrayList<>();

    private Map<String, Variable> localVariableTypes = new HashMap<>();
    private Map<String, Integer> localVariableAddresses = new HashMap<>();
    int localVarAddrCounterInt = 0;
    int localVarAddrCounterDec = 0;
    int localVarAddrCounterBool = 0;
    int localVarAddrCounterString = 0;
    int localVarAddrCounterStruct = 0;
    int localVarAddrCounterUnion = 0;

    @Override
    public void visit(ProgramNode node) {
        // Visit package declaration
        if (node.getPackageDecl() != null) {
            node.getPackageDecl().accept(this);
        }
        
        // Visit imports
        for (ImportDeclNode importNode : node.getImports()) {
            importNode.accept(this);
        }
        
        // Visit declarations (structs, unions, functions)
        for (Object decl : node.getDeclarations()) {
            if (decl instanceof StructDeclNode) {
                ((StructDeclNode) decl).accept(this);
            } else if (decl instanceof UnionDeclNode) {
                ((UnionDeclNode) decl).accept(this);
            } else if (decl instanceof FunctionDeclNode) {
                ((FunctionDeclNode) decl).accept(this);
            }
        }
    }

    @Override
    public void visit(PackageDeclNode node) {
        this.packageName = node.getPackageName();
    }

    @Override
    public void visit(ImportDeclNode node) {
        imports.add(node.getImportPath());
    }

    @Override
    public void visit(StructDeclNode node) {
        structNames.add(node.getName());
        // TODO: Process struct fields
    }

    @Override
    public void visit(UnionDeclNode node) {
        unionNames.add(node.getName());
        // TODO: Process union members
    }

    @Override
    public void visit(FunctionDeclNode node) {
        functionNames.add(node.getName());

        List<FunctionDeclNode.Parameter> parameters = node.getParameters();
        List<ReturnVar> returnVars = node.getReturnVars();

        int[] mappedParameters = new int[parameters.size()];
        Types[] mappedParameterTypes = new Types[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            FunctionDeclNode.Parameter param = parameters.get(i);
            String paramName = param.getName();
            String paramType = param.getType();
            Types type = Types.fromString(paramType);
            mappedParameterTypes[i] = type;

        }
        
        // TODO: Process parameters
        // TODO: Process return types
        
        // Visit function body
        if (node.getBlock() != null) {
            node.getBlock().accept(this);
        }
    }

    @Override
    public void visit(BlockNode node) {
        // Visit all statements in the block
        for (Object stmt : node.getStatements()) {
            if (stmt instanceof AssignStmt) {
                ((AssignStmt) stmt).accept(this);
            } else if (stmt instanceof VarDeclStmt) {
                ((VarDeclStmt) stmt).accept(this);
            } else if (stmt instanceof ReturnStmt) {
                ((ReturnStmt) stmt).accept(this);
            } else if (stmt instanceof IfStmt) {
                ((IfStmt) stmt).accept(this);
            } else if (stmt instanceof WhileStmt) {
                ((WhileStmt) stmt).accept(this);
            } else if (stmt instanceof ExprStmt) {
                ((ExprStmt) stmt).accept(this);
            } else if (stmt instanceof EmptyStmt) {
                ((EmptyStmt) stmt).accept(this);
            }
        }
    }

    @Override
    public void visit(AssignStmt node) {
        // TODO: Process assignment
        // Visit lvalue and value expressions
    }

    @Override
    public void visit(EmptyStmt node) {
        // No-op for empty statements
    }

    @Override
    public void visit(ExprStmt node) {
        // TODO: Process expression statement
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
    }

    @Override
    public void visit(IfStmt node) {
        // TODO: Process if statement
        // Visit condition, then block, else block
    }

    @Override
    public void visit(ReturnStmt node) {
        // TODO: Process return statement
        // Visit return value expressions
        System.out.println("Processing return statement");
    }

    @Override
    public void visit(SwitchStmt node) {
        // TODO: Process switch statement
    }

    @Override
    public void visit(TupleAssignStmt node) {
        // TODO: Process tuple assignment
    }

    @Override
    public void visit(VarDeclStmt node) {
        // TODO: Process variable declaration
        System.out.println("Processing variable declaration: " + node.getName() + " of type " + node.getType());
    }

    @Override
    public void visit(WhileStmt node) {
        // TODO: Process while statement
    }

    @Override
    public void visit(BinaryExpr node) {
        // TODO: Process binary expression
        System.out.println("Processing binary expression with operator: " + node.getOperator());
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        // TODO: Process boolean literal
    }

    @Override
    public void visit(CallExpr node) {
        // TODO: Process function call
    }

    @Override
    public void visit(IdentifierExpr node) {
        // TODO: Process identifier
        System.out.println("Processing identifier: " + node.getName());
    }

    @Override
    public void visit(IndexAccessExpr node) {
        // TODO: Process array/index access
    }

    @Override
    public void visit(IntLiteralExpr node) {
        // TODO: Process integer literal
        System.out.println("Processing int literal: " + node.getLongValue());
    }

    @Override
    public void visit(MemberAccessExpr node) {
        // TODO: Process member access
    }

    @Override
    public void visit(StringLiteralExpr node) {
        // TODO: Process string literal
    }

    @Override
    public void visit(StructLiteralExpr node) {
        // TODO: Process struct literal
    }

    @Override
    public void visit(UnaryExpr node) {
        // TODO: Process unary expression
    }
    
}
