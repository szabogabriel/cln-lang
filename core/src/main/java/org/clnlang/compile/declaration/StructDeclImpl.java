package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.types.StructDefinition;

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

    public void addField(String type, String fieldName, boolean isVar) {
        fields.add(new FieldDecl(type, fieldName, isVar));
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

    /**
     * Create a StructDefinition from this declaration
     */
    public StructDefinition toStructDefinition(String packageName) {
        StructDefinition definition = new StructDefinition(name, packageName, isExposed);
        for (FieldDecl field : fields) {
            definition.addField(field.getName(), field.getType(), field.isVar());
        }
        return definition;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Struct registration is handled by ProgramImpl
        // This method is for potential future runtime logic
    }

    /**
     * Struct field declaration
     */
    public static class FieldDecl {
        private String type;
        private String name;
        private boolean isVar;

        public FieldDecl(String type, String name, boolean isVar) {
            this.type = type;
            this.name = name;
            this.isVar = isVar;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public boolean isVar() {
            return isVar;
        }
    }
}
