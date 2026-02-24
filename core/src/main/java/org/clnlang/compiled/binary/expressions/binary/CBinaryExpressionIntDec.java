package org.clnlang.compiled.binary.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

public class CBinaryExpressionIntDec extends CExpression implements CExecutable {

    private int left_int;
    private int right_dec;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public CBinaryExpressionIntDec(int left_int, int right_dec, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        super(ExpressionType.BINARY_EXPRESSION_INT_DEC);
        this.left_int = left_int;
        this.right_dec = right_dec;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        long leftLongValue = left_is_global ?
            context.getGlobalContext().getLong(left_int) :
            context.getCurrentLocalContext().getLong(left_int);
        BigDecimal leftValue = BigDecimal.valueOf(leftLongValue);
        BigDecimal rightValue = right_is_global ?
            context.getGlobalContext().getBigDecimal(right_dec) :
            context.getCurrentLocalContext().getBigDecimal(right_dec);
        BigDecimal result;

        switch (operator) {
            case PLUS:
                result = leftValue.add(rightValue);
                break;
            case MINUS:
                result = leftValue.subtract(rightValue);
                break;
            case MULTIPLY:
                result = leftValue.multiply(rightValue);
                break;
            case DIVIDE:
                if (rightValue.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue.divide(rightValue);
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setBigDecimal(target, result);
        } else {
            context.getCurrentLocalContext().setBigDecimal(target, result);
        }
    }

    @Override
    public int[] getResults() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[]{Types.DEC};
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
