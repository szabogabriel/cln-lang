package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

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
