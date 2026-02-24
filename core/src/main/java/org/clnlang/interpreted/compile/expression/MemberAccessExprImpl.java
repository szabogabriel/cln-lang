package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.util.List;
import java.util.Map;

/**
 * Compiled representation of a member access expression.
 * Supports accessing struct fields and special properties (like array.length).
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
        
        // Handle array.length
        if (objValue instanceof List && member.equals("length")) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) objValue;
            return (long) list.size();
        }
        
        // Handle string.length
        if (objValue instanceof String && member.equals("length")) {
            String str = (String) objValue;
            return (long) str.length();
        }
        
        // Structs (and union members) are represented as Maps
        if (objValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> structMap = (Map<String, Object>) objValue;
            
            if (!structMap.containsKey(member)) {
                String typeName = (String) structMap.get("__type__");
                
                // Check if this might be a union type and if the field is a common field
                // This provides better error messages for union types
                String errorMsg = "Struct " + (typeName != null ? typeName : "unknown") + 
                                " has no field '" + member + "'";
                
                // Try to provide more helpful error for union types
                if (typeName != null) {
                    org.clnlang.interpreted.runtime.types.UnionDefinition unionDef = 
                        context.getGlobalContext().getUnionType(typeName);
                    if (unionDef != null && unionDef.hasCommonField(member)) {
                        // This shouldn't happen - the struct is marked as a union but doesn't have the field
                        errorMsg = "Union " + typeName + " has common field '" + member + 
                                 "' but the actual instance doesn't contain it";
                    }
                }
                
                throw new RuntimeException(errorMsg);
            }
            
            return structMap.get(member);
        }
        
        throw new RuntimeException("Cannot access member '" + member + "' on type: " + 
                                 objValue.getClass().getSimpleName());
    }
}
