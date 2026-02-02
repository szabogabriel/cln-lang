package org.clnlang.runtime.values;

import org.clnlang.compile.declaration.GlobalVarDeclImpl;

/**
 * Represents a global variable with its declaration and runtime value
 */
public class GlobalVariable {
    private final GlobalVarDeclImpl declaration;
    private Object value;
    
    public GlobalVariable(GlobalVarDeclImpl declaration, Object value) {
        this.declaration = declaration;
        this.value = value;
    }
    
    public GlobalVarDeclImpl getDeclaration() {
        return declaration;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        if (!declaration.isMutable()) {
            throw new IllegalStateException("Cannot modify constant: " + declaration.getName());
        }
        this.value = value;
    }
    
    public boolean isMutable() {
        return declaration.isMutable();
    }
    
    public String getName() {
        return declaration.getName();
    }
    
    public String getType() {
        return declaration.getType();
    }
}
