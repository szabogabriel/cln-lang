package org.clnlang.runtime.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Struct type definition
 */
public class StructDefinition {
    private final String name;
    private final Map<String, String> fields; // fieldName -> fieldType
    private final boolean isExposed;
    
    public StructDefinition(String name, boolean isExposed) {
        this.name = name;
        this.isExposed = isExposed;
        this.fields = new HashMap<>();
    }
    
    public void addField(String fieldName, String fieldType) {
        fields.put(fieldName, fieldType);
    }
    
    public String getName() {
        return name;
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
}
