package org.clnlang.compile.expression;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.runtime.FunctionInvoker;

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
        
        // Evaluate all arguments in the caller's context
        List<Object> argValues = new ArrayList<>();
        for (CompiledExpr arg : arguments) {
            argValues.add(arg.evaluate(context));
        }
        
        // Delegate to runtime invoker for call frame management
        return FunctionInvoker.invoke(funcDecl, argValues, context);
    }
}
