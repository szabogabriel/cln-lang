package org.clnlang.compreg.commands.array;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ArrayLoadInt implements Command {
    private final int base;
    private final int length;
    private final int index;
    private final int target;
    private final boolean global;

    public ArrayLoadInt(int base, int length, int index, int target, boolean global) {
        this.base = base; this.length = length; this.index = index; this.target = target; this.global = global;
    }

    @Override
    public void execute(Memory memory) {
        int source = base + ArrayBounds.checkedIndex(memory.getInt(index), length);
        memory.setInt(target, global ? memory.getIntAbsolute(source) : memory.getInt(source));
    }
}