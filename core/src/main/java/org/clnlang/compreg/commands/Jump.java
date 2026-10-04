package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class Jump implements Command {

    private final Label label;
    private int target = -1;

    public Jump(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    public void resolveTarget(int target) {
        this.target = target;
    }

    @Override
    public void execute(Memory memory) {
        if (target < 0) {
            throw new IllegalStateException("Jump target was not resolved.");
        }
        memory.jumpTo(target);
    }
}
