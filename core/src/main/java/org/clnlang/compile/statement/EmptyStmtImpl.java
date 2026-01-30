package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of an empty statement.
 */
public class EmptyStmtImpl implements CompiledAction {

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Do nothing
    }
}
