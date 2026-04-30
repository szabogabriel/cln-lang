package org.clnlang.persistance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClnLoaderFactory {

    private ClnLoaderFactory() {
    }

    /**
     * Factory method to create a ClnLoader from environment and command line arguments.
     * Handles parsing of CLN_HOME, CLN_PATH, -cp arguments, and source files.
     *
     * <p>If {@code clnPathEnv} or {@code cpArg} starts with {@code jdbc:} the value is
     * treated as a JDBC URL and an {@link H2Loader} is returned instead of the default
     * {@link FileSystemLoader}.  The {@code dbDriver} parameter (or the
     * {@code CLN_DB_DRIVER} environment variable) supplies the driver class to load;
     * when both are absent the H2 default ({@value JdbcLoader#DEFAULT_DRIVER}) is used.
     *
     * @param clnHome    CLN_HOME environment variable value (can be null)
     * @param clnPathEnv CLN_PATH environment variable value (can be null)
     * @param cpArg      Command line -cp argument value (can be null)
     * @param sourceArgs Raw source file/package arguments
     * @param verbose    Whether to enable verbose logging
     * @param dbDriver   Explicit JDBC driver class (-cdd / CLN_DB_DRIVER); may be null
     * @return A ClnLoader instance configured with the parsed paths and sources
     */
    public static ClnLoader fromEnvironment(String clnHome, String clnPathEnv, String cpArg,
            List<String> sourceArgs, boolean verbose, String dbDriver) {

        // Detect JDBC URL before splitting by path separator (which would corrupt the URL
        // on Linux where ':' is both the path separator and part of every JDBC URL).
        String jdbcUrl = null;
        if (clnPathEnv != null && clnPathEnv.trim().startsWith("jdbc:")) {
            jdbcUrl = clnPathEnv.trim();
        } else if (cpArg != null && cpArg.trim().startsWith("jdbc:")) {
            jdbcUrl = cpArg.trim();
        }

        if (jdbcUrl != null) {
            return new JdbcLoader(jdbcUrl, dbDriver, sourceArgs, verbose);
        }

        // Fall through to file-system loader
        List<String> paths = new ArrayList<>();

        // Add CLN_HOME/lib if available
        if (clnHome != null && !clnHome.trim().isEmpty()) {
            String libPath = clnHome + File.separator + "lib";
            File libDir = new File(libPath);
            if (libDir.exists() && libDir.isDirectory()) {
                paths.add(libPath);
            }
        }

        // Parse CLN_PATH environment variable
        if (clnPathEnv != null && !clnPathEnv.trim().isEmpty()) {
            String[] envPaths = clnPathEnv.split(File.pathSeparator);
            for (String path : envPaths) {
                String trimmed = path.trim();
                if (!trimmed.isEmpty()) {
                    paths.add(trimmed);
                }
            }
        }

        // Parse -cp argument
        if (cpArg != null && !cpArg.trim().isEmpty()) {
            String[] cpPaths = cpArg.split(File.pathSeparator);
            for (String path : cpPaths) {
                String trimmed = path.trim();
                if (!trimmed.isEmpty()) {
                    paths.add(trimmed);
                }
            }
        }

        return new FileSystemLoader(paths, sourceArgs, verbose);
    }
}
