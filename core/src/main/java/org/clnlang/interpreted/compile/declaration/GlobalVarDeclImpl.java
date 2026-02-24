package org.clnlang.interpreted.compile.declaration;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a global variable or constant declaration.
 */
public class GlobalVarDeclImpl implements CompiledAction {
    private final boolean isMutable; // true if 'var', false if constant
    private final String type;
    private final String name;
    private final CompiledExpr initializer;
    private final boolean isExposed;
    private String packageName;

    public GlobalVarDeclImpl(boolean isMutable, String type, String name, CompiledExpr initializer, boolean isExposed) {
        this.isMutable = isMutable;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
        this.isExposed = isExposed;
        this.packageName = null; // Will be set later
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
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Global variable registration is handled by ProgramImpl
        // This method is for potential future runtime logic
    }
}
