package org.clnlang.compiled.context;

import java.util.Stack;

public class ExecutionContext {

    private GlobalContext globalContext = new GlobalContext();

    private Stack<LocalContext> localContexts = new Stack<>();

    public void pushLocalContext() {
        LocalContext localContext = new LocalContext();
        localContexts.push(localContext);
    }

    public LocalContext getCurrentLocalContext() {
        return localContexts.peek();
    }

    public GlobalContext getGlobalContext() {
        return globalContext;
    }
    
}
