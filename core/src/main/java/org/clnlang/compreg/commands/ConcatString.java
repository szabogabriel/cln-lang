package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class ConcatString implements Command {

    private final String leftType;
    private final String rightType;
    private final int leftOffset;
    private final int rightOffset;
    private final int targetOffset;

    public ConcatString(String leftType, String rightType, int leftOffset, int rightOffset, int targetOffset) {
        this.leftType = leftType;
        this.rightType = rightType;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        memory.setStr(targetOffset, stringify(memory, leftType, leftOffset)
                + stringify(memory, rightType, rightOffset));
    }

    private String stringify(Memory memory, String type, int offset) {
        return switch (type) {
            case "int" -> Long.toString(memory.getInt(offset));
            case "dec" -> String.valueOf(memory.getDec(offset));
            case "bool" -> Boolean.toString(memory.getBool(offset));
            case "string" -> String.valueOf(memory.getStr(offset));
            default -> throw new IllegalStateException("Cannot concatenate value of type '" + type + "'.");
        };
    }
}
