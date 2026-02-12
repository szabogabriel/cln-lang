package org.clnlang.compiled.expressions.binary;

public enum BinaryOperators {

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER(">"),
    LESS("<"),
    GREATER_EQUALS(">="),
    LESS_EQUALS("<="),
    AND("&&"),
    OR("||"),
    NOT("!"),
    BITWISE_AND("&"),
    BITWISE_OR("|"),
    BITWISE_XOR("^"),
    BITWISE_NOT("~"),
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    UNSUPPORTED("unsupported")
    ;

    private String symbol;

    BinaryOperators(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static BinaryOperators fromSymbol(String symbol) {
        for (BinaryOperators op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        return UNSUPPORTED;
    }
}   
