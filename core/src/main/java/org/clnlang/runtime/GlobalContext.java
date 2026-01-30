package org.clnlang.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Global context holding program-wide state
 */
public class GlobalContext {
    // Storage for struct type definitions
    private final Map<String, StructDefinition> structTypes;
    
    // Storage for union type definitions
    private final Map<String, UnionDefinition> unionTypes;
    
    // Storage for global functions
    private final Map<String, Object> functions;
    
    // Storage for global mutable variables (declared with "var")
    private final Map<String, Object> globalVariables;
    
    // Storage for global constants (immutable, default)
    private final Map<String, Object> globalConstants;
    
    // Current package name
    private String packageName;
    
    public GlobalContext() {
        this.structTypes = new HashMap<>();
        this.unionTypes = new HashMap<>();
        this.functions = new HashMap<>();
        this.globalVariables = new HashMap<>();
        this.globalConstants = new HashMap<>();
    }
    
    // Struct type methods
    public void registerStructType(String name, StructDefinition definition) {
        structTypes.put(name, definition);
    }
    
    public StructDefinition getStructType(String name) {
        return structTypes.get(name);
    }
    
    public boolean hasStructType(String name) {
        return structTypes.containsKey(name);
    }
    
    // Union type methods
    public void registerUnionType(String name, UnionDefinition definition) {
        unionTypes.put(name, definition);
    }
    
    public UnionDefinition getUnionType(String name) {
        return unionTypes.get(name);
    }
    
    public boolean hasUnionType(String name) {
        return unionTypes.containsKey(name);
    }
    
    // Function methods
    public void registerFunction(String name, Object function) {
        functions.put(name, function);
    }
    
    public Object getFunction(String name) {
        return functions.get(name);
    }
    
    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }

    public void setGlobalVariable(String name, Object value) {
        globalVariables.put(name, value);
    }
    
    public Object getGlobalVariable(String name) {
        return globalVariables.get(name);
    }
    
    public boolean hasGlobalVariable(String name) {
        return globalVariables.containsKey(name);
    }
    
    /**
     * Update a mutable global variable. Returns false if variable doesn't exist
     * or is a constant.
     */
    public boolean updateGlobalVariable(String name, Object value) {
        if (globalVariables.containsKey(name)) {
            globalVariables.put(name, value);
            return true;
        }
        return false;
    }
    
    // Global constant methods (immutable)
    public void setGlobalConstant(String name, Object value) {
        globalConstants.put(name, value);
    }
    
    public Object getGlobalConstant(String name) {
        return globalConstants.get(name);
    }
    
    public boolean hasGlobalConstant(String name) {
        return globalConstants.containsKey(name);
    }
    
    /**
     * Get any global value (variable or constant), checking both storages
     */
    public Object getGlobalValue(String name) {
        if (globalVariables.containsKey(name)) {
            return globalVariables.get(name);
        }
        if (globalConstants.containsKey(name)) {
            return globalConstants.get(name);
        }
        return null;
    }
    
    /**
     * Check if a global value exists (variable or constant)
     */
    public boolean hasGlobalValue(String name) {
        return globalVariables.containsKey(name) || globalConstants.containsKey(name);
    }
    
    /**
     * Check if a global value is mutable
     */
    public boolean isGlobalMutable(String name) {
        return globalVariables.containsKey(name);
    }
    
    // Package methods
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return packageName;
    }
}
