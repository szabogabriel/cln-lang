package org.clnlang.compreg;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import org.clnlang.compile.types.DecimalTypeInfo;

public class Memory {

    private static final int TYPE_INT = 0;
    private static final int TYPE_DEC = 1;
    private static final int TYPE_BOOL = 2;
    private static final int TYPE_STR = 3;

    private final int FRAME_SIZE = 1024;
    
    private long [] memInt = new long[FRAME_SIZE];
    private BigDecimal [] memDec = new BigDecimal[FRAME_SIZE];
    private boolean [] memBool = new boolean[FRAME_SIZE];
    private String [] memStr = new String[FRAME_SIZE];

    public static class MemoryOffset {
        private int intOffset = 0;
        private int decOffset = 0;
        private int boolOffset = 0;
        private int strOffset = 0;

        private MemoryOffset copy() {
            MemoryOffset copy = new MemoryOffset();
            copy.intOffset = this.intOffset;
            copy.decOffset = this.decOffset;
            copy.boolOffset = this.boolOffset;
            copy.strOffset = this.strOffset;
            return copy;
        }

        private MemoryOffset add(MemoryOffset other) {
            MemoryOffset result = new MemoryOffset();
            result.intOffset = this.intOffset + other.intOffset;
            result.decOffset = this.decOffset + other.decOffset;
            result.boolOffset = this.boolOffset + other.boolOffset;
            result.strOffset = this.strOffset + other.strOffset;
            return result;
        }
    }

    public static final class FrameLayout {
        private final MemoryOffset offsets;

        public FrameLayout(int intSlots, int decSlots, int boolSlots, int stringSlots) {
            offsets = new MemoryOffset();
            offsets.intOffset = intSlots;
            offsets.decOffset = decSlots;
            offsets.boolOffset = boolSlots;
            offsets.strOffset = stringSlots;
        }

        public static FrameLayout empty() {
            return new FrameLayout(0, 0, 0, 0);
        }

        private MemoryOffset toOffset() {
            return offsets.copy();
        }
    }

    private MemoryOffset baseOffset = new MemoryOffset();
    private MemoryOffset currentOffset = new MemoryOffset();

    private final Stack<MemoryOffset> offsetStack = new Stack<>();
    private final Stack<MemoryOffset> baseOffsetStack = new Stack<>();
    private final Stack<Boolean> returnStateStack = new Stack<>();
    private final Deque<InstructionCursor> instructionCursors = new ArrayDeque<>();
    private boolean returnRequested;

    private static final class InstructionCursor {
        private int instruction = -1;
        private int jumpTarget = -1;
    }

    private void extendMemoryIfNeeded(int offset, int type) {
        switch (type) {
            case TYPE_INT -> {
                if (offset >= memInt.length) {
                    long[] newMemInt = new long[memInt.length * 2];
                    System.arraycopy(memInt, 0, newMemInt, 0, memInt.length);
                    memInt = newMemInt;
                }
            }
            case TYPE_DEC -> {
                if (offset >= memDec.length) {
                    BigDecimal[] newMemDec = new BigDecimal[memDec.length * 2];
                    System.arraycopy(memDec, 0, newMemDec, 0, memDec.length);
                    memDec = newMemDec;
                }
            }
            case TYPE_BOOL -> {
                if (offset >= memBool.length) {
                    boolean[] newMemBool = new boolean[memBool.length * 2];
                    System.arraycopy(memBool, 0, newMemBool, 0, memBool.length);
                    memBool = newMemBool;
                }
            }
            case TYPE_STR -> {
                if (offset >= memStr.length) {
                    String[] newMemStr = new String[memStr.length * 2];
                    System.arraycopy(memStr, 0, newMemStr, 0, memStr.length);
                    memStr = newMemStr;
                }
            }
        }
    }

    public int allocateInt() {
        int offset = currentOffset.intOffset;
        currentOffset.intOffset++;
        extendMemoryIfNeeded(offset + baseOffset.intOffset, TYPE_INT);
        return offset;
    }

