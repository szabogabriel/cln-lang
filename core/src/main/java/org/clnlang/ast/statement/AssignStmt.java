package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;

/**
 * Assignment statement: lvalue = expr ;
 */
public class AssignStmt extends Stmt {
    private Expr lvalue;
    private Expr value;

    public AssignStmt(Expr lvalue, Expr value) {
        this.lvalue = lvalue;
        this.value = value;
    }

    public Expr getLvalue() {
        return lvalue;
    }

    public Expr getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    // Can be extended if needed
    public String toString() {
        return lvalue + " = " + value;
    }
}
