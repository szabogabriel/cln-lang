package org.clnlang.compile.expression;

import org.clnlang.compile.ExecutionContext;

/**
 * Compiled representation of an identifier expression.
 */
public class IdentifierExprImpl implements CompiledExpr {
    private String name;

    public IdentifierExprImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        // Look up variable/function in context
        return null; // TODO: implement context lookup
    }
}
