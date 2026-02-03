package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Compiled representation of an index access expression.
 */
public class IndexAccessExprImpl implements CompiledExpr {
    private CompiledExpr array;
    private CompiledExpr index;

    public IndexAccessExprImpl(CompiledExpr array, CompiledExpr index) {
        this.array = array;
        this.index = index;
    }

    public CompiledExpr getArray() {
        return array;
    }

    public CompiledExpr getIndex() {
        return index;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object arrayObj = array.evaluate(context);
        Object indexObj = index.evaluate(context);
        // Perform index access
        return null; // TODO: implement array indexing
    }
}
