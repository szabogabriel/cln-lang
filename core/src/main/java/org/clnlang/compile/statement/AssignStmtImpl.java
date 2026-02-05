package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.List;
import java.util.Map;

/**
 * Compiled representation of an assignment statement.
 */
public class AssignStmtImpl implements CompiledAction {
    private CompiledExpr lvalue;
    private CompiledExpr value;

    public AssignStmtImpl(CompiledExpr lvalue, CompiledExpr value) {
        this.lvalue = lvalue;
        this.value = value;
    }

    public CompiledExpr getLvalue() {
        return lvalue;
    }

    public CompiledExpr getValue() {
        return value;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object val = value.evaluate(context);
        
        // Handle different types of lvalues
        if (lvalue instanceof org.clnlang.compile.expression.IdentifierExprImpl) {
            // Simple variable assignment: x = value
            org.clnlang.compile.expression.IdentifierExprImpl id = 
                (org.clnlang.compile.expression.IdentifierExprImpl) lvalue;
            String varName = id.getName();
            
            // Try to update the variable in the local context first
            boolean updated = context.getLocalContext().updateVariable(varName, val);
            
            if (!updated) {
                // Try to update in global context
                updated = context.getGlobalContext().updateGlobalVariable(varName, val);
                
                if (!updated) {
                    // If not found in either context, throw an error
                    throw new RuntimeException("Cannot assign to undefined or constant variable: " + varName);
                }
            }
        } else if (lvalue instanceof org.clnlang.compile.expression.MemberAccessExprImpl) {
            // Member access assignment: obj.field = value
            org.clnlang.compile.expression.MemberAccessExprImpl memberAccess = 
                (org.clnlang.compile.expression.MemberAccessExprImpl) lvalue;
            
            // Evaluate the object expression to get the struct instance
            Object objValue = memberAccess.getObject().evaluate(context);
            
            if (objValue == null) {
                throw new RuntimeException("Cannot assign to field '" + memberAccess.getMember() + 
                                         "' of null object");
            }
            
            // Structs are represented as Maps
            if (objValue instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> structMap = (Map<String, Object>) objValue;
                
                String typeName = (String) structMap.get("__type__");
                
                // Check if the field exists
                if (!structMap.containsKey(memberAccess.getMember())) {
                    throw new RuntimeException("Struct " + (typeName != null ? typeName : "unknown") + 
                                             " has no field '" + memberAccess.getMember() + "'");
                }
                
                // Check if the field is mutable
                org.clnlang.runtime.types.StructDefinition structDef = 
                    context.getGlobalContext().getStructType(typeName);
                if (structDef != null && !structDef.isFieldMutable(memberAccess.getMember())) {
                    throw new RuntimeException("Cannot assign to constant field '" + memberAccess.getMember() + 
                                             "' of struct " + typeName + " (field not declared with 'var')");
                }
                
                // Assign the new value to the field
                structMap.put(memberAccess.getMember(), val);
            } else {
                throw new RuntimeException("Cannot assign to member '" + memberAccess.getMember() + 
                                         "' on non-struct type: " + objValue.getClass().getSimpleName());
            }
        } else if (lvalue instanceof org.clnlang.compile.expression.IndexAccessExprImpl) {
            // Array index assignment: arr[i] = value
            org.clnlang.compile.expression.IndexAccessExprImpl indexAccess = 
                (org.clnlang.compile.expression.IndexAccessExprImpl) lvalue;
            
            // Evaluate the array expression
            Object arrayObj = indexAccess.getArray().evaluate(context);
            
            if (arrayObj == null) {
                throw new RuntimeException("Cannot assign to index of null array");
            }
            
            // Index must be an integer
            Object indexObj = indexAccess.getIndex().evaluate(context);
            if (!(indexObj instanceof Long)) {
                throw new RuntimeException("Array index must be an integer, got: " + 
                    (indexObj == null ? "null" : indexObj.getClass().getSimpleName()));
            }
            
            long indexValue = (Long) indexObj;
            
            // Only arrays (List) support index assignment, not strings
            if (arrayObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) arrayObj;
                
                // Check bounds
                if (indexValue < 0 || indexValue >= list.size()) {
                    throw new RuntimeException("Array index out of bounds: " + indexValue + 
                        " (array size: " + list.size() + ")");
                }
                
                // Assign the new value
                list.set((int) indexValue, val);
            } else {
                throw new RuntimeException("Cannot assign to index of non-array type: " + 
                    arrayObj.getClass().getSimpleName());
            }
        } else {
            throw new RuntimeException("Unsupported lvalue type for assignment: " + lvalue.getClass().getSimpleName());
        }
    }
}
