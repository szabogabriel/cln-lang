package org.clnlang.ast.expression;
import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Array/index access expression: expr[expr]
 */
public class IndexAccessExpr extends Expr {
    private Expr array;
    private Expr index;
    
    public IndexAccessExpr(Expr array, Expr index) {
        this.array = array;
        this.index = index;
    }
    public Expr getArray() {
        return array;
    }
    public Expr getIndex() {
        return index;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        // Can be extended if needed
    }
    public String toString() {
        return array + "[" + index + "]";
    }
}
