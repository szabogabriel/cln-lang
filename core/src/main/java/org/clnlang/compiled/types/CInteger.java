package org.clnlang.compiled.types;

import org.clnlang.exception.ValueAlreadySetException;

public class CInteger {

    private String name;
    private long value;
    private long index;
    private boolean isConstant;
    private boolean isAlreadySet;

    public CInteger(String name, boolean isConstant) {
        this.name = name;
        this.value = 0L;
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = false;
    }

    public CInteger(String name, long value, boolean isConstant) {
        this.name = name;
        this.value = value;
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = true;
    }

    public String getName() {
        return this.name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        if (isConstant && isAlreadySet) {
            throw new ValueAlreadySetException("Integer value already set for name " + name);
        }
        this.value = value;
        isAlreadySet = true;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
    
}
