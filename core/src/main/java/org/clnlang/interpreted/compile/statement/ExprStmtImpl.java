package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of an expression statement.
 */
public class ExprStmtImpl implements CompiledAction {
    private CompiledExpr expression;

    public ExprStmtImpl(CompiledExpr expression) {
        this.expression = expression;
    }

    public CompiledExpr getExpression() {
        return expression;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        expression.evaluate(context);
    }
}
