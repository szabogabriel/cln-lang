package org.clnlang.compreg.commands.modulo;

import java.math.BigDecimal;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ModuloDecDec implements Command {

    private final int target;
    private final int left;
    private final int right;

    public ModuloDecDec(int target, int left, int right) {
        this.target = target;
        this.left = left;
        this.right = right;
    }

    @Override
    public void execute(Memory memory) {
        BigDecimal divisor = memory.getDec(right);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        memory.setDec(target, memory.getDec(left).remainder(divisor));
    }
}