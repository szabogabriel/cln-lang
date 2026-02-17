package org.clnlang.compiled.register.elements;

import org.clnlang.compiled.binary.Types;

public class VariableSignature {

    private String packageName;
    private String variableName;
    private String fullyQualifiedName;
    private Types typeName;
    private boolean isMutable;
    private boolean isExposed;

    public VariableSignature(String packageName, String variableName, Types typeName, boolean isMutable, boolean isExposed) {
        this.packageName = packageName;
        this.variableName = variableName;
        this.fullyQualifiedName = packageName + "." + variableName;
        this.typeName = typeName;
        this.isMutable = isMutable;
        this.isExposed = isExposed;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public Types getTypeName() {
        return typeName;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableSignature that = (VariableSignature) o;
        return isExposed == that.isExposed && fullyQualifiedName.equals(that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fullyQualifiedName, isExposed);
    }
}
