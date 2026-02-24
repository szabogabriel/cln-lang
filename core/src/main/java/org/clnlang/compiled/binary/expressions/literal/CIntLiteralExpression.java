package org.clnlang.compiled.binary.expressions.literal;

import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;

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
