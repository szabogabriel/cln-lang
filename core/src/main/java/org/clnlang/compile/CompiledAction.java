package org.clnlang.compile;

import org.clnlang.runtime.context.ExecutionContext;

public interface CompiledAction {

    void execute(ExecutionContext context) throws Exception;
    
}
