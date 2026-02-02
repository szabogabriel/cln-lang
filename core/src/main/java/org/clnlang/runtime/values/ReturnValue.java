package org.clnlang.runtime.values;

import org.clnlang.compile.declaration.FunctionDeclImpl;

/**
 * Represents a function return value with its return variable metadata and runtime value
 */
public class ReturnValue {
    private final FunctionDeclImpl.ReturnVar returnVar;
    private final Object value;
    
    public ReturnValue(FunctionDeclImpl.ReturnVar returnVar, Object value) {
        this.returnVar = returnVar;
        this.value = value;
    }
    
    public FunctionDeclImpl.ReturnVar getReturnVar() {
        return returnVar;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getName() {
        return returnVar.getName();
    }
    
    public String getType() {
        return returnVar.getType();
    }
}
