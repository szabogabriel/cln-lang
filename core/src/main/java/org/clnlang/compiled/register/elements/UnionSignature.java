package org.clnlang.compiled.register.elements;

public class UnionSignature {

    private String unionName;
    private String packageName;
    private String fullyQualifiedName;
    private boolean isExposed;

    public UnionSignature(String unionName, String packageName, boolean isExposed) {
        this.unionName = unionName;
        this.packageName = packageName;
        this.fullyQualifiedName = packageName + "." + unionName;
        this.isExposed = isExposed;
    }

    public String getUnionName() {
        return unionName;
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
        UnionSignature that = (UnionSignature) o;
        return isExposed == that.isExposed && fullyQualifiedName.equals(that.fullyQualifiedName);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fullyQualifiedName, isExposed);
    }

}
