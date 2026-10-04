package org.clnlang.compreg.commands.global.store;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class GlobalStoreString implements Command {
    private final int source;
    private final int target;

    public GlobalStoreString(int source, int target) { this.source = source; this.target = target; }

    @Override
    public void execute(Memory memory) { memory.setStrAbsolute(target, memory.getStr(source)); }
}