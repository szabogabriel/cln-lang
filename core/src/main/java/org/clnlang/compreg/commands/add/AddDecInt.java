package org.clnlang.compreg.commands.add;

import java.math.BigDecimal;

public class AddDecInt implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public AddDecInt(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setDec(target, memory.getDec(opA).add(BigDecimal.valueOf(memory.getInt(opB))));
    }
}
