package org.clnlang.ast.visitor;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.*;
import org.clnlang.ast.expression.*;
import org.clnlang.ast.statement.*;

/**
 * Visitor interface for traversing the AST.
 */
public interface ASTVisitor {
    // Declarations
    void visit(ProgramNode node);
    void visit(PackageDeclNode node);
    void visit(ImportDeclNode node);
    void visit(StructDeclNode node);
    void visit(UnionDeclNode node);
    void visit(FunctionDeclNode node);
    
    // Block
    void visit(BlockNode node);
    
    // Statements
    void visit(AssignStmt node);
    void visit(EmptyStmt node);
    void visit(ExprStmt node);
    void visit(IfStmt node);
    void visit(ReturnStmt node);
    void visit(SwitchStmt node);
    void visit(TupleAssignStmt node);
    void visit(VarDeclStmt node);
    void visit(WhileStmt node);
    
    // Expressions
    void visit(BinaryExpr node);
    void visit(BoolLiteralExpr node);
    void visit(CallExpr node);
    void visit(IdentifierExpr node);
    void visit(IndexAccessExpr node);
    void visit(IntLiteralExpr node);
    void visit(MemberAccessExpr node);
    void visit(StringLiteralExpr node);
    void visit(StructLiteralExpr node);
    void visit(UnaryExpr node);
}
