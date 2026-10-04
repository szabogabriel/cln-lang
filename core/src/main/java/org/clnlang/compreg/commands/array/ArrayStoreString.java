package org.clnlang.compreg.commands.array;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ArrayStoreString implements Command {
    private final int source;
    private final int base;
    private final int length;
    private final int index;
    private final boolean global;

    public ArrayStoreString(int source, int base, int length, int index, boolean global) {
        this.source = source; this.base = base; this.length = length; this.index = index; this.global = global;
    }

    @Override
    public void execute(Memory memory) {
        int target = base + ArrayBounds.checkedIndex(memory.getInt(index), length);
        String value = memory.getStr(source);
        if (global) memory.setStrAbsolute(target, value); else memory.setStr(target, value);
    }
}