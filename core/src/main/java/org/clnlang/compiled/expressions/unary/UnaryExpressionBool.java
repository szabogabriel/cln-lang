package org.clnlang.compiled.expressions.unary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionBool implements Instruction {

    private int operand;
    private int target;

    private boolean operand_is_global;
    private boolean target_is_global;

    private UnaryOperators operator;

    public UnaryExpressionBool(int operand, int target, boolean operand_is_global, boolean target_is_global, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operand_is_global = operand_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        boolean operandValue = operand_is_global ?
            context.getGlobalContext().getBoolean(operand) :
            context.getCurrentLocalContext().getBoolean(operand);
        boolean result;

        switch (operator) {
            case NOT:
                result = !operandValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for boolean: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setBoolean(target, result);
        } else {
            context.getCurrentLocalContext().setBoolean(target, result);
        }
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
