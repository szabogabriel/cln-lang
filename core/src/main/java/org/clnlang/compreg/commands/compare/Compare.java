package org.clnlang.compreg.commands.compare;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public abstract class Compare implements Command {

    public enum Operator {
        EQ, NEQ, LT, LTE, GT, GTE
    }

    private final Operator operator;
    private final int target;

    protected Compare(Operator operator, int target) {
        this.operator = operator;
        this.target = target;
    }

    @Override
    public final void execute(Memory memory) {
        int comparison = compareValues(memory);
        boolean result = switch (operator) {
            case EQ -> comparison == 0;
            case NEQ -> comparison != 0;
            case LT -> comparison < 0;
            case LTE -> comparison <= 0;
            case GT -> comparison > 0;
            case GTE -> comparison >= 0;
        };
        memory.setBool(target, result);
    }

    protected abstract int compareValues(Memory memory);
}