package org.clnlang.compiled.expressions.unary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionBool implements Instruction {

    private int operand;
    private int target;
    private UnaryOperators operator;

    public UnaryExpressionBool(int operand, int target, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        boolean operandValue = localContext.getBoolean(operand);
        boolean result;

        switch (operator) {
            case NOT:
                result = !operandValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for boolean: " + operator);
        }

        localContext.setBoolean(target, result);
    }

    @Override
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.BOOL};
    }
    
}
