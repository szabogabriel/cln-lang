package org.clnlang.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.RuntimeConfiguration;
import org.clnlang.ast.visitor.CompilerVisitor;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.ProgramImpl;
import org.clnlang.exception.ClnException;
import org.clnlang.linker.Linker;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.persistance.ClnLoader;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;

/**
 * Manages the startup process for CLN applications.
 * Determines whether to start from specific files or a package,
 * loads all necessary files into an index, and prepares the execution context.
 */
public class StartupContext {
    private final RuntimeConfiguration config;
    private final ExecutionContext executionContext;
    private final Registry registry;
    private final Linker linker;
    private final boolean verbose;
    
    // Startup mode
    private ClnLoader.StartupMode mode;
    private List<File> targetFiles;
    private String targetPackage;
    
    public enum StartupMode {
        FILES,      // Start from specific .cln files (must be in default package)
        PACKAGE     // Start from a package name
    }
    
    public StartupContext(RuntimeConfiguration config, Registry registry, boolean verbose) {
        this.config = config;
        this.executionContext = new ExecutionContext();
        this.registry = registry;
        this.linker = new Linker();
        this.verbose = verbose;
    }
    
    /**
     * Initialize the startup context by loading all files and determining mode.
     * 
     * @throws Exception If initialization fails
     */
    public void initialize() throws Exception {
        log("Initializing startup context...");
        
        // Step 1: Load ALL .cln files from source paths using the loader
        config.getClnLoader().loadSources(registry);
        
        // Step 2: Determine startup mode
        determineStartupMode();
        
        log("Startup context initialized. Mode: " + mode);
    }
    
    /**
     * Determine the startup mode based on runtime configuration.
     */
    private void determineStartupMode() throws Exception {
        ClnLoader loader = config.getClnLoader();
        List<String> sourceFiles = loader.getSourceFiles();
        
        if (sourceFiles.isEmpty()) {
            throw new ClnException("No source files or package specified");
        }
        
        // Get the mode from the loader
        mode = loader.getSupportedStartupMode();
        
        if (mode == ClnLoader.StartupMode.FILES) {
            // File-based startup
            targetFiles = new ArrayList<>();
            
            for (String sourceFile : sourceFiles) {
                if (!sourceFile.endsWith(".cln")) {
                    throw new ClnException("Mixed file and package arguments not supported");
                }
                File file = new File(sourceFile);
                if (!file.exists()) {
                    throw new ClnException("File not found: " + sourceFile);
                }
                targetFiles.add(file);
            }
            
            log("Startup mode: FILES (" + targetFiles.size() + " files)");
        } else {
            // Package-based startup
            if (sourceFiles.size() > 1) {
                throw new ClnException("Only one package can be specified for startup");
            }
            
            targetPackage = sourceFiles.get(0);
            
            // Validate that the package exists in the registry
            if (!registry.hasPackage(targetPackage)) {
                throw new ClnException("Package not found: " + targetPackage);
            }
            
            log("Startup mode: PACKAGE (" + targetPackage + ")");
        }
    }
    
    /**
     * Prepare the execution context by populating it with the appropriate programs
     * based on the startup mode.
     * 
     * @throws Exception If preparation fails
     */
    public void prepareExecutionContext() throws Exception {
        log("Preparing execution context...");
        
        if (mode == ClnLoader.StartupMode.FILES) {
            prepareForFileStartup();
        } else {
            prepareForPackageStartup();
        }
        
        // Resolve imports
        log("Resolving imports...");
        linker.resolveImports(executionContext, registry);
        log("Imports resolved.");
    }
    
    /**
     * Prepare execution context for file-based startup.
     */
    private void prepareForFileStartup() throws Exception {
        log("Preparing for file-based startup...");
        
        // All files must be in the default package (no package declaration)
        for (File file : targetFiles) {
            ProgramImpl program = registry.getProgram(file);
            
            // If file not in registry, compile and add it
            if (program == null) {
                log("File not in registry, compiling: " + file.getName());
                program = compileFile(file);
                
                String declaredPackage = "default";
                if (program.getPackageDecl() != null && 
                    program.getPackageDecl().getPackageName() != null &&
                    !program.getPackageDecl().getPackageName().isEmpty()) {
                    declaredPackage = program.getPackageDecl().getPackageName();
                }
                
                registry.addProgram(file, program, declaredPackage);
                registerProgramSymbols(program, declaredPackage);
            }
            
            if (program.getPackageDecl() != null && 
                !program.getPackageDecl().getPackageName().isEmpty()) {
                throw new ClnException(
                    "File-based startup requires files in default package (no package declaration). " +
                    "File " + file.getName() + " declares package: " + 
                    program.getPackageDecl().getPackageName());
            }
            
            // Populate context with this program
            program.populateContext(executionContext);
        }
        
        log("Loaded " + targetFiles.size() + " files into execution context");
    }
    
