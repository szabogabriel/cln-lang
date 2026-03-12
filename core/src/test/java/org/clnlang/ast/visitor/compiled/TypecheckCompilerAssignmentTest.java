package org.clnlang.ast.visitor.compiled;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Unit tests for variable assignment and initialization in TypecheckCompilerVisitor.
 */
public class TypecheckCompilerAssignmentTest {
    
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
    
    @Test
    public void testVariableInitializationLiteral() {
        String source = """
            func test() int {
                var int x = 42;
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(42L, result);
    }
    
    @Test
    public void testVariableInitializationExpression() {
        String source = """
            func test() int {
                var int x = 10 + 5;
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(15L, result);
    }
    
    @Test
    public void testSimpleAssignment() {
        String source = """
            func test() int {
                var int x = 10;
                x = 20;
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(20L, result);
    }
    
    @Test
    public void testAssignmentWithExpression() {
        String source = """
            func test() int {
                var int x = 10;
                x = x + 5;
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(15L, result);
    }
    
    @Test
    public void testMultipleAssignments() {
        String source = """
            func test() int {
                var int x = 1;
                x = x + 1;
                x = x * 2;
                x = x + 3;
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        // x = 1, x = 2, x = 4, x = 7
        assertEquals(7L, result);
    }
    
    @Test
    public void testStringAssignment() {
        String source = """
            func test() string {
                var string msg = "Hello";
                msg = "World";
                return msg;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        assertEquals(Types.STRING, test.getReturnTypes()[0]);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        String result = context.getCurrentLocalContext().getString(returnAddress);
        assertEquals("World", result);
    }
    
    @Test
    public void testBooleanAssignment() {
        String source = """
            func test() bool {
                var bool flag = true;
                flag = false;
                return flag;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        assertEquals(Types.BOOL, test.getReturnTypes()[0]);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        boolean result = context.getCurrentLocalContext().getBoolean(returnAddress);
        assertEquals(false, result);
    }
    
    @Test
    public void testMultipleVariables() {
        String source = """
            func test() int {
                var int a = 10;
                var int b = 20;
                var int c = 30;
                a = b;
                b = c;
                c = a;
                return a + b + c;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(1, functions.size());
        
        CFunction test = functions.get(0);
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        // a=10, b=20, c=30 -> a=20, b=30, c=20 -> return 20+30+20=70
        assertEquals(70L, result);
    }
    
    @Test
    public void testAssignmentWithFunctionCall() {
        String source = """
            func getValue() int {
                return 100;
            }
            
            func test() int {
                var int x = 0;
                x = getValue();
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(2, functions.size());
        
        CFunction test = functions.stream()
            .filter(f -> "test".equals(f.getName()))
            .findFirst()
            .orElseThrow();
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(100L, result);
    }
    
    @Test
    public void testInitializationWithFunctionCall() {
        String source = """
            func getValue() int {
                return 42;
            }
            
            func test() int {
                var int x = getValue();
                return x;
            }
            """;
        
        List<CFunction> functions = compileProgram(source);
        assertEquals(2, functions.size());
        
        CFunction test = functions.stream()
            .filter(f -> "test".equals(f.getName()))
            .findFirst()
            .orElseThrow();
        
        // Execute function
        ExecutionContext context = new ExecutionContext();
        context.pushLocalContext(test.getMemoryAllocatorDescription());
        
        CExecutable[] instructions = test.getInstructions();
        for (CExecutable instr : instructions) {
            instr.execute(context);
            if (context.getCurrentLocalContext().hasReturned()) {
                break;
            }
        }
        
        int returnAddress = test.getReturns()[0];
        long result = context.getCurrentLocalContext().getLong(returnAddress);
        assertEquals(42L, result);
    }
}
