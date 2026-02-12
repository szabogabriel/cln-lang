package org.clnlang.compiled.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;
import org.clnlang.compiled.types.Types;

public class BinaryExpressionDecInt implements Instruction {

    private int left_dec;
    private int right_int;
    private int target;

    private BinaryOperators operator;

    public BinaryExpressionDecInt(int left_dec, int right_int, int target, BinaryOperators operator) {
        this.left_dec = left_dec;
        this.right_int = right_int;
        this.target = target;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext localContext = context.getCurrentLocalContext();
        
        BigDecimal leftValue = localContext.getBigDecimal(left_dec);
        BigDecimal rightValue = BigDecimal.valueOf(localContext.getLong(right_int));
        BigDecimal result;

        switch (operator) {
            case PLUS:
                result = leftValue.add(rightValue);
                break;
            case MINUS:
                result = leftValue.subtract(rightValue);
                break;
            case MULTIPLY:
                result = leftValue.multiply(rightValue);
                break;
            case DIVIDE:
                if (rightValue.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue.divide(rightValue);
                break;
            case MODULO:
                if (rightValue.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = leftValue.remainder(rightValue);
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }

        localContext.setBigDecimal(target, result);
    }

    @Override
    public int result() {
        return target;
    }

    @Override
    public Types getResultType() {
        return Types.DEC;
    }
    
}
