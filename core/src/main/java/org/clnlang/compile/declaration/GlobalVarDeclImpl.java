package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.expression.CompiledExpr;
import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of a global variable or constant declaration.
 */
public class GlobalVarDeclImpl implements CompiledAction {
    private final boolean isMutable; // true if 'var', false if constant
    private final String type;
    private final String name;
    private final CompiledExpr initializer;
    private final boolean isExposed;

    public GlobalVarDeclImpl(boolean isMutable, String type, String name, CompiledExpr initializer, boolean isExposed) {
        this.isMutable = isMutable;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
        this.isExposed = isExposed;
    }

    public boolean isMutable() {
        return isMutable;
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

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Evaluate the initializer
        Object value = initializer.evaluate(context);
        
        // Register in global context
        if (isMutable) {
            context.getGlobalContext().setGlobalVariable(name, value);
        } else {
            context.getGlobalContext().setGlobalConstant(name, value);
        }
    }
}
