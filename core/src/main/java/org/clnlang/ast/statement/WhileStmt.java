package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.expression.Expr;

/**
 * While statement: while (expr) block
 */
public class WhileStmt extends Stmt {
    private Expr condition;
    private BlockNode body;

    public WhileStmt(Expr condition, BlockNode body) {
        this.condition = condition;
        this.body = body;
    }

    public Expr getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "while (" + condition + ") " + body;
    }
}