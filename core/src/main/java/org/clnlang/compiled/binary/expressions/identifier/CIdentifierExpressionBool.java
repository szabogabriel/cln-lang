package org.clnlang.compiled.binary.expressions.identifier;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

public class CIdentifierExpressionBool extends CExpression implements CExecutable {

    private int offset;
    private boolean isGlobal;

    public CIdentifierExpressionBool(int offset, boolean isGlobal) {
        super(ExpressionType.IDENTIFIER, new Types[] {Types.BOOL});
        this.offset = offset;
        this.isGlobal = isGlobal;   
    }

    @Override
    public void execute(ExecutionContext context) {
        
    }

    @Override
    public int[] getResults() {
        return new int [] {offset};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[] {Types.BOOL};
    }

    @Override
    public boolean isGlobal() {
        return isGlobal;
    }
}
