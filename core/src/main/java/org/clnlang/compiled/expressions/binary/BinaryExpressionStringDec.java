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

    private BinaryOperators operator;

    public BinaryExpressionStringDec(int left_string, int right_dec, int target, BinaryOperators operator) {
        this.left_string = left_string;
        this.right_dec = right_dec;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        String leftValue = localContext.getString(left_string);
        BigDecimal rightValue = localContext.getBigDecimal(right_dec);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue.toString();
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-decimal: " + operator);
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
