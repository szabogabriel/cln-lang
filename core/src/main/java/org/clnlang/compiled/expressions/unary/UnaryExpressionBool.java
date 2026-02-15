package org.clnlang.compiled.expressions.unary;

import org.clnlang.compiled.CExecutable;
import org.clnlang.compiled.Types;
import org.clnlang.compiled.context.ExecutionContext;

public class UnaryExpressionBool implements CExecutable {

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
    public int[] getResults() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[]{Types.BOOL};
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
