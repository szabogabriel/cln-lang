package org.clnlang.compreg.commands.move;

public class MoveString implements org.clnlang.compreg.commands.Command {
    private final int source;
    private final int target;

    public MoveString(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        String value = memory.getStr(source);
        memory.setStr(target, value);
    }
    
}
