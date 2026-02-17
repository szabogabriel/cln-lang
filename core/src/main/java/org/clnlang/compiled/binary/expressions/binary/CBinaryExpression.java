package org.clnlang.compiled.binary.expressions.binary;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.context.ExecutionContext;

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
