package org.clnlang.compreg.commands.array;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ArrayStoreDec implements Command {
    private final int source;
    private final int base;
    private final int length;
    private final int index;
    private final boolean global;
    private final DecimalTypeInfo decimalTypeInfo;

    public ArrayStoreDec(int source, int base, int length, int index, boolean global,
            DecimalTypeInfo decimalTypeInfo) {
        this.source = source; this.base = base; this.length = length; this.index = index; this.global = global;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        int target = base + ArrayBounds.checkedIndex(memory.getInt(index), length);
        var value = decimalTypeInfo.applyConstraints(memory.getDec(source));
        if (global) memory.setDecAbsolute(target, value); else memory.setDec(target, value);
    }
}