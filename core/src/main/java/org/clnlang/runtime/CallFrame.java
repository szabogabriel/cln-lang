package org.clnlang.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single function call frame on the call stack.
 * Each frame maintains its own local context, arguments, and return values.
 */
public class CallFrame {
    private final String functionName;
    private final LocalContext localContext;
    private final Map<String, Object> arguments;
    private List<Object> returnValues; // Support multiple return values
    
    public CallFrame(String functionName) {
        this.functionName = functionName;
        this.localContext = new LocalContext();
        this.arguments = new HashMap<>();
        this.returnValues = null;
    }
    
    public CallFrame(String functionName, LocalContext parentContext) {
        this.functionName = functionName;
        this.localContext = new LocalContext(parentContext);
        this.arguments = new HashMap<>();
        this.returnValues = null;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public LocalContext getLocalContext() {
        return localContext;
    }
    
    /**
     * Set a function argument.
     * By default, arguments are immutable (constants) unless explicitly marked as var.
     */
    public void setArgument(String name, Object value, boolean isMutable) {
        arguments.put(name, value);
        if (isMutable) {
            localContext.setVariable(name, value);
        } else {
            localContext.setConstant(name, value);
        }
    }
    
    /**
     * Set an argument as constant (default behavior)
     */
    public void setArgument(String name, Object value) {
        setArgument(name, value, false);
    }
    
    public Object getArgument(String name) {
        return arguments.get(name);
    }
    
    public Map<String, Object> getArguments() {
        return arguments;
    }
    
    /**
     * Set return values for this frame.
     * Marks the frame as having returned.
     */
    public void setReturnValues(List<Object> values) {
        this.returnValues = values;
    }
    
    public List<Object> getReturnValues() {
        return returnValues;
    }
    
    /**
     * Check if this frame has executed a return statement
     */
    public boolean hasReturned() {
        return returnValues != null;
    }
    
    /**
     * Clear the return flag (useful for control flow)
     */
    public void clearReturn() {
        this.returnValues = null;
    }
}
