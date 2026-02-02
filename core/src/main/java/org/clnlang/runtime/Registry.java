package org.clnlang.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;

public enum Registry {
    INSTANCE;

    private final Set<String> registeredNames = new HashSet<>();
    
    private final Map<FullyQualifiedName, FunctionDeclImpl> registeredFunctions = new HashMap<>();

    private final Map<FullyQualifiedName, GlobalVarDeclImpl> registeredGlobalVariables = new HashMap<>();

    private final Map<FullyQualifiedName, StructDefinition> registeredStructTypes = new HashMap<>();

    private final Map<FullyQualifiedName, UnionDefinition> registeredUnionTypes = new HashMap<>();
    
    public void registerFunction(FullyQualifiedName name, FunctionDeclImpl function) {
        for (String it : name.getParts()) {
           registeredNames.add(it);
        }
        registeredFunctions.put(name, function);
    }
}
