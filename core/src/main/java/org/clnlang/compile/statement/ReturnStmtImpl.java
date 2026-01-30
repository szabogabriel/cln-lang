package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.compile.expression.CompiledExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a return statement.
 */
public class ReturnStmtImpl implements CompiledAction {
    private List<CompiledExpr> returnValues;

    public ReturnStmtImpl() {
        this.returnValues = new ArrayList<>();
    }

    public ReturnStmtImpl(CompiledExpr value) {
        this.returnValues = new ArrayList<>();
        this.returnValues.add(value);
    }

    public ReturnStmtImpl(List<CompiledExpr> values) {
        this.returnValues = values != null ? values : new ArrayList<>();
    }

    public List<CompiledExpr> getReturnValues() {
        return returnValues;
    }

    public boolean hasReturnValues() {
        return !returnValues.isEmpty();
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        List<Object> values = new ArrayList<>();
        for (CompiledExpr expr : returnValues) {
            values.add(expr.evaluate(context));
        }
        // Set return values in context
    }
}
