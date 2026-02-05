package org.clnlang.linker;

import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.context.GlobalContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.clnlang.runtime.types.StructDefinition;
import org.clnlang.runtime.types.UnionDefinition;

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
        String importingPackage = globalContext.getPackageName();
        
        // Import all functions from the package
        for (Map.Entry<FullyQualifiedName, FunctionDeclImpl> entry : registry.getAllFunctions().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                FunctionDeclImpl funcDecl = entry.getValue();
                String simpleName = entry.getKey().getEntityName();
                
                // Check visibility
                if (isAccessible(importingPackage, funcDecl.getPackageName(), funcDecl.isExposed())) {
                    foundAny = true;  // Mark that we found at least one accessible symbol
                    if (!resolvedSymbols.contains(simpleName)) {
                        globalContext.registerFunction(simpleName, funcDecl);
                        resolvedSymbols.add(simpleName);
                    }
                }
            }
        }
        
        // Import all global variables from the package
        for (Map.Entry<FullyQualifiedName, GlobalVarDeclImpl> entry : registry.getAllGlobalVariables().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                GlobalVarDeclImpl varDecl = entry.getValue();
                String simpleName = entry.getKey().getEntityName();
                
                // Check visibility
                if (isAccessible(importingPackage, varDecl.getPackageName(), varDecl.isExposed())) {
                    foundAny = true;
                    if (!resolvedSymbols.contains(simpleName)) {
                        globalContext.registerGlobalVariable(varDecl, null); // Value will be set during execution
                        resolvedSymbols.add(simpleName);
                    }
                }
            }
        }
        
        // Import all global constants from the package
        for (Map.Entry<FullyQualifiedName, GlobalVarDeclImpl> entry : registry.getAllGlobalConstants().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                GlobalVarDeclImpl constDecl = entry.getValue();
                String simpleName = entry.getKey().getEntityName();
                
                // Check visibility
                if (isAccessible(importingPackage, constDecl.getPackageName(), constDecl.isExposed())) {
                    foundAny = true;
                    if (!resolvedSymbols.contains(simpleName)) {
                        globalContext.registerGlobalVariable(constDecl, null); // Value will be set during execution
                        resolvedSymbols.add(simpleName);
                    }
                }
            }
        }
        
        // Import all struct types from the package
        for (Map.Entry<FullyQualifiedName, StructDefinition> entry : registry.getAllStructTypes().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                StructDefinition structDef = entry.getValue();
                String simpleName = entry.getKey().getEntityName();
                
                // Check visibility
                if (isAccessible(importingPackage, structDef.getPackageName(), structDef.isExposed())) {
                    foundAny = true;
                    if (!resolvedSymbols.contains(simpleName)) {
                        globalContext.registerStructType(simpleName, structDef);
                        resolvedSymbols.add(simpleName);
                    }
                }
            }
        }
        
        // Import all union types from the package
        for (Map.Entry<FullyQualifiedName, UnionDefinition> entry : registry.getAllUnionTypes().entrySet()) {
            if (entry.getKey().getPackageName().equals(packagePath)) {
                UnionDefinition unionDef = entry.getValue();
                String simpleName = entry.getKey().getEntityName();
                
                // Check visibility
                if (isAccessible(importingPackage, unionDef.getPackageName(), unionDef.isExposed())) {
                    foundAny = true;
                    if (!resolvedSymbols.contains(simpleName)) {
                        globalContext.registerUnionType(simpleName, unionDef);
                        resolvedSymbols.add(simpleName);
                    }
                }
            }
        }
        
        if (!foundAny) {
            throw new Exception("Wildcard import failed: No accessible symbols found in package '" + packagePath + "'");
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
        String importingPackage = globalContext.getPackageName();
        
        // Try to find the symbol in each category
        boolean found = false;
        
        // Try function
        FunctionDeclImpl function = registry.getFunction(fqn);
        if (function != null) {
            if (!isAccessible(importingPackage, function.getPackageName(), function.isExposed())) {
                throw new Exception("Import failed: Function '" + fullPath + "' is not exposed for cross-package import");
            }
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerFunction(entityName, function);
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try global variable
        GlobalVarDeclImpl variable = registry.getGlobalVariable(fqn);
        if (variable != null) {
            if (!isAccessible(importingPackage, variable.getPackageName(), variable.isExposed())) {
                throw new Exception("Import failed: Variable '" + fullPath + "' is not exposed for cross-package import");
            }
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerGlobalVariable(variable, null); // Value will be set during execution
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try global constant
        GlobalVarDeclImpl constant = registry.getGlobalConstant(fqn);
        if (constant != null) {
            if (!isAccessible(importingPackage, constant.getPackageName(), constant.isExposed())) {
                throw new Exception("Import failed: Constant '" + fullPath + "' is not exposed for cross-package import");
            }
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerGlobalVariable(constant, null); // Value will be set during execution
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try struct type
        StructDefinition struct = registry.getStructType(fqn);
        if (struct != null) {
            if (!isAccessible(importingPackage, struct.getPackageName(), struct.isExposed())) {
                throw new Exception("Import failed: Struct '" + fullPath + "' is not exposed for cross-package import");
            }
            if (!resolvedSymbols.contains(entityName)) {
                globalContext.registerStructType(entityName, struct);
                resolvedSymbols.add(entityName);
            }
            found = true;
        }
        
        // Try union type
        UnionDefinition union = registry.getUnionType(fqn);
        if (union != null) {
            if (!isAccessible(importingPackage, union.getPackageName(), union.isExposed())) {
                throw new Exception("Import failed: Union '" + fullPath + "' is not exposed for cross-package import");
            }
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
    
    /**
     * Check if a symbol from targetPackage is accessible from importingPackage.
     * A symbol is accessible if:
     * 1. It's in the same package (full access to all symbols), OR
     * 2. It's in a different package AND marked as exposed
     * 
     * @param importingPackage The package trying to import the symbol
     * @param targetPackage The package where the symbol is defined
     * @param isExposed Whether the symbol is marked with 'expose' keyword
     * @return true if the symbol is accessible, false otherwise
     */
    private boolean isAccessible(String importingPackage, String targetPackage, boolean isExposed) {
        // Same package: full access
        if (importingPackage != null && importingPackage.equals(targetPackage)) {
            return true;
        }
        
        // Different package: only if exposed
        return isExposed;
    }
}
