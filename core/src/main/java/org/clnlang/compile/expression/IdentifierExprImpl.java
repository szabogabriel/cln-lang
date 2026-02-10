package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;
import java.math.BigDecimal;

/**
 * Compiled representation of an identifier expression.
 * Supports both index-based access (fast, zero-boxing for local primitives)
 * and name-based access (fallback for globals, closures).
 */
public class IdentifierExprImpl implements CompiledExpr {
    private final String name;
    private final String type;  // Can be null if unknown at compile time
    private final int index;    // -1 if not a local variable with known index
    
    /**
     * Create identifier with name only (fallback to name-based lookup)
     */
    public IdentifierExprImpl(String name) {
        this(name, null, -1);
    }
    
    /**
     * Create identifier with compile-time resolved type and index
     */
    public IdentifierExprImpl(String name, String type, int index) {
        this.name = name;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public int getIndex() {
        return index;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        // Fast path: index-based access for local variables
        if (index >= 0 && type != null) {
            String baseType = type.replaceAll("\\[\\]", "");
            switch (baseType) {
                case "int":
                    return context.getLocalContext().getLongByIndex(index);
                case "bool":
                    return context.getLocalContext().getBoolByIndex(index);
                case "dec":
                    return context.getLocalContext().getDecimalByIndex(index);
                case "string":
                    return context.getLocalContext().getStringByIndex(index);
                default:
                    return context.getLocalContext().getObjectByIndex(index);
            }
        }
        
        // Fallback: name-based lookup (for globals, unresolved locals, functions)
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
    
    // ===== Typed evaluation methods (zero-boxing!) =====
    
    @Override
    public long longValue(ExecutionContext context) throws Exception {
        // Fast path: direct primitive access via index
        if (index >= 0 && "int".equals(type)) {
            return context.getLocalContext().getLongByIndex(index); // ✅ Zero boxing!
        }
        
        // Fallback: name-based lookup (boxes)
        Object value = evaluate(context);
        if (value instanceof Long) {
            return (Long) value;
        }
        throw new RuntimeException("Identifier '" + name + "' is not a long value: " + value);
    }
    
    @Override
    public boolean boolValue(ExecutionContext context) throws Exception {
        // Fast path: direct primitive access via index
        if (index >= 0 && "bool".equals(type)) {
            return context.getLocalContext().getBoolByIndex(index); // ✅ Zero boxing!
        }
        
        // Fallback: name-based lookup (boxes)
        Object value = evaluate(context);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new RuntimeException("Identifier '" + name + "' is not a boolean value: " + value);
    }
    
    @Override
    public BigDecimal decimalValue(ExecutionContext context) throws Exception {
        // Fast path: direct access via index
        if (index >= 0 && "dec".equals(type)) {
            return context.getLocalContext().getDecimalByIndex(index);
        }
        
        // Fallback: name-based lookup
        Object value = evaluate(context);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        throw new RuntimeException("Identifier '" + name + "' is not a decimal value: " + value);
    }
    
    @Override
    public String stringValue(ExecutionContext context) throws Exception {
        // Fast path: direct access via index
        if (index >= 0 && "string".equals(type)) {
            return context.getLocalContext().getStringByIndex(index);
        }
        
        // Fallback: name-based lookup
        Object value = evaluate(context);
        if (value instanceof String) {
            return (String) value;
        }
        throw new RuntimeException("Identifier '" + name + "' is not a string value: " + value);
    }
}
