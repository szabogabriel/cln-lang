package org.clnlang.compiled.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionDecString implements Instruction {

    private int left_dec;
    private int right_string;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public BinaryExpressionDecString(int left_dec, int right_string, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        this.left_dec = left_dec;
        this.right_string = right_string;
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
        String rightValue = right_is_global ?
            context.getGlobalContext().getString(right_string) :
            context.getCurrentLocalContext().getString(right_string);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue.toString() + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for decimal-string: " + operator);
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
