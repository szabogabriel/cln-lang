package org.clnlang.runtime.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Union type definition
 */
public class UnionDefinition {
    private final String name;
    private final String packageName;
    private final Map<String, String> members; // memberName -> memberType
    private final boolean isExposed;
    private final Map<String, String> commonFields; // fieldName -> fieldType (fields common to all members)
    
    public UnionDefinition(String name, String packageName, boolean isExposed) {
        this.name = name;
        this.packageName = packageName;
        this.isExposed = isExposed;
        this.members = new HashMap<>();
        this.commonFields = new HashMap<>();
    }
    
    public void addMember(String memberType) {
        // For unions, members are typically just types
        members.put(memberType, memberType);
    }
    
    public String getName() {
        return name;
    }
    
    public String getPackageName() {
        return packageName;
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
    
    /**
     * Get the common fields shared by all member structs of this union.
     * @return Map of field name to field type for fields common to all members
     */
    public Map<String, String> getCommonFields() {
        return commonFields;
    }
    
    /**
     * Check if a field is common to all members of this union.
     * @param fieldName The name of the field to check
     * @return true if the field exists in all union members with the same type
     */
    public boolean hasCommonField(String fieldName) {
        return commonFields.containsKey(fieldName);
    }
    
    /**
     * Get the type of a common field.
     * @param fieldName The name of the field
     * @return The field type, or null if not a common field
     */
    public String getCommonFieldType(String fieldName) {
        return commonFields.get(fieldName);
    }
    
    /**
     * Compute the common fields by finding the intersection of all member struct fields.
     * This should be called after all member structs are defined.
     * @param structRegistry Map of struct names to their definitions
     */
    public void computeCommonFields(Map<String, StructDefinition> structRegistry) {
        commonFields.clear();
        
        if (members.isEmpty()) {
            return; // No members, no common fields
        }
        
        // Start with the fields of the first member
        boolean firstMember = true;
        Map<String, String> candidateFields = new HashMap<>();
        
        for (String memberType : members.keySet()) {
            StructDefinition structDef = structRegistry.get(memberType);
            
            if (structDef == null) {
                // Member struct not found - skip this union member
                // This could happen with forward references or external types
                continue;
            }
            
            Map<String, String> memberFields = structDef.getFields();
            
            if (firstMember) {
                // Initialize with all fields from first struct
                candidateFields.putAll(memberFields);
                firstMember = false;
            } else {
                // Keep only fields that exist in this member with the same type
                candidateFields.entrySet().removeIf(entry -> {
                    String fieldName = entry.getKey();
                    String fieldType = entry.getValue();
                    String memberFieldType = memberFields.get(fieldName);
                    
                    // Remove if field doesn't exist or has different type
                    return memberFieldType == null || !memberFieldType.equals(fieldType);
                });
            }
        }
        
        // Store the computed common fields
        commonFields.putAll(candidateFields);
    }
}
