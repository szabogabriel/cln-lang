package org.clnlang.compreg.commands.global.store;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class GlobalStoreDec implements Command {
    private final int source;
    private final int target;
    private final DecimalTypeInfo decimalTypeInfo;

    public GlobalStoreDec(int source, int target, DecimalTypeInfo decimalTypeInfo) {
        this.source = source;
        this.target = target;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        memory.setDecAbsolute(target, decimalTypeInfo.applyConstraints(memory.getDec(source)));
    }
}