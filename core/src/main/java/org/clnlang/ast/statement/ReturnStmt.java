package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;
import java.util.ArrayList;
import java.util.List;

/**
 * Return statement: return; | return expr; | return (exprList?);
 */
public class ReturnStmt extends Stmt {
    private List<Expr> returnValues;

    public ReturnStmt() {
        this.returnValues = new ArrayList<>();
    }

    public ReturnStmt(Expr value) {
        this.returnValues = new ArrayList<>();
        this.returnValues.add(value);
    }

    public ReturnStmt(List<Expr> values) {
        this.returnValues = values != null ? values : new ArrayList<>();
    }

    public List<Expr> getReturnValues() {
        return returnValues;
    }

    public boolean hasReturnValues() {
        return !returnValues.isEmpty();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        if (returnValues.isEmpty()) {
            return "return";
        } else if (returnValues.size() == 1) {
            return "return " + returnValues.get(0);
        } else {
            return "return (" + String.join(", ", returnValues.stream()
                    .map(Object::toString).toArray(String[]::new)) + ")";
        }
    }
}
