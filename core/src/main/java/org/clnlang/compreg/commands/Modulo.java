package org.clnlang.compreg.commands;

import java.math.BigDecimal;

import org.clnlang.compreg.Memory;

public final class Modulo implements Command {

    private final String leftType;
    private final String rightType;
    private final int targetOffset;
    private final int leftOffset;
    private final int rightOffset;

    public Modulo(String leftType, String rightType, int targetOffset, int leftOffset, int rightOffset) {
        this.leftType = leftType;
        this.rightType = rightType;
        this.targetOffset = targetOffset;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
    }

    @Override
    public void execute(Memory memory) {
        if (leftType.equals("int") && rightType.equals("int")) {
            long divisor = memory.getInt(rightOffset);
            if (divisor == 0) {
                throw new ArithmeticException("Modulo by zero");
            }
            memory.setInt(targetOffset, memory.getInt(leftOffset) % divisor);
            return;
        }

        BigDecimal dividend = asDecimal(memory, leftType, leftOffset);
        BigDecimal divisor = asDecimal(memory, rightType, rightOffset);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        memory.setDec(targetOffset, dividend.remainder(divisor));
    }

    private BigDecimal asDecimal(Memory memory, String type, int offset) {
        return type.equals("dec") ? memory.getDec(offset) : BigDecimal.valueOf(memory.getInt(offset));
    }
}