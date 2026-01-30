package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.ExecutionContext;
import org.clnlang.compile.expression.CompiledExpr;

/**
 * Compiled representation of a variable declaration statement.
 */
public class VarDeclStmtImpl implements CompiledAction {
    private boolean isVar;
    private String type;
    private String name;
    private CompiledExpr initializer;

    public VarDeclStmtImpl(boolean isVar, String type, String name, CompiledExpr initializer) {
        this.isVar = isVar;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
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

    public CompiledExpr getInitializer() {
        return initializer;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object value = initializer.evaluate(context);
        // Store variable in context
    }
}
