package org.clnlang.runtime;

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
    
}
