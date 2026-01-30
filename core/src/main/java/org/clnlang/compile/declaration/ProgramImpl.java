package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a complete program.
 */
public class ProgramImpl implements CompiledAction {
    private PackageDeclImpl packageDecl;
    private List<ImportDeclImpl> imports;
    private List<CompiledAction> declarations;

    public ProgramImpl() {
        this.imports = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    public void setPackageDecl(PackageDeclImpl packageDecl) {
        this.packageDecl = packageDecl;
    }

    public PackageDeclImpl getPackageDecl() {
        return packageDecl;
    }

    public void addImport(ImportDeclImpl importDecl) {
        imports.add(importDecl);
    }

    public List<ImportDeclImpl> getImports() {
        return imports;
    }

    public void addDeclaration(CompiledAction decl) {
        declarations.add(decl);
    }

    public List<CompiledAction> getDeclarations() {
        return declarations;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        if (packageDecl != null) {
            packageDecl.execute(context);
        }
        for (ImportDeclImpl importDecl : imports) {
            importDecl.execute(context);
        }
        for (CompiledAction decl : declarations) {
            decl.execute(context);
        }
    }
}
