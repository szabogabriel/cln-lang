package org.clnlang.compreg.commands.move;

import org.clnlang.compile.types.DecimalTypeInfo;

public class MoveDec implements org.clnlang.compreg.commands.Command {

    private final int source;
    private final int target;
    private final DecimalTypeInfo decimalTypeInfo;

    public MoveDec(int source, int target) {
        this(source, target, DecimalTypeInfo.DEFAULT);
    }

    public MoveDec(int source, int target, DecimalTypeInfo decimalTypeInfo) {
        this.source = source;
        this.target = target;
        this.decimalTypeInfo = decimalTypeInfo != null ? decimalTypeInfo : DecimalTypeInfo.DEFAULT;
    }

    @Override
    public void execute(org.clnlang.compreg.Memory memory) {
        java.math.BigDecimal value = memory.getDec(source);
        memory.setDec(target, decimalTypeInfo.applyConstraints(value));
    }
    
}
