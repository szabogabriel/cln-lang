package org.clnlang.compreg.commands.move;

public class MoveBool implements org.clnlang.compreg.commands.Command {
    private final int source;
    private final int target;

    public MoveBool(int source, int target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        boolean value = memory.getBool(source);
        memory.setBool(target, value);
    }
    
}
