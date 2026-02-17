package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiled representation of a struct literal expression.
 */
public class StructLiteralExprImpl implements CompiledExpr {
    private String typeName;
    private List<FieldInit> fields;

    public StructLiteralExprImpl(String typeName, List<FieldInit> fields) {
        this.typeName = typeName;
        this.fields = fields != null ? fields : new ArrayList<>();
    }

    public String getTypeName() {
        return typeName;
    }

    public List<FieldInit> getFields() {
        return fields;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        // Create struct instance as a Map with field values
        Map<String, Object> structInstance = new HashMap<>();
        
        // Store type metadata
        structInstance.put("__type__", typeName);
        
        // Evaluate and store each field value
        for (FieldInit field : fields) {
            Object fieldValue = field.getValue().evaluate(context);
            structInstance.put(field.getFieldName(), fieldValue);
        }
        
        return structInstance;
    }

    /**
     * Field initialization in struct literal
     */
    public static class FieldInit {
        private String fieldName;
        private CompiledExpr value;

        public FieldInit(String fieldName, CompiledExpr value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        public String getFieldName() {
            return fieldName;
        }

        public CompiledExpr getValue() {
            return value;
        }
    }
}
