package org.clnlang.ast.visitor.compiled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.clnlang.compiled.CExecutable;
import org.clnlang.compiled.Types;

public class CompilerContextPart {

    private CompilerContextPart parent;

    private Map<String, Types> variableTypes = new HashMap<>();
    private Map<String, Integer> variableAddresses = new HashMap<>();
    private int varAddrCounterInt = 0;
    private int varAddrCounterDec = 0;
    private int varAddrCounterBool = 0;
    private int varAddrCounterString = 0;
    private int varAddrCounterStruct = 0;
    private int varAddrCounterUnion = 0;

    // Current function being compiled
    private List<CExecutable> currentFunctionInstructions = new ArrayList<>();

    public int registerVariable(String name, Types type) {
        if (variableTypes.containsKey(name)) {
            throw new IllegalStateException("Variable already registered: " + name);
        }
        variableTypes.put(name, type);
        int address = getNextAddress(type);
        variableAddresses.put(name, address);
        return address;
    }

    private int getNextAddress(Types type) {
        switch (type) {
            case INT:
                return varAddrCounterInt++;
            case DEC:
                return varAddrCounterDec++;
            case BOOL:
                return varAddrCounterBool++;
            case STRING:
                return varAddrCounterString++;
            case STRUCT:
                return varAddrCounterStruct++;
            case UNION:
                return varAddrCounterUnion++;
            default:
                throw new IllegalStateException("Unexpected variable type: " + type);
        }
    }

    public Types getVariableType(String name) {
        if (!variableTypes.containsKey(name)) {
            throw new IllegalStateException("Variable not registered: " + name);
        }
        return variableTypes.get(name);
    }

    public int getVariableAddress(String name) {
        if (!variableAddresses.containsKey(name)) {
            throw new IllegalStateException("Variable not registered: " + name);
        }
        return variableAddresses.get(name);
    }

    public List<CExecutable> getCurrentFunctionInstructions() {
        return currentFunctionInstructions;
    }

    public void addInstruction(CExecutable instr) {
        currentFunctionInstructions.add(instr);
    }

    public boolean hasVariable(String name) {
        return variableTypes.containsKey(name);
    }

    public void setParent(CompilerContextPart parent) {
        this.parent = parent;
    }

    public CompilerContextPart getParent() {
        return parent;
    }
}
