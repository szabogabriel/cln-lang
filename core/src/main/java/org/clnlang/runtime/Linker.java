package org.clnlang.runtime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.compile.declaration.ImportDeclImpl;

/**
 * Links imported symbols from the Registry into the ExecutionContext.
 * Resolves imports, handles wildcards, and validates symbol availability.
 */
public class Linker {

    /**
     * Resolve all imports from the ExecutionContext by pulling symbols from the Registry
     * into the ExecutionContext's GlobalContext.
     * 
     * @param context The execution context containing imports to resolve
     * @param registry The registry containing all available symbols
     * @throws Exception If a required import cannot be resolved
     */
    public void resolveImports(ExecutionContext context, Registry registry) throws Exception {
        GlobalContext globalContext = context.getGlobalContext();
        Set<String> resolvedSymbols = new HashSet<>();
        
        // Get imports from the ExecutionContext
        List<ImportDeclImpl> imports = context.getImports();
        
        for (ImportDeclImpl importDecl : imports) {
            String importPath = importDecl.getImportPath();
            
            if (importDecl.isWildcard()) {
                // Import all symbols from the package (e.g., "std.io.*")
                resolveWildcardImport(importPath, globalContext, registry, resolvedSymbols);
            } else {
                // Import specific symbol (e.g., "std.io.console.write")
                resolveSpecificImport(importPath, globalContext, registry, resolvedSymbols);
            }
        }
    }
    
    /**
     * Resolve a wildcard import (e.g., "std.io.*") by importing all symbols from the package.
     */
    private void resolveWildcardImport(String packagePath, GlobalContext globalContext, 
                                      Registry registry, Set<String> resolvedSymbols) throws Exception {
        boolean foundAny = false;
        
        // Import all functions from the package
        for (Map.Entry<FullyQualifiedName, FunctionDeclImpl> entry : registry.getAllFunctions().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                String simpleName = entry.getKey().getEntityName();
                if (!resolvedSymbols.contains(simpleName)) {
                    globalContext.registerFunction(simpleName, entry.getValue());
                    resolvedSymbols.add(simpleName);
                    foundAny = true;
                }
            }
        }
        
        // Import all global variables from the package
        for (Map.Entry<FullyQualifiedName, GlobalVarDeclImpl> entry : registry.getAllGlobalVariables().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                String simpleName = entry.getKey().getEntityName();
                if (!resolvedSymbols.contains(simpleName)) {
                    globalContext.registerGlobalVariable(entry.getValue(), null); // Value will be set during execution
                    resolvedSymbols.add(simpleName);
                    foundAny = true;
                }
            }
        }
        
        // Import all global constants from the package
        for (Map.Entry<FullyQualifiedName, GlobalVarDeclImpl> entry : registry.getAllGlobalConstants().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                String simpleName = entry.getKey().getEntityName();
                if (!resolvedSymbols.contains(simpleName)) {
                    globalContext.registerGlobalVariable(entry.getValue(), null); // Value will be set during execution
                    resolvedSymbols.add(simpleName);
                    foundAny = true;
                }
            }
        }
        
        // Import all struct types from the package
        for (Map.Entry<FullyQualifiedName, StructDefinition> entry : registry.getAllStructTypes().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                String simpleName = entry.getKey().getEntityName();
                if (!resolvedSymbols.contains(simpleName)) {
                    globalContext.registerStructType(simpleName, entry.getValue());
                    resolvedSymbols.add(simpleName);
                    foundAny = true;
                }
            }
        }
        
        // Import all union types from the package
        for (Map.Entry<FullyQualifiedName, UnionDefinition> entry : registry.getAllUnionTypes().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                String simpleName = entry.getKey().getEntityName();
                if (!resolvedSymbols.contains(simpleName)) {
                    globalContext.registerUnionType(simpleName, entry.getValue());
                    resolvedSymbols.add(simpleName);
                    foundAny = true;
                }
            }
        }
        
        if (!foundAny) {
            throw new Exception("Wildcard import failed: No symbols found in package '" + packagePath + "'");
        }
    }
    
    /**
     * Resolve a specific import (e.g., "std.io.console.write") by finding and importing that symbol.
     */
    private void resolveSpecificImport(String fullPath, GlobalContext globalContext, 
                                      Registry registry, Set<String> resolvedSymbols) throws Exception {
        // Parse the path to extract package and entity name
        int lastDot = fullPath.lastIndexOf('.');
        if (lastDot == -1) {
            throw new Exception("Invalid import path: '" + fullPath + "' (must contain at least one dot)");
        }
        
        String packageName = fullPath.substring(0, lastDot);
        String entityName = fullPath.substring(lastDot + 1);
        FullyQualifiedName fqn = new FullyQualifiedName(packageName, entityName);
        
        // Try to find the symbol in each category
        boolean found = false;
        
        // Try function
        FunctionDeclImpl function = registry.getFunction(fqn);
        if (function != null) {
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerFunction(entityName, function);
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try global variable
        GlobalVarDeclImpl variable = registry.getGlobalVariable(fqn);
        if (variable != null) {
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerGlobalVariable(variable, null); // Value will be set during execution
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try global constant
        GlobalVarDeclImpl constant = registry.getGlobalConstant(fqn);
        if (constant != null) {
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerGlobalVariable(constant, null); // Value will be set during execution
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try struct type
        StructDefinition struct = registry.getStructType(fqn);
        if (struct != null) {
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerStructType(entityName, struct);
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try union type
        UnionDefinition union = registry.getUnionType(fqn);
        if (union != null) {
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerUnionType(entityName, union);
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        if (!found) {
            throw new Exception("Import failed: Symbol '" + fullPath + "' not found in registry");
        }
    }
}
