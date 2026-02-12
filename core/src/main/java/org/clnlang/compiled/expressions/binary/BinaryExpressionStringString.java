package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionStringString implements Instruction {

    private int left;
    private int right;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionStringString(int left, int right, int target, BinaryOperators operator) {
        this.left = left;
        this.right = right;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        String leftValue = localContext.getString(left);
        String rightValue = localContext.getString(right);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            case EQUALS:
                // For string comparison, you might want to store boolean result
                // This is a simplified version
                throw new IllegalStateException("String comparison should use comparison instructions");
            default:
                throw new IllegalStateException("Unexpected operator for strings: " + operator);
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
