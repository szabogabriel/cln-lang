package org.clnlang.ast.expression;
import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Boolean literal (true or false)
 */
public class BoolLiteralExpr extends Expr {
    private boolean value;
    
    public BoolLiteralExpr(boolean value) {
        this.value = value;
    }
    public boolean getValue() {
        return value;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    public String toString() {
        return String.valueOf(value);
    }
}

