package org.clnlang.compiled.expressions;

import java.math.BigDecimal;

import org.clnlang.compiled.Types;

public abstract class CExpression {

    public static enum ExpressionType {
        INT_LITERAL,
        DEC_LITERAL,
        BOOL_LITERAL,
        STRING_LITERAL,

        IDENTIFIER,

        BINARY_EXPRESSION,
        BINARY_EXPRESSION_BOOL_BOOL,
        BINARY_EXPRESSION_BOOL_STRING,
        BINARY_EXPRESSION_DEC_DEC,
        BINARY_EXPRESSION_DEC_INT,
        BINARY_EXPRESSION_DEC_STRING,
        BINARY_EXPRESSION_INT_DEC,
        BINARY_EXPRESSION_INT_INT,
        BINARY_EXPRESSION_INT_STRING,
        BINARY_EXPRESSION_STRING_BOOL,
        BINARY_EXPRESSION_STRING_DEC,
        BINARY_EXPRESSION_STRING_INT,
        BINARY_EXPRESSION_STRING_STRING,
        
        ;
    }

    private final ExpressionType type;
    private Types[] resultTypes;

    public CExpression(ExpressionType type) {
        this.type = type;
    }

    public CExpression(ExpressionType type, Types[] resultTypes) {
        this.type = type;
        this.resultTypes = resultTypes;
    }

    public ExpressionType getExpressionType() {
        return type;
    }

    public int [] getResults() {
        return null;
    }

    public Types[] getResultTypes() {
        return resultTypes;
    }

    public long getIntValue() {
        throw new UnsupportedOperationException("Not an integer literal expression");
    }

    public BigDecimal getDecValue() {
        throw new UnsupportedOperationException("Not a decimal literal expression");
    }

    public boolean getBoolValue() {
        throw new UnsupportedOperationException("Not a boolean literal expression");
    }

    public String getStringValue() {
        throw new UnsupportedOperationException("Not a string literal expression");
    }

    public boolean isGlobal() {
        return false;
    }
    
}
