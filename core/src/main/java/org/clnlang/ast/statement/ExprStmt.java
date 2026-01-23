package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;

/**
 * Expression statement: expr ;
 */
public class ExprStmt extends Stmt {
    private Expr expression;

    public ExprStmt(Expr expression) {
        this.expression = expression;
    }

    public Expr getExpression() {
        return expression;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        //TODO
    }

    // Can be extended if needed
    public String toString() {
        return expression.toString();
    }
}
