package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of an array literal expression.
 * Example: [1, 2, 3] or ["hello", "world"]
 */
public class ArrayLiteralExprImpl implements CompiledExpr {
    private List<CompiledExpr> elements;

    public ArrayLiteralExprImpl(List<CompiledExpr> elements) {
        this.elements = elements;
    }

    public List<CompiledExpr> getElements() {
        return elements;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        List<Object> result = new ArrayList<>();
        for (CompiledExpr element : elements) {
            result.add(element.evaluate(context));
        }
        return result;
    }
}
