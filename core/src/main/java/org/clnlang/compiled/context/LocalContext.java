package org.clnlang.compiled.context;

public class LocalContext extends Context {

    private Memory memory;

    public LocalContext(MemoryAllocatorDescription memoryAllocatorDescription) {
        this.memory = new Memory(memoryAllocatorDescription);
    }
    
}
