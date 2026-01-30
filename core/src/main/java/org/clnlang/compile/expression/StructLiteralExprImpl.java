package org.clnlang.compile.expression;

import org.clnlang.compile.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

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
        // Create struct instance with field values
        return null; // TODO: implement struct construction
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
