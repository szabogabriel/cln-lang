package org.clnlang.compreg.commands;

import java.util.Arrays;
import java.util.List;

import org.clnlang.compreg.Memory;

public final class CommandSequence implements Command {

    private final Command[] commands;
    private final boolean hasJumps;

    public CommandSequence(List<Command> commands) {
        this.commands = commands.toArray(Command[]::new);
        boolean containsJump = false;
        for (Command command : this.commands) {
            if (command instanceof Jump || command instanceof JumpIfFalse || command instanceof JumpIfTrue) {
                containsJump = true;
                break;
            }
        }
        this.hasJumps = containsJump;
    }

    public List<Command> getCommands() {
        return Arrays.asList(commands);
    }

    @Override
    public void execute(Memory memory) {
        if (!hasJumps) {
            for (Command command : commands) {
                if (memory.isReturnRequested()) {
                    break;
                }
                command.execute(memory);
            }
            return;
        }

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
