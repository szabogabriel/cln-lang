package org.clnlang.compile.expression;

import org.clnlang.runtime.context.ExecutionContext;

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
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal + (Integer) rightVal;
                }
                if (leftVal instanceof String || rightVal instanceof String) {
                    return String.valueOf(leftVal) + String.valueOf(rightVal);
                }
                throw new IllegalArgumentException("Invalid operands for + operator");
            
            case MINUS:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal - (Integer) rightVal;
                }
                throw new IllegalArgumentException("Invalid operands for - operator");
            
            case STAR:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal * (Integer) rightVal;
                }
                throw new IllegalArgumentException("Invalid operands for * operator");
            
            case SLASH:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    if ((Integer) rightVal == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return (Integer) leftVal / (Integer) rightVal;
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
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal < (Integer) rightVal;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) < 0;
                }
                throw new IllegalArgumentException("Invalid operands for < operator");
            
            case LTE:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal <= (Integer) rightVal;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) <= 0;
                }
                throw new IllegalArgumentException("Invalid operands for <= operator");
            
            case GT:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal > (Integer) rightVal;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return ((String) leftVal).compareTo((String) rightVal) > 0;
                }
                throw new IllegalArgumentException("Invalid operands for > operator");
            
            case GTE:
                if (leftVal instanceof Integer && rightVal instanceof Integer) {
                    return (Integer) leftVal >= (Integer) rightVal;
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
}
