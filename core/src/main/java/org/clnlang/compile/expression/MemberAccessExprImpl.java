package org.clnlang.compile.expression;

import org.clnlang.runtime.ExecutionContext;

/**
 * Compiled representation of a member access expression.
 */
public class MemberAccessExprImpl implements CompiledExpr {
    private CompiledExpr object;
    private String member;

    public MemberAccessExprImpl(CompiledExpr object, String member) {
        this.object = object;
        this.member = member;
    }

    public CompiledExpr getObject() {
        return object;
    }

    public String getMember() {
        return member;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object objValue = object.evaluate(context);
        // Access member field
        return null; // TODO: implement member access
    }
}
