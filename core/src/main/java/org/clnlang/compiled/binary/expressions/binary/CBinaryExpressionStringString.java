package org.clnlang.compiled.binary.expressions.binary;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

public class CBinaryExpressionStringString extends CExpression implements CExecutable {

    private int left;
    private int right;
    private int target;

    private boolean left_is_global;
    private boolean right_is_global;
    private boolean target_is_global;

    private BinaryOperators operator;

    public CBinaryExpressionStringString(int left, int right, int target, boolean left_is_global, boolean right_is_global, boolean target_is_global, BinaryOperators operator) {
        super(ExpressionType.BINARY_EXPRESSION_STRING_STRING, new Types[] {Types.STRING});
        this.left = left;
        this.right = right;
        this.target = target;
        this.left_is_global = left_is_global;
        this.right_is_global = right_is_global;
        this.target_is_global = target_is_global;
        this.operator = operator;
    }

    @Override
    public void execute(ExecutionContext context) {
        String leftValue = left_is_global ?
            context.getGlobalContext().getString(left) :
            context.getCurrentLocalContext().getString(left);
        String rightValue = right_is_global ?
            context.getGlobalContext().getString(right) :
            context.getCurrentLocalContext().getString(right);
        String result;

        switch (operator) {
            case PLUS:
                result = leftValue + rightValue;
                break;
            case EQUALS:
                // For string comparison, you might want to store boolean result
                // This is a simplified version
                throw new IllegalStateException("String comparison should use comparison instructions");
            default:
                throw new IllegalStateException("Unexpected operator for strings: " + operator);
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
