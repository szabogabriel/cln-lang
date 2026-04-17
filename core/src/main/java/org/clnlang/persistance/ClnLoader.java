package org.clnlang.persistance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.clnlang.runtime.execution.Registry;

public interface ClnLoader {

    /**
     * Startup mode for the CLN application.
     */
    enum StartupMode {
        FILES,      // Start from specific .cln files (must be in default package)
        PACKAGE     // Start from a package name
    }

    int loadSources(Registry  context) throws Exception;
    
    /**
     * Returns the startup mode based on the source files.
     * 
     * @return StartupMode (FILES or PACKAGE)
     */
    StartupMode getSupportedStartupMode();
    
    /**
     * Returns the list of source files or package definitions.
     * 
     * @return List of source files/packages
     */
    List<String> getSourceFiles();
    
    /**
     * Factory method to create a ClnLoader from environment and command line arguments.
     * Handles parsing of CLN_HOME, CLN_PATH, -cp arguments, and source files.
     * 
     * @param clnHome CLN_HOME environment variable value (can be null)
     * @param clnPathEnv CLN_PATH environment variable value (can be null)
     * @param cpArg Command line -cp argument value (can be null)
     * @param sourceArgs Raw source file/package arguments
     * @param verbose Whether to enable verbose logging
     * @return A ClnLoader instance configured with the parsed paths and sources
     */
    static ClnLoader fromEnvironment(String clnHome, String clnPathEnv, String cpArg, List<String> sourceArgs, boolean verbose) {
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
