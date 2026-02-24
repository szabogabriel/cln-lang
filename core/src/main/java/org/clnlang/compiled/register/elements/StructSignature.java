package org.clnlang.compiled.register.elements;

public class StructSignature {

    private String structName;
    private String packageName;
    private String fullyQualifiedName;
    private boolean isExposed;

    public StructSignature(String structName, String packageName, boolean isExposed) {
        this.structName = structName;
        this.packageName = packageName;
        this.fullyQualifiedName = packageName + "." + structName;
        this.isExposed = isExposed;
    }

    public String getStructName() {
        return structName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructSignature that = (StructSignature) o;
        return isExposed == that.isExposed && fullyQualifiedName.equals(that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fullyQualifiedName, isExposed);
    }
    
}
