package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

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
        boolean condBool = evaluateAsBoolean(condValue);
        if (condBool) {
            thenBlock.execute(context);
        } else if (elseBlock != null) {
            elseBlock.execute(context);
        }
    }

    /**
     * Converts the condition value to a boolean.
     * Only Boolean type is accepted as a valid condition.
     * 
     * @param value the value to evaluate
     * @return the boolean value
     * @throws IllegalArgumentException if the value is not a Boolean
     */
    private boolean evaluateAsBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new IllegalArgumentException(
            "If condition must be a boolean expression, got: " + 
            (value == null ? "null" : value.getClass().getSimpleName())
        );
    }
}
