package org.clnlang.ast.visitor;
import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.declaration.*;
/**
 * Visitor interface for traversing the AST.
 */
public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(PackageDeclNode node);
    void visit(ImportDeclNode node);
    void visit(StructDeclNode node);
    void visit(UnionDeclNode node);
    void visit(FunctionDeclNode node);
}
