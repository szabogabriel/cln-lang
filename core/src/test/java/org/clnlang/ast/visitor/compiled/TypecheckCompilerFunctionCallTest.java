package org.clnlang.ast.visitor.compiled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.CFunction;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for TypecheckCompilerVisitor with focus on function calls and returns.
 */
public class TypecheckCompilerFunctionCallTest {
    
    private TypecheckCompilerVisitor visitor;
    private GlobalRegistry globalRegistry;
    private File testFile;
    
    @BeforeEach
    public void setUp() {
        globalRegistry = new GlobalRegistry();
        visitor = new TypecheckCompilerVisitor(globalRegistry);
        testFile = new File("test.cln");
    }
    
    /**
     * Helper method to compile a program source
     */
    private List<CFunction> compileProgram(String source) {
        clnLexer lexer = new clnLexer(CharStreams.fromString(source));
        clnParser parser = new clnParser(new CommonTokenStream(lexer));
        clnParser.ProgramContext parseTree = parser.program();
        
        visitor.compileProgram(parseTree, testFile);
        return visitor.getCompiledFunctions();
    }
    
    /**
     * Helper method to execute a function and get results
     */
    private ExecutionContext executeFunction(CFunction function, ExecutionContext context) {
        function.execute(context);
        return context;
    }
    
    @Test
    public void testSimpleReturnInt() {
        String source = """
            func getNumber() int {
                return 42;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction getNumber = functions.get(0);
        assertEquals("getNumber", getNumber.getName());
        assertEquals(0, getNumber.getParameters().length);
        assertEquals(1, getNumber.getReturns().length);
        assertEquals(Types.INT, getNumber.getReturnTypes()[0]);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(getNumber.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = getNumber.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
        
        // Check return value is in the return slot
        int returnAddress = getNumber.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(42L, result);
    }
    
    @Test
    public void testReturnWithExpression() {
        String source = """
            func add5() int {
                return 3 + 2;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction add5 = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(add5.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = add5.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
        
        int returnAddress = add5.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(5L, result);
    }
    
    @Test
    public void testFunctionWithParameter() {
        String source = """
            func double(x int) int {
                return x * 2;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction doubleFunc = functions.get(0);
        assertEquals("double", doubleFunc.getName());
        assertEquals(1, doubleFunc.getParameters().length);
        assertEquals(Types.INT, doubleFunc.getParameterTypes()[0]);
        assertEquals(1, doubleFunc.getReturns().length);
        assertEquals(Types.INT, doubleFunc.getReturnTypes()[0]);
        
        // Execute function with parameter value
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(doubleFunc.getMemoryAllocatorDescription());
        
        // Set parameter value
        int paramAddress = doubleFunc.getParameters()[0];
        context.getCurrentLocalContext().setLong(paramAddress, 21L);
        
        CExecutable[] instructions = doubleFunc.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
        
        int returnAddress = doubleFunc.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(42L, result);
    }
    
    @Test
    public void testFunctionCall() {
        String source = """
            func getValue() int {
                return 10;
            }
            
            func caller() int {
                return getValue();
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(2, functions.size());
        
        CFunction caller = functions.stream()
            .filter(f -> "caller".equals(f.getName()))
            .findFirst()
            .orElseThrow();
        
        // Execute caller function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(caller.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = caller.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = caller.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(10L, result);
    }
    
    @Test
    public void testFunctionCallWithArgument() {
        String source = """
            func addTen(x int) int {
                return x + 10;
            }
            
            func caller() int {
                return addTen(5);
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(2, functions.size());
        
        CFunction caller = functions.stream()
            .filter(f -> "caller".equals(f.getName()))
            .findFirst()
            .orElseThrow();
        
        // Execute caller function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(caller.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = caller.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = caller.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(15L, result);
    }
    
    @Test
    public void testFunctionCallInExpression() {
        String source = """
            func getTwo() int {
                return 2;
            }
            
            func getThree() int {
                return 3;
            }
            
            func caller() int {
                return getTwo() + getThree();
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(3, functions.size());
        
        CFunction caller = functions.stream()
            .filter(f -> "caller".equals(f.getName()))
            .findFirst()
            .orElseThrow();
        
        // Execute caller function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(caller.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = caller.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = caller.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(5L, result);
    }
    
    @Test
    public void testReturnString() {
        String source = """
            func getMessage() string {
                return "Hello";
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction getMessage = functions.get(0);
        assertEquals(Types.STRING, getMessage.getReturnTypes()[0]);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(getMessage.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = getMessage.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
        
        int returnAddress = getMessage.getReturns()[0];
        String result = context.getCurrentLocalContext().getString(returnAddress);
        assertEquals("Hello", result);
    }
    
    @Test
    public void testReturnBoolean() {
        String source = """
            func isTrue() bool {
                return true;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction isTrue = functions.get(0);
        assertEquals(Types.BOOL, isTrue.getReturnTypes()[0]);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(isTrue.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = isTrue.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
        }
        
        int returnAddress = isTrue.getReturns()[0];
        boolean result = context.getCurrentLocalContext().getBoolean(returnAddress);
        assertTrue(result);
    }
    
    @Test
    public void testFunctionRegistry() {
        String source = """
            func first() int {
                return 1;
            }
            
            func second() int {
                return 2;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(2, functions.size());
        
        // Check that functions are registered in context
        CompilerContext ctx = visitor.getCompilerContext();
        assertNotNull(ctx.getCompiledFunction("first"));
        assertNotNull(ctx.getCompiledFunction("second"));
        assertTrue(ctx.hasCompiledFunction("first"));
        assertTrue(ctx.hasCompiledFunction("second"));
    }
}
