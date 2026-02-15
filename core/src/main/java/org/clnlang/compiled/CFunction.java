package org.clnlang.compiled;

import org.clnlang.compiled.context.ExecutionContext;

public class CFunction implements CExecutable {

    private String name;

    private int[] parameters;
    private Types[] parameterTypes;
    private int[] returns;
    private Types[] returnTypes;
    private CExecutable[] instructions;

    public CFunction(String name, int[] parameters, Types[] parameterTypes, int[] returns, Types[] returnTypes, CExecutable[] instructions) {
        this.name = name;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.returns = returns;
        this.returnTypes = returnTypes;
        this.instructions = instructions;
    }

    @Override
    public void execute(ExecutionContext context) {
        createNewLocalContext(context);
        executeInstructions(context);
        popCurrentLocalContext(context);
    }

    private void executeInstructions(ExecutionContext context) {
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
    }

    private void createNewLocalContext(ExecutionContext context) {
        context.pushLocalContext();
    }

    private void popCurrentLocalContext(ExecutionContext context) {
        context.popLocalContext();
    }

    @Override
    public int[] getResults() {
        return returns;
    }

    @Override
    public Types[] getResultType() {
        return returnTypes;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
