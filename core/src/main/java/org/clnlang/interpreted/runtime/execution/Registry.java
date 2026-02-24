package org.clnlang.interpreted.runtime.execution;

import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;
import org.clnlang.interpreted.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.interpreted.compile.declaration.ProgramImpl;
import org.clnlang.interpreted.runtime.types.FullyQualifiedName;
import org.clnlang.interpreted.runtime.types.StructDefinition;
import org.clnlang.interpreted.runtime.types.UnionDefinition;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Global registry for all available functions, variables, constants, types, etc.
 * This serves as a catalog that the Linker uses to resolve symbols during the linking phase.
 * Also manages all compiled programs organized by package name.
 */
public class Registry {
    
    private final Set<String> registeredNames = new HashSet<>();
    
    private final Map<FullyQualifiedName, FunctionDeclImpl> registeredFunctions = new HashMap<>();

    private final Map<FullyQualifiedName, GlobalVarDeclImpl> registeredGlobalVariables = new HashMap<>();

    private final Map<FullyQualifiedName, GlobalVarDeclImpl> registeredGlobalConstants = new HashMap<>();

    private final Map<FullyQualifiedName, StructDefinition> registeredStructTypes = new HashMap<>();

    private final Map<FullyQualifiedName, UnionDefinition> registeredUnionTypes = new HashMap<>();
    
    // Map of package name to list of compiled programs in that package
    private final Map<String, Map<File, ProgramImpl>> packagePrograms = new HashMap<>();
    
    // ===== Registration Methods =====
    
