package org.clnlang.ast.expression;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * String literal
 */
public class StringLiteralExpr extends Expr {
    private String value;

    public StringLiteralExpr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        // TODO: implement visitor method
        // Can be extended if needed
    }

    public String toString() {
        return "\"" + value + "\"";
    }
}
