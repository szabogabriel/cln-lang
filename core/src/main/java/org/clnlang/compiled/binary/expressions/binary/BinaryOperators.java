package org.clnlang.compiled.binary.expressions.binary;

public enum BinaryOperators {

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER(">"),
    LESS("<"),
    GREATER_EQUALS(">="),
    LESS_EQUALS("<="),
    AND("&&"),
    OR("||"),
    NOT("!"),
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
