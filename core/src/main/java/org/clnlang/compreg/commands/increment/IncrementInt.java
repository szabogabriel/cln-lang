package org.clnlang.compreg.commands.increment;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class IncrementInt implements Command {
    private final int variable;
    private final int result;
    private final boolean global;
    private final boolean increment;
    private final boolean prefix;

    public IncrementInt(int variable, int result, boolean global, boolean increment, boolean prefix) {
        this.variable = variable;
        this.result = result;
        this.global = global;
        this.increment = increment;
        this.prefix = prefix;
    }

    @Override
    public void execute(Memory memory) {
        long oldValue = global ? memory.getIntAbsolute(variable) : memory.getInt(variable);
        long newValue = oldValue + (increment ? 1 : -1);
        if (global) memory.setIntAbsolute(variable, newValue); else memory.setInt(variable, newValue);
        memory.setInt(result, prefix ? newValue : oldValue);
    }
}