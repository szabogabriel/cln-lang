package org.clnlang.compreg.commands.move;

public class MoveInt implements org.clnlang.compreg.commands.Command {
    private final int source;
    private final int target;

    public MoveInt(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        long value = memory.getInt(source);
        memory.setInt(target, value);
    }
    
}
