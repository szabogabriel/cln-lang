package org.clnlang.interpreted.compile;

import org.clnlang.interpreted.runtime.context.ExecutionContext;

public interface CompiledAction {

    void execute(ExecutionContext context) throws Exception;
    
}
