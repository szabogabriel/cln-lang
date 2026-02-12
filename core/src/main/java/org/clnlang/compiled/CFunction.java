package org.clnlang.compiled;

import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.types.Types;

public class CFunction implements Instruction {

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

    @Override
    public void execute(ExecutionContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public int[] result() {
        return returns;
    }

    @Override
    public Types[] getResultType() {
        return returnTypes;
    }
    
}
