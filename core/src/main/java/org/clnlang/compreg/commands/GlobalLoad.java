package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class GlobalLoad implements Command {

    private final String type;
    private final int globalOffset;
    private final int targetOffset;

    public GlobalLoad(String type, int globalOffset, int targetOffset) {
        this.type = type;
        this.globalOffset = globalOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        memory.copyGlobalToCurrent(type, globalOffset, targetOffset);
    }
}
