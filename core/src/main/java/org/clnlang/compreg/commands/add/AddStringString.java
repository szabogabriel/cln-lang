package org.clnlang.compreg.commands.add;

public class AddStringString implements  org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public AddStringString(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setStr(target, memory.getStr(opA) + memory.getStr(opB));
    }
}
