package org.clnlang.compile.expression;

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
        Object localValue = context.getLocalContext().getValue(name);
        if (localValue != null) {
            return localValue;
        }
        
        // Then check global context for functions
        Object function = context.getGlobalContext().getFunction(name);
        if (function != null) {
            return function;
        }
        
        // Then check global context for variables/constants
        Object globalValue = context.getGlobalContext().getGlobalValue(name);
        if (globalValue != null) {
            return globalValue;
        }
        
        // If not found, throw an exception
        throw new RuntimeException("Undefined identifier: '" + name + "'");
    }
}
