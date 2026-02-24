package org.clnlang.compiled.register;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.clnlang.compiled.register.elements.FunctionSignature;
import org.clnlang.compiled.register.elements.StructSignature;
import org.clnlang.compiled.register.elements.UnionSignature;
import org.clnlang.compiled.register.elements.VariableSignature;
import org.clnlang.exception.DuplicateDeclarationException;

public class GlobalRegistry {

    private Map<FunctionSignature, File> functions;

    private Map<VariableSignature, File> variables;

    private Map<StructSignature, File> structs;
    private Map<UnionSignature, File> unions;

    public GlobalRegistry(){
        functions = new HashMap<>();
        structs = new HashMap<>();
        unions = new HashMap<>();
        variables = new HashMap<>();
    }

    public void registerFunction(FunctionSignature signature, File file) {
        if (functions.containsKey(signature)) {
            throw new DuplicateDeclarationException("Function with the same signature already exists: " + signature.getFullyQualifiedName());
        }
        functions.put(signature, file);
    }

    public void registerVariable(VariableSignature signature, File file) {
        if (variables.containsKey(signature)) {
            throw new DuplicateDeclarationException("Variable with the same signature already exists: " + signature.getFullyQualifiedName());
        }
        variables.put(signature, file);
    }

    public void registerStruct(StructSignature signature, File file) {
        if (structs.containsKey(signature)) {
            throw new DuplicateDeclarationException("Struct with the same signature already exists: " + signature.getFullyQualifiedName());
        }
        structs.put(signature, file);
    }

    public void registerUnion(UnionSignature signature, File file) {
        if (unions.containsKey(signature)) {
            throw new DuplicateDeclarationException("Union with the same signature already exists: " + signature.getFullyQualifiedName());
        }
        unions.put(signature, file);
    }

    public Map<FunctionSignature, File> getFunctions() {
        return functions;
    }

    public Map<VariableSignature, File> getVariables() {
        return variables;
    }

    public Map<StructSignature, File> getStructs() {
        return structs;
    }

    public Map<UnionSignature, File> getUnions() {
        return unions;
    }
}
