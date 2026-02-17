package org.clnlang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
    private List<String> clnPaths;
    private List<String> sourceFiles;
    private String clnHome;
    
    /**
     * Creates a new RuntimeConfiguration with default values.
     * Reads CLN_HOME and CLN_PATH environment variables and initializes paths.
     */
    public RuntimeConfiguration() {
        this.verbose = false;
        this.clnPaths = new ArrayList<>();
        this.sourceFiles = new ArrayList<>();
        
        // Read CLN_HOME environment variable
        this.clnHome = System.getenv("CLN_HOME");
        
        // Load CLN_HOME/lib if available
        if (clnHome != null && !clnHome.trim().isEmpty()) {
            String libPath = clnHome + File.separator + "lib";
            File libDir = new File(libPath);
            if (libDir.exists() && libDir.isDirectory()) {
                this.clnPaths.add(libPath);
            }
        }
        
        // Load CLN_PATH if available
        String clnPathEnv = System.getenv("CLN_PATH");
        if (clnPathEnv != null && !clnPathEnv.trim().isEmpty()) {
            parseClnPaths(clnPathEnv);
        }
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
                String pathArg = args[i];
                parseClnPaths(pathArg);
            } else if (!arg.startsWith("-")) {
                // This is a source file or package definition
                parseSourceFiles(arg);
            } else {
                throw new IllegalArgumentException("Unknown option: " + arg);
            }
        }
    }
    
    /**
     * Parses CLN paths separated by the OS file separator.
     * 
     * @param pathArg Path string containing one or more paths separated by File.pathSeparator
     */
    private void parseClnPaths(String pathArg) {
        String[] paths = pathArg.split(File.pathSeparator);
        for (String path : paths) {
            String trimmed = path.trim();
            if (!trimmed.isEmpty()) {
                this.clnPaths.add(trimmed);
            }
        }
    }
    
    /**
     * Parses source files which can be:
     * - A single .cln file
     * - A package definition (e.g., myapp.main)
     * - Multiple .cln files separated by File.pathSeparator
     * 
     * @param sourceArg Source file(s) or package definition
     */
    private void parseSourceFiles(String sourceArg) {
        // Check if this contains multiple files separated by path separator
        if (sourceArg.contains(File.pathSeparator)) {
            String[] files = sourceArg.split(File.pathSeparator);
            for (String file : files) {
                String trimmed = file.trim();
                if (!trimmed.isEmpty()) {
                    this.sourceFiles.add(trimmed);
                }
            }
        } else {
            // Single file or package definition
            this.sourceFiles.add(sourceArg);
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
     * Returns the list of CLN paths for source files and libraries.
     * 
     * @return List of CLN paths
     */
    public List<String> getClnPaths() {
        return new ArrayList<>(clnPaths);
    }
    
    /**
     * Adds a CLN path to the configuration.
     * 
     * @param path Path to add
     */
    public void addClnPath(String path) {
        if (path != null && !path.trim().isEmpty()) {
            this.clnPaths.add(path.trim());
        }
    }
    
    /**
     * Returns the list of source files or package definitions.
     * 
     * @return List of source files
     */
    public List<String> getSourceFiles() {
        return new ArrayList<>(sourceFiles);
    }

    /**
     * Returns the list of source files as File objects.
     * @return
     */
    public List<File> getSourceFilesAsFiles() {
        List<File> files = new ArrayList<>();
        for (String source : sourceFiles) {
            files.add(new File(source));
        }
        return files;
    }
    
    /**
     * Returns the first source file or package definition, or null if none specified.
     * 
     * @return First source file or null
     */
    public String getFirstSourceFile() {
        return sourceFiles.isEmpty() ? null : sourceFiles.get(0);
    }
    
    /**
     * Checks if any source files were specified.
     * 
     * @return true if at least one source file is specified, false otherwise
     */
    public boolean hasSourceFiles() {
        return !sourceFiles.isEmpty();
    }
    
    /**
     * Checks if any CLN paths were specified.
     * 
     * @return true if at least one CLN path is specified, false otherwise
     */
    public boolean hasClnPaths() {
        return !clnPaths.isEmpty();
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
        System.err.println("  -cp, --cln_path <paths>    Set path(s) for source files and libraries");
        System.err.println("                             Multiple paths separated by '" + File.pathSeparator + "'");
        System.err.println();
        System.err.println("Source:");
        System.err.println("  file.cln                   A CLN source file");
        System.err.println("  package.name               A package definition (e.g., myapp.main)");
        System.err.println("  file1.cln" + File.pathSeparator + "file2.cln     Multiple files separated by '" + File.pathSeparator + "'");
        System.err.println();
        System.err.println("Environment Variables:");
        System.err.println("  CLN_HOME                   Home folder for out-of-the-box libraries");
        System.err.println("                             Automatically adds ${CLN_HOME}/lib to search path");
        System.err.println("  CLN_PATH                   Default library paths (used when -cp is not specified)");
        System.err.println("                             Multiple paths separated by '" + File.pathSeparator + "'");
        System.err.println();
        System.err.println("Examples:");
        System.err.println("  java -jar cln.jar hello.cln");
        System.err.println("  java -jar cln.jar -v myapp.main");
        System.err.println("  java -jar cln.jar -cp /path/to/libs -v hello.cln");
        System.err.println("  export CLN_HOME=/opt/cln && java -jar cln.jar hello.cln");
    }
    
    /**
     * Loads all source files from the CLN paths.
     * Scans all directories in clnPaths and organizes .cln files by package (relative directory path).
     * 
     * @return Map where keys are package names (relative paths) and values are lists of .cln files in that package
     * @throws IOException if an I/O error occurs while walking the directory tree
     * @throws IllegalStateException if the same package is found in multiple source folders
     */
    public Map<String, List<File>> loadAllSourceFiles() throws IOException {
        Map<String, List<File>> packageFiles = new HashMap<>();
        
        for (String clnPath : clnPaths) {
            File rootDir = new File(clnPath);
            if (!rootDir.exists() || !rootDir.isDirectory()) {
                continue; // Skip invalid paths
            }
            
            // First collect all files from this source folder, grouped by package
            Map<String, List<File>> thisRootPackages = new HashMap<>();
            
            try (Stream<Path> paths = Files.walk(rootDir.toPath())) {
                paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".cln"))
                    .forEach(path -> {
                        File clnFile = path.toFile();
                        File parent = clnFile.getParentFile();
                        
                        // Get relative path from root (this is the package)
                        String packageName = rootDir.toPath().relativize(parent.toPath()).toString();
                        
                        // Add to this root's packages
                        thisRootPackages.computeIfAbsent(packageName, k -> new ArrayList<>()).add(clnFile);
                    });
            }
            
            // Now check for conflicts and add to main map
            for (Map.Entry<String, List<File>> entry : thisRootPackages.entrySet()) {
                String packageName = entry.getKey();
                if (packageFiles.containsKey(packageName)) {
                    throw new IllegalStateException("Package '" + packageName + "' is defined in multiple source folders");
                }
                packageFiles.put(packageName, entry.getValue());
            }
        }
        
        return packageFiles;
    }

    /**
     * Loads all source files from the CLN paths and returns them as a flat list.
     * This method calls loadAllSourceFiles() and flattens all the files from all packages into a single list.
     * 
     * @return List of all .cln files found across all CLN paths
     * @throws IOException if an I/O error occurs while walking the directory tree
     * @throws IllegalStateException if the same package is found in multiple source folders
     */
    public List<File> loadAllSourceFilesAsFiles() throws IOException {
        Map<String, List<File>> packageFiles = loadAllSourceFiles();
        List<File> allFiles = new ArrayList<>();
        
        for (List<File> files : packageFiles.values()) {
            allFiles.addAll(files);
        }
        
        return allFiles;
    }

    public boolean isSourceFileExecution() {
        return sourceFiles.size() > 0;
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
                ", clnPaths=" + clnPaths +
                ", sourceFiles=" + sourceFiles +
                ", clnHome='" + clnHome + '\'' +
                '}';
    }
}
