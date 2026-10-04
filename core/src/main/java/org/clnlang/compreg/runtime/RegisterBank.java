package org.clnlang.compreg.runtime;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;

public enum RegisterBank {
    INT(0) {
        @Override
        public int absoluteOffset(Memory memory, int offset) { return memory.absoluteIntOffset(offset); }
        @Override
        public void copyAbsolute(Memory memory, int source, int target, DecimalTypeInfo info) {
            memory.setIntAbsolute(target, memory.getIntAbsolute(source));
        }
    },
    DEC(1) {
        @Override
        public int absoluteOffset(Memory memory, int offset) { return memory.absoluteDecOffset(offset); }
        @Override
        public void copyAbsolute(Memory memory, int source, int target, DecimalTypeInfo info) {
            memory.setDecAbsolute(target, info.applyConstraints(memory.getDecAbsolute(source)));
        }
    },
    BOOL(2) {
        @Override
        public int absoluteOffset(Memory memory, int offset) { return memory.absoluteBoolOffset(offset); }
        @Override
        public void copyAbsolute(Memory memory, int source, int target, DecimalTypeInfo info) {
            memory.setBoolAbsolute(target, memory.getBoolAbsolute(source));
        }
    },
    STRING(3) {
        @Override
        public int absoluteOffset(Memory memory, int offset) { return memory.absoluteStringOffset(offset); }
        @Override
        public void copyAbsolute(Memory memory, int source, int target, DecimalTypeInfo info) {
            memory.setStrAbsolute(target, memory.getStrAbsolute(source));
        }
    };

    private final int index;

    RegisterBank(int index) { this.index = index; }

    public int getIndex() { return index; }

    public abstract int absoluteOffset(Memory memory, int offset);

    public abstract void copyAbsolute(Memory memory, int source, int target, DecimalTypeInfo decimalTypeInfo);

    public static RegisterBank forType(String type) {
        return switch (type) {
            case "int" -> INT;
            case "dec" -> DEC;
            case "bool" -> BOOL;
            case "string" -> STRING;
            default -> null;
        };
    }
}