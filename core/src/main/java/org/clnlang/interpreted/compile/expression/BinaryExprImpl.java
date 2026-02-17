package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.math.BigDecimal;

/**
 * Compiled representation of a binary expression.
 */
public class BinaryExprImpl implements CompiledExpr {
    private CompiledExpr left;
    private Operator operator;
    private CompiledExpr right;

    public BinaryExprImpl(CompiledExpr left, Operator operator, CompiledExpr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public CompiledExpr getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public CompiledExpr getRight() {
        return right;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object leftVal = left.evaluate(context);
        Object rightVal = right.evaluate(context);

        switch (operator) {
            case PLUS:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal + (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).add((BigDecimal) rightVal);
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).add(BigDecimal.valueOf((Long) rightVal));
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).add((BigDecimal) rightVal);
                }
                if (leftVal instanceof String || rightVal instanceof String) {
                    return String.valueOf(leftVal) + String.valueOf(rightVal);
                }
                throw new IllegalArgumentException("Invalid operands for + operator");
            
            case MINUS:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal - (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).subtract((BigDecimal) rightVal);
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).subtract(BigDecimal.valueOf((Long) rightVal));
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).subtract((BigDecimal) rightVal);
                }
                throw new IllegalArgumentException("Invalid operands for - operator");
            
            case STAR:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal * (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).multiply((BigDecimal) rightVal);
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).multiply(BigDecimal.valueOf((Long) rightVal));
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).multiply((BigDecimal) rightVal);
                }
                throw new IllegalArgumentException("Invalid operands for * operator");
            
            case SLASH:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    if ((Long) rightVal == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return (Long) leftVal / (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    if (((BigDecimal) rightVal).compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return ((BigDecimal) leftVal).divide((BigDecimal) rightVal, java.math.MathContext.DECIMAL128);
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    if ((Long) rightVal == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return ((BigDecimal) leftVal).divide(BigDecimal.valueOf((Long) rightVal), java.math.MathContext.DECIMAL128);
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    if (((BigDecimal) rightVal).compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return BigDecimal.valueOf((Long) leftVal).divide((BigDecimal) rightVal, java.math.MathContext.DECIMAL128);
                }
                throw new IllegalArgumentException("Invalid operands for / operator");
            
            case EQ:
                if (leftVal == null && rightVal == null) {
                    return true;
                }
                if (leftVal == null || rightVal == null) {
                    return false;
                }
                return leftVal.equals(rightVal);
            
            case NEQ:
                if (leftVal == null && rightVal == null) {
                    return false;
                }
                if (leftVal == null || rightVal == null) {
                    return true;
                }
                return !leftVal.equals(rightVal);
            
            case LT:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal < (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) < 0;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).compareTo(BigDecimal.valueOf((Long) rightVal)) < 0;
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).compareTo((BigDecimal) rightVal) < 0;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) < 0;
                }
                throw new IllegalArgumentException("Invalid operands for < operator");
            
            case LTE:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal <= (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) <= 0;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).compareTo(BigDecimal.valueOf((Long) rightVal)) <= 0;
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).compareTo((BigDecimal) rightVal) <= 0;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) <= 0;
                }
                throw new IllegalArgumentException("Invalid operands for <= operator");
            
            case GT:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal > (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) > 0;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).compareTo(BigDecimal.valueOf((Long) rightVal)) > 0;
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).compareTo((BigDecimal) rightVal) > 0;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) > 0;
                }
                throw new IllegalArgumentException("Invalid operands for > operator");
            
            case GTE:
                if (leftVal instanceof Long && rightVal instanceof Long) {
                    return (Long) leftVal >= (Long) rightVal;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) {
                    return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) >= 0;
                }
                if (leftVal instanceof BigDecimal && rightVal instanceof Long) {
                    return ((BigDecimal) leftVal).compareTo(BigDecimal.valueOf((Long) rightVal)) >= 0;
                }
                if (leftVal instanceof Long && rightVal instanceof BigDecimal) {
                    return BigDecimal.valueOf((Long) leftVal).compareTo((BigDecimal) rightVal) >= 0;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) >= 0;
                }
                throw new IllegalArgumentException("Invalid operands for >= operator");
            
            case AND:
                if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
                    return (Boolean) leftVal && (Boolean) rightVal;
                }
                throw new IllegalArgumentException("Invalid operands for && operator");
            
            case OR:
                if (leftVal instanceof Boolean && rightVal instanceof Boolean) {
                    return (Boolean) leftVal || (Boolean) rightVal;
                }
                throw new IllegalArgumentException("Invalid operands for || operator");
            
            default:
                throw new UnsupportedOperationException("Operator not implemented: " + operator);
        }
    }
    
    @Override
    public long longValue(ExecutionContext context) throws Exception {
        // Optimized path for integer arithmetic (zero boxing!)
        switch (operator) {
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
                // Assume both operands are integers - use typed methods (zero boxing!)
                long leftVal = left.longValue(context);
                long rightVal = right.longValue(context);
                
                switch (operator) {
                    case PLUS:
                        return leftVal + rightVal;
                    case MINUS:
                        return leftVal - rightVal;
                    case STAR:
                        return leftVal * rightVal;
                    case SLASH:
                        if (rightVal == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        return leftVal / rightVal;
                }
                break;
            default:
                // For non-arithmetic operators, fallback to generic evaluate()
                Object result = evaluate(context);
                if (result instanceof Long) {
                    return (Long) result;
                }
                throw new IllegalStateException("Expected long result from operator: " + operator);
        }
        throw new IllegalStateException("Unreachable");
    }
    
    @Override
    public boolean boolValue(ExecutionContext context) throws Exception {
        // Optimized path for comparisons and logical operations (zero boxing!)
        switch (operator) {
            case LT:
            case LTE:
            case GT:
            case GTE:
            case EQ:
            case NEQ:
                // Assume both operands are integers - use typed methods (zero boxing!)
                long leftVal = left.longValue(context);
                long rightVal = right.longValue(context);
                
                switch (operator) {
                    case LT:
                        return leftVal < rightVal;
                    case LTE:
                        return leftVal <= rightVal;
                    case GT:
                        return leftVal > rightVal;
                    case GTE:
                        return leftVal >= rightVal;
                    case EQ:
                        return leftVal == rightVal;
                    case NEQ:
                        return leftVal != rightVal;
                }
                break;
            case AND:
            case OR:
                // Boolean logical operations
                boolean leftBool = left.boolValue(context);
                boolean rightBool = right.boolValue(context);
                
                switch (operator) {
                    case AND:
                        return leftBool && rightBool;
                    case OR:
                        return leftBool || rightBool;
                }
                break;
            default:
                // For non-boolean operators, fallback to generic evaluate()
                Object result = evaluate(context);
                if (result instanceof Boolean) {
                    return (Boolean) result;
                }
                throw new IllegalStateException("Expected boolean result from operator: " + operator);
        }
        throw new IllegalStateException("Unreachable");
    }
}
