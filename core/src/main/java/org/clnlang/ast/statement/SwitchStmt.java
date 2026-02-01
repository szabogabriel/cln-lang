package org.clnlang.ast.statement;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.expression.Expr;
import java.util.ArrayList;
import java.util.List;

/**
 * Switch statement with case clauses
 */
public class SwitchStmt extends Stmt {
    private Expr expression;
    private List<CaseClause> cases;

    public SwitchStmt(Expr expression) {
        this.expression = expression;
        this.cases = new ArrayList<>();
    }

    public void addCase(CaseClause caseClause) {
        cases.add(caseClause);
    }

    public Expr getExpression() {
        return expression;
    }

    public List<CaseClause> getCases() {
        return cases;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Represents a single case clause in a switch statement
     */
    public static class CaseClause {
        private String qualifiedName;

        private String varName;
        private List<Stmt> statements;
        private boolean isDefault;

        public CaseClause(String qualifiedName, String varName, boolean isDefault) {
            this.qualifiedName = qualifiedName;
            this.varName = varName;
            this.isDefault = isDefault;
            this.statements = new ArrayList<>();
        }

        public void addStatement(Stmt stmt) {
            statements.add(stmt);
        }

        public String getQualifiedName() {
            return qualifiedName;
        }

        public String getVarName() {
            return varName;
        }

        public List<Stmt> getStatements() {
            return statements;
        }

        public boolean isDefault() {
            return isDefault;
        }

        @Override
        public String toString() {
            if (isDefault) {
                return "default: " + statements.size() + " stmts";
            }
            return "case " + qualifiedName + " " + varName + ": " + statements.size() + " stmts";
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toString'");
    }
}
