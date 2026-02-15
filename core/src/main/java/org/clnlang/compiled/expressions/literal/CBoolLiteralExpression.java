package org.clnlang.compiled.expressions.literal;

import org.clnlang.compiled.Types;
import org.clnlang.compiled.expressions.CExpression;

public class CBoolLiteralExpression extends CExpression {

    private boolean value;

    public CBoolLiteralExpression(boolean value) {
        super(ExpressionType.BOOL_LITERAL, new Types[] {Types.BOOL});
        this.value = value;
    }

    @Override
    public boolean getBoolValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        return Boolean.toString(value);
    }

    
}
