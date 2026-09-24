package org.clnlang.compile.statement;

import java.util.List;
import java.util.Map;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Compiled representation of an assignment statement.
 */
public class AssignStmtImpl implements CompiledAction {
    private CompiledExpr lvalue;
    private CompiledExpr value;

    // Monomorphic cache for member-access assignments: avoids re-resolving the struct
    // definition's field mutability on every iteration when the struct type doesn't change
    // (the member name is fixed per node, so only the runtime struct type can vary, e.g. unions).
    private String cachedStructType;
    private boolean cachedFieldMutable;

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
        // Handle different types of lvalues
        if (lvalue instanceof org.clnlang.compile.expression.IdentifierExprImpl) {
            // Simple variable assignment: x = value
            org.clnlang.compile.expression.IdentifierExprImpl id = 
                (org.clnlang.compile.expression.IdentifierExprImpl) lvalue;
            String varName = id.getName();
            
            // Determine the fastest known storage slot: a local index (compile-time
            // resolved) or, failing that, a cached global registry slot.
            String targetType = id.getType();
            int targetIndex = id.getIndex();
            boolean isGlobal = false;
            if (targetIndex < 0 && id.ensureGlobalResolved(context)) {
                isGlobal = true;
                targetType = id.getResolvedGlobalType();
                targetIndex = id.getResolvedGlobalIndex();
            }
            
            if (targetIndex >= 0 && targetType != null) {
                boolean updated;
                
                switch (targetType) {
                    case "int": {
                        long longValue = value.longValue(context);
                        updated = isGlobal
                                ? context.getGlobalContext().updateLongByIndex(targetIndex, longValue)
                                : context.getLocalContext().updateLongByIndex(targetIndex, longValue);
                        break;
                    }
                    case "bool": {
                        boolean boolValue = value.boolValue(context);
                        updated = isGlobal
                                ? context.getGlobalContext().updateBoolByIndex(targetIndex, boolValue)
                                : context.getLocalContext().updateBoolByIndex(targetIndex, boolValue);
                        break;
                    }
                    case "dec":
                    case "decimal": {  // Backward compatibility
                        java.math.BigDecimal decimalValue = value.decimalValue(context);
                        updated = isGlobal
                                ? context.getGlobalContext().updateDecimalByIndex(targetIndex, decimalValue)
                                : context.getLocalContext().updateDecimalByIndex(targetIndex, decimalValue);
                        break;
                    }
                    case "string": {
                        String stringValue = value.stringValue(context);
                        updated = isGlobal
                                ? context.getGlobalContext().updateStringByIndex(targetIndex, stringValue)
                                : context.getLocalContext().updateStringByIndex(targetIndex, stringValue);
                        break;
                    }
                    default: {
                        // Object type
                        Object objectValue = value.evaluate(context);
                        updated = isGlobal
                                ? context.getGlobalContext().updateObjectByIndex(targetIndex, objectValue)
                                : context.getLocalContext().updateObjectByIndex(targetIndex, objectValue);
                        break;
                    }
                }
                
                if (updated) {
                    return; // Success!
                }
                // If index-based update failed, fall through to name-based update
            }
            
            // Fallback to name-based update (backward compatibility)
            Object val = value.evaluate(context);
            boolean updated = context.getLocalContext().updateVariable(varName, val);
            
            if (!updated) {
                updated = context.getGlobalContext().updateGlobalVariable(varName, val);
                
                if (!updated) {
                    // If not found in either context, throw an error
                    throw new RuntimeException("Cannot assign to undefined or constant variable: " + varName);
                }
            }
        } else if (lvalue instanceof org.clnlang.compile.expression.MemberAccessExprImpl) {
            // Member access assignment: obj.field = value
            Object val = value.evaluate(context);
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
                String member = memberAccess.getMember();
                
                // Check if the field exists
                if (!structMap.containsKey(member)) {
                    throw new RuntimeException("Struct " + (typeName != null ? typeName : "unknown") + 
                                             " has no field '" + member + "'");
                }
                
                // Check if the field is mutable (cached per struct type - the member name
                // is fixed for this node, so only the type can change between calls)
                if (!java.util.Objects.equals(typeName, cachedStructType)) {
                    org.clnlang.runtime.types.StructDefinition structDef = 
                        context.getGlobalContext().getStructType(typeName);
                    cachedFieldMutable = structDef == null || structDef.isFieldMutable(member);
                    cachedStructType = typeName;
                }
                if (!cachedFieldMutable) {
                    throw new RuntimeException("Cannot assign to constant field '" + member + 
                                             "' of struct " + typeName + " (field not declared with 'var')");
                }
                
                // Assign the new value to the field
                structMap.put(member, val);
            } else {
                throw new RuntimeException("Cannot assign to member '" + memberAccess.getMember() + 
                                         "' on non-struct type: " + objValue.getClass().getSimpleName());
            }
        } else if (lvalue instanceof org.clnlang.compile.expression.IndexAccessExprImpl) {
            // Array index assignment: arr[i] = value
            Object val = value.evaluate(context);
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
