package org.clnlang.compreg.commands.sub;

import java.math.BigDecimal;

public class SubIntDec implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public SubIntDec(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setDec(target, BigDecimal.valueOf(memory.getInt(opA)).subtract(memory.getDec(opB)));
    }
}
