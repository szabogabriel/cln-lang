package org.clnlang.ast.expression;

import org.clnlang.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Function call expression
 */
public class CallExpr extends Expr {
    private Expr function;
    private List<Expr> arguments;

    public CallExpr(Expr function, List<Expr> arguments) {
        this.function = function;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
    }

    public Expr getFunction() {
        return function;
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(function).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(arguments.get(i));
        }
        sb.append(")");
        return sb.toString();
    }
}
