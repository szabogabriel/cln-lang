package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

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
        
        // Convert to list if it's not already
        List<Object> values;
        if (tupleValue instanceof List<?>) {
            // Safely extract values from the list
            List<?> rawList = (List<?>) tupleValue;
            values = new ArrayList<>(rawList.size());
            for (Object item : rawList) {
                values.add(item);
            }
        } else {
            // Single value - wrap it in a list
            values = new ArrayList<>();
            values.add(tupleValue);
        }
        
        // Validate that we have the right number of values
        if (values.size() != bindings.size()) {
            throw new RuntimeException(
                String.format("Tuple assignment expects %d values but got %d",
                    bindings.size(), values.size())
            );
        }
        
        // Assign each value to the corresponding binding
        for (int i = 0; i < bindings.size(); i++) {
            TupleBind binding = bindings.get(i);
            Object val = values.get(i);
            
            // Declare the variable in the current context
            // Variables declared with 'var' are mutable
            if (binding.isVar()) {
                context.getLocalContext().setVariable(binding.getName(), val);
            } else {
                context.getLocalContext().setConstant(binding.getName(), val);
            }
        }
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
