package org.clnlang.ast.statement;
import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Empty statement: ;
 */
public class EmptyStmt extends Stmt {
    
    @Override
    public void accept(ASTVisitor visitor) {
        // Can be extended if needed
    }
    public String toString() {
        return ";";
}
}
