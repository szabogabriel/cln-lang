package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class NotBool implements Command {

    private final int sourceOffset;
    private final int targetOffset;

    public NotBool(int sourceOffset, int targetOffset) {
        this.sourceOffset = sourceOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        memory.setBool(targetOffset, !memory.getBool(sourceOffset));
    }
}
