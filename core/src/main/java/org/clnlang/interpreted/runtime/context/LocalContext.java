package org.clnlang.interpreted.runtime.context;

import java.math.BigDecimal;
import java.util.Arrays;

import org.clnlang.interpreted.compile.types.DecimalTypeInfo;

/**
 * Local context for function-local variables (supports scoping).
 * Uses primitive arrays for zero-boxing storage with index-based access.
 */
public class LocalContext {
    private static final int INITIAL_CAPACITY = 8;
    
    // Parent context for nested scopes
    private final LocalContext parent;
    
    // Primitive storage arrays (zero boxing!)
    private long[] longValues;
    private boolean[] longMutable;
    private int longCount;
    
    private boolean[] boolValues;
    private boolean[] boolMutable;
    private int boolCount;
    
    // Reference type storage
    private BigDecimal[] decimalValues;
    private boolean[] decimalMutable;
    private DecimalTypeInfo[] decimalTypeInfos;  // Track precision and rounding for each decimal
    private int decimalCount;
    
    private String[] stringValues;
    private boolean[] stringMutable;
    private int stringCount;
    
    private Object[] objectValues;
    private boolean[] objectMutable;
    private int objectCount;
    
    // For backward compatibility - name-based lookup (fallback for globals, closures, etc.)
    private String[] longNames;
    private String[] boolNames;
    private String[] decimalNames;
    private String[] stringNames;
    private String[] objectNames;
    
    public LocalContext() {
        this(null);
    }
    
    public LocalContext(LocalContext parent) {
        this.parent = parent;
        
        // Initialize primitive arrays
        this.longValues = new long[INITIAL_CAPACITY];
        this.longMutable = new boolean[INITIAL_CAPACITY];
        this.longNames = new String[INITIAL_CAPACITY];
        this.longCount = 0;
        
        this.boolValues = new boolean[INITIAL_CAPACITY];
        this.boolMutable = new boolean[INITIAL_CAPACITY];
        this.boolNames = new String[INITIAL_CAPACITY];
        this.boolCount = 0;
        
        this.decimalValues = new BigDecimal[INITIAL_CAPACITY];
        this.decimalMutable = new boolean[INITIAL_CAPACITY];
        this.decimalNames = new String[INITIAL_CAPACITY];
        this.decimalTypeInfos = new DecimalTypeInfo[INITIAL_CAPACITY];
        this.decimalCount = 0;
        
        this.stringValues = new String[INITIAL_CAPACITY];
        this.stringMutable = new boolean[INITIAL_CAPACITY];
        this.stringNames = new String[INITIAL_CAPACITY];
        this.stringCount = 0;
        
        this.objectValues = new Object[INITIAL_CAPACITY];
        this.objectMutable = new boolean[INITIAL_CAPACITY];
        this.objectNames = new String[INITIAL_CAPACITY];
        this.objectCount = 0;
    }
    
    public LocalContext getParent() {
        return parent;
    }
    
    // ========== Index-based access (zero boxing for primitives) ==========
    
    /**
     * Get long value by index (zero boxing!)
     */
    public long getLongByIndex(int index) {
        if (index < 0 || index >= longCount) {
            throw new RuntimeException("Long variable index out of bounds: " + index);
        }
        return longValues[index];
    }
    
    /**
     * Set long value by index (zero boxing!)
     */
    public void setLongByIndex(int index, long value, boolean mutable) {
        ensureLongCapacity(index + 1);
        longValues[index] = value;
        longMutable[index] = mutable;
        if (index >= longCount) {
            longCount = index + 1;
        }
    }
    
    /**
     * Update long value by index (zero boxing!)
     */
    public boolean updateLongByIndex(int index, long value) {
        if (index < 0 || index >= longCount) {
            return false;
        }
        if (!longMutable[index]) {
            return false; // Cannot update constant
        }
        longValues[index] = value;
        return true;
    }
    
