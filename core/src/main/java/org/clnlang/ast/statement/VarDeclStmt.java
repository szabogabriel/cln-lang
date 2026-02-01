package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;

/**
 * Variable declaration statement: var? type ID = expr ;
 */
public class VarDeclStmt extends Stmt {
    private boolean isVar;
    private String type;
    private String name;
    private Expr initializer;

    public VarDeclStmt(boolean isVar, String type, String name, Expr initializer) {
        this.isVar = isVar;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    public boolean isVar() {
        return isVar;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expr getInitializer() {
        return initializer;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return (isVar ? "var " : "") + type + " " + name + " = " + initializer;
    }
}