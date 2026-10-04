package org.clnlang.compreg;

import java.util.ArrayList;
import java.util.List;

import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.compreg.runtime.RegisterBank;

public class CurrentExecution {

    private String currentPackage = "";
    private final List<CompiledFunction> functions = new ArrayList<>();
    private final List<Command> globalInitializers = new ArrayList<>();
    private Memory.FrameLayout globalFrameLayout = Memory.FrameLayout.empty();

    public void setCurrentPackage(String currentPackage) {
        if (currentPackage != null) {
            this.currentPackage = currentPackage;
        }
    }

    public String getCurrentPackage() {
        return currentPackage;
    }

    public void addFunction(CompiledFunction function) {
        if (function != null) {
            functions.add(function);
        }
    }

    public CompiledFunction getFunction(String name) {
        return getFunction(currentPackage, name);
    }

    public CompiledFunction getFunction(String packageName, String name) {
        for (CompiledFunction function : functions) {
            if (function.getPackageName().equals(packageName) && function.getName().equals(name)) {
                return function;
            }
        }
        return null;
    }

    public List<CompiledFunction> getFunctions() {
        return List.copyOf(functions);
    }

    public void addGlobalInitializer(Command command) {
        if (command != null) {
            globalInitializers.add(command);
        }
    }

    public void setGlobalFrameLayout(Memory.FrameLayout globalFrameLayout) {
        this.globalFrameLayout = globalFrameLayout;
    }

    public List<Command> getGlobalInitializers() {
        return List.copyOf(globalInitializers);
    }

    public void executeGlobalInitializers(Memory memory) {
        memory.setRootFrameLayout(globalFrameLayout);
        for (Command initializer : globalInitializers) {
            initializer.execute(memory);
        }
    }

    public long executeMain(Memory memory) {
        executeGlobalInitializers(memory);
        CompiledFunction main = getFunction("main");
        if (main == null) {
            throw new IllegalStateException("No main function was compiled in package '" + currentPackage + "'.");
        }
        if (!main.getParameters().isEmpty() || main.getReturnValues().size() != 1
                || !main.getReturnValues().get(0).getType().equals("int")) {
            throw new IllegalStateException("main must take no arguments and return one int value.");
        }
        int resultOffset = memory.allocateInt();
        new Call(main, List.of(), List.of(new Call.Register(RegisterBank.INT, resultOffset))).execute(memory);
        return memory.getInt(resultOffset);
    }
}
