package org.clnlang.compreg.commands.set;

public class SetBool implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final boolean value;

    public SetBool(int target, boolean value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setBool(target, value);
    }
    
}
