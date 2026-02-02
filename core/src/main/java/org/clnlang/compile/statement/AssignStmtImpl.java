package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.context.ExecutionContext;
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
        // For now, we only support identifier lvalues
        if (lvalue instanceof org.clnlang.compile.expression.IdentifierExprImpl) {
            org.clnlang.compile.expression.IdentifierExprImpl id = 
                (org.clnlang.compile.expression.IdentifierExprImpl) lvalue;
            String varName = id.getName();
            
            // Try to update the variable in the local context
            boolean updated = context.getLocalContext().updateVariable(varName, val);
            
            if (!updated) {
                // If not found in local context, throw an error
                throw new RuntimeException("Cannot assign to undefined or constant variable: " + varName);
            }
        } else {
            // TODO: Support member access and index access lvalues
            throw new RuntimeException("Unsupported lvalue type for assignment: " + lvalue.getClass().getSimpleName());
        }
    }
}
