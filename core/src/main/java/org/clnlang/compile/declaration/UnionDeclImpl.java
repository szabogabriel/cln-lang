package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;

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
    public org.clnlang.runtime.UnionDefinition toUnionDefinition() {
        org.clnlang.runtime.UnionDefinition definition = new org.clnlang.runtime.UnionDefinition(name, isExposed);
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
