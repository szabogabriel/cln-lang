package org.clnlang.compile.expression;

import org.clnlang.compile.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a function call expression.
 */
public class CallExprImpl implements CompiledExpr {
    private CompiledExpr function;
    private List<CompiledExpr> arguments;

    public CallExprImpl(CompiledExpr function, List<CompiledExpr> arguments) {
        this.function = function;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
    }

    public CompiledExpr getFunction() {
        return function;
    }

    public List<CompiledExpr> getArguments() {
        return arguments;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object funcObj = function.evaluate(context);
        List<Object> argValues = new ArrayList<>();
        for (CompiledExpr arg : arguments) {
            argValues.add(arg.evaluate(context));
        }
        // Invoke function with arguments
        return null; // TODO: implement function invocation
    }
}
