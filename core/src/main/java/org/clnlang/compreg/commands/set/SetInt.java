package org.clnlang.compreg.commands.set;

public class SetInt implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int value;

    public SetInt(int target, int value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setInt(target, value);
    }
    
}
