package org.clnlang.ast.expression;
import org.clnlang.ast.visitor.ASTVisitor;

/**
 * Unary expression with an operator
 */
public class UnaryExpr extends Expr {
    private String operator;
    private Expr operand;
    
    public UnaryExpr(String operator, Expr operand) {
        this.operator = operator;
        this.operand = operand;
    }
    public String getOperator() {
        return operator;
    }
    public Expr getOperand() {
        return operand;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        //TODO: implement visitor pattern
        // Can be extended if needed
    }
    public String toString() {
        return operator + operand;
    }
}