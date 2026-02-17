package org.clnlang.interpreted.compile.declaration;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;
import org.clnlang.interpreted.runtime.types.UnionDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a union declaration.
 */
public class UnionDeclImpl implements CompiledAction {
    private String name;
    private List<String> members;
    private boolean isExposed;

    public UnionDeclImpl(String name, boolean isExposed) {
        this.name = name;
        this.isExposed = isExposed;
        this.members = new ArrayList<>();
    }

    public void addMember(String memberType) {
        members.add(memberType);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }

    public boolean isExposed() {
        return isExposed;
    }

    /**
     * Create a UnionDefinition from this declaration
     */
    public UnionDefinition toUnionDefinition(String packageName) {
        UnionDefinition definition = new UnionDefinition(name, packageName, isExposed);
        for (String memberType : members) {
            definition.addMember(memberType);
        }
        return definition;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Union registration is handled by ProgramImpl
        // This method is for potential future runtime logic
    }
}
