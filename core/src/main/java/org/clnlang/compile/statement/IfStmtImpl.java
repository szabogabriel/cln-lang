package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.ExecutionContext;
import org.clnlang.compile.expression.CompiledExpr;

/**
 * Compiled representation of an if statement.
 */
public class IfStmtImpl implements CompiledAction {
    private CompiledExpr condition;
    private CompiledAction thenBlock;
    private CompiledAction elseBlock;

    public IfStmtImpl(CompiledExpr condition, CompiledAction thenBlock, CompiledAction elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public CompiledExpr getCondition() {
        return condition;
    }

    public CompiledAction getThenBlock() {
        return thenBlock;
    }

    public CompiledAction getElseBlock() {
        return elseBlock;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object condValue = condition.evaluate(context);
        boolean condBool = (Boolean) condValue; // TODO: add proper type conversion
        if (condBool) {
            thenBlock.execute(context);
        } else if (elseBlock != null) {
            elseBlock.execute(context);
        }
    }
}
