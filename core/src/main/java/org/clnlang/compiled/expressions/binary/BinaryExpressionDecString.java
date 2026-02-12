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

    private BinaryOperators operator;

    public BinaryExpressionDecString(int left_dec, int right_string, int target, BinaryOperators operator) {
        this.left_dec = left_dec;
        this.right_string = right_string;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        BigDecimal leftValue = localContext.getBigDecimal(left_dec);
        String rightValue = localContext.getString(right_string);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue.toString() + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for decimal-string: " + operator);
        }

        localContext.setString(target, result);
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
