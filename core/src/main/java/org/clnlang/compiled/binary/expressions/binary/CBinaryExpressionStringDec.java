package org.clnlang.compiled.binary.expressions.binary;

import java.math.BigDecimal;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

public class CBinaryExpressionStringDec extends CExpression implements CExecutable {

    private int left_string;
    private int right_dec;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public CBinaryExpressionStringDec(int left_string, int right_dec, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        super(ExpressionType.BINARY_EXPRESSION_STRING_DEC);
        this.left_string = left_string;
        this.right_dec = right_dec;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        String leftValue = left_is_global ?
            context.getGlobalContext().getString(left_string) :
            context.getCurrentLocalContext().getString(left_string);
        BigDecimal rightValue = right_is_global ?
            context.getGlobalContext().getBigDecimal(right_dec) :
            context.getCurrentLocalContext().getBigDecimal(right_dec);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue.toString();
                break;
            default:
                throw new IllegalStateException("Unexpected operator for string-decimal: " + operator);
        }

        if (target_is_global) {
            context.getGlobalContext().setString(target, result);
        } else {
            context.getCurrentLocalContext().setString(target, result);
        }
    }

    @Override
    public int[] getResults() {
        return new int[]{target};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[]{Types.STRING};
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
