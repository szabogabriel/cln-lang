package org.clnlang.compile;

import java.util.HashMap;
import java.util.Map;

/**
 * Execution context for the compiled program.
 * Contains global and local scopes for variables, functions, and type definitions.
 */
public class ExecutionContext {
    
    // Global context - shared across entire program
    private final GlobalContext globalContext;
    
    // Local context - for function-local variables (scoped)
    private LocalContext localContext;
    
    public ExecutionContext() {
        this.globalContext = new GlobalContext();
        this.localContext = new LocalContext();
    }
    
    /**
     * Get the global context
     */
    public GlobalContext getGlobalContext() {
        return globalContext;
    }
    
    /**
     * Get the current local context
     */
    public LocalContext getLocalContext() {
        return localContext;
    }
    
    /**
     * Set a new local context (e.g., when entering a function)
     */
    public void setLocalContext(LocalContext localContext) {
        this.localContext = localContext;
    }
    
    /**
     * Push a new local context (for nested scopes)
     */
    public void pushLocalContext() {
        this.localContext = new LocalContext(this.localContext);
    }
    
    /**
     * Pop the current local context (return to parent scope)
     */
    public void popLocalContext() {
        if (this.localContext != null && this.localContext.getParent() != null) {
            this.localContext = this.localContext.getParent();
        }
    }
    
    /**
     * Global context holding program-wide state
     */
    public static class GlobalContext {
        // Storage for struct type definitions
        private final Map<String, StructDefinition> structTypes;
        
        // Storage for union type definitions
        private final Map<String, UnionDefinition> unionTypes;
        
        // Storage for global functions
        private final Map<String, Object> functions;
        
        // Storage for global variables
        private final Map<String, Object> globalVariables;
        
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
        
        // Global variable methods
        public void setGlobalVariable(String name, Object value) {
            globalVariables.put(name, value);
        }
        
        public Object getGlobalVariable(String name) {
            return globalVariables.get(name);
        }
        
        public boolean hasGlobalVariable(String name) {
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
    
    /**
     * Local context for function-local variables (supports scoping)
     */
    public static class LocalContext {
        // Parent context for nested scopes
        private final LocalContext parent;
        
        // Local variables in this scope
        private final Map<String, Object> variables;
        
        public LocalContext() {
            this(null);
        }
        
        public LocalContext(LocalContext parent) {
            this.parent = parent;
            this.variables = new HashMap<>();
        }
        
        public LocalContext getParent() {
            return parent;
        }
        
        /**
         * Set a variable in the current scope
         */
        public void setVariable(String name, Object value) {
            variables.put(name, value);
        }
        
        /**
         * Get a variable, checking current scope and parent scopes
         */
        public Object getVariable(String name) {
            if (variables.containsKey(name)) {
                return variables.get(name);
            }
            if (parent != null) {
                return parent.getVariable(name);
            }
            return null;
        }
        
        /**
         * Check if a variable exists in current or parent scopes
         */
        public boolean hasVariable(String name) {
            if (variables.containsKey(name)) {
                return true;
            }
            if (parent != null) {
                return parent.hasVariable(name);
            }
            return false;
        }
        
        /**
         * Update a variable in the scope where it was declared
         */
        public boolean updateVariable(String name, Object value) {
            if (variables.containsKey(name)) {
                variables.put(name, value);
                return true;
            }
            if (parent != null) {
                return parent.updateVariable(name, value);
            }
            return false;
        }
    }
    
    /**
     * Struct type definition
     */
    public static class StructDefinition {
        private final String name;
        private final Map<String, String> fields; // fieldName -> fieldType
        private final boolean isExposed;
        
        public StructDefinition(String name, boolean isExposed) {
            this.name = name;
            this.isExposed = isExposed;
            this.fields = new HashMap<>();
        }
        
        public void addField(String fieldName, String fieldType) {
            fields.put(fieldName, fieldType);
        }
        
        public String getName() {
            return name;
        }
        
        public Map<String, String> getFields() {
            return fields;
        }
        
        public boolean isExposed() {
            return isExposed;
        }
        
        public String getFieldType(String fieldName) {
            return fields.get(fieldName);
        }
        
        public boolean hasField(String fieldName) {
            return fields.containsKey(fieldName);
        }
    }
    
    /**
     * Union type definition
     */
    public static class UnionDefinition {
        private final String name;
        private final Map<String, String> members; // memberName -> memberType
        private final boolean isExposed;
        
        public UnionDefinition(String name, boolean isExposed) {
            this.name = name;
            this.isExposed = isExposed;
            this.members = new HashMap<>();
        }
        
        public void addMember(String memberType) {
            // For unions, members are typically just types
            members.put(memberType, memberType);
        }
        
        public String getName() {
            return name;
        }
        
        public Map<String, String> getMembers() {
            return members;
        }
        
        public boolean isExposed() {
            return isExposed;
        }
        
        public boolean hasMember(String memberType) {
            return members.containsKey(memberType);
        }
    }
}
