package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.expression.Expr;

/**
 * If statement: if (expr) block (else block)?
 */
public class IfStmt extends Stmt {
    private Expr condition;
    private BlockNode thenBlock;
    private BlockNode elseBlock;

    public IfStmt(Expr condition, BlockNode thenBlock, BlockNode elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public Expr getCondition() {
        return condition;
    }

    public BlockNode getThenBlock() {
        return thenBlock;
    }

    public BlockNode getElseBlock() {
        return elseBlock;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        // Can be extended if needed
    }

    public String toString() {
        String result = "if (" + condition + ") " + thenBlock;
        if (elseBlock != null) {
            result += " else " + elseBlock;
        }
        return result;
    }
}
