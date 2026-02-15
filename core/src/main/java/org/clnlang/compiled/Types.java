package org.clnlang.compiled;

public enum Types {

    INT,
    DEC,
    BOOL,
    STRING,
    STRUCT,
    UNION,
    ;

    public static Types fromString(String typeStr) {
        switch (typeStr) {
            case "int":
                return INT;
            case "dec":
                return DEC;
            case "bool":
                return BOOL;
            case "string":
                return STRING;
            case "struct":
                return STRUCT;
            case "union":
                return UNION;
            default:
                throw new IllegalArgumentException("Unknown type: " + typeStr);
        }
    }
    
}
