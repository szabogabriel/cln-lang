package org.clnlang.compreg.commands.modulo;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Command;

public final class ModuloIntInt implements Command {

    private final int target;
    private final int left;
    private final int right;

    public ModuloIntInt(int target, int left, int right) {
        this.target = target;
        this.left = left;
        this.right = right;
    }

    @Override
    public void execute(Memory memory) {
        long divisor = memory.getInt(right);
        if (divisor == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        memory.setInt(target, memory.getInt(left) % divisor);
    }
}