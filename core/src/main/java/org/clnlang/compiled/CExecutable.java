package org.clnlang.compiled;

import org.clnlang.compiled.context.ExecutionContext;

public interface CExecutable {

    void execute(ExecutionContext context);

    int[] getResults();

    Types[] getResultTypes();

    boolean isGlobal();
    
}
