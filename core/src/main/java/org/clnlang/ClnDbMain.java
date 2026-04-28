package org.clnlang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Library entry point for running CLN programs stored in an H2/JDBC database.
 * <p>
 * Unlike {@link Main}, this class never calls {@link System#exit}. All errors
 * propagate as exceptions and the CLN program's return value is surfaced as a
 * plain {@code int}, making it safe to embed in larger applications.
 *
 * <pre>{@code
 * ClnDbMain runner = new ClnDbMain(
 *     "jdbc:h2:./mydb",
 *     null,               // use default org.h2.Driver
 *     List.of("myapp"),
 *     false,
 *     System.out::println
 * );
 * int exitCode = runner.execute();
 * }</pre>
 */
public class ClnDbMain {

    private final String jdbcUrl;
    private final String driverClass;
    private final List<String> packages;
    private final boolean verbose;
    private final Consumer<String> logger;

    /**
     * Creates a new {@code ClnDbMain}.
     *
     * @param jdbcUrl     JDBC connection URL (must start with {@code jdbc:})
     * @param driverClass JDBC driver class to load explicitly, or {@code null} /
     *                    blank to use the default ({@code org.h2.Driver})
     * @param packages    one or more package names that identify the source to load;
     *                    the first package's {@code main()} function will be executed
     * @param verbose     whether to emit verbose diagnostic output via {@code logger}
     * @param logger      consumer for diagnostic/verbose messages; may be {@code null}
     * @throws IllegalArgumentException if {@code jdbcUrl} is null/blank or
     *                                  {@code packages} is null/empty
     */
    public ClnDbMain(String jdbcUrl, String driverClass, List<String> packages,
                     boolean verbose, Consumer<String> logger) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new IllegalArgumentException("jdbcUrl must not be null or blank");
        }
        if (packages == null || packages.isEmpty()) {
            throw new IllegalArgumentException("packages must contain at least one entry-point package");
        }
        this.jdbcUrl = jdbcUrl.trim();
        this.driverClass = driverClass;
        this.packages = List.copyOf(packages);
        this.verbose = verbose;
        this.logger = logger;
    }

    /**
     * Convenience constructor: uses the default JDBC driver, non-verbose, no logger.
     *
     * @param jdbcUrl  JDBC connection URL
     * @param packages one or more entry-point package names
     */
    public ClnDbMain(String jdbcUrl, List<String> packages) {
        this(jdbcUrl, null, packages, false, null);
    }

    /**
     * Executes the CLN program.
     * <p>
     * Loads all CLN sources from the database, resolves imports, and invokes the
     * {@code main()} function of the first package in the {@code packages} list.
     *
     * @return the exit code returned by the CLN {@code main()} function
     * @throws Exception if loading, compilation, linking, or execution fails
     */
    public int execute() throws Exception {
        RuntimeConfiguration config = buildConfig();
        return ClnRuntime.execute(config, null, true, logger);
    }

    private RuntimeConfiguration buildConfig() {
        List<String> args = new ArrayList<>();
        if (verbose) {
            args.add("-v");
        }
        args.add("-cp");
        args.add(jdbcUrl);
        if (driverClass != null && !driverClass.isBlank()) {
            args.add("-cdd");
            args.add(driverClass);
        }
        args.addAll(packages);

        RuntimeConfiguration config = new RuntimeConfiguration();
        config.parse(args.toArray(new String[0]));
        return config;
    }
}
