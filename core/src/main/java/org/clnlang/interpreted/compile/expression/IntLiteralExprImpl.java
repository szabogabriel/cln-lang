package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of an integer literal.
 */
public class IntLiteralExprImpl implements CompiledExpr {
    private long value;

    public IntLiteralExprImpl(String value) {
        this.value = Long.parseLong(value);
    }

    public IntLiteralExprImpl(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        return value;
    }
}
