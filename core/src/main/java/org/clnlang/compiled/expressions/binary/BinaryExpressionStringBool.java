package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionStringBool implements Instruction {

    private int left_string;
    private int right_bool;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionStringBool(int left_string, int right_bool, int target, BinaryOperators operator) {
        this.left_string = left_string;
        this.right_bool = right_bool;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        String leftValue = localContext.getString(left_string);
        boolean rightValue = localContext.getBoolean(right_bool);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-boolean: " + operator);
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
