package org.clnlang.ast.expression;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Integer literal
 */
public class IntLiteralExpr extends Expr {
    private String value;

    public IntLiteralExpr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public long getLongValue() {
        return Long.parseLong(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        // TODO: implement visitor method
        // Can be extended if needed
        visitor.visit(this);
    }

    public String toString() {
        return "IntLiteral(" + value + ")";
    }
}
