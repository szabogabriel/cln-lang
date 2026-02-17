package org.clnlang.interpreted;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.RuntimeConfiguration;
import org.clnlang.exception.ClnException;
import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;
import org.clnlang.interpreted.compile.declaration.ProgramImpl;
import org.clnlang.interpreted.lib.StandardLibrary;
import org.clnlang.interpreted.linker.Linker;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.interpreted.runtime.context.ExecutionContext;
import org.clnlang.interpreted.runtime.execution.FunctionInvoker;
import org.clnlang.interpreted.runtime.execution.Registry;
import org.clnlang.interpreted.startup.StartupContext;

/**
 * Embeddable runtime entry point for executing CLN programs.
 * Provides API methods that do not call System.exit or write to stdout/stderr.
 */
public final class ClnRuntime {

    public ClnRuntime() {
    }

    /**
     * Execute a CLN program using the provided configuration.
     * Registers the standard library by default.
     *
     * @param config Runtime configuration
     * @return Exit code from the CLN main function
     * @throws Exception if execution fails
     */
    public static int execute(RuntimeConfiguration config) throws Exception {
        return execute(config, null, true, null);
    }

    /**
     * Execute a CLN program using the provided configuration and registry.
     * Registers the standard library by default.
     *
     * @param config Runtime configuration
     * @param registry Custom registry (optional)
     * @return Exit code from the CLN main function
     * @throws Exception if execution fails
     */
    public static int execute(RuntimeConfiguration config, Registry registry) throws Exception {
        return execute(config, registry, true, null);
    }

    /**
     * Execute a CLN program using the provided configuration and registry.
     *
     * @param config Runtime configuration
     * @param registry Custom registry (optional)
     * @param registerStdLib Whether to register the standard library
     * @param logger Optional logger for verbose messages
     * @return Exit code from the CLN main function
     * @throws Exception if execution fails
     */
    public static int execute(RuntimeConfiguration config,
                              Registry registry,
                              boolean registerStdLib,
                              Consumer<String> logger) throws Exception {
        boolean verbose = config.isVerbose();
        Registry actualRegistry = registry != null ? registry : new Registry();

        if (registerStdLib) {
            log(logger, verbose, "Registering standard library...");
            StandardLibrary stdlib = new StandardLibrary();
            stdlib.registerAll(actualRegistry);
            log(logger, verbose, "Standard library registered (" + stdlib.getComponentCount() + " components).");
        }

        StartupContext startupContext = new StartupContext(config, actualRegistry, verbose);
        startupContext.initialize();
        startupContext.prepareExecutionContext();

        FunctionDeclImpl mainFunction = startupContext.findMainFunction();
        ExecutionContext context = startupContext.getExecutionContext();

        if (verbose) {
            log(logger, true, "=== Program Execution ===");
        }

        return executeMainFunction(context, mainFunction, logger, verbose);
    }

    private static int executeMainFunction(ExecutionContext context,
                                          FunctionDeclImpl mainFunction,
                                          Consumer<String> logger,
                                          boolean verbose) throws Exception {
        log(logger, verbose, "Executing main function...");

        Object result = FunctionInvoker.invoke(mainFunction, new ArrayList<>(), context);

        log(logger, verbose, "Main function execution completed.");

        if (result instanceof Long) {
            long exitCode = (Long) result;
            if (verbose) {
                log(logger, true, "Main function returned: " + result);
            }
            log(logger, verbose, "Exit code: " + exitCode);
            return (int) exitCode;
        }

        log(logger, verbose, "Exit code: 0");
        return 0;
    }

    /**
     * Execute an inline CLN script.
     * Registers the standard library by default.
     *
     * @param script Inline CLN source code
     * @return Exit code from the CLN main function
     * @throws Exception if execution fails
     */
    public static int executeInline(String script) throws Exception {
        RuntimeConfiguration config = new RuntimeConfiguration();
        return executeInline(script, config, null, true, null);
    }

