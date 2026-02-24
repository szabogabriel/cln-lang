package org.clnlang.compiled.register.elements;

import org.clnlang.compiled.binary.Types;

public class FunctionSignature {

    private String fullyQualifiedName;
    private String functionName;
    private String packageName;
    
    private Types[] returnTypes;
    
    private Types[] parameterTypes;
    private String[] parameterNames;

    private boolean isExposed;

    public FunctionSignature(String packageName, String functionName, Types[] returnTypes, Types[] parameterTypes, String[] parameterNames, boolean isExposed) {
        this.packageName = packageName;
        this.functionName = functionName;
        this.fullyQualifiedName = packageName + "." + functionName;
        this.returnTypes = returnTypes;
        this.parameterTypes = parameterTypes;
        this.parameterNames = parameterNames;
        this.isExposed = isExposed;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Types[] getReturnTypes() {
        return returnTypes;
    }

    public Types[] getParameterTypes() {
        return parameterTypes;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionSignature that = (FunctionSignature) o;
        return isExposed == that.isExposed && fullyQualifiedName.equals(that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fullyQualifiedName, isExposed);
    }

}
