package org.clnlang.compiled.expressions.unary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionInt implements Instruction {

    private int operand;
    private int target;
    private UnaryOperators operator;

    public UnaryExpressionInt(int operand, int target, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        long operandValue = localContext.getLong(operand);
        long result;

        switch (operator) {
            case MINUS:
                result = -operandValue;
                break;
            case PLUSPLUS:
                result = operandValue + 1;
                localContext.setLong(operand, result);  // Modify operand in place
                break;
            case MINUSMINUS:
                result = operandValue - 1;
                localContext.setLong(operand, result);  // Modify operand in place
                break;
            default:
                throw new IllegalStateException("Unexpected operator for int: " + operator);
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