    /**
     * Get boolean value by index (zero boxing!)
     */
    public boolean getBoolByIndex(int index) {
        if (index < 0 || index >= boolCount) {
            throw new RuntimeException("Bool variable index out of bounds: " + index);
        }
        return boolValues[index];
    }
    
    /**
     * Set boolean value by index (zero boxing!)
     */
    public void setBoolByIndex(int index, boolean value, boolean mutable) {
        ensureBoolCapacity(index + 1);
        boolValues[index] = value;
        boolMutable[index] = mutable;
        if (index >= boolCount) {
            boolCount = index + 1;
        }
    }
    
    /**
     * Update boolean value by index (zero boxing!)
     */
    public boolean updateBoolByIndex(int index, boolean value) {
        if (index < 0 || index >= boolCount) {
            return false;
        }
        if (!boolMutable[index]) {
            return false; // Cannot update constant
        }
        boolValues[index] = value;
        return true;
    }
    
    /**
     * Get BigDecimal value by index
     */
    public BigDecimal getDecimalByIndex(int index) {
        if (index < 0 || index >= decimalCount) {
            throw new RuntimeException("Decimal variable index out of bounds: " + index);
        }
        return decimalValues[index];
    }
    
    /**
     * Set BigDecimal value by index
     */
    public void setDecimalByIndex(int index, BigDecimal value, boolean mutable) {
        setDecimalByIndex(index, value, mutable, DecimalTypeInfo.DEFAULT);
    }
    
    /**
     * Set BigDecimal value by index with type info
     */
    public void setDecimalByIndex(int index, BigDecimal value, boolean mutable, DecimalTypeInfo typeInfo) {
        ensureDecimalCapacity(index + 1);
        // Apply constraints before storing
        decimalValues[index] = typeInfo != null ? typeInfo.applyConstraints(value) : value;
        decimalMutable[index] = mutable;
        decimalTypeInfos[index] = typeInfo != null ? typeInfo : DecimalTypeInfo.DEFAULT;
        if (index >= decimalCount) {
            decimalCount = index + 1;
        }
    }
    
    /**
     * Update BigDecimal value by index
     */
    public boolean updateDecimalByIndex(int index, BigDecimal value) {
        if (index < 0 || index >= decimalCount) {
            return false;
        }
        if (!decimalMutable[index]) {
            return false; // Cannot update constant
        }
        // Apply stored constraints when updating
        DecimalTypeInfo typeInfo = decimalTypeInfos[index];
        if (typeInfo != null) {
            value = typeInfo.applyConstraints(value);
        }
        decimalValues[index] = value;
        return true;
    }
    
    /**
     * Get String value by index
     */
    public String getStringByIndex(int index) {
        if (index < 0 || index >= stringCount) {
            throw new RuntimeException("String variable index out of bounds: " + index);
        }
        return stringValues[index];
    }
    
    /**
     * Set String value by index
     */
    public void setStringByIndex(int index, String value, boolean mutable) {
        ensureStringCapacity(index + 1);
        stringValues[index] = value;
        stringMutable[index] = mutable;
        if (index >= stringCount) {
            stringCount = index + 1;
        }
    }
    
    /**
     * Update String value by index
     */
    public boolean updateStringByIndex(int index, String value) {
        if (index < 0 || index >= stringCount) {
            return false;
        }
        if (!stringMutable[index]) {
            return false; // Cannot update constant
        }
        stringValues[index] = value;
        return true;
    }
    
    /**
     * Get Object value by index
     */
    public Object getObjectByIndex(int index) {
        if (index < 0 || index >= objectCount) {
            throw new RuntimeException("Object variable index out of bounds: " + index);
        }
        return objectValues[index];
    }
    
    /**
     * Set Object value by index
     */
    public void setObjectByIndex(int index, Object value, boolean mutable) {
        ensureObjectCapacity(index + 1);
        objectValues[index] = value;
        objectMutable[index] = mutable;
        if (index >= objectCount) {
            objectCount = index + 1;
        }
    }
    
