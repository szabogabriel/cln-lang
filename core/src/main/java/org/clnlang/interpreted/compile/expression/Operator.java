package org.clnlang.interpreted.compile.expression;

/**
 * Binary operators supported by the language.
 * Based on the operator precedence defined in the grammar.
 */
public enum Operator {
    // Logical operators
    OR("||"),
    AND("&&"),
    
    // Equality operators
    EQ("=="),
    NEQ("!="),
    
    // Relational operators
    LT("<"),
    LTE("<="),
    GT(">"),
    GTE(">="),
    
    // Arithmetic operators
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/");
    
    private final String symbol;
    
    Operator(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Converts a string operator symbol to the corresponding enum value.
     * 
     * @param symbol the operator symbol (e.g., "+", "==", "&&")
     * @return the corresponding Operator enum value
     * @throws IllegalArgumentException if the symbol is not a valid operator
     */
    public static Operator fromSymbol(String symbol) {
        for (Operator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}
