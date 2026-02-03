package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

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
