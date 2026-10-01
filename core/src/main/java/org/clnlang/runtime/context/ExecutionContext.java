package org.clnlang.runtime.context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.ImportDeclImpl;

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

    // Per-function free lists of recycled call frames (LIFO, since calls nest in a stack
    // discipline), keyed by the compiled function they were sized for. Avoids re-allocating
    // CallFrame/LocalContext storage arrays on every call to a function that's already run.
    // Not thread-safe - each ExecutionContext is expected to run on a single thread at a time.
    private final Map<FunctionDeclImpl, ArrayDeque<CallFrame>> framePools = new HashMap<>();
    
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
     * Push a call frame for a compiled function, reusing a pooled frame (with its
     * storage arrays already sized and cleared) when one is available.
     */
    public void pushCallFrame(FunctionDeclImpl funcDecl) {
        ArrayDeque<CallFrame> pool = framePools.get(funcDecl);
        CallFrame frame = (pool != null) ? pool.poll() : null;
        if (frame == null) {
            frame = new CallFrame(funcDecl);
        }
        callStack.push(frame);
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
        List<Object> returnValues = frame.getReturnValueObjects();
        FunctionDeclImpl owner = frame.getOwner();
        if (owner != null) {
            frame.recycle();
            framePools.computeIfAbsent(owner, k -> new ArrayDeque<>()).push(frame);
        }
        return returnValues;
    }
    
    /**
     * Set return values for the current frame.
     * This marks the frame as having returned.
     */
    public void setReturnValues(List<Object> values) {
        CallFrame frame = callStack.peek();
        if (frame != null) {
            frame.setReturnValues(values);
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
