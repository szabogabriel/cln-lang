package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a tuple assignment statement.
 */
public class TupleAssignStmtImpl implements CompiledAction {
    private List<TupleBind> bindings;
    private CompiledExpr value;

    public TupleAssignStmtImpl(List<TupleBind> bindings, CompiledExpr value) {
        this.bindings = bindings != null ? bindings : new ArrayList<>();
        this.value = value;
    }

    public List<TupleBind> getBindings() {
        return bindings;
    }

    public CompiledExpr getValue() {
        return value;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object tupleValue = value.evaluate(context);
        // Unpack tuple and assign to bindings
    }

    /**
     * Represents a single binding in a tuple assignment
     */
    public static class TupleBind {
        private boolean isVar;
        private String type;
        private String name;

        public TupleBind(boolean isVar, String type, String name) {
            this.isVar = isVar;
            this.type = type;
            this.name = name;
        }

        public boolean isVar() {
            return isVar;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
