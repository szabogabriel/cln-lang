package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of an import declaration.
 */
public class ImportDeclarationImpl implements CompiledAction {
    private String importPath;
    private boolean isWildcard;

    public ImportDeclarationImpl(String importPath, boolean isWildcard) {
        this.importPath = importPath;
        this.isWildcard = isWildcard;
    }

    public String getImportPath() {
        return importPath;
    }

    public boolean isWildcard() {
        return isWildcard;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Load and register imported modules
    }
}
