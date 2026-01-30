package org.clnlang.compile.statement;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.compile.expression.CompiledExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a switch statement.
 */
public class SwitchStmtImpl implements CompiledAction {
    private CompiledExpr expression;
    private List<CaseClause> cases;

    public SwitchStmtImpl(CompiledExpr expression) {
        this.expression = expression;
        this.cases = new ArrayList<>();
    }

    public void addCase(CaseClause caseClause) {
        cases.add(caseClause);
    }

    public CompiledExpr getExpression() {
        return expression;
    }

    public List<CaseClause> getCases() {
        return cases;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        Object exprValue = expression.evaluate(context);
        // Match against cases and execute matching case
    }

    /**
     * Represents a single case clause in a switch statement
     */
    public static class CaseClause {
        private String qualifiedName;
        private String varName;
        private List<CompiledAction> statements;
        private boolean isDefault;

        public CaseClause(String qualifiedName, String varName, boolean isDefault) {
            this.qualifiedName = qualifiedName;
            this.varName = varName;
            this.isDefault = isDefault;
            this.statements = new ArrayList<>();
        }

        public void addStatement(CompiledAction stmt) {
            statements.add(stmt);
        }

        public String getQualifiedName() {
            return qualifiedName;
        }

        public String getVarName() {
            return varName;
        }

        public List<CompiledAction> getStatements() {
            return statements;
        }

        public boolean isDefault() {
            return isDefault;
        }
    }
}
