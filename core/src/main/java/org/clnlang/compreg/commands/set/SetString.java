package org.clnlang.compreg.commands.set;

public class SetString implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final String value;

    public SetString(int target, String value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        memory.setStr(target, value);
    }
    
}
