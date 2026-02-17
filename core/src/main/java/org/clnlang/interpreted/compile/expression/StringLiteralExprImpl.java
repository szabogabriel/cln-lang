package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a string literal.
 */
public class StringLiteralExprImpl implements CompiledExpr {
    private String value;

    public StringLiteralExprImpl(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        return value;
    }
}
