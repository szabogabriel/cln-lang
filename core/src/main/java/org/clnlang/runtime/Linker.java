package org.clnlang.runtime;

import java.util.ArrayList;
import java.util.List;

import org.clnlang.compile.declaration.ImportDeclImpl;

public class Linker {

    private final List<ImportDeclImpl> imports = new ArrayList<>();

    public void registerImport(ImportDeclImpl importDecl) {
        imports.add(importDecl);
    }

    public void resolveImports(ExecutionContext context) throws Exception {
        for (ImportDeclImpl importDecl : imports) {
            importDecl.execute(context);
        }
    }
    
}
