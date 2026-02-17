package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.math.BigDecimal;

/**
 * Compiled representation of a decimal literal.
 */
public class DecLiteralExprImpl implements CompiledExpr {
    private BigDecimal value;

    public DecLiteralExprImpl(String value) {
        this.value = new BigDecimal(value);
    }

    public DecLiteralExprImpl(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        return value;
    }
}
