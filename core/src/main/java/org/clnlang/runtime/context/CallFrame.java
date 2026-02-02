package org.clnlang.runtime.context;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.values.ArgumentValue;
import org.clnlang.runtime.values.ReturnValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a single function call frame on the call stack.
 * Each frame maintains its own local context, arguments, and return values.
 */
public class CallFrame {
    private final String functionName;
    private final LocalContext localContext;
    private final Map<String, ArgumentValue> arguments;
    private List<ReturnValue> returnValues; // Support multiple return values
    
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
     * Set a function argument with full parameter metadata.
     */
    public void setArgument(FunctionDeclImpl.Parameter parameter, Object value, boolean isMutable) {
        ArgumentValue argValue = new ArgumentValue(parameter, value, isMutable);
        arguments.put(parameter.getName(), argValue);
        if (isMutable) {
            localContext.setVariable(parameter.getName(), value);
        } else {
            localContext.setConstant(parameter.getName(), value);
        }
    }
    
    /**
     * Set a function argument (legacy method for compatibility).
     * By default, arguments are immutable (constants) unless explicitly marked as var.
     */
    public void setArgument(String name, Object value, boolean isMutable) {
        // Create a simple parameter without full metadata
        FunctionDeclImpl.Parameter parameter = new FunctionDeclImpl.Parameter("unknown", name);
        setArgument(parameter, value, isMutable);
    }
    
    /**
     * Set an argument as constant (default behavior)
     */
    public void setArgument(String name, Object value) {
        setArgument(name, value, false);
    }
    
    /**
     * Get the argument value wrapper (includes metadata)
     */
    public ArgumentValue getArgumentValue(String name) {
        return arguments.get(name);
    }
    
    /**
     * Get the raw argument value (convenience method)
     */
    public Object getArgument(String name) {
        ArgumentValue argValue = arguments.get(name);
        return argValue != null ? argValue.getValue() : null;
    }
    
    /**
     * Get all arguments as a map (legacy compatibility)
     */
    public Map<String, Object> getArguments() {
        return arguments.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getValue()
                ));
    }
    
    /**
     * Get all argument values (typed)
     */
    public Map<String, ArgumentValue> getArgumentValues() {
        return arguments;
    }
    
    /**
     * Set return values for this frame with metadata.
     * Marks the frame as having returned.
     */
    public void setReturnValues(List<ReturnValue> values) {
        this.returnValues = values;
    }
    
    /**
     * Set return values with return variable metadata
     */
    public void setReturnValues(List<FunctionDeclImpl.ReturnVar> returnVars, List<Object> values) {
        if (returnVars.size() != values.size()) {
            throw new IllegalArgumentException("Return variables and values count mismatch");
        }
        
        List<ReturnValue> returnValues = new ArrayList<>();
        for (int i = 0; i < returnVars.size(); i++) {
            returnValues.add(new ReturnValue(returnVars.get(i), values.get(i)));
        }
        this.returnValues = returnValues;
    }
    
    /**
     * Get return values (typed)
     */
    public List<ReturnValue> getReturnValues() {
        return returnValues;
    }
    
    /**
     * Get raw return values (convenience method for backward compatibility)
     */
    public List<Object> getReturnValueObjects() {
        if (returnValues == null) {
            return null;
        }
        return returnValues.stream()
                .map(ReturnValue::getValue)
                .collect(Collectors.toList());
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
