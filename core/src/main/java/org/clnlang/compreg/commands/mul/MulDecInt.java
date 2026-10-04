package org.clnlang.compreg.commands.mul;

import java.math.BigDecimal;

public class MulDecInt implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public MulDecInt(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setDec(target, memory.getDec(opA).multiply(BigDecimal.valueOf(memory.getInt(opB))));
    }
}
