package org.clnlang.runtime.context;

import java.util.HashMap;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.exception.OverloadingNotSupportedException;
import org.clnlang.runtime.types.StructDefinition;
import org.clnlang.runtime.types.UnionDefinition;
import org.clnlang.runtime.values.GlobalVariable;

/**
 * Global context holding program-wide state
 */
public class GlobalContext {
    // Storage for struct type definitions
    private final Map<String, StructDefinition> structTypes;
    
    // Storage for union type definitions
    private final Map<String, UnionDefinition> unionTypes;
    
    // Storage for global functions
    private final Map<String, FunctionDeclImpl> functions;
    
    // Storage for global variables with their runtime values
    private final Map<String, GlobalVariable> globalVariables;

    // Current package name
    private String packageName;
    
    public GlobalContext() {
        this.structTypes = new HashMap<>();
        this.unionTypes = new HashMap<>();
        this.functions = new HashMap<>();
        this.globalVariables = new HashMap<>();
    }
    
    // Struct type methods
    public void registerStructType(String name, StructDefinition definition) {
        if (hasStructType(name)) {
            throw new OverloadingNotSupportedException("Struct " + name + " is already defined.");
        }
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
        if (hasUnionType(name)) {
            throw new OverloadingNotSupportedException("Union " + name + " is already defined.");
        }
        unionTypes.put(name, definition);
        
        // Compute common fields now that we have the struct registry
        // This allows access to fields that are common across all union members
        definition.computeCommonFields(structTypes);
    }
    
    public UnionDefinition getUnionType(String name) {
        return unionTypes.get(name);
    }
    
    public boolean hasUnionType(String name) {
        return unionTypes.containsKey(name);
    }
    
    // Function methods
    public void registerFunction(String name, FunctionDeclImpl function) {
        if (hasFunction(name)) {
            //TODO: currently only the name is checked. Make sure signature is also checked for overloading support
            throw new OverloadingNotSupportedException("Function " + name + " is already defined.");
        }
        functions.put(name, function);
    }
    
    public FunctionDeclImpl getFunction(String name) {
        return functions.get(name);
    }
    
    public boolean hasFunction(String name) {
        return functions.containsKey(name);
    }
    
    /**
     * Get all functions in the global context.
     * 
     * @return Map of function name to function declaration
     */
    public Map<String, FunctionDeclImpl> getAllFunctions() {
        return new HashMap<>(functions);
    }

    // Global variable methods
    /**
     * Register a global variable or constant with its declaration and initial value
     */
    public void registerGlobalVariable(GlobalVarDeclImpl declaration, Object value) {
        globalVariables.put(declaration.getName(), new GlobalVariable(declaration, value));
    }
    
    /**
     * Get the global variable wrapper (includes both declaration and value)
     */
    public GlobalVariable getGlobalVariable(String name) {
        return globalVariables.get(name);
    }
    
    /**
     * Get the value of a global variable or constant
     */
    public Object getGlobalValue(String name) {
        GlobalVariable var = globalVariables.get(name);
        return var != null ? var.getValue() : null;
    }
    
    /**
     * Check if a global variable exists
     */
    public boolean hasGlobalVariable(String name) {
        return globalVariables.containsKey(name);
    }
    
    /**
     * Update a mutable global variable. Returns false if variable doesn't exist
     * or is a constant.
     */
    public boolean updateGlobalVariable(String name, Object value) {
        GlobalVariable var = globalVariables.get(name);
        if (var != null && var.isMutable()) {
            var.setValue(value);
            return true;
        }
        return false;
    }
    
    /**
     * Check if a global value is mutable
     */
    public boolean isGlobalMutable(String name) {
        GlobalVariable var = globalVariables.get(name);
        return var != null && var.isMutable();
    }
    
    /**
     * Get the declaration for a global variable
     */
    public GlobalVarDeclImpl getGlobalDeclaration(String name) {
        GlobalVariable var = globalVariables.get(name);
        return var != null ? var.getDeclaration() : null;
    }
    
    // Package methods
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return packageName;
    }
}
