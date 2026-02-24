package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of an empty statement.
 */
public class EmptyStmtImpl implements CompiledAction {

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Do nothing
    }
}