    /**
     * Execute an inline CLN script with full control over registry and logging.
     *
     * @param script Inline CLN source code
     * @param config Runtime configuration (verbose flag is respected)
     * @param registry Custom registry (optional)
     * @param registerStdLib Whether to register the standard library
     * @param logger Optional logger for verbose messages
     * @return Exit code from the CLN main function
     * @throws Exception if execution fails
     */
    public static int executeInline(String script,
                                    RuntimeConfiguration config,
                                    Registry registry,
                                    boolean registerStdLib,
                                    Consumer<String> logger) throws Exception {
        boolean verbose = config.isVerbose();
        Registry actualRegistry = registry != null ? registry : new Registry();

        if (registerStdLib) {
            log(logger, verbose, "Registering standard library...");
            StandardLibrary stdlib = new StandardLibrary();
            stdlib.registerAll(actualRegistry);
            log(logger, verbose, "Standard library registered (" + stdlib.getComponentCount() + " components).");
        }

        ProgramImpl program = compileInlineProgram(script);
        String declaredPackage = getDeclaredPackage(program);

        registerProgramSymbols(program, declaredPackage, actualRegistry);

        ExecutionContext context = new ExecutionContext();
        program.populateContext(context);

        Linker linker = new Linker();
        linker.resolveImports(context, actualRegistry);

        if (verbose) {
            log(logger, true, "=== Program Execution ===");
        }

        FunctionDeclImpl mainFunction = findMainFunction(context);
        return executeMainFunction(context, mainFunction, logger, verbose);
    }

    private static void log(Consumer<String> logger, boolean verbose, String message) {
        if (verbose && logger != null) {
            logger.accept(message);
        }
    }

    private static ProgramImpl compileInlineProgram(String script) throws Exception {
        if (script == null || script.trim().isEmpty()) {
            throw new ClnException("Inline script is empty");
        }

        CharStream input = CharStreams.fromString(script);
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        clnParser.ProgramContext programContext = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new ClnException("Parsing failed for inline script with " +
                parser.getNumberOfSyntaxErrors() + " errors.");
        }

        org.clnlang.ast.visitor.itnerpreted.CompilerVisitor compiler = new org.clnlang.ast.visitor.itnerpreted.CompilerVisitor();
        return compiler.compileProgram(programContext);
    }

    private static String getDeclaredPackage(ProgramImpl program) {
        if (program.getPackageDecl() != null && program.getPackageDecl().getPackageName() != null) {
            return program.getPackageDecl().getPackageName();
        }
        return "default";
    }

    private static void registerProgramSymbols(ProgramImpl program, String packageName, Registry registry) {
        for (var decl : program.getDeclarations()) {
            if (decl instanceof FunctionDeclImpl) {
                FunctionDeclImpl funcDecl = (FunctionDeclImpl) decl;
                funcDecl.setPackageName(packageName);
                registry.registerFunction(
                    new org.clnlang.interpreted.runtime.types.FullyQualifiedName(packageName, funcDecl.getName()),
                    funcDecl
                );
            } else if (decl instanceof org.clnlang.interpreted.compile.declaration.StructDeclImpl) {
                org.clnlang.interpreted.compile.declaration.StructDeclImpl structDecl =
                    (org.clnlang.interpreted.compile.declaration.StructDeclImpl) decl;
                registry.registerStructType(
                    new org.clnlang.interpreted.runtime.types.FullyQualifiedName(packageName, structDecl.getName()),
                    structDecl.toStructDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.interpreted.compile.declaration.UnionDeclImpl) {
                org.clnlang.interpreted.compile.declaration.UnionDeclImpl unionDecl =
                    (org.clnlang.interpreted.compile.declaration.UnionDeclImpl) decl;
                registry.registerUnionType(
                    new org.clnlang.interpreted.runtime.types.FullyQualifiedName(packageName, unionDecl.getName()),
                    unionDecl.toUnionDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.interpreted.compile.declaration.GlobalVarDeclImpl) {
                org.clnlang.interpreted.compile.declaration.GlobalVarDeclImpl varDecl =
                    (org.clnlang.interpreted.compile.declaration.GlobalVarDeclImpl) decl;
                varDecl.setPackageName(packageName);
                if (varDecl.isMutable()) {
                    registry.registerGlobalVariable(
                        new org.clnlang.interpreted.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                } else {
                    registry.registerGlobalConstant(
                        new org.clnlang.interpreted.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                }
            }
        }
    }

    private static FunctionDeclImpl findMainFunction(ExecutionContext context) throws ClnException {
        Map<String, FunctionDeclImpl> allFunctions = context.getGlobalContext().getAllFunctions();

        List<FunctionDeclImpl> mainFunctions = allFunctions.entrySet().stream()
            .filter(entry -> entry.getKey().equals("main"))
            .map(Map.Entry::getValue)
            .toList();

        if (mainFunctions.isEmpty()) {
            throw new ClnException("No 'main' function found in inline script.");
        }

        if (mainFunctions.size() > 1) {
            throw new ClnException("Multiple 'main' functions found. There should be exactly one main function.");
        }

        FunctionDeclImpl mainFunction = mainFunctions.get(0);
        if (mainFunction.getParameters() != null && !mainFunction.getParameters().isEmpty()) {
            throw new ClnException("Main function should not have parameters");
        }

        return mainFunction;
    }
}
