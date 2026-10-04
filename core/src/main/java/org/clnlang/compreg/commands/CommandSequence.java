package org.clnlang.compreg.commands;

import java.util.List;

import org.clnlang.compreg.Memory;

public final class CommandSequence implements Command {

    private final List<Command> commands;

    public CommandSequence(List<Command> commands) {
        this.commands = List.copyOf(commands);
    }

    public List<Command> getCommands() {
        return List.copyOf(commands);
    }

    @Override
    public void execute(Memory memory) {
        memory.beginInstructionSequence();
        try {
            int instruction = 0;
            while (instruction < commands.size() && !memory.isReturnRequested()) {
                memory.setCurrentInstruction(instruction);
                commands.get(instruction).execute(memory);
                if (!memory.isReturnRequested()) {
                    instruction = memory.nextInstruction();
                }
            }
        } finally {
            memory.endInstructionSequence();
        }
    }
}
