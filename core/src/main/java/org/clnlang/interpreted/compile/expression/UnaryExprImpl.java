package org.clnlang.interpreted.compile.expression;

import java.math.BigDecimal;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

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
                if (value instanceof Long) {
                    return -(Long) value;
                } else if (value instanceof BigDecimal) {
                    return ((BigDecimal) value).negate();
                }
                throw new RuntimeException("Cannot apply '-' operator to non-numeric value: " + value);
                
            default:
                throw new RuntimeException("Unknown unary operator: " + operator);
        }
    }
    
    @Override
    public long longValue(ExecutionContext context) throws Exception {
        // Optimized path for integer negation (zero boxing!)
        if ("-".equals(operator)) {
            return -operand.longValue(context);
        }
        // Fallback to generic evaluate()
        Object result = evaluate(context);
        if (result instanceof Long) {
            return (Long) result;
        }
        throw new IllegalStateException("Expected long result from unary operator: " + operator);
    }
    
    @Override
    public boolean boolValue(ExecutionContext context) throws Exception {
        // Optimized path for boolean NOT (zero boxing!)
        if ("!".equals(operator)) {
            return !operand.boolValue(context);
        }
        // Fallback to generic evaluate()
        Object result = evaluate(context);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        throw new IllegalStateException("Expected boolean result from unary operator: " + operator);
    }


}
