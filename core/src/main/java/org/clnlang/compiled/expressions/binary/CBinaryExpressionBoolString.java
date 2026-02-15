package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.CExecutable;
import org.clnlang.compiled.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.expressions.CExpression;

public class CBinaryExpressionBoolString extends CExpression implements CExecutable {

    private int left_bool;
    private int right_string;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public CBinaryExpressionBoolString(int left_bool, int right_string, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        super(ExpressionType.BINARY_EXPRESSION_BOOL_STRING);
        this.left_bool = left_bool;
        this.right_string = right_string;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        boolean leftValue = left_is_global ? 
            context.getGlobalContext().getBoolean(left_bool) :
            context.getCurrentLocalContext().getBoolean(left_bool);
        String rightValue = right_is_global ?
            context.getGlobalContext().getString(right_string) :
            context.getCurrentLocalContext().getString(right_string);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for boolean-string: " + operator);
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
