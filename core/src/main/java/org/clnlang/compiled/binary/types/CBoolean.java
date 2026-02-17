package org.clnlang.compiled.binary.types;

import org.clnlang.exception.ValueAlreadySetException;

public class CBoolean {

    private String name;
    private boolean value;
    private long index;
    private boolean isConstant;
    private boolean isAlreadySet;

    public CBoolean(String name, boolean isConstant) {
        this.name = name;
        this.value = false;
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = false;
    }

    public CBoolean(String name, boolean value, boolean isConstant) {
        this.name = name;
        this.value = false;
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = true;
    }

    public String getName() {
        return this.name;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        if (isConstant && isAlreadySet) {
            throw new ValueAlreadySetException("Boolean value already set for name " + name);
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
