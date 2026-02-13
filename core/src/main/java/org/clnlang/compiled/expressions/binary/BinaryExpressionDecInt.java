package org.clnlang.compiled.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionDecInt implements Instruction {

    private int left_dec;
    private int right_int;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public BinaryExpressionDecInt(int left_dec, int right_int, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        this.left_dec = left_dec;
        this.right_int = right_int;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        BigDecimal leftValue = left_is_global ?
            context.getGlobalContext().getBigDecimal(left_dec) :
            context.getCurrentLocalContext().getBigDecimal(left_dec);
        long rightLongValue = right_is_global ?
            context.getGlobalContext().getLong(right_int) :
            context.getCurrentLocalContext().getLong(right_int);
        BigDecimal rightValue = BigDecimal.valueOf(rightLongValue);
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
            case MODULO:
                if (rightValue.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue.remainder(rightValue);
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
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.DEC};
    }
    
}
