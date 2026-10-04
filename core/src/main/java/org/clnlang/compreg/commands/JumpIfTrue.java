package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class JumpIfTrue implements Command {

    private final int conditionOffset;
    private final Label label;
    private int target = -1;

    public JumpIfTrue(int conditionOffset, Label label) {
        this.conditionOffset = conditionOffset;
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
        if (memory.getBool(conditionOffset)) {
            if (target < 0) {
                throw new IllegalStateException("Conditional jump target was not resolved.");
            }
            memory.jumpTo(target);
        }
    }
}
