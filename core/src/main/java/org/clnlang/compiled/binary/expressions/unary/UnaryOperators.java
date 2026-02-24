package org.clnlang.compiled.binary.expressions.unary;

public enum UnaryOperators {

        PLUSPLUS("++"),
        MINUSMINUS("--"),
        MINUS("-"),
        NOT("!"),
        UNSUPPORTED("unsupported")
        ;
    
        private String symbol;
    
        UnaryOperators(String symbol) {
            this.symbol = symbol;
        }
    
        public String getSymbol() {
            return symbol;
        }
    
        public static UnaryOperators fromSymbol(String symbol) {
            for (UnaryOperators op : values()) {
                if (op.symbol.equals(symbol)) {
                    return op;
                }
            }
            return UNSUPPORTED;
        }
    
}
