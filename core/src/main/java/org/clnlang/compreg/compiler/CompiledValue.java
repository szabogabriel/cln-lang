package org.clnlang.compreg.compiler;

import java.util.List;

import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.runtime.StructValue;

final class CompiledValue {
    final String type;
    final int offset;
    final List<Command> commands;
    final List<Call.Register> tupleValues;
    final int arrayLength;
    final int[] arrayDimensions;
    final boolean globalArray;
    final boolean arrayLiteral;
    final StructValue structValue;

    CompiledValue(String type, int offset, List<Command> commands) {
        this(type, offset, commands, null, null, false, false, null);
    }

    CompiledValue(String type, int offset, List<Command> commands, List<Call.Register> tupleValues) {
        this(type, offset, commands, tupleValues, null, false, false, null);
    }

    CompiledValue(String type, int offset, List<Command> commands, int[] arrayDimensions,
            boolean globalArray, boolean arrayLiteral) {
        this(type, offset, commands, null, arrayDimensions, globalArray, arrayLiteral, null);
    }

    CompiledValue(String type, int offset, List<Command> commands, StructValue structValue) {
        this(type, offset, commands, null, null, false, false, structValue);
    }

    private CompiledValue(String type, int offset, List<Command> commands, List<Call.Register> tupleValues,
            int[] arrayDimensions, boolean globalArray, boolean arrayLiteral, StructValue structValue) {
        this.type = type;
        this.offset = offset;
        this.commands = commands;
        this.tupleValues = tupleValues;
        this.arrayDimensions = arrayDimensions == null ? null : arrayDimensions.clone();
        this.arrayLength = arrayDimensions == null ? -1 : arrayElementCount(arrayDimensions);
        this.globalArray = globalArray;
        this.arrayLiteral = arrayLiteral;
        this.structValue = structValue;
    }

    private static int arrayElementCount(int[] dimensions) {
        int count = 1;
        for (int dimension : dimensions) {
            count = Math.multiplyExact(count, dimension);
        }
        return count;
    }
}
