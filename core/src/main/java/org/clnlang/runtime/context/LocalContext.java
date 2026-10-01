package org.clnlang.runtime.context;

import java.math.BigDecimal;
import java.util.Arrays;

import org.clnlang.compile.types.DecimalTypeInfo;

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
        // Per-type arrays are allocated lazily (see ensure*Capacity) on first use of that
        // type, since most functions only use a subset of the 5 storage kinds - this avoids
        // 16 small array allocations on every call frame for types the function never touches.
        this.longCount = 0;
        this.boolCount = 0;
        this.decimalCount = 0;
        this.stringCount = 0;
        this.objectCount = 0;
    }

    /**
     * Pre-sized constructor for pooled call frames: allocates each type's value/mutable
     * arrays at the exact size the compiler determined the function needs (from
     * CompilerVisitor.VariableScope), instead of lazily growing from INITIAL_CAPACITY.
     * Name arrays are NOT allocated here - they're only needed by the name-based fallback
     * path (globals/closures/switch-case bindings) and stay null until first used.
     */
    public LocalContext(int longSlots, int boolSlots, int decimalSlots, int stringSlots, int objectSlots) {
        this.parent = null;
        if (longSlots > 0) {
            longValues = new long[longSlots];
            longMutable = new boolean[longSlots];
        }
        if (boolSlots > 0) {
            boolValues = new boolean[boolSlots];
            boolMutable = new boolean[boolSlots];
        }
        if (decimalSlots > 0) {
            decimalValues = new BigDecimal[decimalSlots];
            decimalMutable = new boolean[decimalSlots];
            decimalTypeInfos = new DecimalTypeInfo[decimalSlots];
        }
        if (stringSlots > 0) {
            stringValues = new String[stringSlots];
            stringMutable = new boolean[stringSlots];
        }
        if (objectSlots > 0) {
            objectValues = new Object[objectSlots];
            objectMutable = new boolean[objectSlots];
        }
    }

    /**
     * Clears this context for reuse by a pooled call frame. Keeps the allocated arrays
     * (so a recycled frame doesn't re-pay allocation cost) but drops references to the
     * previous call's object/decimal/string values so they don't stay reachable, and
     * resets the name-based fallback slots (globals/closures/switch-case bindings) since
     * those aren't tracked by the compiler's per-function slot counts.
     */
    public void reset() {
        longCount = 0;
        boolCount = 0;
        if (decimalValues != null) {
            Arrays.fill(decimalValues, 0, decimalCount, null);
        }
        decimalCount = 0;
        if (stringValues != null) {
            Arrays.fill(stringValues, 0, stringCount, null);
        }
        stringCount = 0;
        if (objectValues != null) {
            Arrays.fill(objectValues, 0, objectCount, null);
        }
        objectCount = 0;
        longNames = null;
        boolNames = null;
        decimalNames = null;
        stringNames = null;
        objectNames = null;
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
        if (longNames != null) {
            for (int i = 0; i < longCount; i++) {
                if (name.equals(longNames[i])) {
                    return longValues[i]; // Boxes here
                }
            }
        }
        // Check bools
        if (boolNames != null) {
            for (int i = 0; i < boolCount; i++) {
                if (name.equals(boolNames[i])) {
                    return boolValues[i]; // Boxes here
                }
            }
        }
        // Check decimals
        if (decimalNames != null) {
            for (int i = 0; i < decimalCount; i++) {
                if (name.equals(decimalNames[i])) {
                    return decimalValues[i];
                }
            }
        }
        // Check strings
        if (stringNames != null) {
            for (int i = 0; i < stringCount; i++) {
                if (name.equals(stringNames[i])) {
                    return stringValues[i];
                }
            }
        }
        // Check objects
        if (objectNames != null) {
            for (int i = 0; i < objectCount; i++) {
                if (name.equals(objectNames[i])) {
                    return objectValues[i];
                }
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
        if (longNames != null) {
            for (int i = 0; i < longCount; i++) {
                if (name.equals(longNames[i])) return true;
            }
        }
        if (boolNames != null) {
            for (int i = 0; i < boolCount; i++) {
                if (name.equals(boolNames[i])) return true;
            }
        }
        if (decimalNames != null) {
            for (int i = 0; i < decimalCount; i++) {
                if (name.equals(decimalNames[i])) return true;
            }
        }
        if (stringNames != null) {
            for (int i = 0; i < stringCount; i++) {
                if (name.equals(stringNames[i])) return true;
            }
        }
        if (objectNames != null) {
            for (int i = 0; i < objectCount; i++) {
                if (name.equals(objectNames[i])) return true;
            }
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
        if (longNames != null) {
            for (int i = 0; i < longCount; i++) {
                if (name.equals(longNames[i])) return longMutable[i];
            }
        }
        if (boolNames != null) {
            for (int i = 0; i < boolCount; i++) {
                if (name.equals(boolNames[i])) return boolMutable[i];
            }
        }
        if (decimalNames != null) {
            for (int i = 0; i < decimalCount; i++) {
                if (name.equals(decimalNames[i])) return decimalMutable[i];
            }
        }
        if (stringNames != null) {
            for (int i = 0; i < stringCount; i++) {
                if (name.equals(stringNames[i])) return stringMutable[i];
            }
        }
        if (objectNames != null) {
            for (int i = 0; i < objectCount; i++) {
                if (name.equals(objectNames[i])) return objectMutable[i];
            }
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
        if (longNames != null) {
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
        }
        // Check bools
        if (boolNames != null) {
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
        }
        // Check decimals
        if (decimalNames != null) {
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
        }
        // Check strings
        if (stringNames != null) {
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
        }
        // Check objects
        if (objectNames != null) {
            for (int i = 0; i < objectCount; i++) {
                if (name.equals(objectNames[i])) {
                    if (!objectMutable[i]) return false;
                    objectValues[i] = value;
                    return true;
                }
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
    
    /**
     * Look up the index of a variable by name and type without allocating a slot.
     * Returns -1 if not found. Intended for one-time (cached) resolution by callers
     * such as global-variable identifier nodes, not for repeated per-access use.
     */
    public int resolveIndex(String name, String type) {
        if (type == null) {
            return -1;
        }
        if (type.contains("[]")) {
            return indexOfObject(name);
        }
        switch (type) {
            case "int":
                return indexOfLong(name);
            case "bool":
                return indexOfBool(name);
            case "dec":
            case "decimal":
                return indexOfDecimal(name);
            case "string":
                return indexOfString(name);
            default:
                return indexOfObject(name);
        }
    }

    private int indexOfLong(String name) {
        if (longNames == null) return -1;
        for (int i = 0; i < longCount; i++) {
            if (name.equals(longNames[i])) return i;
        }
        return -1;
    }

    private int indexOfBool(String name) {
        if (boolNames == null) return -1;
        for (int i = 0; i < boolCount; i++) {
            if (name.equals(boolNames[i])) return i;
        }
        return -1;
    }

    private int indexOfDecimal(String name) {
        if (decimalNames == null) return -1;
        for (int i = 0; i < decimalCount; i++) {
            if (name.equals(decimalNames[i])) return i;
        }
        return -1;
    }

    private int indexOfString(String name) {
        if (stringNames == null) return -1;
        for (int i = 0; i < stringCount; i++) {
            if (name.equals(stringNames[i])) return i;
        }
        return -1;
    }

    private int indexOfObject(String name) {
        if (objectNames == null) return -1;
        for (int i = 0; i < objectCount; i++) {
            if (name.equals(objectNames[i])) return i;
        }
        return -1;
    }

    private int findOrAddLongName(String name) {
        if (longNames != null) {
            for (int i = 0; i < longCount; i++) {
                if (name.equals(longNames[i])) {
                    return i;
                }
            }
        }
        ensureLongCapacity(longCount + 1);
        longNames = ensureNamesCapacity(longNames, longValues.length);
        longNames[longCount] = name;
        return longCount;
    }
    
    private int findOrAddBoolName(String name) {
        if (boolNames != null) {
            for (int i = 0; i < boolCount; i++) {
                if (name.equals(boolNames[i])) {
                    return i;
                }
            }
        }
        ensureBoolCapacity(boolCount + 1);
        boolNames = ensureNamesCapacity(boolNames, boolValues.length);
        boolNames[boolCount] = name;
        return boolCount;
    }
    
    private int findOrAddDecimalName(String name) {
        if (decimalNames != null) {
            for (int i = 0; i < decimalCount; i++) {
                if (name.equals(decimalNames[i])) {
                    return i;
                }
            }
        }
        ensureDecimalCapacity(decimalCount + 1);
        decimalNames = ensureNamesCapacity(decimalNames, decimalValues.length);
        decimalNames[decimalCount] = name;
        return decimalCount;
    }
    
    private int findOrAddStringName(String name) {
        if (stringNames != null) {
            for (int i = 0; i < stringCount; i++) {
                if (name.equals(stringNames[i])) {
                    return i;
                }
            }
        }
        ensureStringCapacity(stringCount + 1);
        stringNames = ensureNamesCapacity(stringNames, stringValues.length);
        stringNames[stringCount] = name;
        return stringCount;
    }
    
    private int findOrAddObjectName(String name) {
        if (objectNames != null) {
            for (int i = 0; i < objectCount; i++) {
                if (name.equals(objectNames[i])) {
                    return i;
                }
            }
        }
        ensureObjectCapacity(objectCount + 1);
        objectNames = ensureNamesCapacity(objectNames, objectValues.length);
        objectNames[objectCount] = name;
        return objectCount;
    }

    /**
     * Lazily allocates/grows a *Names array to match its sibling values array's length.
     * Kept separate from ensure*Capacity so the common index-based fast path (which never
     * touches names) doesn't pay for an array it will never use.
     */
    private static String[] ensureNamesCapacity(String[] names, int requiredLength) {
        if (names == null) {
            return new String[requiredLength];
        }
        if (names.length < requiredLength) {
            return Arrays.copyOf(names, requiredLength);
        }
        return names;
    }
    
    // ========== Array growth helpers ==========
    
    private void ensureLongCapacity(int minCapacity) {
        if (longValues == null) {
            int initial = Math.max(minCapacity, INITIAL_CAPACITY);
            longValues = new long[initial];
            longMutable = new boolean[initial];
        } else if (minCapacity > longValues.length) {
            int newCapacity = Math.max(minCapacity, longValues.length * 2);
            longValues = Arrays.copyOf(longValues, newCapacity);
            longMutable = Arrays.copyOf(longMutable, newCapacity);
        }
    }
    
    private void ensureBoolCapacity(int minCapacity) {
        if (boolValues == null) {
            int initial = Math.max(minCapacity, INITIAL_CAPACITY);
            boolValues = new boolean[initial];
            boolMutable = new boolean[initial];
        } else if (minCapacity > boolValues.length) {
            int newCapacity = Math.max(minCapacity, boolValues.length * 2);
            boolValues = Arrays.copyOf(boolValues, newCapacity);
            boolMutable = Arrays.copyOf(boolMutable, newCapacity);
        }
    }
    
    private void ensureDecimalCapacity(int minCapacity) {
        if (decimalValues == null) {
            int initial = Math.max(minCapacity, INITIAL_CAPACITY);
            decimalValues = new BigDecimal[initial];
            decimalMutable = new boolean[initial];
            decimalTypeInfos = new DecimalTypeInfo[initial];
        } else if (minCapacity > decimalValues.length) {
            int newCapacity = Math.max(minCapacity, decimalValues.length * 2);
            decimalValues = Arrays.copyOf(decimalValues, newCapacity);
            decimalMutable = Arrays.copyOf(decimalMutable, newCapacity);
            decimalTypeInfos = Arrays.copyOf(decimalTypeInfos, newCapacity);
        }
    }
    
    private void ensureStringCapacity(int minCapacity) {
        if (stringValues == null) {
            int initial = Math.max(minCapacity, INITIAL_CAPACITY);
            stringValues = new String[initial];
            stringMutable = new boolean[initial];
        } else if (minCapacity > stringValues.length) {
            int newCapacity = Math.max(minCapacity, stringValues.length * 2);
            stringValues = Arrays.copyOf(stringValues, newCapacity);
            stringMutable = Arrays.copyOf(stringMutable, newCapacity);
        }
    }
    
    private void ensureObjectCapacity(int minCapacity) {
        if (objectValues == null) {
            int initial = Math.max(minCapacity, INITIAL_CAPACITY);
            objectValues = new Object[initial];
            objectMutable = new boolean[initial];
        } else if (minCapacity > objectValues.length) {
            int newCapacity = Math.max(minCapacity, objectValues.length * 2);
            objectValues = Arrays.copyOf(objectValues, newCapacity);
            objectMutable = Arrays.copyOf(objectMutable, newCapacity);
        }
    }
}
