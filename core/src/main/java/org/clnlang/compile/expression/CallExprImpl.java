package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.FunctionInvoker;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a function call expression.
 * Delegates runtime execution to FunctionInvoker.
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
        // Evaluate the function expression to get the FunctionDeclImpl
        Object funcObj = function.evaluate(context);
        
        if (!(funcObj instanceof FunctionDeclImpl)) {
            throw new RuntimeException("Cannot call non-function object: " + funcObj);
        }
        
        FunctionDeclImpl funcDecl = (FunctionDeclImpl) funcObj;
        
        // Arguments are evaluated by FunctionInvoker directly into the callee's
        // registry slots (by index), avoiding an intermediate boxed List<Object>.
        return FunctionInvoker.invoke(funcDecl, arguments, context);
    }
}
