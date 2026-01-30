package org.clnlang.compile.expression;

import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of an integer literal.
 */
public class IntLiteralExprImpl implements CompiledExpr {
    private int value;

    public IntLiteralExprImpl(String value) {
        this.value = Integer.parseInt(value);
    }

    public IntLiteralExprImpl(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        return value;
    }
}
