package org.clnlang.compreg.commands.concat;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ConcatString implements Command {

    private final Stringifier leftStringifier;
    private final Stringifier rightStringifier;
    private final int leftOffset;
    private final int rightOffset;
    private final int targetOffset;

    public ConcatString(Stringifier leftStringifier, Stringifier rightStringifier,
            int leftOffset, int rightOffset, int targetOffset) {
        this.leftStringifier = leftStringifier;
        this.rightStringifier = rightStringifier;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        memory.setStr(targetOffset, leftStringifier.stringify(memory, leftOffset)
                + rightStringifier.stringify(memory, rightOffset));
    }
}