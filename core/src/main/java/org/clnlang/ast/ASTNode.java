package org.clnlang.ast;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Base class for all AST nodes in the Clean language.
 */
public abstract class ASTNode {
    public abstract void accept(ASTVisitor visitor);
    
    @Override
    public abstract String toString();
}
