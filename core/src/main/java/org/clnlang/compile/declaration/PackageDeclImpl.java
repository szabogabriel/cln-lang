package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.ExecutionContext;

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
    }
}
