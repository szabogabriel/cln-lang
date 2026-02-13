package org.clnlang.compiled.expressions.unary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionDec implements Instruction {

    private int operand;
    private int target;

    private boolean operand_is_global;
    private boolean target_is_global;

    private UnaryOperators operator;

    public UnaryExpressionDec(int operand, int target, boolean operand_is_global, boolean target_is_global, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operand_is_global = operand_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        BigDecimal operandValue = operand_is_global ?
            context.getGlobalContext().getBigDecimal(operand) :
            context.getCurrentLocalContext().getBigDecimal(operand);
        BigDecimal result;

        switch (operator) {
            case MINUS:
                result = operandValue.negate();
                break;
            case PLUSPLUS:
                result = operandValue.add(BigDecimal.ONE);
                if (operand_is_global) {
                    context.getGlobalContext().setBigDecimal(operand, result);
                } else {
                    context.getCurrentLocalContext().setBigDecimal(operand, result);
                }
                break;
            case MINUSMINUS:
                result = operandValue.subtract(BigDecimal.ONE);
                if (operand_is_global) {
                    context.getGlobalContext().setBigDecimal(operand, result);
                } else {
                    context.getCurrentLocalContext().setBigDecimal(operand, result);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected operator for decimal: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setBigDecimal(target, result);
        } else {
            context.getCurrentLocalContext().setBigDecimal(target, result);
        }
    }

    @Override
    public int[] result() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultType() {
        return new Types[]{Types.DEC};
    }
    
}
