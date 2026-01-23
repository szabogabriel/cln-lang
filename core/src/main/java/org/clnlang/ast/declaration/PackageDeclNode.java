package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;

/**
 * AST node representing a package declaration.
 */
public class PackageDeclNode extends ASTNode {
    private String packageName;

    public PackageDeclNode(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "Package: " + packageName;
    }
}
