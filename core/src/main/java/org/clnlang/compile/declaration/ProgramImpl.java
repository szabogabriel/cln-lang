package org.clnlang.compile.declaration;

import java.util.ArrayList;
import java.util.List;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.runtime.Linker;

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

    public void execute(ExecutionContext context) throws Exception {
        // TODO
    }

    public void populateContext(ExecutionContext context) throws Exception {
        // First, register all declarations in the context
        if (packageDecl != null) {
            packageDecl.execute(context);
        }

        for (ImportDeclImpl importDecl : imports) {
            context.registerImport(importDecl);
        }

        // Register all type definitions, functions, and global variables
        for (CompiledAction decl : declarations) {
            if (decl instanceof StructDeclImpl) {
                StructDeclImpl structDecl = (StructDeclImpl) decl;
                context.getGlobalContext().registerStructType(
                        structDecl.getName(),
                        structDecl.toStructDefinition());
            } else if (decl instanceof UnionDeclImpl) {
                UnionDeclImpl unionDecl = (UnionDeclImpl) decl;
                context.getGlobalContext().registerUnionType(
                        unionDecl.getName(),
                        unionDecl.toUnionDefinition());
            } else if (decl instanceof FunctionDeclImpl) {
                FunctionDeclImpl funcDecl = (FunctionDeclImpl) decl;
                context.getGlobalContext().registerFunction(funcDecl.getName(), funcDecl);
            } else if (decl instanceof GlobalVarDeclImpl) {
                GlobalVarDeclImpl varDecl = (GlobalVarDeclImpl) decl;
                // Evaluate the initializer and register
                Object value = varDecl.getInitializer().evaluate(context);
                context.getGlobalContext().registerGlobalVariable(varDecl, value);
            }
        }
    }
}
