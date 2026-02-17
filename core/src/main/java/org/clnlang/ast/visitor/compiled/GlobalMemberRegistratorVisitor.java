package org.clnlang.ast.visitor.compiled;

import java.io.File;
import java.util.List;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.ImportDeclNode;
import org.clnlang.ast.declaration.PackageDeclNode;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.declaration.StructDeclNode;
import org.clnlang.ast.declaration.UnionDeclNode;
import org.clnlang.ast.expression.BinaryExpr;
import org.clnlang.ast.expression.BoolLiteralExpr;
import org.clnlang.ast.expression.CallExpr;
import org.clnlang.ast.expression.DecLiteralExpr;
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

public class GlobalMemberRegistratorVisitor implements ASTVisitor {

    public GlobalMemberRegistratorVisitor(List<File> sourceFiles) {
        // No initialization needed for now
    }

    @Override
    public void visit(ProgramNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(PackageDeclNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(ImportDeclNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(StructDeclNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(UnionDeclNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(FunctionDeclNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(BlockNode node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(AssignStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(EmptyStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(ExprStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(IfStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(ReturnStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(SwitchStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(TupleAssignStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(VarDeclStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(WhileStmt node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(BinaryExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(CallExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(DecLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(IdentifierExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(IndexAccessExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(IntLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(MemberAccessExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(StringLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(StructLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }

    @Override
    public void visit(UnaryExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
    
}
