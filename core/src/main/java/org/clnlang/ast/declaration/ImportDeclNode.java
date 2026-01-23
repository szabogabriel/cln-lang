package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;

/**
 * AST node representing an import declaration.
 */
public class ImportDeclNode extends ASTNode {
    private String importPath;
    private boolean isWildcard;

    public ImportDeclNode(String importPath, boolean isWildcard) {
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "Import: " + importPath + (isWildcard ? ".*" : "");
    }
}
