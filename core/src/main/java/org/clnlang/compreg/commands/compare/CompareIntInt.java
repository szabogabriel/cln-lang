package org.clnlang.compreg.commands.compare;

import org.clnlang.compreg.Memory;

public final class CompareIntInt extends Compare {

    private final int left;
    private final int right;

    public CompareIntInt(Operator operator, int left, int right, int target) {
        super(operator, target);
        this.left = left;
        this.right = right;
    }

    @Override
    protected int compareValues(Memory memory) {
        return Long.compare(memory.getInt(left), memory.getInt(right));
    }
}