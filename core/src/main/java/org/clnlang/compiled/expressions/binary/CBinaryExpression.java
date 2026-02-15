package org.clnlang.compiled.expressions.binary;

import org.clnlang.compiled.CExecutable;
import org.clnlang.compiled.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.expressions.CExpression;

public class CBinaryExpression extends CExpression implements CExecutable {

    private CExpression left;
    private String operator;
    private CExpression right;

     public CBinaryExpression(CExpression left, String operator, CExpression right, Types returnType) {
        super(ExpressionType.BINARY_EXPRESSION, new Types[] {returnType});
        this.left = left;
        this.operator = operator;
        this.right = right;
     }

    @Override
    public void execute(ExecutionContext context) {
        
    }

    @Override
    public int[] getResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'result'");
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}
