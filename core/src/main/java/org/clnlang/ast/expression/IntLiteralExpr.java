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

    public int getIntValue() {
        return Integer.parseInt(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        // TODO: implement visitor method
        // Can be extended if needed
    }

    public String toString() {
        return "IntLiteral(" + value + ")";
    }
}
