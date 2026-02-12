package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionStringInt implements Instruction {

    private int left_string;
    private int right_int;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionStringInt(int left_string, int right_int, int target, BinaryOperators operator) {
        this.left_string = left_string;
        this.right_int = right_int;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        String leftValue = localContext.getString(left_string);
        long rightValue = localContext.getLong(right_int);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-int: " + operator);
        }

        localContext.setString(target, result);
    }

    @Override
    public int result() {
        return target;
    }

    @Override
    public Types getResultType() {
        return Types.STRING;
    }
    
}
