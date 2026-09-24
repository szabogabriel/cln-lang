package org.clnlang.compile;

import java.math.BigDecimal;

import org.clnlang.runtime.context.ExecutionContext;

/**
 * Base interface for compiled expressions.
 * Provides both generic Object-based evaluation (for backward compatibility)
 * and typed primitive evaluation methods (for performance).
 */
public interface CompiledExpr extends CompiledAction {
    /**
     * Evaluates the expression and returns the result as an Object.
     * This may box primitive values.
     */
    Object evaluate(ExecutionContext context) throws Exception;
    
    /**
     * Evaluates the expression as a long value (no boxing).
     * Throws RuntimeException if the expression doesn't evaluate to a long.
     */
    default long longValue(ExecutionContext context) throws Exception {
        Object value = evaluate(context);
        if (value instanceof Long) {
            return (Long) value;
        }
        throw new RuntimeException("Expression does not evaluate to long: " + value);
    }
    
    /**
     * Evaluates the expression as a BigDecimal value.
     * Throws RuntimeException if the expression doesn't evaluate to a BigDecimal.
     */
    default BigDecimal decimalValue(ExecutionContext context) throws Exception {
        Object value = evaluate(context);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        throw new RuntimeException("Expression does not evaluate to BigDecimal: " + value);
    }
    
    /**
     * Evaluates the expression as a boolean value (no boxing).
     * Throws RuntimeException if the expression doesn't evaluate to a boolean.
     */
    default boolean boolValue(ExecutionContext context) throws Exception {
        Object value = evaluate(context);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new RuntimeException("Expression does not evaluate to boolean: " + value);
    }
    
    /**
     * Evaluates the expression as a String value.
     * Throws RuntimeException if the expression doesn't evaluate to a String.
     */
    default String stringValue(ExecutionContext context) throws Exception {
        Object value = evaluate(context);
        if (value instanceof String) {
            return (String) value;
        }
        throw new RuntimeException("Expression does not evaluate to String: " + value);
    }
    
    @Override
    default void execute(ExecutionContext context) throws Exception {
        evaluate(context);
    }

    /**
     * The statically-known result type ("int"/"bool"/"dec"/"string"), or null if it can only
     * be determined at runtime. Lets composite expressions (e.g. BinaryExprImpl) pick a
     * zero-boxing evaluation strategy once, without re-checking types on every call.
     */
    default String getStaticType() {
        return null;
    }
}
