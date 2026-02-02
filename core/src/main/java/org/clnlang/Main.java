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
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.runtime.FunctionInvoker;
import org.clnlang.runtime.Linker;
import org.clnlang.runtime.Registry;

public class Main {
    private static boolean verbose = false;
    private static clnParser.ProgramContext programContext;

    public static void main(String[] args) {
        try {
            run(args);
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
     */
    public static void run(String[] args) throws Exception {
        // Parse command-line arguments first
        verbose = false;  // Reset the static field
        String fileName = null;
        
        for (String arg : args) {
            if (arg.equals("-v") || arg.equals("--verbose")) {
                verbose = true;
            } else if (!arg.startsWith("-")) {
                fileName = arg;
            }
        }
        
        // Create execution context and populate it
        ExecutionContext context = new ExecutionContext();
        Linker linker = new Linker();
        Registry registry = new Registry();
        
        // Register standard library (after parsing args so log() respects verbose flag)
        log("Registering standard library...");
        StandardLibrary stdlib = new StandardLibrary();
        stdlib.registerAll(registry);
        log("Standard library registered (" + stdlib.getComponentCount() + " components).");
        
        fileName = handleSourceFileName(fileName);
        
        log("Loading file: " + fileName);
        ProgramImpl program = createProgram(fileName);
        log("Compilation completed successfully.");
        
        log("Populating program context...");
        program.populateContext(context);
        log("Program context populated.");

        log("Resolving imports...");
        linker.resolveImports(context, registry);
        log("Imports resolved.");

        // Check for main function
        if (!context.getGlobalContext().hasFunction("main")) {
            System.err.println("Error: No 'main' function found in the program.");
            throw new ClnException("No 'main' function found in the program.");
        }
        
        log("Found 'main' function.");

        printProgramDetails();
        
        executeMainFunction(context);
    }

    /**
     * Execute the main function of the program.
     * Retrieves the main function from the global context and invokes it with no arguments.
     * 
     * @param context The execution context containing the main function
     * @throws Exception If execution fails
     */
    private static void executeMainFunction(ExecutionContext context) throws Exception {
        log("Executing main function...");
        
        // Get the main function from the global context
        FunctionDeclImpl mainFunction = context.getGlobalContext().getFunction("main");
        
        if (mainFunction == null) {
            throw new ClnException("Main function not found in global context");
        }
        
        // Main function should have no parameters
        if (mainFunction.getParameters() != null && !mainFunction.getParameters().isEmpty()) {
            throw new ClnException("Main function should not have parameters");
        }
        
        // Invoke the main function with no arguments
        Object result = FunctionInvoker.invoke(mainFunction, new ArrayList<>(), context);
        
        log("Main function execution completed.");
        
        // If main function returns a value, it could be used as exit code
        if (result != null && verbose) {
            System.out.println("Main function returned: " + result);
        }
    }
    
    private static void printProgramDetails() {
        if (verbose) {
            System.out.println("Found 'main' function.");
            
            // Build AST using the visitor pattern (for verbose output)
            ClnASTBuilder astBuilder = new ClnASTBuilder();
            ProgramNode ast = (ProgramNode) astBuilder.visit(programContext);
            
            // Print the AST using toString()
            System.out.println("\n=== Abstract Syntax Tree (toString) ===");
            System.out.println(ast);
            
            // Print the AST using visitor pattern
            System.out.println("\n=== Abstract Syntax Tree (Visitor Pattern) ===");
            ASTPrinterVisitor printer = new ASTPrinterVisitor();
            ast.accept(printer);
            
            // Print detailed AST with statements and expressions
            System.out.println("\n=== Detailed AST (Statements & Expressions) ===");
            DetailedASTPrinter detailedPrinter = new DetailedASTPrinter();
            ast.accept(detailedPrinter);
        }
    }

    private static ProgramImpl createProgram(String fileName) throws Exception {
                // Create a CharStream from the input file
        CharStream input = CharStreams.fromFileName(fileName);
        
        // Create a lexer that feeds off of input CharStream
        clnLexer lexer = new clnLexer(input);
        
        // Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Create a parser that feeds off the tokens buffer
        clnParser parser = new clnParser(tokens);
        
        // Begin parsing at the program rule
        programContext = parser.program();
        
        // Check for syntax errors
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Parsing failed with " + parser.getNumberOfSyntaxErrors() + " errors.");
            throw new ClnException("Parsing failed with " + parser.getNumberOfSyntaxErrors() + " errors.");
        }
        
        if (verbose) {
            System.out.println("Parsing completed successfully.");
        }
        
        // Compile the program
        CompilerVisitor compiler = new CompilerVisitor();
        ProgramImpl program = compiler.compileProgram(programContext);
        return program;
    }
    
    private static String handleSourceFileName(String fileName) throws ClnException {
                // If no file specified, find first .cln file in current directory
        if (fileName == null) {
            fileName = findFirstClnFile();
            if (fileName == null) {
                System.err.println("Error: No .cln file found in current directory.");
                System.err.println("Usage: java -jar cln.jar [options] [file.cln]");
                System.err.println("Options:");
                System.err.println("  -v, --verbose    Enable verbose output");
                throw new ClnException("No .cln file found in current directory.");
            }
            if (verbose) {
                System.out.println("No file specified, using: " + fileName);
            }
        }
        
        // Verify file exists
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("Error: File not found: " + fileName);
            throw new ClnException("File not found: " + fileName);
        }

        return fileName;
    }

    /**
     * Find the first .cln file in the current working directory
     */
    private static String findFirstClnFile() {
        try {
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            try (Stream<Path> paths = Files.walk(currentDir, 1)) {
                List<Path> clnFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".cln"))
                    .collect(Collectors.toList());
                
                if (!clnFiles.isEmpty()) {
                    return clnFiles.get(0).toString();
                }
            }
        } catch (IOException e) {
            System.err.println("Error searching for .cln files: " + e.getMessage());
        }
        return null;
    }

    private static void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}