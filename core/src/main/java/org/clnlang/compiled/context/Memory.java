package org.clnlang.compiled.context;

import java.math.BigDecimal;

public class Memory {

    private long [] longStorage;
    private BigDecimal [] decimalStorage;
    private boolean [] booleanStorage;
    private String [] stringStorage;
    private Object [] structStorage;
    private Object [] unionStorage;
    private Object [] arrayStorage;

    public Memory(MemoryAllocatorDescription description) {
        this.longStorage = new long[description.getIntSize()];
        this.decimalStorage = new BigDecimal[description.getDecSize()];
        this.booleanStorage = new boolean[description.getBoolSize()];
        this.stringStorage = new String[description.getStringSize()];
        this.structStorage = new Object[description.getStructSize()];
        this.unionStorage = new Object[description.getUnionSize()];
        this.arrayStorage = new Object[description.getArraySize()];
    }
    
}
