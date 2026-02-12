package org.clnlang.compiled;

import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.types.Types;

public interface Instruction {

    void execute(ExecutionContext context);

    int[] result();

    Types[] getResultType();
    
}
