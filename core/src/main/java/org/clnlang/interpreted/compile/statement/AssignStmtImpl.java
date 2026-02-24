package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

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
        // Evaluate the value once (used by all assignment types)
        Object val = value.evaluate(context);
        
        // Handle different types of lvalues
        if (lvalue instanceof org.clnlang.interpreted.compile.expression.IdentifierExprImpl) {
            // Simple variable assignment: x = value
            org.clnlang.interpreted.compile.expression.IdentifierExprImpl id = 
                (org.clnlang.interpreted.compile.expression.IdentifierExprImpl) lvalue;
            String varName = id.getName();
            
            // Try index-based update first (zero boxing for primitives!)
            if (id.getIndex() >= 0 && id.getType() != null) {
                int index = id.getIndex();
                boolean updated = false;
                
                switch (id.getType()) {
                    case "int":
                        long longValue = value.longValue(context);
                        updated = context.getLocalContext().updateLongByIndex(index, longValue);
                        break;
                    case "bool":
                        boolean boolValue = value.boolValue(context);
                        updated = context.getLocalContext().updateBoolByIndex(index, boolValue);
                        break;
                    case "dec":
                    case "decimal":  // Backward compatibility
                        java.math.BigDecimal decimalValue = value.decimalValue(context);
                        updated = context.getLocalContext().updateDecimalByIndex(index, decimalValue);
                        break;
                    case "string":
                        String stringValue = value.stringValue(context);
                        updated = context.getLocalContext().updateStringByIndex(index, stringValue);
                        break;
                    default:
                        // Object type
                        Object objectValue = value.evaluate(context);
                        updated = context.getLocalContext().updateObjectByIndex(index, objectValue);
                        break;
                }
                
                if (updated) {
                    return; // Success!
                }
                // If index-based update failed, fall through to name-based update
            }
            
            // Fallback to name-based update (backward compatibility)
            boolean updated = context.getLocalContext().updateVariable(varName, val);
            
            if (!updated) {
                // Try to update in global context
                updated = context.getGlobalContext().updateGlobalVariable(varName, val);
                
                if (!updated) {
                    // If not found in either context, throw an error
                    throw new RuntimeException("Cannot assign to undefined or constant variable: " + varName);
                }
            }
        } else if (lvalue instanceof org.clnlang.interpreted.compile.expression.MemberAccessExprImpl) {
            // Member access assignment: obj.field = value
            org.clnlang.interpreted.compile.expression.MemberAccessExprImpl memberAccess = 
                (org.clnlang.interpreted.compile.expression.MemberAccessExprImpl) lvalue;
            
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
                org.clnlang.interpreted.runtime.types.StructDefinition structDef = 
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
        } else if (lvalue instanceof org.clnlang.interpreted.compile.expression.IndexAccessExprImpl) {
            // Array index assignment: arr[i] = value
            org.clnlang.interpreted.compile.expression.IndexAccessExprImpl indexAccess = 
                (org.clnlang.interpreted.compile.expression.IndexAccessExprImpl) lvalue;
            
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
