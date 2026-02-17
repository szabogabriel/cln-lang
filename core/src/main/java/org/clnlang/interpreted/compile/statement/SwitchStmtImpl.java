package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        
        // Determine the type name of the expression value
        String valueTypeName = getTypeName(exprValue);
        
        // Try to match against each case clause
        for (CaseClause caseClause : cases) {
            if (caseClause.isDefault()) {
                // Default case always matches if no other case did
                continue;
            }
            
            // Check if this case's type matches the value's type
            if (valueTypeName != null && valueTypeName.equals(caseClause.getQualifiedName())) {
                // Match found! Bind the variable to the value and execute the case body
                String varName = caseClause.getVarName();
                
                // Bind the case variable to the expression value
                context.getLocalContext().setVariable(varName, exprValue);
                
                // Execute all statements in this case
                for (CompiledAction stmt : caseClause.getStatements()) {
                    stmt.execute(context);
                    // Stop if return was encountered
                    if (context.hasReturned()) {
                        return;
                    }
                }
                
                // Match found and executed, exit switch
                return;
            }
        }
        
        // No case matched, execute default if present
        for (CaseClause caseClause : cases) {
            if (caseClause.isDefault()) {
                // Execute default case statements
                for (CompiledAction stmt : caseClause.getStatements()) {
                    stmt.execute(context);
                    // Stop if return was encountered
                    if (context.hasReturned()) {
                        return;
                    }
                }
                return;
            }
        }
        
        // No match and no default - switch completes without action
    }
    
    /**
     * Determine the type name of a value for switch matching.
     * Returns the primitive type name for primitives, or the struct type name for structs.
     */
    private String getTypeName(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Long) {
            return "int";
        }
        if (value instanceof Boolean) {
            return "bool";
        }
        if (value instanceof String) {
            return "string";
        }
        if (value instanceof Map) {
            // For structs, we need to determine the struct type
            // This is a simplified approach - in a real implementation,
            // we would need to store type information with struct instances
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            // Check if there's a "__type__" metadata field
            Object typeInfo = map.get("__type__");
            if (typeInfo instanceof String) {
                return (String) typeInfo;
            }
            // Fall back to generic map type
            return null;
        }
        
        // Unknown type
        return null;
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
