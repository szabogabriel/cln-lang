package org.clnlang.interpreted.runtime.types;

import java.util.Objects;

public class FullyQualifiedName {
    private final String packageName;
    private final String entityName;

    public FullyQualifiedName(String packageName, String entityName) {
        this.packageName = packageName;
        this.entityName = entityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String[] getParts() {
        return (packageName + "." + entityName).split("\\.");
    }

    @Override
    public String toString() {
        return packageName + "." + entityName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullyQualifiedName that = (FullyQualifiedName) o;
        return Objects.equals(packageName, that.packageName) &&
               Objects.equals(entityName, that.entityName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(packageName, entityName);
    }
}
