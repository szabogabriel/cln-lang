package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionIntString implements Instruction {

    private int left_int;
    private int right_string;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionIntString(int left_int, int right_string, int target, BinaryOperators operator) {
        this.left_int = left_int;
        this.right_string = right_string;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        long leftValue = localContext.getLong(left_int);
        String rightValue = localContext.getString(right_string);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for int-string: " + operator);
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
