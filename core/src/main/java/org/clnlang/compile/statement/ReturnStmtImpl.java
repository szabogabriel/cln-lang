package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a return statement.
 */
public class ReturnStmtImpl implements CompiledAction {
    private List<CompiledExpr> returnValues;
    private List<String> returnVarNames;  // Names of return variables to look up

    public ReturnStmtImpl() {
        this.returnValues = new ArrayList<>();
        this.returnVarNames = new ArrayList<>();
    }

    public ReturnStmtImpl(CompiledExpr value) {
        this.returnValues = new ArrayList<>();
        this.returnValues.add(value);
        this.returnVarNames = new ArrayList<>();
    }

    public ReturnStmtImpl(List<CompiledExpr> values) {
        this.returnValues = values != null ? values : new ArrayList<>();
        this.returnVarNames = new ArrayList<>();
    }
    
    /**
     * Static factory method for returning named return variables
     */
    public static ReturnStmtImpl withNamedReturnVars(List<String> returnVarNames) {
        ReturnStmtImpl stmt = new ReturnStmtImpl();
        stmt.returnVarNames = returnVarNames != null ? returnVarNames : new ArrayList<>();
        return stmt;
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
        
        // If we have explicit return expressions, evaluate them
        if (!returnValues.isEmpty()) {
            for (CompiledExpr expr : returnValues) {
                values.add(expr.evaluate(context));
            }
        } 
        // Otherwise, look up the named return variables
        else if (!returnVarNames.isEmpty()) {
            for (String varName : returnVarNames) {
                //Object value = context.getLocalContext().getVariable(varName);
                Object value = context.getLocalContext().getValue(varName);
                values.add(value);
            }
        }
        
        // Set return values in context
        context.setReturnValues(values);
    }
}
