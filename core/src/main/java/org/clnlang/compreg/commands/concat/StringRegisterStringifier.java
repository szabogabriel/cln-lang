package org.clnlang.compreg.commands.concat;

import org.clnlang.compreg.Memory;

public final class StringRegisterStringifier implements Stringifier {
    @Override
    public String stringify(Memory memory, int offset) { return String.valueOf(memory.getStr(offset)); }
}