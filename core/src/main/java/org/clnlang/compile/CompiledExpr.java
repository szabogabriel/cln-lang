package org.clnlang.compile;

import org.clnlang.runtime.context.ExecutionContext;

/**
 * Base interface for compiled expressions.
 */
public interface CompiledExpr extends CompiledAction {
    /**
     * Evaluates the expression and returns the result.
     */
    Object evaluate(ExecutionContext context) throws Exception;
    
    @Override
    default void execute(ExecutionContext context) throws Exception {
        evaluate(context);
    }
}
