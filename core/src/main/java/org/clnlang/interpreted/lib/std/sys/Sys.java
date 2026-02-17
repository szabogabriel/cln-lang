package org.clnlang.interpreted.lib.std.sys;

import java.util.Collections;

import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;
import org.clnlang.interpreted.lib.ClnFunction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;
import org.clnlang.interpreted.runtime.execution.Registry;
import org.clnlang.interpreted.runtime.types.FullyQualifiedName;

/**
 * System library providing access to system-level functionality.
 * Includes time functions, environment access, and process control.
 */
public class Sys implements ClnFunction {

    private final String packageName = "std.sys";

    // ========== Time Functions ==========

    /**
     * Returns the current time in milliseconds since the Unix epoch (January 1, 1970 UTC).
     */
    private void executeCurrentTimeMillis(ExecutionContext context) {
        Long result = System.currentTimeMillis();
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Returns the current value of the running JVM's high-resolution time source, in nanoseconds.
     * This is useful for measuring elapsed time with high precision.
     */
    private void executeNanoTime(ExecutionContext context) {
        Long result = System.nanoTime();
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== Process Control Functions ==========

    /**
     * Terminates the currently running program with the specified exit code.
     * By convention, 0 indicates normal termination, non-zero indicates an error.
     */
    private void executeExit(ExecutionContext context) {
        Long code = (Long) context.getLocalContext().getValue("code");
        System.exit(code.intValue());
    }

    // ========== Environment and System Properties ==========

    /**
     * Gets the value of the specified environment variable.
     * Returns null if the variable is not defined.
     */
    private void executeGetenv(ExecutionContext context) {
        String name = (String) context.getLocalContext().getValue("name");
        String result = System.getenv(name);
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Gets the system property indicated by the specified key.
     * Returns null if there is no property with that key.
     * 
     * Common properties include:
     * - "user.name": User's account name
     * - "user.home": User's home directory
     * - "user.dir": Current working directory
     * - "os.name": Operating system name
     * - "os.version": Operating system version
     * - "java.version": Java Runtime Environment version
     */
    private void executeGetProperty(ExecutionContext context) {
        String key = (String) context.getLocalContext().getValue("key");
        String result = System.getProperty(key);
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Gets the system property indicated by the specified key, 
     * or returns the default value if the property is not found.
     */
    private void executeGetPropertyWithDefault(ExecutionContext context) {
        String key = (String) context.getLocalContext().getValue("key");
        String defaultValue = (String) context.getLocalContext().getValue("defaultValue");
        String result = System.getProperty(key, defaultValue);
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== Memory Functions ==========

    /**
     * Returns the amount of free memory in the Java Virtual Machine (in bytes).
     */
    private void executeFreeMemory(ExecutionContext context) {
        Long result = Runtime.getRuntime().freeMemory();
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Returns the total amount of memory in the Java Virtual Machine (in bytes).
     */
    private void executeTotalMemory(ExecutionContext context) {
        Long result = Runtime.getRuntime().totalMemory();
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Returns the maximum amount of memory that the Java Virtual Machine will 
     * attempt to use (in bytes).
     */
    private void executeMaxMemory(ExecutionContext context) {
        Long result = Runtime.getRuntime().maxMemory();
        context.setReturnValues(Collections.singletonList(result));
    }

    /**
     * Runs the garbage collector.
     * Calling this method suggests that the Java Virtual Machine expend effort 
     * toward recycling unused objects.
     */
    private void executeGc(ExecutionContext context) {
        System.gc();
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // Time functions
        FunctionDeclImpl currentTimeMillisFunc = new FunctionDeclImpl("currentTimeMillis", true);
        currentTimeMillisFunc.setBlock(this::executeCurrentTimeMillis);
        registry.registerFunction(new FullyQualifiedName(packageName, "currentTimeMillis"), currentTimeMillisFunc);

        FunctionDeclImpl nanoTimeFunc = new FunctionDeclImpl("nanoTime", true);
        nanoTimeFunc.setBlock(this::executeNanoTime);
        registry.registerFunction(new FullyQualifiedName(packageName, "nanoTime"), nanoTimeFunc);

        // Process control
        FunctionDeclImpl exitFunc = new FunctionDeclImpl("exit", true);
        exitFunc.addParameter("Long", "code");
        exitFunc.setBlock(this::executeExit);
        registry.registerFunction(new FullyQualifiedName(packageName, "exit"), exitFunc);

        // Environment and system properties
        FunctionDeclImpl getenvFunc = new FunctionDeclImpl("getenv", true);
        getenvFunc.addParameter("String", "name");
        getenvFunc.setBlock(this::executeGetenv);
        registry.registerFunction(new FullyQualifiedName(packageName, "getenv"), getenvFunc);

        FunctionDeclImpl getPropertyFunc = new FunctionDeclImpl("getProperty", true);
        getPropertyFunc.addParameter("String", "key");
        getPropertyFunc.setBlock(this::executeGetProperty);
        registry.registerFunction(new FullyQualifiedName(packageName, "getProperty"), getPropertyFunc);

        FunctionDeclImpl getPropertyWithDefaultFunc = new FunctionDeclImpl("getPropertyWithDefault", true);
        getPropertyWithDefaultFunc.addParameter("String", "key");
        getPropertyWithDefaultFunc.addParameter("String", "defaultValue");
        getPropertyWithDefaultFunc.setBlock(this::executeGetPropertyWithDefault);
        registry.registerFunction(new FullyQualifiedName(packageName, "getPropertyWithDefault"), getPropertyWithDefaultFunc);

        // Memory functions
        FunctionDeclImpl freeMemoryFunc = new FunctionDeclImpl("freeMemory", true);
        freeMemoryFunc.setBlock(this::executeFreeMemory);
        registry.registerFunction(new FullyQualifiedName(packageName, "freeMemory"), freeMemoryFunc);

        FunctionDeclImpl totalMemoryFunc = new FunctionDeclImpl("totalMemory", true);
        totalMemoryFunc.setBlock(this::executeTotalMemory);
        registry.registerFunction(new FullyQualifiedName(packageName, "totalMemory"), totalMemoryFunc);

        FunctionDeclImpl maxMemoryFunc = new FunctionDeclImpl("maxMemory", true);
        maxMemoryFunc.setBlock(this::executeMaxMemory);
        registry.registerFunction(new FullyQualifiedName(packageName, "maxMemory"), maxMemoryFunc);

        FunctionDeclImpl gcFunc = new FunctionDeclImpl("gc", true);
        gcFunc.setBlock(this::executeGc);
        registry.registerFunction(new FullyQualifiedName(packageName, "gc"), gcFunc);
    }
}
