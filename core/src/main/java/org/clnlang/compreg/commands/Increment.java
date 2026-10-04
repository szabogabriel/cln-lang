package org.clnlang.compreg.commands;

import java.math.BigDecimal;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;

public final class Increment implements Command {

    private final String type;
    private final int variableOffset;
    private final int resultOffset;
    private final boolean global;
    private final boolean increment;
    private final boolean prefix;
    private final DecimalTypeInfo decimalTypeInfo;

    public Increment(String type, int variableOffset, int resultOffset, boolean global,
            boolean increment, boolean prefix, DecimalTypeInfo decimalTypeInfo) {
        this.type = type;
        this.variableOffset = variableOffset;
        this.resultOffset = resultOffset;
        this.global = global;
        this.increment = increment;
        this.prefix = prefix;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        switch (type) {
            case "int" -> executeInt(memory);
            case "dec" -> executeDecimal(memory);
            default -> throw new IllegalStateException("Unsupported increment type: " + type);
        }
    }

    private void executeInt(Memory memory) {
        long oldValue = global ? memory.getIntAbsolute(variableOffset) : memory.getInt(variableOffset);
        long newValue = oldValue + (increment ? 1 : -1);
        if (global) {
            memory.setIntAbsolute(variableOffset, newValue);
        } else {
            memory.setInt(variableOffset, newValue);
        }
        memory.setInt(resultOffset, prefix ? newValue : oldValue);
    }

    private void executeDecimal(Memory memory) {
        BigDecimal oldValue = global ? memory.getDecAbsolute(variableOffset) : memory.getDec(variableOffset);
        BigDecimal newValue = decimalTypeInfo.applyConstraints(
                increment ? oldValue.add(BigDecimal.ONE) : oldValue.subtract(BigDecimal.ONE));
        if (global) {
            memory.setDecAbsolute(variableOffset, newValue);
        } else {
            memory.setDec(variableOffset, newValue);
        }
        memory.setDec(resultOffset, prefix ? newValue : oldValue);
    }
}