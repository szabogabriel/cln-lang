package org.clnlang.compiled.binary.expressions.literal;

import java.math.BigDecimal;

import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;

public class CDecLiteralExpression extends CExpression {

    private BigDecimal value;

    public CDecLiteralExpression(BigDecimal value) {
        super(ExpressionType.DEC_LITERAL, new Types[] {Types.DEC});
        this.value = value;
    }

    @Override
    public BigDecimal getDecValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return value.toString();
    }

}
