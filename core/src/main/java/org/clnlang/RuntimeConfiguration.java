package org.clnlang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.clnlang.persistance.ClnLoader;
import org.clnlang.persistance.ClnLoaderFactory;

/**
 * Runtime configuration class for parsing command line arguments.
 * Supports:
 * - Verbose flag: -v or --verbose
 * - CLN path: -cp or --cln_path followed by path(s) separated by File.pathSeparator
 * - Source files: .cln files or package definitions (e.g., myapp.main)
 * - Environment variables:
 *   - CLN_HOME: Home folder for out-of-the-box libraries (${CLN_HOME}/lib is added to paths)
 *   - CLN_PATH: Default library paths loaded at construction time
 */
public class RuntimeConfiguration {
    private boolean verbose;
    private String clnHome;
    private String cpArg; // Raw -cp argument
    private String dbDriverArg; // Raw -cdd argument
    private List<String> sourceArgs; // Raw source file/package arguments
    private ClnLoader clnLoader; // Cached loader instance
    
    /**
     * Creates a new RuntimeConfiguration with default values.
     * Reads CLN_HOME environment variable.
     */
    public RuntimeConfiguration() {
        this.verbose = false;
        this.sourceArgs = new ArrayList<>();
        this.clnHome = System.getenv("CLN_HOME");
        this.cpArg = null;
        this.dbDriverArg = null;
    }
    
    /**
     * Parses command line arguments and populates configuration.
     * 
     * @param args Command line arguments from main method
     * @throws IllegalArgumentException if arguments are invalid
     */
    public void parse(String[] args) throws IllegalArgumentException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            
            if (arg.equals("-v") || arg.equals("--verbose")) {
                this.verbose = true;
            } else if (arg.equals("-cp") || arg.equals("--cln_path")) {
                // Next argument should be the path(s)
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Option " + arg + " requires a path argument");
                }
                i++; // Move to next argument
                this.cpArg = args[i];
                // Invalidate cached loader since paths changed
                this.clnLoader = null;
            } else if (arg.equals("-cdd") || arg.equals("--cln-db-driver")) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Option " + arg + " requires a driver class argument");
                }
                i++;
                this.dbDriverArg = args[i];
                // Invalidate cached loader since driver changed
                this.clnLoader = null;
            } else if (!arg.startsWith("-")) {
                // This is a source file or package definition
                this.sourceArgs.add(arg);
            } else {
                throw new IllegalArgumentException("Unknown option: " + arg);
            }
        }
    }
    
    /**
     * Returns whether verbose mode is enabled.
     * 
     * @return true if verbose mode is enabled, false otherwise
     */
    public boolean isVerbose() {
        return verbose;
    }
    
    /**
     * Sets the verbose flag.
     * 
     * @param verbose true to enable verbose mode, false otherwise
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
    
    /**
     * Checks if any source files were specified.
     * 
     * @return true if at least one source file is specified, false otherwise
     */
    public boolean hasSourceFiles() {
        return !sourceArgs.isEmpty();
    }
    
    /**
     * Returns the CLN_HOME environment variable value.
     * 
     * @return CLN_HOME value or null if not set
     */
    public String getClnHome() {
        return clnHome;
    }
    
    /**
     * Sets the CLN_HOME value (mainly for testing purposes).
     * 
     * @param clnHome CLN_HOME path
     */
    public void setClnHome(String clnHome) {
        this.clnHome = clnHome;
    }
    
    /**
     * Prints usage information for the CLN interpreter.
     */
    public static void printUsage() {
        System.err.println("Usage: java -jar cln.jar [options] [source]");
        System.err.println();
        System.err.println("Options:");
        System.err.println("  -v, --verbose              Enable verbose output");
        System.err.println("  -cp, --cln_path <path>     Set path(s) for source files/libraries, or a JDBC URL");
        System.err.println("                             Multiple file-system paths separated by '" + File.pathSeparator + "'");
        System.err.println("  -cdd, --cln-db-driver <class>  JDBC driver class to load (database mode)");
        System.err.println("                             Overrides CLN_DB_DRIVER environment variable");
        System.err.println();
        System.err.println("Source:");
        System.err.println("  file.cln                   A CLN source file");
        System.err.println("  package.name               A package definition (e.g., myapp.main)");
        System.err.println("  file1.cln" + File.pathSeparator + "file2.cln     Multiple files separated by '" + File.pathSeparator + "'");
        System.err.println();
        System.err.println("Environment Variables:");
        System.err.println("  CLN_HOME                   Home folder for out-of-the-box libraries");
        System.err.println("                             Automatically adds ${CLN_HOME}/lib to search path");
        System.err.println("  CLN_PATH                   Default library paths or JDBC URL (used when -cp is not specified)");
        System.err.println("                             Multiple file-system paths separated by '" + File.pathSeparator + "'");
        System.err.println("  CLN_DB_DRIVER              JDBC driver class for database mode (default: org.h2.Driver)");
        System.err.println();
        System.err.println("Examples:");
        System.err.println("  java -jar cln.jar hello.cln");
        System.err.println("  java -jar cln.jar -v myapp.main");
        System.err.println("  java -jar cln.jar -cp /path/to/libs -v hello.cln");
        System.err.println("  export CLN_HOME=/opt/cln && java -jar cln.jar hello.cln");
    }
    
    /**
     * Creates and returns a ClnLoader instance configured with environment and command line paths.
     * Uses the factory method in ClnLoader to parse and configure paths and source files.
     * The loader is cached and reused unless paths are modified.
     * 
     * @return A ClnLoader instance for loading source files
     */
    public ClnLoader getClnLoader() {
        if (clnLoader == null) {
            String clnPathEnv = System.getenv("CLN_PATH");
            String dbDriverEnv = System.getenv("CLN_DB_DRIVER");
            String effectiveDbDriver = (dbDriverArg != null) ? dbDriverArg : dbDriverEnv;
            clnLoader = ClnLoaderFactory.fromEnvironment(clnHome, clnPathEnv, cpArg, sourceArgs, verbose, effectiveDbDriver);
        }
        return clnLoader;
    }
       
    /**
     * Returns a string representation of the configuration for debugging.
     * 
     * @return String representation
     */
    @Override
    public String toString() {
        return "RuntimeConfiguration{" +
                "verbose=" + verbose +
                ", cpArg='" + cpArg + '\'' +
                ", dbDriverArg='" + dbDriverArg + '\'' +
                ", sourceArgs=" + sourceArgs +
                ", clnHome='" + clnHome + '\'' +
                '}';
    }
}
