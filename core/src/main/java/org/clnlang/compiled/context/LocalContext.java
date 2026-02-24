package org.clnlang.compiled.context;

import org.clnlang.compiled.binary.Types;

public class LocalContext extends Context {

    private Memory memory;
    
    // Caller information for return handling
    private LocalContext callerContext;
    private int[] callerReturnRegisters;
    private Types[] callerReturnTypes;
    private boolean hasReturned = false;

    public LocalContext(MemoryAllocatorDescription memoryAllocatorDescription) {
        this.memory = new Memory(memoryAllocatorDescription);
    }
    
    public void setCallerReturnInfo(LocalContext callerContext, int[] returnRegisters, Types[] returnTypes) {
        this.callerContext = callerContext;
        this.callerReturnRegisters = returnRegisters;
        this.callerReturnTypes = returnTypes;
    }
    
    public LocalContext getCallerContext() {
        return callerContext;
    }
    
    public int[] getCallerReturnRegisters() {
        return callerReturnRegisters;
    }
    
    public Types[] getCallerReturnTypes() {
        return callerReturnTypes;
    }
    
    public boolean hasReturned() {
        return hasReturned;
    }
    
    public void markReturned() {
        this.hasReturned = true;
    }
    
}
