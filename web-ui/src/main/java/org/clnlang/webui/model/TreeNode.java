package org.clnlang.webui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A node in the package/symbol tree displayed in the left panel.
 */
public class TreeNode {

    public enum Type { PACKAGE, FUNCTION, STRUCT, UNION }

    private final String name;
    private final Type type;
    private final Long sourceId;       // null for synthetic package nodes
    private final String packageName;  // fully-qualified package name this node belongs to
    private final List<TreeNode> children = new ArrayList<>();

    public TreeNode(String name, Type type, String packageName, Long sourceId) {
        this.name = name;
        this.type = type;
        this.packageName = packageName;
        this.sourceId = sourceId;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public String getName()        { return name; }
    public Type   getType()        { return type; }
    public Long   getSourceId()    { return sourceId; }
    public String getPackageName() { return packageName; }
    public List<TreeNode> getChildren() { return children; }

    public String getLabel() {
        return switch (type) {
            case PACKAGE  -> "📦 " + name;
            case FUNCTION -> "⚙ "  + name + "()";
            case STRUCT   -> "⬡ "  + name;
            case UNION    -> "⬢ "  + name;
        };
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
