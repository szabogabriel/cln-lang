package org.clnlang.compreg.commands.div;

import java.math.BigDecimal;
import java.math.MathContext;

public class DivIntDec implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public DivIntDec(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        BigDecimal divisor = memory.getDec(opB);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        memory.setDec(target, BigDecimal.valueOf(memory.getInt(opA)).divide(divisor, MathContext.DECIMAL128));
    }
}
