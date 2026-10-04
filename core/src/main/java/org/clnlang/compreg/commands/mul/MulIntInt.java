package org.clnlang.compreg.commands.mul;

public class MulIntInt implements org.clnlang.compreg.commands.Command {

    private final int target;
    private final int opA;
    private final int opB;

    public MulIntInt(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setInt(target, memory.getInt(opA) * memory.getInt(opB));
    }
}
