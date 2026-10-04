package org.clnlang.compreg.commands.concat;

import org.clnlang.compreg.Memory;

public final class IntStringifier implements Stringifier {
    @Override
    public String stringify(Memory memory, int offset) { return Long.toString(memory.getInt(offset)); }
}