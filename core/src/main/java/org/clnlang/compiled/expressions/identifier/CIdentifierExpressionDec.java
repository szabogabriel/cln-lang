package org.clnlang.compiled.expressions.identifier;

import org.clnlang.compiled.CExecutable;
import org.clnlang.compiled.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.expressions.CExpression;

public class CIdentifierExpressionDec extends CExpression implements CExecutable {

    private int offset;
    private boolean isGlobal;

    public CIdentifierExpressionDec(int offset, boolean isGlobal) {
        super(ExpressionType.IDENTIFIER, new Types[] {Types.DEC});
        this.offset = offset;
        this.isGlobal = isGlobal;   
    }

    @Override
    public boolean isGlobal() {
        return isGlobal;
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
        return new Types[] {Types.DEC};
    }
}
