package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a boolean literal.
 */
public class BoolLiteralExprImpl implements CompiledExpr {
    private boolean value;

    public BoolLiteralExprImpl(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        return value;
    }
}
