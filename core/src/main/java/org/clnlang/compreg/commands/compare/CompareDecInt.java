package org.clnlang.compreg.commands.compare;

import java.math.BigDecimal;

import org.clnlang.compreg.Memory;

public final class CompareDecInt extends Compare {

    private final int left;
    private final int right;

    public CompareDecInt(Operator operator, int left, int right, int target) {
        super(operator, target);
        this.left = left;
        this.right = right;
    }

    @Override
    protected int compareValues(Memory memory) {
        return memory.getDec(left).compareTo(BigDecimal.valueOf(memory.getInt(right)));
    }
}