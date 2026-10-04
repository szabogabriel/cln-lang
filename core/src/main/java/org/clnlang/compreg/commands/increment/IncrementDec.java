package org.clnlang.compreg.commands.increment;

import java.math.BigDecimal;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class IncrementDec implements Command {
    private final int variable;
    private final int result;
    private final boolean global;
    private final boolean increment;
    private final boolean prefix;
    private final DecimalTypeInfo decimalTypeInfo;

    public IncrementDec(int variable, int result, boolean global, boolean increment, boolean prefix,
            DecimalTypeInfo decimalTypeInfo) {
        this.variable = variable;
        this.result = result;
        this.global = global;
        this.increment = increment;
        this.prefix = prefix;
        this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
    }

    @Override
    public void execute(Memory memory) {
        BigDecimal oldValue = global ? memory.getDecAbsolute(variable) : memory.getDec(variable);
        BigDecimal newValue = decimalTypeInfo.applyConstraints(
                increment ? oldValue.add(BigDecimal.ONE) : oldValue.subtract(BigDecimal.ONE));
        if (global) memory.setDecAbsolute(variable, newValue); else memory.setDec(variable, newValue);
        memory.setDec(result, prefix ? newValue : oldValue);
    }
}