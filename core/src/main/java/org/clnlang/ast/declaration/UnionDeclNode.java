package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a union declaration.
 */
public class UnionDeclNode extends ASTNode {
    private String name;
    private List<String> members;
    private boolean isExposed;

    public UnionDeclNode(String name, boolean isExposed) {
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

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((isExposed ? "expose " : "")).append("Union: ").append(name);
        sb.append(" { ");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(members.get(i));
        }
        sb.append(" }");
        return sb.toString();
    }
}
