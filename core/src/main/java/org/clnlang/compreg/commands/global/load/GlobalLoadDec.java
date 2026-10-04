package org.clnlang.compreg.commands.global.load;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class GlobalLoadDec implements Command {
    private final int source;
    private final int target;

    public GlobalLoadDec(int source, int target) { this.source = source; this.target = target; }

    @Override
    public void execute(Memory memory) { memory.setDec(target, memory.getDecAbsolute(source)); }
}