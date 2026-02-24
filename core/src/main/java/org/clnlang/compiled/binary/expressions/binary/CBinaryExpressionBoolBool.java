package org.clnlang.compiled.binary.expressions.binary;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

public class CBinaryExpressionBoolBool extends CExpression implements CExecutable {

    private int left_bool;
    private int right_bool;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public CBinaryExpressionBoolBool(int left_bool, int right_bool, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        super(ExpressionType.BINARY_EXPRESSION_BOOL_BOOL);
        this.left_bool = left_bool;
        this.right_bool = right_bool;
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
        boolean rightValue = right_is_global ?
            context.getGlobalContext().getBoolean(right_bool) :
            context.getCurrentLocalContext().getBoolean(right_bool);
        boolean result;

        switch (operator) {
            case AND:
                result = leftValue && rightValue;
                break;
            case OR:
                result = leftValue || rightValue;
                break;
            case EQUALS:
                result = leftValue == rightValue;
                break;
            case NOT_EQUALS:
                result = leftValue != rightValue;
                break;
            default:
                throw new IllegalStateException("Unexpected operator for boolean-boolean: " + operator);
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
        return new Types[]{Types.STRING};
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
