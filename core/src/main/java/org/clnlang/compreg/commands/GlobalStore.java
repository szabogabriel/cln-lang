package org.clnlang.compreg.commands;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;

public final class GlobalStore implements Command {

    private final String type;
    private final int sourceOffset;
    private final int globalOffset;
    private final DecimalTypeInfo decimalTypeInfo;

    public GlobalStore(String type, int sourceOffset, int globalOffset) {
        this(type, sourceOffset, globalOffset, DecimalTypeInfo.DEFAULT);
    }

    public GlobalStore(String type, int sourceOffset, int globalOffset, DecimalTypeInfo decimalTypeInfo) {
        this.type = type;
        this.sourceOffset = sourceOffset;
        this.globalOffset = globalOffset;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        memory.copyCurrentToGlobal(type, sourceOffset, globalOffset, decimalTypeInfo);
    }
}
