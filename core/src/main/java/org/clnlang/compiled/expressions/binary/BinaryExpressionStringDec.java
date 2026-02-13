package org.clnlang.compiled.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionStringDec implements Instruction {

    private int left_string;
    private int right_dec;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public BinaryExpressionStringDec(int left_string, int right_dec, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        this.left_string = left_string;
        this.right_dec = right_dec;
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
        BigDecimal rightValue = right_is_global ?
            context.getGlobalContext().getBigDecimal(right_dec) :
            context.getCurrentLocalContext().getBigDecimal(right_dec);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue.toString();
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-decimal: " + operator);
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
