package org.clnlang.compiled.binary.expressions.unary;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.context.ExecutionContext;

public class UnaryExpressionInt implements CExecutable {

    private int operand;
    private int target;

    private boolean operand_is_global;
    private boolean target_is_global;

    private UnaryOperators operator;

    public UnaryExpressionInt(int operand, int target, boolean operand_is_global, boolean target_is_global, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operand_is_global = operand_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        long operandValue = operand_is_global ?
            context.getGlobalContext().getLong(operand) :
            context.getCurrentLocalContext().getLong(operand);
        long result;

        switch (operator) {
            case MINUS:
                result = -operandValue;
                break;
            case PLUSPLUS:
                result = operandValue + 1;
                if (operand_is_global) {
                    context.getGlobalContext().setLong(operand, result);
                } else {
                    context.getCurrentLocalContext().setLong(operand, result);
                }
                break;
            case MINUSMINUS:
                result = operandValue - 1;
                if (operand_is_global) {
                    context.getGlobalContext().setLong(operand, result);
                } else {
                    context.getCurrentLocalContext().setLong(operand, result);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected operator for int: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setLong(target, result);
        } else {
            context.getCurrentLocalContext().setLong(target, result);
        }
    }

    @Override
    public int[] getResults() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[]{Types.INT};
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
