package org.clnlang.compreg.commands.global.load;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class GlobalLoadString implements Command {
    private final int source;
    private final int target;

    public GlobalLoadString(int source, int target) { this.source = source; this.target = target; }

    @Override
    public void execute(Memory memory) { memory.setStr(target, memory.getStrAbsolute(source)); }
}