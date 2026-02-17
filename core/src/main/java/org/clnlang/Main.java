package org.clnlang;

import java.io.File;

import org.clnlang.exception.ClnException;
import org.clnlang.interpreted.ClnRuntime;

public class Main {
    public static void main(String[] args) {
        try {
            //int exitCode = run(args);
            File f = new File(".");
            System.out.println(f.getAbsolutePath());
            int exitCode = run(new String[] {"examples/hello_world.cln"});
            System.exit(exitCode);
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
     * Returns the exit code from the main function.
     */
    public static int run(String[] args) throws Exception {
        // Parse command-line arguments using RuntimeConfiguration
        RuntimeConfiguration config = new RuntimeConfiguration();
        try {
            config.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            RuntimeConfiguration.printUsage();
            throw new ClnException("Invalid command line arguments: " + e.getMessage());
        }
        
        return ClnRuntime.execute(config, null, true, System.out::println);
    }
}