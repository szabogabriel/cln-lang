package org.clnlang.compreg.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;

public final class StructValue {

    public static class FieldLayout {
        private final String name;
        private final String type;
        private final boolean mutable;
        private final DecimalTypeInfo decimalTypeInfo;

        public FieldLayout(String name, String type, boolean mutable, DecimalTypeInfo decimalTypeInfo) {
            this.name = name;
            this.type = type;
            this.mutable = mutable;
            this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public boolean isMutable() { return mutable; }
        public DecimalTypeInfo getDecimalTypeInfo() { return decimalTypeInfo; }
    }

    public static final class FieldSlot extends FieldLayout {
        private final int offset;
        private final boolean global;

        public FieldSlot(String name, String type, boolean mutable, int offset, boolean global,
                DecimalTypeInfo decimalTypeInfo) {
            super(name, type, mutable, decimalTypeInfo);
            this.offset = offset;
            this.global = global;
        }

        public int getOffset() { return offset; }
        public boolean isGlobal() { return global; }

        public long getInt(Memory memory) {
            requireType("int");
            return global ? memory.getIntAbsolute(offset) : memory.getInt(offset);
        }

        public void setInt(Memory memory, long value) {
            requireType("int");
            if (global) memory.setIntAbsolute(offset, value); else memory.setInt(offset, value);
        }

        public java.math.BigDecimal getDec(Memory memory) {
            requireType("dec");
            return global ? memory.getDecAbsolute(offset) : memory.getDec(offset);
        }

        public void setDec(Memory memory, java.math.BigDecimal value) {
            requireType("dec");
            java.math.BigDecimal constrained = getDecimalTypeInfo().applyConstraints(value);
            if (global) memory.setDecAbsolute(offset, constrained); else memory.setDec(offset, constrained);
        }

        public boolean getBool(Memory memory) {
            requireType("bool");
            return global ? memory.getBoolAbsolute(offset) : memory.getBool(offset);
        }

        public void setBool(Memory memory, boolean value) {
            requireType("bool");
            if (global) memory.setBoolAbsolute(offset, value); else memory.setBool(offset, value);
        }

        public String getString(Memory memory) {
            requireType("string");
            return global ? memory.getStrAbsolute(offset) : memory.getStr(offset);
        }

        public void setString(Memory memory, String value) {
            requireType("string");
            if (global) memory.setStrAbsolute(offset, value); else memory.setStr(offset, value);
        }

        private void requireType(String expected) {
            if (!getType().equals(expected)) {
                throw new IllegalStateException("Struct field '" + getName() + "' is '" + getType()
                        + "', not '" + expected + "'.");
            }
        }

    }

    private final String typeName;
    private final Map<String, FieldSlot> fields = new LinkedHashMap<>();

    public StructValue(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() { return typeName; }

    public void addField(FieldSlot field) {
        if (fields.putIfAbsent(field.getName(), field) != null) {
            throw new IllegalArgumentException("Duplicate struct value field '" + field.getName() + "'.");
        }
    }

    public FieldSlot getField(String name) { return fields.get(name); }

    public Map<String, FieldSlot> getFields() { return Map.copyOf(fields); }
}