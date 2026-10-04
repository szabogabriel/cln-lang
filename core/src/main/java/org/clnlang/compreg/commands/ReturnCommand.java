package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class ReturnCommand implements Command {

    public static final ReturnCommand INSTANCE = new ReturnCommand();

    private ReturnCommand() {
    }

    @Override
    public void execute(Memory memory) {
        memory.requestReturn();
    }
}