    public int allocateDec() {
        int offset = currentOffset.decOffset;
        currentOffset.decOffset++;
        extendMemoryIfNeeded(offset + baseOffset.decOffset, TYPE_DEC);
        return offset;
    }

    public int allocateBool() {
        int offset = currentOffset.boolOffset;
        currentOffset.boolOffset++;
        extendMemoryIfNeeded(offset + baseOffset.boolOffset, TYPE_BOOL);
        return offset;
    }

    public int allocateStr() {
        int offset = currentOffset.strOffset;
        currentOffset.strOffset++;
        extendMemoryIfNeeded(offset + baseOffset.strOffset, TYPE_STR);
        return offset;
    }

    public long getInt(int offset) {
        return memInt[baseOffset.intOffset + offset];
    }

    public BigDecimal getDec(int offset) {
        return memDec[baseOffset.decOffset + offset];
    }

    public boolean getBool(int offset) {
        return memBool[baseOffset.boolOffset + offset];
    }

    public String getStr(int offset) {
        return memStr[baseOffset.strOffset + offset];
    }

    public long getIntAbsolute(int offset) {
        return memInt[offset];
    }

    public BigDecimal getDecAbsolute(int offset) {
        return memDec[offset];
    }

    public boolean getBoolAbsolute(int offset) {
        return memBool[offset];
    }

    public String getStrAbsolute(int offset) {
        return memStr[offset];
    }

    public void setInt(int offset, long value) {
        memInt[baseOffset.intOffset + offset] = value;
    }

    public void setDec(int offset, BigDecimal value) {
        memDec[baseOffset.decOffset + offset] = value;
    }

    public void setBool(int offset, boolean value) {
        memBool[baseOffset.boolOffset + offset] = value;
    }

    public void setStr(int offset, String value) {
        memStr[baseOffset.strOffset + offset] = value;
    }

    public void setIntAbsolute(int offset, long value) {
        memInt[offset] = value;
    }

    public void setDecAbsolute(int offset, BigDecimal value) {
        memDec[offset] = value;
    }

    public void setBoolAbsolute(int offset, boolean value) {
        memBool[offset] = value;
    }

    public void setStrAbsolute(int offset, String value) {
        memStr[offset] = value;
    }

    public int absoluteOffset(String type, int offset) {
        return switch (type) {
            case "int" -> baseOffset.intOffset + offset;
            case "dec" -> baseOffset.decOffset + offset;
            case "bool" -> baseOffset.boolOffset + offset;
            case "string" -> baseOffset.strOffset + offset;
            default -> throw new IllegalArgumentException("Unknown register type: " + type);
        };
    }

    public void copyAbsolute(String type, int source, int target) {
        copyAbsolute(type, source, target, DecimalTypeInfo.DEFAULT);
    }

    public void copyAbsolute(String type, int source, int target, DecimalTypeInfo decimalTypeInfo) {
        switch (type) {
            case "int" -> memInt[target] = memInt[source];
            case "dec" -> memDec[target] = decimalTypeInfo.applyConstraints(memDec[source]);
            case "bool" -> memBool[target] = memBool[source];
            case "string" -> memStr[target] = memStr[source];
            default -> throw new IllegalArgumentException("Unknown register type: " + type);
        }
    }

    public void copyGlobalToCurrent(String type, int globalOffset, int currentOffset) {
        copyAbsolute(type, globalOffset, absoluteOffset(type, currentOffset));
    }

    public void copyCurrentToGlobal(String type, int currentOffset, int globalOffset) {
        copyCurrentToGlobal(type, currentOffset, globalOffset, DecimalTypeInfo.DEFAULT);
    }

    public void copyCurrentToGlobal(String type, int currentOffset, int globalOffset,
            DecimalTypeInfo decimalTypeInfo) {
        int source = absoluteOffset(type, currentOffset);
        if (type.equals("dec")) {
            memDec[globalOffset] = decimalTypeInfo.applyConstraints(memDec[source]);
        } else {
            copyAbsolute(type, source, globalOffset);
        }
    }

