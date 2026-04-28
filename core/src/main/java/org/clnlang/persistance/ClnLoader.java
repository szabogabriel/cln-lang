package org.clnlang.persistance;

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
     * Returns the list of source files or packages.
     * 
     * @return List of ClnSourceFile objects
     */
    List<ClnSourceFile> getSourceFiles();
    
}