    /**
     * Register a function in the registry.
     */
    public void registerFunction(FullyQualifiedName name, FunctionDeclImpl function) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredFunctions.put(name, function);
    }
    
    /**
     * Register a global variable in the registry.
     */
    public void registerGlobalVariable(FullyQualifiedName name, GlobalVarDeclImpl variable) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredGlobalVariables.put(name, variable);
    }
    
    /**
     * Register a global constant in the registry.
     */
    public void registerGlobalConstant(FullyQualifiedName name, GlobalVarDeclImpl constant) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredGlobalConstants.put(name, constant);
    }
    
    /**
     * Register a struct type in the registry.
     */
    public void registerStructType(FullyQualifiedName name, StructDefinition struct) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredStructTypes.put(name, struct);
    }
    
    /**
     * Register a union type in the registry.
     */
    public void registerUnionType(FullyQualifiedName name, UnionDefinition union) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredUnionTypes.put(name, union);
    }
    
    // ===== Lookup Methods =====
    
    /**
     * Get a function declaration by its fully qualified name.
     * @return The function declaration, or null if not found
     */
    public FunctionDeclImpl getFunction(FullyQualifiedName name) {
        return registeredFunctions.get(name);
    }
    
    /**
     * Get a global variable by its fully qualified name.
     * @return The global variable, or null if not found
     */
    public GlobalVarDeclImpl getGlobalVariable(FullyQualifiedName name) {
        return registeredGlobalVariables.get(name);
    }
    
    /**
     * Get a global constant by its fully qualified name.
     * @return The global constant, or null if not found
     */
    public GlobalVarDeclImpl getGlobalConstant(FullyQualifiedName name) {
        return registeredGlobalConstants.get(name);
    }
    
    /**
     * Get a struct type by its fully qualified name.
     * @return The struct definition, or null if not found
     */
    public StructDefinition getStructType(FullyQualifiedName name) {
        return registeredStructTypes.get(name);
    }
    
    /**
     * Get a union type by its fully qualified name.
     * @return The union definition, or null if not found
     */
    public UnionDefinition getUnionType(FullyQualifiedName name) {
        return registeredUnionTypes.get(name);
    }
    
    // ===== Query Methods =====
    
    /**
     * Check if a function is registered.
     */
    public boolean hasFunction(FullyQualifiedName name) {
        return registeredFunctions.containsKey(name);
    }
    
    /**
     * Check if a global variable is registered.
     */
    public boolean hasGlobalVariable(FullyQualifiedName name) {
        return registeredGlobalVariables.containsKey(name);
    }
    
    /**
     * Check if a global constant is registered.
     */
    public boolean hasGlobalConstant(FullyQualifiedName name) {
        return registeredGlobalConstants.containsKey(name);
    }
    
    /**
     * Check if a struct type is registered.
     */
    public boolean hasStructType(FullyQualifiedName name) {
        return registeredStructTypes.containsKey(name);
    }
    
    /**
     * Check if a union type is registered.
     */
    public boolean hasUnionType(FullyQualifiedName name) {
        return registeredUnionTypes.containsKey(name);
    }
    
    /**
     * Check if any symbol with the given name part is registered.
     */
    public boolean hasRegisteredName(String namePart) {
        return registeredNames.contains(namePart);
    }
    
    // ===== Collection Methods =====
    
    /**
     * Get all registered functions.
     */
    public Map<FullyQualifiedName, FunctionDeclImpl> getAllFunctions() {
        return new HashMap<>(registeredFunctions);
    }
    
    /**
     * Get all registered global variables.
     */
    public Map<FullyQualifiedName, GlobalVarDeclImpl> getAllGlobalVariables() {
        return new HashMap<>(registeredGlobalVariables);
    }
    
    /**
     * Get all registered global constants.
     */
    public Map<FullyQualifiedName, GlobalVarDeclImpl> getAllGlobalConstants() {
        return new HashMap<>(registeredGlobalConstants);
    }
    
    /**
     * Get all registered struct types.
     */
    public Map<FullyQualifiedName, StructDefinition> getAllStructTypes() {
        return new HashMap<>(registeredStructTypes);
    }
    
    /**
     * Get all registered union types.
     */
    public Map<FullyQualifiedName, UnionDefinition> getAllUnionTypes() {
        return new HashMap<>(registeredUnionTypes);
    }
    
    // ===== Program Index Methods =====
    
    /**
     * Add a compiled program to the registry.
     * 
     * @param sourceFile The source file
     * @param program The compiled program
     * @param packageName The package name (empty string for default package)
     */
    public void addProgram(File sourceFile, ProgramImpl program, String packageName) {
        try {
            // Normalize to canonical path for consistent lookups
            File canonicalFile = sourceFile.getCanonicalFile();
            packagePrograms
                .computeIfAbsent(packageName, k -> new HashMap<>())
                .put(canonicalFile, program);
        } catch (java.io.IOException e) {
            // Fallback to absolute path if canonical fails
            packagePrograms
                .computeIfAbsent(packageName, k -> new HashMap<>())
                .put(sourceFile.getAbsoluteFile(), program);
        }
    }
    
    /**
     * Get all programs in a specific package.
     * 
     * @param packageName The package name (empty string for default package)
     * @return Map of source files to programs, or empty map if package not found
     */
    public Map<File, ProgramImpl> getPackagePrograms(String packageName) {
        return packagePrograms.getOrDefault(packageName, new HashMap<>());
    }
    
    /**
     * Get a specific program by its source file.
     * 
     * @param sourceFile The source file
     * @return The compiled program, or null if not found
     */
    public ProgramImpl getProgram(File sourceFile) {
        try {
            // Normalize to canonical path for consistent lookups
            File canonicalFile = sourceFile.getCanonicalFile();
            for (Map<File, ProgramImpl> programs : packagePrograms.values()) {
                ProgramImpl program = programs.get(canonicalFile);
                if (program != null) {
                    return program;
                }
            }
        } catch (java.io.IOException e) {
            // Fallback to absolute path if canonical fails
            File absoluteFile = sourceFile.getAbsoluteFile();
            for (Map<File, ProgramImpl> programs : packagePrograms.values()) {
                ProgramImpl program = programs.get(absoluteFile);
                if (program != null) {
                    return program;
                }
            }
        }
        return null;
    }
    
    /**
     * Get all package names in the registry.
     * 
     * @return List of package names
     */
    public List<String> getPackageNames() {
        return List.copyOf(packagePrograms.keySet());
    }
    
    /**
     * Check if a package exists in the registry.
     * 
     * @param packageName The package name
     * @return true if the package exists, false otherwise
     */
    public boolean hasPackage(String packageName) {
        return packagePrograms.containsKey(packageName);
    }
    
    /**
     * Get all programs across all packages.
     * 
     * @return Map of all programs indexed by source file
     */
    public Map<File, ProgramImpl> getAllPrograms() {
        Map<File, ProgramImpl> allPrograms = new HashMap<>();
        for (Map<File, ProgramImpl> programs : packagePrograms.values()) {
            allPrograms.putAll(programs);
        }
        return allPrograms;
    }
    
    /**
     * Clear all programs from the registry.
     */
    public void clearPrograms() {
        packagePrograms.clear();
    }
}
