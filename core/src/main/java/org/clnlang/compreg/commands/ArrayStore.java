package org.clnlang.compreg.commands;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;

public final class ArrayStore implements Command {

    private final String elementType;
    private final int sourceOffset;
    private final int baseOffset;
    private final int length;
    private final int indexOffset;
    private final boolean global;
    private final DecimalTypeInfo decimalTypeInfo;

    public ArrayStore(String elementType, int sourceOffset, int baseOffset, int length, int indexOffset,
            boolean global, DecimalTypeInfo decimalTypeInfo) {
        this.elementType = elementType;
        this.sourceOffset = sourceOffset;
        this.baseOffset = baseOffset;
        this.length = length;
        this.indexOffset = indexOffset;
        this.global = global;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        int index = checkedIndex(memory.getInt(indexOffset));
        int targetOffset = baseOffset + index;
        switch (elementType) {
            case "int" -> writeInt(memory, targetOffset);
            case "dec" -> writeDec(memory, targetOffset);
            case "bool" -> writeBool(memory, targetOffset);
            case "string" -> writeString(memory, targetOffset);
            default -> throw new IllegalStateException("Unsupported array element type: " + elementType);
        }
    }

    private void writeInt(Memory memory, int targetOffset) {
        long value = memory.getInt(sourceOffset);
        if (global) memory.setIntAbsolute(targetOffset, value);
        else memory.setInt(targetOffset, value);
    }

    private void writeDec(Memory memory, int targetOffset) {
        java.math.BigDecimal value = decimalTypeInfo.applyConstraints(memory.getDec(sourceOffset));
        if (global) memory.setDecAbsolute(targetOffset, value);
        else memory.setDec(targetOffset, value);
    }

    private void writeBool(Memory memory, int targetOffset) {
        boolean value = memory.getBool(sourceOffset);
        if (global) memory.setBoolAbsolute(targetOffset, value);
        else memory.setBool(targetOffset, value);
    }

    private void writeString(Memory memory, int targetOffset) {
        String value = memory.getStr(sourceOffset);
        if (global) memory.setStrAbsolute(targetOffset, value);
        else memory.setStr(targetOffset, value);
    }

    private int checkedIndex(long index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Array index " + index + " out of bounds for length " + length + ".");
        }
        return (int) index;
    }
}