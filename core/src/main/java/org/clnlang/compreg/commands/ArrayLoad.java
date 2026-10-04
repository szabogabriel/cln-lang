package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class ArrayLoad implements Command {

    private final String elementType;
    private final int baseOffset;
    private final int length;
    private final int indexOffset;
    private final int targetOffset;
    private final boolean global;

    public ArrayLoad(String elementType, int baseOffset, int length, int indexOffset, int targetOffset,
            boolean global) {
        this.elementType = elementType;
        this.baseOffset = baseOffset;
        this.length = length;
        this.indexOffset = indexOffset;
        this.targetOffset = targetOffset;
        this.global = global;
    }

    @Override
    public void execute(Memory memory) {
        int index = checkedIndex(memory.getInt(indexOffset));
        int sourceOffset = baseOffset + index;
        switch (elementType) {
            case "int" -> memory.setInt(targetOffset,
                    global ? memory.getIntAbsolute(sourceOffset) : memory.getInt(sourceOffset));
            case "dec" -> memory.setDec(targetOffset,
                    global ? memory.getDecAbsolute(sourceOffset) : memory.getDec(sourceOffset));
            case "bool" -> memory.setBool(targetOffset,
                    global ? memory.getBoolAbsolute(sourceOffset) : memory.getBool(sourceOffset));
            case "string" -> memory.setStr(targetOffset,
                    global ? memory.getStrAbsolute(sourceOffset) : memory.getStr(sourceOffset));
            default -> throw new IllegalStateException("Unsupported array element type: " + elementType);
        }
    }

    private int checkedIndex(long index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Array index " + index + " out of bounds for length " + length + ".");
        }
        return (int) index;
    }
}