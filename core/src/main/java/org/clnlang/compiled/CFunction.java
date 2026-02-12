package org.clnlang.compiled;

import org.clnlang.compiled.types.Types;

public class CFunction {

    private String name;

    private int[] parameters;
    private Types[] parameterTypes;
    private int[] returns;
    private Types[] returnTypes;
    private Instruction[] instructions;

    public CFunction(String name, int[] parameters, Types[] parameterTypes, int[] returns, Types[] returnTypes, Instruction[] instructions) {
        this.name = name;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.returns = returns;
        this.returnTypes = returnTypes;
        this.instructions = instructions;
    }
    
}
