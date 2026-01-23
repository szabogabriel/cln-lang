package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a struct declaration.
 */
public class StructDeclNode extends ASTNode {
    private String name;
    private List<FieldDecl> fields;
    private boolean isExposed;

    public StructDeclNode(String name, boolean isExposed) {
        this.name = name;
        this.isExposed = isExposed;
        this.fields = new ArrayList<>();
    }

    public void addField(String type, String fieldName) {
        fields.add(new FieldDecl(type, fieldName));
    }

    public String getName() {
        return name;
    }

    public List<FieldDecl> getFields() {
        return fields;
    }

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((isExposed ? "expose " : "")).append("Struct: ").append(name);
        sb.append(" { ");
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(fields.get(i));
        }
        sb.append(" }");
        return sb.toString();
    }

    public static class FieldDecl {
        private String type;
        private String name;

        public FieldDecl(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return type + " " + name;
        }
    }
}