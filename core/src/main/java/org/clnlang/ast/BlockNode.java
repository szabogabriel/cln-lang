package org.clnlang.ast;

import org.clnlang.ast.statement.Stmt;
import org.clnlang.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a statement block.
 */
public class BlockNode extends ASTNode {
    private List<Stmt> statements;
    
    public BlockNode() {
        this.statements = new ArrayList<>();
    }
    
    public void addStatement(Stmt statement) {
        statements.add(statement);
    }
    
    public List<Stmt> getStatements() {
        return statements;
    }
    
    @Override
    public void accept(ASTVisitor visitor) {
        // Blocks are typically traversed inline, not via visitor
    }
    
    @Override
    public String toString() {
        return "Block { " + statements.size() + " statements }";
    }
}
