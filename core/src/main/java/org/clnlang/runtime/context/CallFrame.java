package org.clnlang.runtime.context;

import java.util.List;

import org.clnlang.compile.declaration.FunctionDeclImpl;

/**
 * Represents a single function call frame on the call stack.
 * Each frame maintains its own local context and return values.
 */
public class CallFrame {
    private final String functionName;
    private final LocalContext localContext;
    private List<Object> returnValues; // null until a return has executed; supports multiple return values

    // Set only for frames created via the pooled constructor, so popCallFrame() knows
    // which per-function pool to return this frame to for reuse.
    private final FunctionDeclImpl owner;

    public CallFrame(String functionName) {
        this.functionName = functionName;
        this.localContext = new LocalContext();
        this.returnValues = null;
        this.owner = null;
    }

    public CallFrame(String functionName, LocalContext parentContext) {
        this.functionName = functionName;
        this.localContext = new LocalContext(parentContext);
        this.returnValues = null;
        this.owner = null;
    }

    /**
     * Pooled constructor: pre-sizes the local context's storage arrays to the exact
     * per-type slot counts the compiler assigned to this function, so reused frames
     * never need to lazily grow (or reallocate) their arrays.
     */
    public CallFrame(FunctionDeclImpl owner) {
        this.functionName = owner.getName();
        this.localContext = new LocalContext(owner.getLongSlotCount(), owner.getBoolSlotCount(),
                owner.getDecimalSlotCount(), owner.getStringSlotCount(), owner.getObjectSlotCount());
        this.returnValues = null;
        this.owner = owner;
    }

    /**
     * The function this pooled frame belongs to, or null if it wasn't created from a pool.
     */
    public FunctionDeclImpl getOwner() {
        return owner;
    }

    /**
     * Resets this frame's state so it can be handed out again for another invocation
     * of the same function.
     */
    public void recycle() {
        returnValues = null;
        localContext.reset();
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
