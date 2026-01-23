package org.clnlang.ast.statement;
import org.clnlang.ast.ASTNode;
/**
 * Base class for all statement nodes.
 */
public abstract class Stmt extends ASTNode {
    @Override
    public abstract String toString();
}
