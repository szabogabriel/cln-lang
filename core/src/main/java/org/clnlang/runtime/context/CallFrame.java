package org.clnlang.runtime.context;

import java.util.List;

/**
 * Represents a single function call frame on the call stack.
 * Each frame maintains its own local context and return values.
 */
public class CallFrame {
    private final String functionName;
    private final LocalContext localContext;
    private List<Object> returnValues; // null until a return has executed; supports multiple return values

    public CallFrame(String functionName) {
        this.functionName = functionName;
        this.localContext = new LocalContext();
        this.returnValues = null;
    }

    public CallFrame(String functionName, LocalContext parentContext) {
        this.functionName = functionName;
        this.localContext = new LocalContext(parentContext);
        this.returnValues = null;
    }

    public String getFunctionName() {
        return functionName;
    }

    public LocalContext getLocalContext() {
        return localContext;
    }

    /**
     * Set return values for this frame. Marks the frame as having returned.
     */
    public void setReturnValues(List<Object> values) {
        this.returnValues = values;
    }

    /**
     * Get the raw return values.
     */
    public List<Object> getReturnValueObjects() {
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
