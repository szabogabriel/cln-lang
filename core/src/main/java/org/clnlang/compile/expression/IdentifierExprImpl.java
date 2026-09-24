package org.clnlang.compile.expression;

import java.math.BigDecimal;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Compiled representation of an identifier expression.
 * Supports both index-based access (fast, zero-boxing for local primitives)
 * and name-based access (fallback for globals, closures).
 */
public class IdentifierExprImpl implements CompiledExpr {
    private final String name;
    private final String type;  // Can be null if unknown at compile time
    private final int index;    // -1 if not a local variable with known index

    // Lazily-resolved global registry slot (globals are registered once, before any
    // code executes, so the slot is stable for the rest of this ExecutionContext's
    // lifetime and safe to cache on the node). -2 = not yet resolved, -1 = confirmed
    // not a global variable (e.g. a function name).
    private volatile int globalIndex = -2;
    private volatile String globalType;
    
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
    public String getStaticType() {
        // Normalize the "decimal" backward-compatibility spelling to "dec"
        return "decimal".equals(type) ? "dec" : type;
    }

    /**
     * Resolve this identifier's global registry slot only if it matches the expected type.
     * Returns -1 if it isn't a global, or is a global of a different type.
     */
    public int resolveGlobalIndexFor(ExecutionContext context, String expectedType) {
        if (resolveGlobal(context) && expectedType.equals(globalType)) {
            return globalIndex;
        }
        return -1;
    }

    /**
     * Resolve (and cache) this identifier's global registry slot, if any. Returns false
     * if this identifier isn't a global (e.g. it's a local, or a function name).
     */
    public boolean ensureGlobalResolved(ExecutionContext context) {
        return resolveGlobal(context);
    }

    /**
     * The type of the resolved global slot. Only meaningful after ensureGlobalResolved
     * returns true.
     */
    public String getResolvedGlobalType() {
        return globalType;
    }

    /**
     * The index of the resolved global slot. Only meaningful after ensureGlobalResolved
     * returns true.
     */
    public int getResolvedGlobalIndex() {
        return globalIndex;
    }

    /**
     * Attempt to update this identifier as a global variable, using the cached registry
     * slot (zero boxing for primitives). Returns false if this identifier isn't a global
     * (e.g. it's a local, or a constant), in which case the caller should fall back to
     * the name-based update path.
     */
    public boolean updateGlobal(ExecutionContext context, Object value) throws Exception {
        if (!resolveGlobal(context)) {
            return false;
        }
        switch (globalType) {
            case "int":
                return context.getGlobalContext().updateLongByIndex(globalIndex, (Long) value);
            case "bool":
                return context.getGlobalContext().updateBoolByIndex(globalIndex, (Boolean) value);
            case "dec":
            case "decimal":
                return context.getGlobalContext().updateDecimalByIndex(globalIndex, (BigDecimal) value);
            case "string":
                return context.getGlobalContext().updateStringByIndex(globalIndex, (String) value);
            default:
                return context.getGlobalContext().updateObjectByIndex(globalIndex, value);
        }
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        // Fast path: index-based access for local variables
        if (index >= 0 && type != null) {
            // Arrays are always objects, regardless of element type
            if (type.contains("[]")) {
                return context.getLocalContext().getObjectByIndex(index);
            }
            switch (type) {
                case "int":
                    return context.getLocalContext().getLongByIndex(index);
                case "bool":
                    return context.getLocalContext().getBoolByIndex(index);
                case "dec":
                case "decimal":
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
        
        // Then check globals via the cached registry slot (zero boxing for primitives)
        if (resolveGlobal(context)) {
            switch (globalType) {
                case "int":
                    return context.getGlobalContext().getLongByIndex(globalIndex);
                case "bool":
                    return context.getGlobalContext().getBoolByIndex(globalIndex);
                case "dec":
                case "decimal":
                    return context.getGlobalContext().getDecimalByIndex(globalIndex);
                case "string":
                    return context.getGlobalContext().getStringByIndex(globalIndex);
                default:
                    return context.getGlobalContext().getObjectByIndex(globalIndex);
            }
        }
        
        // Then check global context for functions
        Object function = context.getGlobalContext().getFunction(name);
        if (function != null) {
            return function;
        }
        
        // If not found, throw an exception
        throw new RuntimeException("Undefined identifier: '" + name + "'");
    }

    /**
     * Resolve (and cache) this identifier's global registry slot. Safe to cache because
     * all globals are registered before any code executes, so the slot never changes
     * for the remaining lifetime of the ExecutionContext this node runs against.
     */
    private boolean resolveGlobal(ExecutionContext context) {
        int cached = globalIndex;
        if (cached != -2) {
            return cached != -1;
        }
        org.clnlang.compile.declaration.GlobalVarDeclImpl decl = context.getGlobalContext().getGlobalDeclaration(name);
        if (decl == null) {
            globalIndex = -1;
            return false;
        }
        String declType = decl.getType();
        int resolved = context.getGlobalContext().resolveGlobalIndex(name, declType);
        if (resolved < 0) {
            globalIndex = -1;
            return false;
        }
        globalType = declType;
        globalIndex = resolved;
        return true;
    }
    
    // ===== Typed evaluation methods (zero-boxing!) =====
    
    @Override
    public long longValue(ExecutionContext context) throws Exception {
        // Fast path: direct primitive access via index
        if (index >= 0 && "int".equals(type)) {
            return context.getLocalContext().getLongByIndex(index); // ✅ Zero boxing!
        }
        if (index < 0 && resolveGlobal(context) && "int".equals(globalType)) {
            return context.getGlobalContext().getLongByIndex(globalIndex); // ✅ Zero boxing!
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
        if (index < 0 && resolveGlobal(context) && "bool".equals(globalType)) {
            return context.getGlobalContext().getBoolByIndex(globalIndex); // ✅ Zero boxing!
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
        if (index < 0 && resolveGlobal(context) && ("dec".equals(globalType) || "decimal".equals(globalType))) {
            return context.getGlobalContext().getDecimalByIndex(globalIndex);
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
        if (index < 0 && resolveGlobal(context) && "string".equals(globalType)) {
            return context.getGlobalContext().getStringByIndex(globalIndex);
        }
        
        // Fallback: name-based lookup
        Object value = evaluate(context);
        if (value instanceof String) {
            return (String) value;
        }
        throw new RuntimeException("Identifier '" + name + "' is not a string value: " + value);
    }
}
