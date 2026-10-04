package org.clnlang.compreg.commands.compare;

import java.math.BigDecimal;

import org.clnlang.compreg.Memory;

public final class CompareIntDec extends Compare {

    private final int left;
    private final int right;

    public CompareIntDec(Operator operator, int left, int right, int target) {
        super(operator, target);
        this.left = left;
        this.right = right;
    }

    @Override
    protected int compareValues(Memory memory) {
        return BigDecimal.valueOf(memory.getInt(left)).compareTo(memory.getDec(right));
    }
}