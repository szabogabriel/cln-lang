package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.Map;

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
        
        if (objValue == null) {
            throw new RuntimeException("Cannot access member '" + member + "' of null object");
        }
        
        // Structs are represented as Maps
        if (objValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> structMap = (Map<String, Object>) objValue;
            
            if (!structMap.containsKey(member)) {
                String typeName = (String) structMap.get("__type__");
                throw new RuntimeException("Struct " + (typeName != null ? typeName : "unknown") + 
                                         " has no field '" + member + "'");
            }
            
            return structMap.get(member);
        }
        
        throw new RuntimeException("Cannot access member '" + member + "' on non-struct type: " + 
                                 objValue.getClass().getSimpleName());
    }
}
