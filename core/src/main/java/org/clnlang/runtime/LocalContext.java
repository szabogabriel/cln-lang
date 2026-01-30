package org.clnlang.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Local context for function-local variables (supports scoping)
 */
public class LocalContext {
    // Parent context for nested scopes
    private final LocalContext parent;
    
    // Local mutable variables (declared with "var")
    private final Map<String, Object> variables;
    
    // Local constants (immutable, default)
    private final Map<String, Object> constants;
    
    public LocalContext() {
        this(null);
    }
    
    public LocalContext(LocalContext parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
        this.constants = new HashMap<>();
    }
    
    public LocalContext getParent() {
        return parent;
    }
    
    /**
     * Set a mutable variable in the current scope
     */
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
    
    /**
     * Set a constant in the current scope
     */
    public void setConstant(String name, Object value) {
        constants.put(name, value);
    }
    
    /**
     * Get a value (variable or constant), checking current scope and parent scopes
     */
    public Object getValue(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        if (constants.containsKey(name)) {
            return constants.get(name);
        }
        if (parent != null) {
            return parent.getValue(name);
        }
        return null;
    }
    
    /**
     * Get a variable, checking current scope and parent scopes
     * @deprecated Use getValue() instead
     */
    @Deprecated
    public Object getVariable(String name) {
        return getValue(name);
    }
    
    /**
     * Check if a value exists in current or parent scopes
     */
    public boolean hasValue(String name) {
        if (variables.containsKey(name) || constants.containsKey(name)) {
            return true;
        }
        if (parent != null) {
            return parent.hasValue(name);
        }
        return false;
    }
    
    /**
     * Check if a variable exists in current or parent scopes
     * @deprecated Use hasValue() instead
     */
    @Deprecated
    public boolean hasVariable(String name) {
        return hasValue(name);
    }
    
    /**
     * Check if a value is mutable in current or parent scopes
     */
    public boolean isMutable(String name) {
        if (variables.containsKey(name)) {
            return true;
        }
        if (constants.containsKey(name)) {
            return false;
        }
        if (parent != null) {
            return parent.isMutable(name);
        }
        return false;
    }
    
    /**
     * Update a mutable variable in the scope where it was declared.
     * Returns false if the variable doesn't exist or is a constant.
     */
    public boolean updateVariable(String name, Object value) {
        if (variables.containsKey(name)) {
            variables.put(name, value);
            return true;
        }
        if (constants.containsKey(name)) {
            // Cannot update a constant
            return false;
        }
        if (parent != null) {
            return parent.updateVariable(name, value);
        }
        return false;
    }
}
