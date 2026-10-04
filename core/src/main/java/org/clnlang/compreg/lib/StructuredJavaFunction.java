package org.clnlang.compreg.lib;

import java.util.List;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Call;

@FunctionalInterface
public interface StructuredJavaFunction {

    void execute(Memory memory, List<Call.Register> arguments, List<Call.Register> results);
}