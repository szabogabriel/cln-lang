package org.clnlang.compreg.commands.array;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ArrayStoreInt implements Command {
    private final int source;
    private final int base;
    private final int length;
    private final int index;
    private final boolean global;

    public ArrayStoreInt(int source, int base, int length, int index, boolean global) {
        this.source = source; this.base = base; this.length = length; this.index = index; this.global = global;
    }

    @Override
    public void execute(Memory memory) {
        int target = base + ArrayBounds.checkedIndex(memory.getInt(index), length);
        long value = memory.getInt(source);
        if (global) memory.setIntAbsolute(target, value); else memory.setInt(target, value);
    }
}