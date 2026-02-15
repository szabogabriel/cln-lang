package org.clnlang.compiled.expressions.literal;

import org.clnlang.compiled.Types;
import org.clnlang.compiled.expressions.CExpression;

public class CStringLiteralExpression extends CExpression {

    private String value;

    public CStringLiteralExpression(String value) {
        super(ExpressionType.STRING_LITERAL, new Types[] {Types.STRING});
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value;
    }

}
