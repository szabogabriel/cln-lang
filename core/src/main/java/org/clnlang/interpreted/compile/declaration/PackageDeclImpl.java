package org.clnlang.interpreted.compile.declaration;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a package declaration.
 */
public class PackageDeclImpl implements CompiledAction {
    private String packageName;

    public PackageDeclImpl(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Set package context
        context.getGlobalContext().setPackageName(packageName);
    }
}