    /**
     * Update Object value by index
     */
    public boolean updateObjectByIndex(int index, Object value) {
        if (index < 0 || index >= objectCount) {
            return false;
        }
        if (!objectMutable[index]) {
            return false; // Cannot update constant
        }
        objectValues[index] = value;
        return true;
    }
    
    // ========== Name-based access (backward compatibility - SLOWER) ==========
    
    /**
     * Set a mutable variable with name (for backward compatibility)
     */
    public void setVariable(String name, Object value) {
        if (value instanceof Long) {
            int index = findOrAddLongName(name);
            setLongByIndex(index, (Long) value, true);
        } else if (value instanceof Boolean) {
            int index = findOrAddBoolName(name);
            setBoolByIndex(index, (Boolean) value, true);
        } else if (value instanceof BigDecimal) {
            int index = findOrAddDecimalName(name);
            setDecimalByIndex(index, (BigDecimal) value, true);
        } else if (value instanceof String) {
            int index = findOrAddStringName(name);
            setStringByIndex(index, (String) value, true);
        } else {
            int index = findOrAddObjectName(name);
            setObjectByIndex(index, value, true);
        }
    }
    
    /**
     * Set a constant with name (for backward compatibility)
     */
    public void setConstant(String name, Object value) {
        if (value instanceof Long) {
            int index = findOrAddLongName(name);
            setLongByIndex(index, (Long) value, false);
        } else if (value instanceof Boolean) {
            int index = findOrAddBoolName(name);
            setBoolByIndex(index, (Boolean) value, false);
        } else if (value instanceof BigDecimal) {
            int index = findOrAddDecimalName(name);
            setDecimalByIndex(index, (BigDecimal) value, false);
        } else if (value instanceof String) {
            int index = findOrAddStringName(name);
            setStringByIndex(index, (String) value, false);
        } else {
            int index = findOrAddObjectName(name);
            setObjectByIndex(index, value, false);
        }
    }
    
