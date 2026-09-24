package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.math.BigDecimal;

/**
 * Compiled representation of a binary expression.
 */
public class BinaryExprImpl implements CompiledExpr {
    private CompiledExpr left;
    private Operator operator;
    private CompiledExpr right;

    // Resolved once from the (already-compiled) operands, since their static type never
    // changes afterwards - avoids re-checking operand types on every evaluation.
    private final String staticType;
    private final boolean intOperands;   // both operands are statically known "int"
    private final boolean decOperands;   // both operands are statically known int/dec (mixed ok), not both int

    public BinaryExprImpl(CompiledExpr left, Operator operator, CompiledExpr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;

        String leftType = left.getStaticType();
        String rightType = right.getStaticType();
        this.intOperands = "int".equals(leftType) && "int".equals(rightType);
        this.decOperands = !intOperands && isNumeric(leftType) && isNumeric(rightType);
        this.staticType = computeStaticType(operator, leftType, rightType, intOperands, decOperands);
    }

    private static boolean isNumeric(String type) {
        return "int".equals(type) || "dec".equals(type);
    }

    private static String computeStaticType(Operator operator, String leftType, String rightType,
            boolean intOperands, boolean decOperands) {
        switch (operator) {
            case PLUS:
                if ("string".equals(leftType) || "string".equals(rightType)) {
                    return "string";
                }
                return intOperands ? "int" : (decOperands ? "dec" : null);
            case MINUS:
            case STAR:
            case SLASH:
                return intOperands ? "int" : (decOperands ? "dec" : null);
            case LT:
            case LTE:
            case GT:
            case GTE:
            case EQ:
            case NEQ:
            case AND:
            case OR:
                return "bool";
            default:
                return null;
        }
    }

    @Override
    public String getStaticType() {
        return staticType;
    }

    /**
     * Reads an operand known to be statically int-or-dec as a BigDecimal, promoting ints
     * without going through the generic (boxing) evaluate()/instanceof dispatch.
     */
    private static BigDecimal decOperand(CompiledExpr expr, ExecutionContext context) throws Exception {
        return "int".equals(expr.getStaticType())
                ? BigDecimal.valueOf(expr.longValue(context))
                : expr.decimalValue(context);
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
        if (intOperands) {
            switch (operator) {
                case PLUS:
                    return left.longValue(context) + right.longValue(context);
                case MINUS:
                    return left.longValue(context) - right.longValue(context);
                case STAR:
                    return left.longValue(context) * right.longValue(context);
                case SLASH: {
                    long leftVal = left.longValue(context);
                    long rightVal = right.longValue(context);
                    if (rightVal == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return leftVal / rightVal;
                }
                default:
                    break;
            }
        }
        // Fallback: generic evaluate() (decimal/string/unknown operands, or type mismatch)
        Object result = evaluate(context);
        if (result instanceof Long) {
            return (Long) result;
        }
        throw new RuntimeException("Expression does not evaluate to long: " + result);
    }
    
    @Override
    public BigDecimal decimalValue(ExecutionContext context) throws Exception {
        if (intOperands || decOperands) {
            BigDecimal leftVal = decOperand(left, context);
            BigDecimal rightVal = decOperand(right, context);
            switch (operator) {
                case PLUS:
                    return leftVal.add(rightVal);
                case MINUS:
                    return leftVal.subtract(rightVal);
                case STAR:
                    return leftVal.multiply(rightVal);
                case SLASH:
                    if (rightVal.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return leftVal.divide(rightVal, java.math.MathContext.DECIMAL128);
                default:
                    break;
            }
        }
        // Fallback: generic evaluate() (string/unknown operands, or type mismatch)
        Object result = evaluate(context);
        if (result instanceof BigDecimal) {
            return (BigDecimal) result;
        }
        throw new RuntimeException("Expression does not evaluate to BigDecimal: " + result);
    }
    
    @Override
    public boolean boolValue(ExecutionContext context) throws Exception {
        if (intOperands) {
            switch (operator) {
                case LT:
                case LTE:
                case GT:
                case GTE:
                case EQ:
                case NEQ: {
                    long leftVal = left.longValue(context);
                    long rightVal = right.longValue(context);
                    switch (operator) {
                        case LT: return leftVal < rightVal;
                        case LTE: return leftVal <= rightVal;
                        case GT: return leftVal > rightVal;
                        case GTE: return leftVal >= rightVal;
                        case EQ: return leftVal == rightVal;
                        case NEQ: return leftVal != rightVal;
                        default: break;
                    }
                    break;
                }
                default:
                    break;
            }
        } else if (decOperands) {
            switch (operator) {
                case LT:
                case LTE:
                case GT:
                case GTE:
                case EQ:
                case NEQ: {
                    BigDecimal leftVal = decOperand(left, context);
                    BigDecimal rightVal = decOperand(right, context);
                    switch (operator) {
                        case LT: return leftVal.compareTo(rightVal) < 0;
                        case LTE: return leftVal.compareTo(rightVal) <= 0;
                        case GT: return leftVal.compareTo(rightVal) > 0;
                        case GTE: return leftVal.compareTo(rightVal) >= 0;
                        // Match the generic evaluate() path, which uses Object.equals()
                        // (scale-sensitive for BigDecimal), not compareTo().
                        case EQ: return leftVal.equals(rightVal);
                        case NEQ: return !leftVal.equals(rightVal);
                        default: break;
                    }
                    break;
                }
                default:
                    break;
            }
        }
        if (operator == Operator.AND || operator == Operator.OR) {
            boolean leftBool = left.boolValue(context);
            boolean rightBool = right.boolValue(context);
            return operator == Operator.AND ? (leftBool && rightBool) : (leftBool || rightBool);
        }
        // Fallback: generic evaluate() (string operands, or type mismatch)
        Object result = evaluate(context);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        throw new RuntimeException("Expression does not evaluate to boolean: " + result);
    }
}
