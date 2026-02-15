package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionIntInt implements Instruction {

    private int left;
    private int right;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public BinaryExpressionIntInt(int left, int right, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        this.left = left;
        this.right = right;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        long leftValue = left_is_global ?
            context.getGlobalContext().getLong(left) :
            context.getCurrentLocalContext().getLong(left);
        long rightValue = right_is_global ?
            context.getGlobalContext().getLong(right) :
            context.getCurrentLocalContext().getLong(right);
        long result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            case MINUS:
                result = leftValue - rightValue;
                break;
            case MULTIPLY:
                result = leftValue * rightValue;
                break;
            case DIVIDE:
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue / rightValue;
                break;
            case MODULO:
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue % rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setLong(target, result);
        } else {
            context.getCurrentLocalContext().setLong(target, result);
        }
    }

    @Override
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.INT};
    }
    
}
