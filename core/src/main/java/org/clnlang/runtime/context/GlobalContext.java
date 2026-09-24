package org.clnlang.runtime.context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.exception.OverloadingNotSupportedException;
import org.clnlang.runtime.types.StructDefinition;
import org.clnlang.runtime.types.UnionDefinition;

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
    
    // Declaration metadata (type, mutability, exposure) for global variables/constants
    private final Map<String, GlobalVarDeclImpl> globalDeclarations;

    // Values for global variables/constants, stored index-based (zero-boxing for primitives),
    // same storage strategy as LocalContext. Indices are assigned once at registration
    // time (before execution starts) and are stable for the lifetime of this context, so
    // identifier nodes may resolve and cache their slot on first access.
    private final LocalContext globalValues;

    // Current package name
    private String packageName;
    
    public GlobalContext() {
        this.structTypes = new HashMap<>();
        this.unionTypes = new HashMap<>();
        this.functions = new HashMap<>();
        this.globalDeclarations = new HashMap<>();
        this.globalValues = new LocalContext();
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
        globalDeclarations.put(declaration.getName(), declaration);
        if (declaration.isMutable()) {
            globalValues.setVariable(declaration.getName(), value);
        } else {
            globalValues.setConstant(declaration.getName(), value);
        }
    }
    
    /**
     * Get the value of a global variable or constant
     */
    public Object getGlobalValue(String name) {
        return globalValues.getValue(name);
    }
    
    /**
     * Check if a global variable exists
     */
    public boolean hasGlobalVariable(String name) {
        return globalDeclarations.containsKey(name);
    }
    
    /**
     * Update a mutable global variable. Returns false if variable doesn't exist
     * or is a constant.
     */
    public boolean updateGlobalVariable(String name, Object value) {
        return globalValues.updateVariable(name, value);
    }
    
    /**
     * Check if a global value is mutable
     */
    public boolean isGlobalMutable(String name) {
        GlobalVarDeclImpl decl = globalDeclarations.get(name);
        return decl != null && decl.isMutable();
    }
    
    /**
     * Get the declaration for a global variable
     */
    public GlobalVarDeclImpl getGlobalDeclaration(String name) {
        return globalDeclarations.get(name);
    }

    // ===== Registry-index fast path (for identifier nodes to cache after first resolution) =====

    /**
     * Resolve the storage slot for a global variable of the given type.
     * Indices are stable once all globals have been registered (before execution starts),
     * so callers may cache the result. Returns -1 if not found.
     */
    public int resolveGlobalIndex(String name, String type) {
        return globalValues.resolveIndex(name, type);
    }

    public long getLongByIndex(int index) {
        return globalValues.getLongByIndex(index);
    }

    public boolean getBoolByIndex(int index) {
        return globalValues.getBoolByIndex(index);
    }

    public BigDecimal getDecimalByIndex(int index) {
        return globalValues.getDecimalByIndex(index);
    }

    public String getStringByIndex(int index) {
        return globalValues.getStringByIndex(index);
    }

    public Object getObjectByIndex(int index) {
        return globalValues.getObjectByIndex(index);
    }

    public boolean updateLongByIndex(int index, long value) {
        return globalValues.updateLongByIndex(index, value);
    }

    public boolean updateBoolByIndex(int index, boolean value) {
        return globalValues.updateBoolByIndex(index, value);
    }

    public boolean updateDecimalByIndex(int index, BigDecimal value) {
        return globalValues.updateDecimalByIndex(index, value);
    }

    public boolean updateStringByIndex(int index, String value) {
        return globalValues.updateStringByIndex(index, value);
    }

    public boolean updateObjectByIndex(int index, Object value) {
        return globalValues.updateObjectByIndex(index, value);
    }

    // Package methods
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return packageName;
    }
}
