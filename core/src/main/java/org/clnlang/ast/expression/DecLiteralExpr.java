package org.clnlang.ast.expression;

import java.math.BigDecimal;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Decimal literal
 */
public class DecLiteralExpr extends Expr {
    private String value;

    public DecLiteralExpr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public BigDecimal getDecimalValue() {
        return new BigDecimal(value);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "DecLiteral(" + value + ")";
    }
}
