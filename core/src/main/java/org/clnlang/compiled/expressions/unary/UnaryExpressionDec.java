package org.clnlang.compiled.expressions.unary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class UnaryExpressionDec implements Instruction {

    private int operand;
    private int target;
    private UnaryOperators operator;

    public UnaryExpressionDec(int operand, int target, UnaryOperators operator) {
        this.operand = operand;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        BigDecimal operandValue = localContext.getBigDecimal(operand);
        BigDecimal result;

        switch (operator) {
            case MINUS:
                result = operandValue.negate();
                break;
            case PLUSPLUS:
                result = operandValue.add(BigDecimal.ONE);
                localContext.setBigDecimal(operand, result);  // Modify operand in place
                break;
            case MINUSMINUS:
                result = operandValue.subtract(BigDecimal.ONE);
                localContext.setBigDecimal(operand, result);  // Modify operand in place
                break;
            default:
                throw new IllegalStateException("Unexpected operator for decimal: " + operator);
        }

        localContext.setBigDecimal(target, result);
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
