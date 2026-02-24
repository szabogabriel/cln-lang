package org.clnlang.interpreted.compile.declaration;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of an import declaration.
 */
public class ImportDeclImpl implements CompiledAction {
    private String importPath;
    private boolean isWildcard;

    public ImportDeclImpl(String importPath, boolean isWildcard) {
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
