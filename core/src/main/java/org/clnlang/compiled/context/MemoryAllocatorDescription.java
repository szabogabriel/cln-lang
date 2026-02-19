package org.clnlang.compiled.context;

public class MemoryAllocatorDescription {

    private int intSize;
    private int decSize;
    private int boolSize;
    private int stringSize;
    private int structSize;
    private int unionSize;

    public MemoryAllocatorDescription(int intSize, int decSize, int boolSize, int stringSize, int structSize, int unionSize) {
        this.intSize = intSize;
        this.decSize = decSize;
        this.boolSize = boolSize;
        this.stringSize = stringSize;
        this.structSize = structSize;
        this.unionSize = unionSize; 
    }

    public int getIntSize() {
        return intSize;
    }

    public int getDecSize() {
        return decSize;
    }

    public int getBoolSize() {
        return boolSize;
    }

    public int getStringSize() {
        return stringSize;
    }

    public int getStructSize() {
        return structSize;
    }

    public int getUnionSize() {
        return unionSize;
    }
}
