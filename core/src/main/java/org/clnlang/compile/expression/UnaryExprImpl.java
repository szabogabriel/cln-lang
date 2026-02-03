package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Compiled representation of a unary expression.
 */
public class UnaryExprImpl implements CompiledExpr {
    private String operator;
    private CompiledExpr operand;

    public UnaryExprImpl(String operator, CompiledExpr operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public CompiledExpr getOperand() {
        return operand;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object value = operand.evaluate(context);
        
        switch (operator) {
            case "!":
                // Boolean NOT
                if (value instanceof Boolean) {
                    return !(Boolean) value;
                }
                throw new RuntimeException("Cannot apply '!' operator to non-boolean value: " + value);
                
            case "-":
                // Numeric negation
                if (value instanceof Integer) {
                    return -(Integer) value;
                }
                throw new RuntimeException("Cannot apply '-' operator to non-numeric value: " + value);
                
            default:
                throw new RuntimeException("Unknown unary operator: " + operator);
        }
    }
}
