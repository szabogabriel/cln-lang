package org.clnlang.ast.statement;
import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;
import java.util.ArrayList;
import java.util.List;
/**
 * Tuple assignment statement: (tupleBind, ...) = expr ;
 */
public class TupleAssignStmt extends Stmt {
    private List<TupleBind> bindings;
    private Expr value;
    
    public TupleAssignStmt(List<TupleBind> bindings, Expr value) {
        this.bindings = bindings != null ? bindings : new ArrayList<>();
        this.value = value;
    }
    public List<TupleBind> getBindings() {
        return bindings;
    }
    public Expr getValue() {
        return value;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        // Can be extended if needed
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < bindings.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(bindings.get(i));
        }
        sb.append(") = ").append(value);
        return sb.toString();
    }
    /**
     * Represents a single binding in a tuple assignment
     */
    public static class TupleBind {
        private boolean isVar;
        private String type;
        private String name;
        
        public TupleBind(boolean isVar, String type, String name) {
            this.isVar = isVar;
            this.type = type;
            this.name = name;
        }
        public boolean isVar() {
            return isVar;
        }
        public String getType() {
            return type;
        }
        public String getName() {
            return name;
        }
        @Override
        public String toString() {
            return (isVar ? "var " : "") + type + " " + name;
        }
    }
}

