package org.clnlang.compreg.commands.array;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ArrayStoreBool implements Command {
    private final int source;
    private final int base;
    private final int length;
    private final int index;
    private final boolean global;

    public ArrayStoreBool(int source, int base, int length, int index, boolean global) {
        this.source = source; this.base = base; this.length = length; this.index = index; this.global = global;
    }

    @Override
    public void execute(Memory memory) {
        int target = base + ArrayBounds.checkedIndex(memory.getInt(index), length);
        boolean value = memory.getBool(source);
        if (global) memory.setBoolAbsolute(target, value); else memory.setBool(target, value);
    }
}