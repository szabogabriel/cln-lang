package org.clnlang.compreg.commands;

import java.util.Arrays;
import java.util.List;

import org.clnlang.compreg.Memory;

public final class CommandSequence implements Command {

    private final Command[] commands;

    public CommandSequence(List<Command> commands) {
        this.commands = commands.toArray(Command[]::new);
    }

    public List<Command> getCommands() {
        return Arrays.asList(commands);
    }

    @Override
    public void execute(Memory memory) {
        memory.beginInstructionSequence();
        try {
            int instruction = 0;
            while (instruction < commands.length && !memory.isReturnRequested()) {
                memory.setCurrentInstruction(instruction);
                commands[instruction].execute(memory);
                if (!memory.isReturnRequested()) {
                    instruction = memory.nextInstruction();
                }
            }
        } finally {
            memory.endInstructionSequence();
        }
    }
}
