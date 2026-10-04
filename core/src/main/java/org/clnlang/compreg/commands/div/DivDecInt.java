package org.clnlang.compreg.commands.div;

import java.math.BigDecimal;
import java.math.MathContext;

public class DivDecInt implements org.clnlang.compreg.commands.Command {
    private final int target;
    private final int opA;
    private final int opB;

    public DivDecInt(int target, int opA, int opB) {
        this.target = target;
        this.opA = opA;
        this.opB = opB;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        long divisor = memory.getInt(opB);
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        memory.setDec(target, memory.getDec(opA).divide(BigDecimal.valueOf(divisor), MathContext.DECIMAL128));
    }
}
