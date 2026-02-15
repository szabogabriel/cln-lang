package org.clnlang.ast.visitor.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CompilerContext {

    // Global variable tracking
    private CompilerContextPart globalVariables = new CompilerContextPart();

    // Local variable tracking (reset per function)
    private Queue<CompilerContextPart> localVariableStack = new LinkedList<>();

    private String packageName = null;

    private Set<String> imports = new HashSet<>();
    
    private List<String> structNames = new ArrayList<>();
    private List<String> unionNames = new ArrayList<>();
    private List<String> functionNames = new ArrayList<>();

    // Track if we're currently in a function (for scoping)
    private boolean inFunction = false;

    /**
     * Reset local variable context when entering a new function.
     */
    public void newLocalContext() {
        localVariableStack.offer(new CompilerContextPart());
    }

    public void popLocalContext() {
        localVariableStack.poll();
    }

    public CompilerContextPart getCurrentLocalContext() {
        if (localVariableStack.isEmpty()) {
            throw new IllegalStateException("No local context available");
        }
        return localVariableStack.peek();
    }

    public CompilerContextPart getGlobalContext() {
        return globalVariables;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void addImport(String importName) {
        imports.add(importName);
    }

    public Set<String> getImports() {
        return imports;
    }

    public void addStructName(String name) {
        structNames.add(name);
    }

    public List<String> getStructNames() {
        return structNames;
    }

    public void addUnionName(String name) {
        unionNames.add(name);
    }

    public List<String> getUnionNames() {
        return unionNames;
    }

    public void addFunctionName(String name) {
        functionNames.add(name);
    }

    public List<String> getFunctionNames() {
        return functionNames;
    }

    public boolean isInFunction() {
        return inFunction;
    }

    public void setInFunction(boolean inFunction) {
        this.inFunction = inFunction;
    }
}
