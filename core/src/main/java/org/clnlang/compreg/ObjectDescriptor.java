package org.clnlang.compreg;

public class ObjectDescriptor {

    private final String packageName;
    private final String name;
    private final Type type;
    private final boolean exposed;

    public ObjectDescriptor(String packageName, String name, Type type, boolean exposed) {
        this.packageName = packageName;
        this.name = name;
        this.type = type;
        this.exposed = exposed;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isExposed() {
        return exposed;
    }

    public static enum Type {
        STRUCT,
        UNION,
        FUNCTION,
        GLOBAL_VARIABLE
    }
    
}
