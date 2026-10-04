package org.clnlang.compreg.commands.concat;

import org.clnlang.compreg.Memory;

public interface Stringifier {
    String stringify(Memory memory, int offset);
}