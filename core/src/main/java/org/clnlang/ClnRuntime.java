package org.clnlang;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.StandardLibrary;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.FunctionInvoker;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.startup.StartupContext;

/**
 * Embeddable runtime entry point for executing CLN programs.
 * Provides API methods that do not call System.exit or write to stdout/stderr.
 */
public final class ClnRuntime {

    private ClnRuntime() {
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

    private static void log(Consumer<String> logger, boolean verbose, String message) {
        if (verbose && logger != null) {
            logger.accept(message);
        }
    }
}
