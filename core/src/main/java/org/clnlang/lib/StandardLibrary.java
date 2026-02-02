package org.clnlang.lib;

import org.clnlang.lib.std.Console;
import org.clnlang.runtime.Registry;

import java.util.ArrayList;
import java.util.List;

/**
 * Central registry for all standard library components.
 * Holds references to all standard library implementations and provides
 * a method to register them all into a given Registry.
 */
public class StandardLibrary {
    
    private final List<ClnFunction> standardLibraryComponents;
    
    public StandardLibrary() {
        this.standardLibraryComponents = new ArrayList<>();
        
        // Register all standard library components
        initializeStandardLibrary();
    }
    
    /**
     * Initialize all standard library components.
     * Add new standard library classes here as they are implemented.
     */
    private void initializeStandardLibrary() {
        // I/O functions
        standardLibraryComponents.add(new Console());
        
        // Future standard library components can be added here:
        // standardLibraryComponents.add(new File());
        // standardLibraryComponents.add(new Math());
        // standardLibraryComponents.add(new String());
        // etc.
    }
    
    /**
     * Register all standard library components into the provided registry.
     * This makes all standard library functions, types, and constants available
     * for import in user programs.
     * 
     * @param registry The registry to register standard library components into
     */
    public void registerAll(Registry registry) {
        for (ClnFunction component : standardLibraryComponents) {
            component.register(registry);
        }
    }
    
    /**
     * Get all standard library components.
     * 
     * @return List of all registered standard library components
     */
    public List<ClnFunction> getAllComponents() {
        return new ArrayList<>(standardLibraryComponents);
    }
    
    /**
     * Get the number of registered standard library components.
     * 
     * @return The count of standard library components
     */
    public int getComponentCount() {
        return standardLibraryComponents.size();
    }
}
