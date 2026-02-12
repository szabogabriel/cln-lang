package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionIntInt implements Instruction {

    private int left;
    private int right;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionIntInt(int left, int right, int target, BinaryOperators operator) {
        this.left = left;
        this.right = right;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        long leftValue = localContext.getLong(left);
        long rightValue = localContext.getLong(right);
        long result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            case MINUS:
                result = leftValue - rightValue;
                break;
            case MULTIPLY:
                result = leftValue * rightValue;
                break;
            case DIVIDE:
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue / rightValue;
                break;
            case MODULO:
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue % rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }

        localContext.setLong(target, result);
    }

    @Override
    public int result() {
        return target;
    }

    @Override
    public Types getResultType() {
        return Types.INT;
    }
    
}
