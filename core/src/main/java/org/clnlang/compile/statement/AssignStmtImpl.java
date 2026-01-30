package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.compile.expression.CompiledExpr;

/**
 * Compiled representation of an assignment statement.
 */
public class AssignStmtImpl implements CompiledAction {
    private CompiledExpr lvalue;
    private CompiledExpr value;

    public AssignStmtImpl(CompiledExpr lvalue, CompiledExpr value) {
        this.lvalue = lvalue;
        this.value = value;
    }

    public CompiledExpr getLvalue() {
        return lvalue;
    }

    public CompiledExpr getValue() {
        return value;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object val = value.evaluate(context);
        // Assign to lvalue
    }
}
