package org.clnlang.compiled.binary.expressions.literal;

import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;

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
