package org.clnlang.compiled.expressions.unary;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionInt implements Instruction {

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
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.INT};
    }
    
}
