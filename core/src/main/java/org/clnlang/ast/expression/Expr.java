package org.clnlang.ast.expression;
import org.clnlang.ast.ASTNode;
/**
 * Base class for all expression nodes.
 */
public abstract class Expr extends ASTNode {
    @Override
    public abstract String toString();
}
