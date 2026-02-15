package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionStringInt implements Instruction {

    private int left_string;
    private int right_int;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public BinaryExpressionStringInt(int left_string, int right_int, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        this.left_string = left_string;
        this.right_int = right_int;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        String leftValue = left_is_global ?
            context.getGlobalContext().getString(left_string) :
            context.getCurrentLocalContext().getString(left_string);
        long rightValue = right_is_global ?
            context.getGlobalContext().getLong(right_int) :
            context.getCurrentLocalContext().getLong(right_int);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-int: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setString(target, result);
        } else {
            context.getCurrentLocalContext().setString(target, result);
        }
    }

    @Override
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.STRING};
    }
    
}
