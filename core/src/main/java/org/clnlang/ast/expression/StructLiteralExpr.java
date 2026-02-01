package org.clnlang.ast.expression;
import org.clnlang.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;
/**
 * Struct literal: TypeName(field: value, ...)
 */
public class StructLiteralExpr extends Expr {
    private String typeName;
    private List<FieldInit> fields;
    
    public StructLiteralExpr(String typeName, List<FieldInit> fields) {
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName).append("(");
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(fields.get(i));
        }
        sb.append(")");
        return sb.toString();
    }
    /**
     * Field initialization in struct literal
     */
    public static class FieldInit {
        private String fieldName;
        private Expr value;
        
        public FieldInit(String fieldName, Expr value) {
            this.fieldName = fieldName;
            this.value = value;
        }
        public String getFieldName() {
            return fieldName;
        }
        public Expr getValue() {
            return value;
        }
        @Override
        public String toString() {
            return fieldName + ": " + value;
        }
    }
}
