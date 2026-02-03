package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

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
        // First check local context
        if (context.getLocalContext().hasValue(name)) {
            return context.getLocalContext().getValue(name);
        }
        
        // Then check global context for variables/constants
        if (context.getGlobalContext().hasGlobalVariable(name)) {
            return context.getGlobalContext().getGlobalValue(name);
        }
        
        // Then check global context for functions
        Object function = context.getGlobalContext().getFunction(name);
        if (function != null) {
            return function;
        }
        
        // If not found, throw an exception
        throw new RuntimeException("Undefined identifier: '" + name + "'");
    }
}