    /**
     * Get value by name (backward compatibility - boxes primitives)
     */
    public Object getValue(String name) {
        // Check longs
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) {
                return longValues[i]; // Boxes here
            }
        }
        // Check bools
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) {
                return boolValues[i]; // Boxes here
            }
        }
        // Check decimals
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) {
                return decimalValues[i];
            }
        }
        // Check strings
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) {
                return stringValues[i];
            }
        }
        // Check objects
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) {
                return objectValues[i];
            }
        }
        // Check parent
        if (parent != null) {
            return parent.getValue(name);
        }
        return null;
    }
    
    /**
     * Check if value exists by name
     */
    public boolean hasValue(String name) {
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) return true;
        }
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) return true;
        }
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) return true;
        }
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) return true;
        }
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) return true;
        }
        if (parent != null) {
            return parent.hasValue(name);
        }
        return false;
    }
    
    /**
     * Check if value is mutable by name
     */
    public boolean isMutable(String name) {
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) return longMutable[i];
        }
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) return boolMutable[i];
        }
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) return decimalMutable[i];
        }
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) return stringMutable[i];
        }
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) return objectMutable[i];
        }
        if (parent != null) {
            return parent.isMutable(name);
        }
        return false;
    }
    
    /**
     * Update variable by name (backward compatibility)
     */
    public boolean updateVariable(String name, Object value) {
        // Check longs
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) {
                if (!longMutable[i]) return false;
                if (value instanceof Long) {
                    longValues[i] = (Long) value;
                    return true;
                }
                throw new RuntimeException("Type mismatch for variable: " + name);
            }
        }
        // Check bools
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) {
                if (!boolMutable[i]) return false;
                if (value instanceof Boolean) {
                    boolValues[i] = (Boolean) value;
                    return true;
                }
                throw new RuntimeException("Type mismatch for variable: " + name);
            }
        }
        // Check decimals
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) {
                if (!decimalMutable[i]) return false;
                if (value instanceof BigDecimal) {
                    decimalValues[i] = (BigDecimal) value;
                    return true;
                }
                throw new RuntimeException("Type mismatch for variable: " + name);
            }
        }
        // Check strings
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) {
                if (!stringMutable[i]) return false;
                if (value instanceof String) {
                    stringValues[i] = (String) value;
                    return true;
                }
                throw new RuntimeException("Type mismatch for variable: " + name);
            }
        }
        // Check objects
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) {
                if (!objectMutable[i]) return false;
                objectValues[i] = value;
                return true;
            }
        }
        // Try parent
        if (parent != null) {
            return parent.updateVariable(name, value);
        }
        return false;
    }
    
    // ========== Deprecated methods (for compatibility) ==========
    
    @Deprecated
    public Object getVariable(String name) {
        return getValue(name);
    }
    
    @Deprecated
    public boolean hasVariable(String name) {
        return hasValue(name);
    }
    
    // ========== Helper methods for name lookup ==========
    
    private int findOrAddLongName(String name) {
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) {
                return i;
            }
        }
        ensureLongCapacity(longCount + 1);
        longNames[longCount] = name;
        return longCount;
    }
    
    private int findOrAddBoolName(String name) {
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) {
                return i;
            }
        }
        ensureBoolCapacity(boolCount + 1);
        boolNames[boolCount] = name;
        return boolCount;
    }
    
    private int findOrAddDecimalName(String name) {
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) {
                return i;
            }
        }
        ensureDecimalCapacity(decimalCount + 1);
        decimalNames[decimalCount] = name;
        return decimalCount;
    }
    
    private int findOrAddStringName(String name) {
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) {
                return i;
            }
        }
        ensureStringCapacity(stringCount + 1);
        stringNames[stringCount] = name;
        return stringCount;
    }
    
    private int findOrAddObjectName(String name) {
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) {
                return i;
            }
        }
        ensureObjectCapacity(objectCount + 1);
        objectNames[objectCount] = name;
        return objectCount;
    }
    
    // ========== Array growth helpers ==========
    
    private void ensureLongCapacity(int minCapacity) {
        if (minCapacity > longValues.length) {
            int newCapacity = Math.max(minCapacity, longValues.length * 2);
            longValues = Arrays.copyOf(longValues, newCapacity);
            longMutable = Arrays.copyOf(longMutable, newCapacity);
            longNames = Arrays.copyOf(longNames, newCapacity);
        }
    }
    
    private void ensureBoolCapacity(int minCapacity) {
        if (minCapacity > boolValues.length) {
            int newCapacity = Math.max(minCapacity, boolValues.length * 2);
            boolValues = Arrays.copyOf(boolValues, newCapacity);
            boolMutable = Arrays.copyOf(boolMutable, newCapacity);
            boolNames = Arrays.copyOf(boolNames, newCapacity);
        }
    }
    
    private void ensureDecimalCapacity(int minCapacity) {
        if (minCapacity > decimalValues.length) {
            int newCapacity = Math.max(minCapacity, decimalValues.length * 2);
            decimalValues = Arrays.copyOf(decimalValues, newCapacity);
            decimalMutable = Arrays.copyOf(decimalMutable, newCapacity);
            decimalNames = Arrays.copyOf(decimalNames, newCapacity);
            decimalTypeInfos = Arrays.copyOf(decimalTypeInfos, newCapacity);
        }
    }
    
    private void ensureStringCapacity(int minCapacity) {
        if (minCapacity > stringValues.length) {
            int newCapacity = Math.max(minCapacity, stringValues.length * 2);
            stringValues = Arrays.copyOf(stringValues, newCapacity);
            stringMutable = Arrays.copyOf(stringMutable, newCapacity);
            stringNames = Arrays.copyOf(stringNames, newCapacity);
        }
    }
    
    private void ensureObjectCapacity(int minCapacity) {
        if (minCapacity > objectValues.length) {
            int newCapacity = Math.max(minCapacity, objectValues.length * 2);
            objectValues = Arrays.copyOf(objectValues, newCapacity);
            objectMutable = Arrays.copyOf(objectMutable, newCapacity);
            objectNames = Arrays.copyOf(objectNames, newCapacity);
        }
    }
}
