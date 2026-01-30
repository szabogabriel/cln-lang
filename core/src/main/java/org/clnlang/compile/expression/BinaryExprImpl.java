package org.clnlang.compile.expression;

import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of a binary expression.
 */
public class BinaryExprImpl implements CompiledExpr {
    private CompiledExpr left;
    private String operator;
    private CompiledExpr right;

    public BinaryExprImpl(CompiledExpr left, String operator, CompiledExpr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public CompiledExpr getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public CompiledExpr getRight() {
        return right;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object leftVal = left.evaluate(context);
        Object rightVal = right.evaluate(context);
        // Perform binary operation based on operator
        return null; // TODO: implement operator logic
    }
}