    public FrameLayout currentFrameLayout() {
        return new FrameLayout(currentOffset.intOffset, currentOffset.decOffset, currentOffset.boolOffset,
            currentOffset.strOffset);
    }

    public void setRootFrameLayout(FrameLayout layout) {
        if (!offsetStack.empty()) {
            throw new IllegalStateException("Cannot reset the root layout while a frame is active.");
        }
        baseOffset = new MemoryOffset();
        currentOffset = layout.toOffset();
        returnRequested = false;
        ensureLayoutCapacity(currentOffset);
    }

    public void pushOffset() {
        offsetStack.push(currentOffset.copy());
        baseOffsetStack.push(baseOffset.copy());
        returnStateStack.push(returnRequested);
        baseOffset = baseOffset.add(currentOffset);
        currentOffset = new MemoryOffset();
    }

    public void pushFrame(FrameLayout layout) {
        offsetStack.push(currentOffset.copy());
        baseOffsetStack.push(baseOffset.copy());
        returnStateStack.push(returnRequested);
        baseOffset = baseOffset.add(currentOffset);
        currentOffset = layout.toOffset();
        returnRequested = false;
        ensureLayoutCapacity(baseOffset.add(currentOffset));
    }

    public void popOffset() {
        currentOffset = offsetStack.pop();
        baseOffset = baseOffsetStack.pop();
        returnRequested = returnStateStack.pop();
    }

    public void requestReturn() {
        returnRequested = true;
    }

    public boolean isReturnRequested() {
        return returnRequested;
    }

    public void beginInstructionSequence() {
        instructionCursors.push(new InstructionCursor());
    }

    public void endInstructionSequence() {
        instructionCursors.pop();
    }

    public void setCurrentInstruction(int instruction) {
        InstructionCursor cursor = currentCursor();
        cursor.instruction = instruction;
        cursor.jumpTarget = -1;
    }

    public void jumpTo(int targetInstruction) {
        currentCursor().jumpTarget = targetInstruction;
    }

    public int nextInstruction() {
        InstructionCursor cursor = currentCursor();
        int next = cursor.jumpTarget >= 0 ? cursor.jumpTarget : cursor.instruction + 1;
        cursor.jumpTarget = -1;
        return next;
    }

    private InstructionCursor currentCursor() {
        if (instructionCursors.isEmpty()) {
            throw new IllegalStateException("No command sequence is currently executing.");
        }
        return instructionCursors.peek();
    }

    private void ensureLayoutCapacity(MemoryOffset required) {
        ensureIntCapacity(required.intOffset);
        ensureDecCapacity(required.decOffset);
        ensureBoolCapacity(required.boolOffset);
        ensureStringCapacity(required.strOffset);
    }

    private void ensureIntCapacity(int slots) {
        while (slots > memInt.length) {
            long[] expanded = new long[memInt.length * 2];
            System.arraycopy(memInt, 0, expanded, 0, memInt.length);
            memInt = expanded;
        }
    }

    private void ensureDecCapacity(int slots) {
        while (slots > memDec.length) {
            BigDecimal[] expanded = new BigDecimal[memDec.length * 2];
            System.arraycopy(memDec, 0, expanded, 0, memDec.length);
            memDec = expanded;
        }
    }

    private void ensureBoolCapacity(int slots) {
        while (slots > memBool.length) {
            boolean[] expanded = new boolean[memBool.length * 2];
            System.arraycopy(memBool, 0, expanded, 0, memBool.length);
            memBool = expanded;
        }
    }

    private void ensureStringCapacity(int slots) {
        while (slots > memStr.length) {
            String[] expanded = new String[memStr.length * 2];
            System.arraycopy(memStr, 0, expanded, 0, memStr.length);
            memStr = expanded;
        }
    }

}
