package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a struct declaration.
 */
public class StructDeclImpl implements CompiledAction {
    private String name;
    private List<FieldDecl> fields;
    private boolean isExposed;

    public StructDeclImpl(String name, boolean isExposed) {
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
    public void execute(ExecutionContext context) throws Exception {
        // Register struct type in context
    }

    /**
     * Struct field declaration
     */
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
    }
}
