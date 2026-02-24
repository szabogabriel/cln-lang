package org.clnlang.interpreted.compile;

import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a statement block.
 */
public class BlockImpl implements CompiledAction {
    private List<CompiledAction> statements;

    public BlockImpl() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(CompiledAction statement) {
        statements.add(statement);
    }

    public List<CompiledAction> getStatements() {
        return statements;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        for (CompiledAction stmt : statements) {
            stmt.execute(context);
            // Stop executing if a return statement was encountered
            if (context.hasReturned()) {
                break;
            }
        }
    }
}
