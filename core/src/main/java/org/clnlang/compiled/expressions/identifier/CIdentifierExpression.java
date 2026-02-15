package org.clnlang.compiled.expressions.identifier;

import org.clnlang.compiled.Types;
import org.clnlang.compiled.expressions.CExpression;

public class CIdentifierExpression extends CExpression {

    private int offset;
    private Types type;
    private boolean isGlobal;

    public CIdentifierExpression(int offset, Types type, boolean isGlobal) {
        super(ExpressionType.IDENTIFIER, new Types[] {type});
        this.offset = offset;
        this.type = type;
        this.isGlobal = isGlobal;   
    }

    public Types getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
    
    public boolean isGlobal() {
        return isGlobal;
    }
}
