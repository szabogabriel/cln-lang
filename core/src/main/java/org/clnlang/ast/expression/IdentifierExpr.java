package org.clnlang.ast.expression;
import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Identifier reference
 */
public class IdentifierExpr extends Expr {
    private String name;
    
    public IdentifierExpr(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    public String toString() {
        return "Identifier(" + name + ")";
    }
}
