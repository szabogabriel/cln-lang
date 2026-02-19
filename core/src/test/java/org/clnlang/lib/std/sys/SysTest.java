package org.clnlang.lib.std.sys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for the Sys standard library class.
 */
class SysTest {

    private Sys sys;
    private ExecutionContext context;
    private Registry registry;

    @BeforeEach
    void setUp() {
        sys = new Sys();
        context = new ExecutionContext();
        registry = new Registry();
    }

    // ========== Time Functions Tests ==========

    @Test
    void testCurrentTimeMillis() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "currentTimeMillis"));
        assertNotNull(func, "currentTimeMillis function should be registered");

        // Record time before execution
        long beforeTime = System.currentTimeMillis();
        
        // Execute the function
        func.getBlock().execute(context);
        
        // Record time after execution
        long afterTime = System.currentTimeMillis();

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        Long result = (Long) returnValues.get(0);
        assertNotNull(result, "Returned time should not be null");
        assertTrue(result >= beforeTime, "Returned time should be >= time before execution");
        assertTrue(result <= afterTime, "Returned time should be <= time after execution");
    }

    @Test
    void testNanoTime() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "nanoTime"));
        assertNotNull(func, "nanoTime function should be registered");

        // Record time before execution
        long beforeTime = System.nanoTime();
        
        // Execute the function
        func.getBlock().execute(context);
        
        // Record time after execution
        long afterTime = System.nanoTime();

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        Long result = (Long) returnValues.get(0);
        assertNotNull(result, "Returned nano time should not be null");
        assertTrue(result >= beforeTime, "Returned nano time should be >= time before execution");
        assertTrue(result <= afterTime, "Returned nano time should be <= time after execution");
    }

    @Test
    void testNanoTimeIsDifferentFromCurrentTimeMillis() throws Exception {
        sys.register(registry);
        
        // Execute currentTimeMillis
        FunctionDeclImpl currentTimeMillisFunc = registry.getFunction(new FullyQualifiedName("std.sys", "currentTimeMillis"));
        currentTimeMillisFunc.getBlock().execute(context);
        Long millisResult = (Long) context.getReturnValues().get(0);
        
        // Create new context for next execution
        ExecutionContext context2 = new ExecutionContext();
        
        // Execute nanoTime
        FunctionDeclImpl nanoTimeFunc = registry.getFunction(new FullyQualifiedName("std.sys", "nanoTime"));
        nanoTimeFunc.getBlock().execute(context2);
        Long nanoResult = (Long) context2.getReturnValues().get(0);
        
        // Nano time should be much larger than millis time due to different epochs/scales
        assertTrue(Math.abs(nanoResult - millisResult) > 1000, 
            "Nano time and current time millis should use different scales");
    }

    // ========== Environment and System Properties Tests ==========

    @Test
    void testGetenv() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getenv"));
        assertNotNull(func, "getenv function should be registered");

        // Set a parameter (environment variable name)
        // Use PATH which should exist on all systems
        context.getLocalContext().setVariable("name", "PATH");
        
        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        String result = (String) returnValues.get(0);
        assertNotNull(result, "PATH environment variable should exist");
        assertFalse(result.isEmpty(), "PATH should not be empty");
    }

    @Test
    void testGetenvNonExistentVariable() throws Exception {
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getenv"));
        
        // Request a non-existent environment variable
        context.getLocalContext().setVariable("name", "THIS_VARIABLE_DOES_NOT_EXIST_12345");
        
        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        String result = (String) returnValues.get(0);
        assertNull(result, "Non-existent environment variable should return null");
    }

    @Test
    void testGetProperty() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getProperty"));
        assertNotNull(func, "getProperty function should be registered");

        // Set a parameter (system property key)
        context.getLocalContext().setVariable("key", "java.version");
        
        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        String result = (String) returnValues.get(0);
        assertNotNull(result, "java.version property should exist");
        assertFalse(result.isEmpty(), "java.version should not be empty");
    }

    @Test
    void testGetPropertyUserName() throws Exception {
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getProperty"));
        
        context.getLocalContext().setVariable("key", "user.name");
        func.getBlock().execute(context);

        List<Object> returnValues = context.getReturnValues();
        String result = (String) returnValues.get(0);
        assertNotNull(result, "user.name property should exist");
        assertFalse(result.isEmpty(), "user.name should not be empty");
    }

    @Test
    void testGetPropertyNonExistent() throws Exception {
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getProperty"));
        
        // Request a non-existent property
        context.getLocalContext().setVariable("key", "this.property.does.not.exist.12345");
        func.getBlock().execute(context);

        List<Object> returnValues = context.getReturnValues();
        String result = (String) returnValues.get(0);
        assertNull(result, "Non-existent property should return null");
    }

    @Test
    void testGetPropertyWithDefault() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getPropertyWithDefault"));
        assertNotNull(func, "getPropertyWithDefault function should be registered");

        // Set parameters
        context.getLocalContext().setVariable("key", "java.version");
        context.getLocalContext().setVariable("defaultValue", "unknown");
        
        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        String result = (String) returnValues.get(0);
        assertNotNull(result, "Result should not be null");
        assertFalse(result.equals("unknown"), "Should return actual java.version, not default");
    }

    @Test
    void testGetPropertyWithDefaultReturnsDefault() throws Exception {
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "getPropertyWithDefault"));
        
        // Request a non-existent property with a default value
        context.getLocalContext().setVariable("key", "this.property.does.not.exist.12345");
        context.getLocalContext().setVariable("defaultValue", "myDefaultValue");
        
        func.getBlock().execute(context);

        List<Object> returnValues = context.getReturnValues();
        String result = (String) returnValues.get(0);
        assertEquals("myDefaultValue", result, "Should return the default value for non-existent property");
    }

    // ========== Memory Functions Tests ==========

    @Test
    void testFreeMemory() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "freeMemory"));
        assertNotNull(func, "freeMemory function should be registered");

        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        Long result = (Long) returnValues.get(0);
        assertNotNull(result, "Free memory should not be null");
        assertTrue(result >= 0, "Free memory should be non-negative");
    }

    @Test
    void testTotalMemory() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "totalMemory"));
        assertNotNull(func, "totalMemory function should be registered");

        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        Long result = (Long) returnValues.get(0);
        assertNotNull(result, "Total memory should not be null");
        assertTrue(result > 0, "Total memory should be positive");
    }

    @Test
    void testMaxMemory() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "maxMemory"));
        assertNotNull(func, "maxMemory function should be registered");

        // Execute the function
        func.getBlock().execute(context);

        // Verify return values
        List<Object> returnValues = context.getReturnValues();
        assertNotNull(returnValues, "Return values should not be null");
        assertEquals(1, returnValues.size(), "Should return exactly one value");
        
        Long result = (Long) returnValues.get(0);
        assertNotNull(result, "Max memory should not be null");
        assertTrue(result > 0, "Max memory should be positive");
    }

    @Test
    void testMemoryRelationship() throws Exception {
        sys.register(registry);
        
        // Get all memory values
        FunctionDeclImpl freeMemFunc = registry.getFunction(new FullyQualifiedName("std.sys", "freeMemory"));
        freeMemFunc.getBlock().execute(context);
        Long freeMemory = (Long) context.getReturnValues().get(0);
        
        ExecutionContext context2 = new ExecutionContext();
        
        FunctionDeclImpl totalMemFunc = registry.getFunction(new FullyQualifiedName("std.sys", "totalMemory"));
        totalMemFunc.getBlock().execute(context2);
        Long totalMemory = (Long) context2.getReturnValues().get(0);
        
        ExecutionContext context3 = new ExecutionContext();
        
        FunctionDeclImpl maxMemFunc = registry.getFunction(new FullyQualifiedName("std.sys", "maxMemory"));
        maxMemFunc.getBlock().execute(context3);
        Long maxMemory = (Long) context3.getReturnValues().get(0);
        
        // Verify logical relationships
        assertTrue(freeMemory <= totalMemory, "Free memory should be <= total memory");
        assertTrue(totalMemory <= maxMemory, "Total memory should be <= max memory");
    }

    @Test
    void testGc() throws Exception {
        // Register and get the function
        sys.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.sys", "gc"));
        assertNotNull(func, "gc function should be registered");

        // Get free memory before GC
        FunctionDeclImpl freeMemFunc = registry.getFunction(new FullyQualifiedName("std.sys", "freeMemory"));
        freeMemFunc.getBlock().execute(context);
        Long freeMemoryBefore = (Long) context.getReturnValues().get(0);
        
        // Create new context for GC execution
        ExecutionContext context2 = new ExecutionContext();
        
        // Execute GC (this should not throw an exception)
        func.getBlock().execute(context2);
        
        // Verify that gc() doesn't return any value
        List<Object> returnValues = context2.getReturnValues();
        // Since gc() doesn't set return values, it might be null or have default values
        assertTrue(returnValues == null || returnValues.isEmpty() || returnValues.get(0) == null, 
            "gc() should not return a meaningful value");
        
        // Get free memory after GC in a new context
        ExecutionContext context3 = new ExecutionContext();
        freeMemFunc.getBlock().execute(context3);
        Long freeMemoryAfter = (Long) context3.getReturnValues().get(0);
        
        // We can't reliably test that GC freed memory, but we can verify the function executed
        assertNotNull(freeMemoryAfter, "Should be able to get free memory after GC");
    }

    // ========== Registration Tests ==========

    @Test
    void testRegisterAllFunctions() {
        sys.register(registry);
        
        // Verify all time functions are registered
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "currentTimeMillis")), 
            "currentTimeMillis should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "nanoTime")), 
            "nanoTime should be registered");
        
        // Verify process control functions are registered
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "exit")), 
            "exit should be registered");
        
        // Verify environment/property functions are registered
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "getenv")), 
            "getenv should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "getProperty")), 
            "getProperty should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "getPropertyWithDefault")), 
            "getPropertyWithDefault should be registered");
        
        // Verify memory functions are registered
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "freeMemory")), 
            "freeMemory should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "totalMemory")), 
            "totalMemory should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "maxMemory")), 
            "maxMemory should be registered");
        assertTrue(registry.hasFunction(new FullyQualifiedName("std.sys", "gc")), 
            "gc should be registered");
    }

    @Test
    void testFunctionParameters() {
        sys.register(registry);
        
        // Test exit function has correct parameter
        FunctionDeclImpl exitFunc = registry.getFunction(new FullyQualifiedName("std.sys", "exit"));
        assertNotNull(exitFunc);
        assertEquals(1, exitFunc.getParameters().size(), "exit should have 1 parameter");
        assertEquals("code", exitFunc.getParameters().get(0).getName(), "exit parameter should be named 'code'");
        
        // Test getenv function has correct parameter
        FunctionDeclImpl getenvFunc = registry.getFunction(new FullyQualifiedName("std.sys", "getenv"));
        assertNotNull(getenvFunc);
        assertEquals(1, getenvFunc.getParameters().size(), "getenv should have 1 parameter");
        assertEquals("name", getenvFunc.getParameters().get(0).getName(), "getenv parameter should be named 'name'");
        
        // Test getProperty function has correct parameter
        FunctionDeclImpl getPropFunc = registry.getFunction(new FullyQualifiedName("std.sys", "getProperty"));
        assertNotNull(getPropFunc);
        assertEquals(1, getPropFunc.getParameters().size(), "getProperty should have 1 parameter");
        assertEquals("key", getPropFunc.getParameters().get(0).getName(), "getProperty parameter should be named 'key'");
        
        // Test getPropertyWithDefault function has correct parameters
        FunctionDeclImpl getPropDefaultFunc = registry.getFunction(new FullyQualifiedName("std.sys", "getPropertyWithDefault"));
        assertNotNull(getPropDefaultFunc);
        assertEquals(2, getPropDefaultFunc.getParameters().size(), "getPropertyWithDefault should have 2 parameters");
        assertEquals("key", getPropDefaultFunc.getParameters().get(0).getName(), "First parameter should be 'key'");
        assertEquals("defaultValue", getPropDefaultFunc.getParameters().get(1).getName(), "Second parameter should be 'defaultValue'");
        
        // Test that time functions have no parameters
        FunctionDeclImpl currentTimeMillisFunc = registry.getFunction(new FullyQualifiedName("std.sys", "currentTimeMillis"));
        assertNotNull(currentTimeMillisFunc);
        assertEquals(0, currentTimeMillisFunc.getParameters().size(), "currentTimeMillis should have no parameters");
        
        FunctionDeclImpl nanoTimeFunc = registry.getFunction(new FullyQualifiedName("std.sys", "nanoTime"));
        assertNotNull(nanoTimeFunc);
        assertEquals(0, nanoTimeFunc.getParameters().size(), "nanoTime should have no parameters");
        
        // Test that memory functions have no parameters
        FunctionDeclImpl freeMemFunc = registry.getFunction(new FullyQualifiedName("std.sys", "freeMemory"));
        assertNotNull(freeMemFunc);
        assertEquals(0, freeMemFunc.getParameters().size(), "freeMemory should have no parameters");
    }

    @Test
    void testAllFunctionsAreExposed() {
        sys.register(registry);
        
        // All functions in Sys should be marked as exposed
        FunctionDeclImpl currentTimeMillisFunc = registry.getFunction(new FullyQualifiedName("std.sys", "currentTimeMillis"));
        assertTrue(currentTimeMillisFunc.isExposed(), "currentTimeMillis should be exposed");
        
        FunctionDeclImpl nanoTimeFunc = registry.getFunction(new FullyQualifiedName("std.sys", "nanoTime"));
        assertTrue(nanoTimeFunc.isExposed(), "nanoTime should be exposed");
        
        FunctionDeclImpl exitFunc = registry.getFunction(new FullyQualifiedName("std.sys", "exit"));
        assertTrue(exitFunc.isExposed(), "exit should be exposed");
        
        FunctionDeclImpl getenvFunc = registry.getFunction(new FullyQualifiedName("std.sys", "getenv"));
        assertTrue(getenvFunc.isExposed(), "getenv should be exposed");
        
        FunctionDeclImpl gcFunc = registry.getFunction(new FullyQualifiedName("std.sys", "gc"));
        assertTrue(gcFunc.isExposed(), "gc should be exposed");
    }
}
