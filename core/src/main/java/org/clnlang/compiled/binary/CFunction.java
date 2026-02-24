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
        // Note: Context management is handled by CCallStatement (push) and CReturnStatement (pop)
        // This just executes the instructions in the current context
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
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

    public int[] getParameters() {
        return parameters;
    }

    public Types[] getParameterTypes() {
        return parameterTypes;
    }

    public int[] getReturns() {
        return returns;
    }

    public Types[] getReturnTypes() {
        return returnTypes;
    }

    public CExecutable[] getInstructions() {
        return instructions;
    }

    public MemoryAllocatorDescription getMemoryAllocatorDescription() {
        return memoryAllocatorDescription;
    }
    
}
