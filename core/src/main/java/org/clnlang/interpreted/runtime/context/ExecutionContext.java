package org.clnlang.interpreted.runtime.context;

import org.clnlang.interpreted.compile.declaration.ImportDeclImpl;
import org.clnlang.interpreted.runtime.values.ReturnValue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Execution context for the compiled program.
 * Contains global context, call stack, and manages function invocations.
 */
public class ExecutionContext {
    
    // Global context - shared across entire program
    private final GlobalContext globalContext;
    
    // Call stack for tracking function invocations
    private final Deque<CallFrame> callStack;

    private final List<ImportDeclImpl> imports = new ArrayList<>();
    
    public ExecutionContext() {
        this.globalContext = new GlobalContext();
        this.callStack = new ArrayDeque<>();
        // Push main/global frame
        this.callStack.push(new CallFrame("<global>"));
    }
    
    /**
     * Get the global context
     */
    public GlobalContext getGlobalContext() {
        return globalContext;
    }
    
    /**
     * Get the current call frame
     */
    public CallFrame getCurrentFrame() {
        return callStack.peek();
    }
    
    /**
     * Get the current local context from the top frame
     */
    public LocalContext getLocalContext() {
        CallFrame frame = callStack.peek();
        return frame != null ? frame.getLocalContext() : null;
    }
    
    /**
     * Push a new call frame when entering a function.
     * The new frame's local context will NOT have access to the previous frame's locals.
     */
    public void pushCallFrame(String functionName) {
        callStack.push(new CallFrame(functionName));
    }
    
    /**
     * Push a new call frame with a parent context (for closures, if needed).
     */
    public void pushCallFrame(String functionName, LocalContext parentContext) {
        callStack.push(new CallFrame(functionName, parentContext));
    }
    
    /**
     * Pop the current call frame when exiting a function.
     * @return the return values from the popped frame, or null if no return
     */
    public List<Object> popCallFrame() {
        if (callStack.size() <= 1) {
            throw new RuntimeException("Cannot pop global frame");
        }
        CallFrame frame = callStack.pop();
        return frame.getReturnValueObjects();
    }
    
    /**
     * Set return values for the current frame.
     * This marks the frame as having returned.
     */
    public void setReturnValues(List<Object> values) {
        CallFrame frame = callStack.peek();
        if (frame != null) {
            // For now, create ReturnValue wrappers without metadata
            // TODO: Pass return variable metadata when available
            List<ReturnValue> returnValues = new ArrayList<>();
            for (Object value : values) {
                // Create a simple ReturnVar without full metadata
                org.clnlang.interpreted.compile.declaration.FunctionDeclImpl.ReturnVar returnVar = 
                    new org.clnlang.interpreted.compile.declaration.FunctionDeclImpl.ReturnVar("unknown", "return" + returnValues.size());
                returnValues.add(new ReturnValue(returnVar, value));
            }
            frame.setReturnValues(returnValues);
        }
    }
    
    /**
     * Get return values from the current frame.
     */
    public List<Object> getReturnValues() {
        CallFrame frame = callStack.peek();
        return frame != null ? frame.getReturnValueObjects() : null;
    }
    
    /**
     * Check if current frame has returned.
     */
    public boolean hasReturned() {
        CallFrame frame = callStack.peek();
        return frame != null && frame.hasReturned();
    }
    
    /**
     * Clear the return flag from the current frame.
     */
    public void clearReturn() {
        CallFrame frame = callStack.peek();
        if (frame != null) {
            frame.clearReturn();
        }
    }

    public void registerImport(ImportDeclImpl importDecl) {
        imports.add(importDecl);
    }
    
    public List<ImportDeclImpl> getImports() {
        return imports;
    }
}
