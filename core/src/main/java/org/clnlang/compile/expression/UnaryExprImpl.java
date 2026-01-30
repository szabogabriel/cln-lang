package org.clnlang.compile.expression;

import org.clnlang.compile.ExecutionContext;

/**
 * Compiled representation of a unary expression.
 */
public class UnaryExprImpl implements CompiledExpr {
    private String operator;
    private CompiledExpr operand;

    public UnaryExprImpl(String operator, CompiledExpr operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public CompiledExpr getOperand() {
        return operand;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object value = operand.evaluate(context);
        // Perform unary operation based on operator
        return null; // TODO: implement operator logic
    }
}
