package org.clnlang.ast.expression;

import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Member access expression (dot notation): expr.ID
 */
public class MemberAccessExpr extends Expr {
    private Expr object;
    private String member;

    public MemberAccessExpr(Expr object, String member) {
        this.object = object;
        this.member = member;
    }

    public Expr getObject() {
        return object;
    }

    public String getMember() {
        return member;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return object + "." + member;
    }
}
