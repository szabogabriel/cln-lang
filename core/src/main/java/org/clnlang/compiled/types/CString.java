package org.clnlang.compiled.types;

import org.clnlang.exception.ValueAlreadySetException;

public class CString {

    private String name;
    private String value;
    private long index;
    private boolean isConstant;
    private boolean isAlreadySet;

    public CString(String name, boolean isConstant) {
        this.name = name;
        this.value = "";
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = false;
    }

    public CString(String name, String value, boolean isConstant) {
        this.name = name;
        this.value = value;
        this.index = -1;
        this.isConstant = isConstant;
        this.isAlreadySet = true;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (isConstant && isAlreadySet) {
            throw new ValueAlreadySetException("String value already set for name " + name);
        }
        this.value = value;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
    
}
