package org.clnlang.compreg.lib;

import java.util.List;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.runtime.CompiledFunction;

@FunctionalInterface
public interface JavaFunction {

    void execute(Memory memory, List<CompiledFunction.Slot> parameters,
            List<CompiledFunction.Slot> results);
}