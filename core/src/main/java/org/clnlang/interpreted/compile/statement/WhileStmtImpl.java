package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a while statement.
 */
public class WhileStmtImpl implements CompiledAction {
    private CompiledExpr condition;
    private CompiledAction body;

    public WhileStmtImpl(CompiledExpr condition, CompiledAction body) {
        this.condition = condition;
        this.body = body;
    }

    public CompiledExpr getCondition() {
        return condition;
    }

    public CompiledAction getBody() {
        return body;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        while ((Boolean) condition.evaluate(context)) {
            body.execute(context);
        }
    }
}
