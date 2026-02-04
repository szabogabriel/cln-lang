package org.clnlang.runtime.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Struct type definition
 */
public class StructDefinition {
    private final String name;
    private final String packageName;
    private final Map<String, String> fields; // fieldName -> fieldType
    private final Map<String, Boolean> fieldMutability; // fieldName -> isVar
    private final boolean isExposed;
    
    public StructDefinition(String name, String packageName, boolean isExposed) {
        this.name = name;
        this.packageName = packageName;
        this.isExposed = isExposed;
        this.fields = new HashMap<>();
        this.fieldMutability = new HashMap<>();
    }
    
    public void addField(String fieldName, String fieldType, boolean isVar) {
        fields.put(fieldName, fieldType);
        fieldMutability.put(fieldName, isVar);
    }
    
    public String getName() {
        return name;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public Map<String, String> getFields() {
        return fields;
    }
    
    public boolean isExposed() {
        return isExposed;
    }
    
    public String getFieldType(String fieldName) {
        return fields.get(fieldName);
    }
    
    public boolean hasField(String fieldName) {
        return fields.containsKey(fieldName);
    }
    
    public boolean isFieldMutable(String fieldName) {
        return fieldMutability.getOrDefault(fieldName, false);
    }
}
