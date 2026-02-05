package org.clnlang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.visitor.ASTPrinterVisitor;
import org.clnlang.ast.visitor.CompilerVisitor;
import org.clnlang.ast.visitor.DetailedASTPrinter;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.ProgramImpl;
import org.clnlang.exception.ClnException;
import org.clnlang.lib.StandardLibrary;
import org.clnlang.linker.Linker;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.FunctionInvoker;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.startup.StartupContext;

public class Main {
    private static boolean verbose = false;

    public static void main(String[] args) {
        try {
            int exitCode = run(args);
            System.exit(exitCode);
        } catch (ClnException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Main program logic that can be tested. Throws exceptions instead of calling System.exit().
     * Returns the exit code from the main function.
     */
    public static int run(String[] args) throws Exception {
        // Parse command-line arguments using RuntimeConfiguration
        RuntimeConfiguration config = new RuntimeConfiguration();
        try {
            config.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            RuntimeConfiguration.printUsage();
            throw new ClnException("Invalid command line arguments: " + e.getMessage());
        }
        
        // Set verbose flag from configuration
        verbose = config.isVerbose();
        
        // Create registry and register standard library
        Registry registry = new Registry();
        log("Registering standard library...");
        StandardLibrary stdlib = new StandardLibrary();
        stdlib.registerAll(registry);
        log("Standard library registered (" + stdlib.getComponentCount() + " components).");
        
        // Create startup context
        StartupContext startupContext = new StartupContext(config, registry, verbose);
        
        // Initialize startup context (loads all files into index, determines mode)
        startupContext.initialize();
        
        // Prepare execution context
        startupContext.prepareExecutionContext();
        
        // Find main function
        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        
        // Get execution context
        ExecutionContext context = startupContext.getExecutionContext();

        printProgramDetails();
        
        return executeMainFunction(context, mainFunction);
    }

    /**
     * Execute the main function of the program.
     * Retrieves the main function from the global context and invokes it with no arguments.
     * 
     * @param context The execution context containing the main function
     * @param mainFunction The main function to execute
     * @return The exit code from the main function (0 if none returned)
     * @throws Exception If execution fails
     */
    private static int executeMainFunction(ExecutionContext context, FunctionDeclImpl mainFunction) throws Exception {
        log("Executing main function...");
        
        // Invoke the main function with no arguments
        Object result = FunctionInvoker.invoke(mainFunction, new ArrayList<>(), context);
        
        log("Main function execution completed.");
        
        // If main function returns a value, use it as exit code
        if (result != null && result instanceof Long) {
            long exitCode = (Long) result;
            if (verbose) {
                System.out.println("Main function returned: " + result);
            }
            log("Exit code: " + exitCode);
            return (int) exitCode;
        } else {
            log("Exit code: 0");
            return 0;
        }
    }
    
    private static void printProgramDetails() {
        if (verbose) {
            System.out.println("\n=== Program Execution ===");
        }
    }

    private static void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}