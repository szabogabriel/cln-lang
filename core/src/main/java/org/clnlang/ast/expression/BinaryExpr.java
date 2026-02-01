package org.clnlang.ast.expression;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Binary expression with an operator
 */
public class BinaryExpr extends Expr {
    private Expr left;
    private String operator;
    private Expr right;

    public BinaryExpr(Expr left, String operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}
