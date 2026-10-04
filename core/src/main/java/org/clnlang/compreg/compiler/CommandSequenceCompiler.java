package org.clnlang.compreg.compiler;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.commands.CommandSequence;
import org.clnlang.compreg.commands.Jump;
import org.clnlang.compreg.commands.JumpIfFalse;
import org.clnlang.compreg.commands.JumpIfTrue;
import org.clnlang.compreg.commands.Label;

public final class CommandSequenceCompiler {

    private CommandSequenceCompiler() {
    }

    public static CommandSequence compile(List<Command> commands) {
        List<Command> executableCommands = new ArrayList<>();
        Map<Label, Integer> labelTargets = new IdentityHashMap<>();
        for (Command command : commands) {
            if (command instanceof Label label) {
                labelTargets.put(label, executableCommands.size());
            } else {
                executableCommands.add(command);
            }
        }

        for (Command command : executableCommands) {
            if (command instanceof Jump jump) {
                jump.resolveTarget(requireLabel(labelTargets, jump.getLabel()));
            } else if (command instanceof JumpIfFalse jumpIfFalse) {
                jumpIfFalse.resolveTarget(requireLabel(labelTargets, jumpIfFalse.getLabel()));
            } else if (command instanceof JumpIfTrue jumpIfTrue) {
                jumpIfTrue.resolveTarget(requireLabel(labelTargets, jumpIfTrue.getLabel()));
            }
        }
        return new CommandSequence(executableCommands);
    }

    private static int requireLabel(Map<Label, Integer> labelTargets, Label label) {
        Integer target = labelTargets.get(label);
        if (target == null) {
            throw new IllegalArgumentException("Jump target label is not part of this command sequence.");
        }
        return target;
    }
}