package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Root node of the AST representing a complete program.
 */
public class ProgramNode extends ASTNode {
    private PackageDeclNode packageDecl;
    private List<ImportDeclNode> imports;
    private List<ASTNode> declarations;

    public ProgramNode() {
        this.imports = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    public void setPackageDecl(PackageDeclNode packageDecl) {
        this.packageDecl = packageDecl;
    }

    public PackageDeclNode getPackageDecl() {
        return packageDecl;
    }

    public void addImport(ImportDeclNode importDecl) {
        imports.add(importDecl);
    }

    public List<ImportDeclNode> getImports() {
        return imports;
    }

    public void addDeclaration(ASTNode decl) {
        declarations.add(decl);
    }

    public List<ASTNode> getDeclarations() {
        return declarations;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Program:\n");
        if (packageDecl != null) {
            sb.append("  ").append(packageDecl).append("\n");
        }
        for (ImportDeclNode imp : imports) {
            sb.append("  ").append(imp).append("\n");
        }
        for (ASTNode decl : declarations) {
            sb.append("  ").append(decl).append("\n");
        }
        return sb.toString();
    }
}