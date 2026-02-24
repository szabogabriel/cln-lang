package org.clnlang.compiled.context;

import java.util.Stack;

public class ExecutionContext {

    private GlobalContext globalContext = new GlobalContext();

    private Stack<LocalContext> localContexts = new Stack<>();

    public void pushLocalContext(MemoryAllocatorDescription memoryAllocatorDescription) {
        LocalContext localContext = new LocalContext(memoryAllocatorDescription);
        localContexts.push(localContext);
    }

    public void popLocalContext() {
        if (!localContexts.isEmpty()) {
            localContexts.pop();
        }
    }

    public LocalContext getCurrentLocalContext() {
        return localContexts.peek();
    }

    public GlobalContext getGlobalContext() {
        return globalContext;
    }
    
}
