package org.clnlang.interpreted.runtime.values;

import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;

/**
 * Represents a function argument with its parameter metadata and runtime value
 */
public class ArgumentValue {
    private final FunctionDeclImpl.Parameter parameter;
    private final Object value;
    private final boolean isMutable;
    
    public ArgumentValue(FunctionDeclImpl.Parameter parameter, Object value, boolean isMutable) {
        this.parameter = parameter;
        this.value = value;
        this.isMutable = isMutable;
    }
    
    public FunctionDeclImpl.Parameter getParameter() {
        return parameter;
    }
    
    public Object getValue() {
        return value;
    }
    
    public boolean isMutable() {
        return isMutable;
    }
    
    public String getName() {
        return parameter.getName();
    }
    
    public String getType() {
        return parameter.getType();
    }
}
