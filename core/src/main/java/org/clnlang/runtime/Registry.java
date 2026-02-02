package org.clnlang.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;

public enum Registry {
    INSTANCE;

    private final Set<String> registeredPackageNames = new HashSet<>();

    private final Set<String> registeredNodes = new HashSet<>();
    
    private final Map<String, FunctionDeclImpl> registeredFunctions = new HashMap<>();

    private final Map<String, GlobalVarDeclImpl> registeredGlobalVariables = new HashMap<>();

    private final Map<String, StructDefinition> registeredStructTypes = new HashMap<>();

    private final Map<String, UnionDefinition> registeredUnionTypes = new HashMap<>();
    
}
