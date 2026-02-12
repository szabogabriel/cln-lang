package org.clnlang.compiled.context;

import java.math.BigDecimal;
import java.util.Arrays;

public abstract class Context {

    private static final int PAGE_SIZE = 256;

    private boolean [] isLongMutable = new boolean[PAGE_SIZE];
    private boolean [] isLongInitialized = new boolean[PAGE_SIZE];
    private long [] longMemory = new long[PAGE_SIZE];
    private int longMemoryCounter = 0;
    
    private boolean [] isDecimalMutable = new boolean[PAGE_SIZE];
    private boolean [] isDecimalInitialized = new boolean[PAGE_SIZE];
    private BigDecimal [] decimalMemory = new BigDecimal[PAGE_SIZE];
    private int decimalMemoryCounter = 0;

    private boolean [] isBooleanMutable = new boolean[PAGE_SIZE];
    private boolean [] isBooleanInitialized = new boolean[PAGE_SIZE];
    private boolean [] booleanMemory = new boolean[PAGE_SIZE];
    private int booleanMemoryCounter = 0;

    private boolean [] isStringMutable = new boolean[PAGE_SIZE];
    private boolean [] isStringInitialized = new boolean[PAGE_SIZE];
    private String [] stringMemory = new String[PAGE_SIZE];
    private int stringMemoryCounter = 0;

    public void setLong(int address, long value) {
        if (!isLongMutable[address] && isLongInitialized[address]) {
            throw new IllegalStateException("Attempt to modify immutable long at address " + address);
        }
        longMemory[address] = value;
        isLongInitialized[address] = true;
    }

    public void setBigDecimal(int address, BigDecimal value) {
        if (!isDecimalMutable[address] && isDecimalInitialized[address]) {
            throw new IllegalStateException("Attempt to modify immutable decimal at address " + address);
        }
        decimalMemory[address] = value;
        isDecimalInitialized[address] = true;
    }

    public void setBoolean(int address, boolean value) {
        if (!isBooleanMutable[address] && isBooleanInitialized[address]) {
            throw new IllegalStateException("Attempt to modify immutable boolean at address " + address);
        }
        booleanMemory[address] = value;
        isBooleanInitialized[address] = true;
    }

    public void setString(int address, String value) {
        if (!isStringMutable[address] && isStringInitialized[address]) {
            throw new IllegalStateException("Attempt to modify immutable string at address " + address);
        }
        stringMemory[address] = value;
        isStringInitialized[address] = true;
    }

    public long getLong(int address) {
        return longMemory[address];
    }

    public BigDecimal getBigDecimal(int address) {
        return decimalMemory[address];
    }

    public boolean getBoolean(int address) {
        return booleanMemory[address];
    }

    public String getString(int address) {
        return stringMemory[address];
    }

    public int allocateLong(boolean mutable) {
        int ret = longMemoryCounter++;
        if (ret == longMemory.length) {
            longMemory = Arrays.copyOf(longMemory, longMemory.length + PAGE_SIZE);
            isLongMutable = Arrays.copyOf(isLongMutable, isLongMutable.length + PAGE_SIZE);
            isLongInitialized = Arrays.copyOf(isLongInitialized, isLongInitialized.length + PAGE_SIZE);
        }
        isLongMutable[ret] = mutable;
        return ret;
    }

    public int allocateDecimal(boolean mutable) {
        int ret = decimalMemoryCounter++;
        if (ret == decimalMemory.length) {
            decimalMemory = Arrays.copyOf(decimalMemory, decimalMemory.length + PAGE_SIZE);
            isDecimalMutable = Arrays.copyOf(isDecimalMutable, isDecimalMutable.length + PAGE_SIZE);
            isDecimalInitialized = Arrays.copyOf(isDecimalInitialized, isDecimalInitialized.length + PAGE_SIZE);
        }
        isDecimalMutable[ret] = mutable;
        return ret;
    }

    public int allocateBoolean(boolean mutable) {
        int ret = booleanMemoryCounter++;
        if (ret == booleanMemory.length) {
            booleanMemory = Arrays.copyOf(booleanMemory, booleanMemory.length + PAGE_SIZE);
            isBooleanMutable = Arrays.copyOf(isBooleanMutable, isBooleanMutable.length + PAGE_SIZE);
            isBooleanInitialized = Arrays.copyOf(isBooleanInitialized, isBooleanInitialized.length + PAGE_SIZE);
        }
        isBooleanMutable[ret] = mutable;
        return ret;
    }

    public int allocateString(boolean mutable) {
        int ret = stringMemoryCounter++;
        if (ret == stringMemory.length) {
            stringMemory = Arrays.copyOf(stringMemory, stringMemory.length + PAGE_SIZE);
            isStringMutable = Arrays.copyOf(isStringMutable, isStringMutable.length + PAGE_SIZE);
            isStringInitialized = Arrays.copyOf(isStringInitialized, isStringInitialized.length + PAGE_SIZE);
        }
        isStringMutable[ret] = mutable;
        return ret;
    }

}
