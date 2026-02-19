package org.clnlang.compiled.binary;

import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.MemoryAllocatorDescription;

public class CFunction implements CExecutable {

    private String name;

    private int[] parameters;
    private Types[] parameterTypes;
    private int[] returns;
    private Types[] returnTypes;
    private CExecutable[] instructions;
    private MemoryAllocatorDescription memoryAllocatorDescription;

    public CFunction(String name, int[] parameters, Types[] parameterTypes, int[] returns, Types[] returnTypes, CExecutable[] instructions, MemoryAllocatorDescription memoryAllocatorDescription) {
        this.name = name;
        this.parameters = parameters;
        this.parameterTypes = parameterTypes;
        this.returns = returns;
        this.returnTypes = returnTypes;
        this.instructions = instructions;
        this.memoryAllocatorDescription = memoryAllocatorDescription;
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
        context.pushLocalContext(memoryAllocatorDescription);
    }

    private void popCurrentLocalContext(ExecutionContext context) {
        context.popLocalContext();
    }

    @Override
    public int[] getResults() {
        return returns;
    }

    @Override
    public Types[] getResultTypes() {
        return returnTypes;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
    public String getName() {
        return name;
    }
    
}
