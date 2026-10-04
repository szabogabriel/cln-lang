package org.clnlang.compreg.commands.set;

public class SetDec implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final java.math.BigDecimal value;

    public SetDec(int target, java.math.BigDecimal value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setDec(target, value);
    }
}
