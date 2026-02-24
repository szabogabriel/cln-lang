package org.clnlang.compiled.binary.types;

import java.math.BigDecimal;

import org.clnlang.exception.ValueAlreadySetException;

public class CDecimal {
    private String name;
    private BigDecimal value;
    private long index;
    private boolean isConstant;
    private boolean isSet;

    public CDecimal(String name, boolean isConstant) {
        this.name = name;
        this.value = BigDecimal.ZERO;
        this.index = 0;
        this.isConstant = isConstant;
        this.isSet = false;
    }

    public CDecimal(String name, BigDecimal value, boolean isConstant) {
        this.name = name;
        this.value = value;
        this.index = 0;
        this.isConstant = isConstant;
        this.isSet = true;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        if (isConstant && isSet) {
            throw new ValueAlreadySetException("Decimal value already set for variable " + name);
        }
        this.value = value;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public boolean isConstant() {
        return isConstant;
    }
}
