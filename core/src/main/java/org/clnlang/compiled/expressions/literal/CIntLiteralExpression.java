package org.clnlang.compiled.expressions.literal;

import org.clnlang.compiled.Types;
import org.clnlang.compiled.expressions.CExpression;

public class CIntLiteralExpression extends CExpression {

    private long value;

    public CIntLiteralExpression(long value) {
        super(ExpressionType.INT_LITERAL, new Types[] {Types.INT});
        this.value = value;
    }

    @Override
    public long getIntValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return Long.toString(value);
    }

}
