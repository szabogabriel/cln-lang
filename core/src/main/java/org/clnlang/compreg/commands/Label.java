package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class Label implements Command {

    @Override
    public void execute(Memory memory) {
        throw new IllegalStateException("Labels are compile-time markers and cannot be executed.");
    }
}
