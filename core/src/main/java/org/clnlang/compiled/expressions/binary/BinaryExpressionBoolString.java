package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionBoolString implements Instruction {

    private int left_bool;
    private int right_string;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionBoolString(int left_bool, int right_string, int target, BinaryOperators operator) {
        this.left_bool = left_bool;
        this.right_string = right_string;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        boolean leftValue = localContext.getBoolean(left_bool);
        String rightValue = localContext.getString(right_string);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for boolean-string: " + operator);
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