    /**
     * Prepare execution context for package-based startup.
     */
    private void prepareForPackageStartup() throws Exception {
        log("Preparing for package-based startup...");
        
        // Load all programs from the target package
        Map<File, ProgramImpl> packagePrograms = registry.getPackagePrograms(targetPackage);
        
        for (ProgramImpl program : packagePrograms.values()) {
            program.populateContext(executionContext);
        }
        
        log("Loaded " + packagePrograms.size() + " files from package " + targetPackage);
    }
    
    /**
     * Find the main function in the execution context.
     * Ensures there is exactly one main function.
     * 
     * @return The main function
     * @throws ClnException If no main function found or multiple main functions found
     */
    public FunctionDeclImpl findMainFunction() throws ClnException {
        log("Searching for main function...");
        
        Map<String, FunctionDeclImpl> allFunctions = executionContext.getGlobalContext().getAllFunctions();
        
        // Find all main functions
        List<FunctionDeclImpl> mainFunctions = allFunctions.entrySet().stream()
            .filter(entry -> entry.getKey().equals("main"))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
        
        if (mainFunctions.isEmpty()) {
            String errorMsg = mode == ClnLoader.StartupMode.FILES 
                ? "No 'main' function found in the specified files."
                : "No 'main' function found in package: " + targetPackage;
            throw new ClnException(errorMsg);
        }
        
        if (mainFunctions.size() > 1) {
            String errorMsg = "Multiple 'main' functions found. There should be exactly one main function.";
            throw new ClnException(errorMsg);
        }
        
        FunctionDeclImpl mainFunction = mainFunctions.get(0);
        
        // Validate main function has no parameters
        if (mainFunction.getParameters() != null && !mainFunction.getParameters().isEmpty()) {
            throw new ClnException("Main function should not have parameters");
        }
        
        log("Found main function");
        return mainFunction;
    }
    
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }
    
    public ClnLoader.StartupMode getMode() {
        return mode;
    }
    
    public Registry getRegistry() {
        return registry;
    }
    
    /**
     * Compile a single .cln file into a ProgramImpl.
     * Used for on-demand compilation of files not in the configured paths.
     */
    private ProgramImpl compileFile(File file) throws Exception {
        CharStream input = CharStreams.fromFileName(file.getAbsolutePath());
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        clnParser.ProgramContext programContext = parser.program();
        
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new ClnException("Parsing failed for " + file.getName() + 
                " with " + parser.getNumberOfSyntaxErrors() + " errors.");
        }
        
        CompilerVisitor compiler = new CompilerVisitor();
        return compiler.compileProgram(programContext);
    }
    
    /**
     * Register all symbols (functions, types, variables) from a program into the registry.
     * Used for on-demand registration of files not in the configured paths.
     */
    private void registerProgramSymbols(ProgramImpl program, String packageName) {
        for (var decl : program.getDeclarations()) {
            if (decl instanceof FunctionDeclImpl) {
                FunctionDeclImpl funcDecl = (FunctionDeclImpl) decl;
                funcDecl.setPackageName(packageName);
                registry.registerFunction(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, funcDecl.getName()),
                    funcDecl
                );
            } else if (decl instanceof org.clnlang.compile.declaration.StructDeclImpl) {
                org.clnlang.compile.declaration.StructDeclImpl structDecl = 
                    (org.clnlang.compile.declaration.StructDeclImpl) decl;
                registry.registerStructType(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, structDecl.getName()),
                    structDecl.toStructDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.compile.declaration.UnionDeclImpl) {
                org.clnlang.compile.declaration.UnionDeclImpl unionDecl = 
                    (org.clnlang.compile.declaration.UnionDeclImpl) decl;
                registry.registerUnionType(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, unionDecl.getName()),
                    unionDecl.toUnionDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.compile.declaration.GlobalVarDeclImpl) {
                org.clnlang.compile.declaration.GlobalVarDeclImpl varDecl = 
                    (org.clnlang.compile.declaration.GlobalVarDeclImpl) decl;
                varDecl.setPackageName(packageName);
                if (varDecl.isMutable()) {
                    registry.registerGlobalVariable(
                        new org.clnlang.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                } else {
                    registry.registerGlobalConstant(
                        new org.clnlang.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                }
            }
        }
    }
    
    private void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}
