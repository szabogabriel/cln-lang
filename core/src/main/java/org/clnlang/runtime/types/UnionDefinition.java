package org.clnlang.runtime.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Union type definition
 */
public class UnionDefinition {
    private final String name;
    private final Map<String, String> members; // memberName -> memberType
    private final boolean isExposed;
    
    public UnionDefinition(String name, boolean isExposed) {
        this.name = name;
        this.isExposed = isExposed;
        this.members = new HashMap<>();
    }
    
    public void addMember(String memberType) {
        // For unions, members are typically just types
        members.put(memberType, memberType);
    }
    
    public String getName() {
        return name;
    }
    
    public Map<String, String> getMembers() {
        return members;
    }
    
    public boolean isExposed() {
        return isExposed;
    }
    
    public boolean hasMember(String memberType) {
        return members.containsKey(memberType);
    }
}
