package org.clnlang.compile.expression;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Compiled representation of increment/decrement expressions (++/--).
 * These are special because they modify the variable they operate on.
 */
public class IncrementExprImpl implements CompiledExpr {
    private CompiledExpr operand;
    private String operator; // "++" or "--"
    private boolean isPrefix; // true for ++x, false for x++
    
    public IncrementExprImpl(CompiledExpr operand, String operator, boolean isPrefix) {
        this.operand = operand;
        this.operator = operator;
        this.isPrefix = isPrefix;
    }
    
    public CompiledExpr getOperand() {
        return operand;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public boolean isPrefix() {
        return isPrefix;
    }
    
    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        // The operand must be an identifier (simple variable)
        if (!(operand instanceof IdentifierExprImpl)) {
            throw new RuntimeException("Increment/decrement operators can only be applied to variables");
        }
        
        IdentifierExprImpl id = (IdentifierExprImpl) operand;
        String varName = id.getName();
        
        // Get current value
        Object currentValue = operand.evaluate(context);
        
        if (currentValue == null) {
            throw new RuntimeException("Variable '" + varName + "' is null");
        }
        
        // Handle both Integer and Long (Java int can be stored as Long internally)
        long longValue;
        if (currentValue instanceof Integer) {
            longValue = ((Integer) currentValue).longValue();
        } else if (currentValue instanceof Long) {
            longValue = (Long) currentValue;
        } else {
            throw new RuntimeException("Increment/decrement operators can only be applied to integer values (got " + 
                currentValue.getClass().getName() + " for variable '" + varName + "')");
        }
        
        long newValue;
        
        // Calculate new value
        if ("++".equals(operator)) {
            newValue = longValue + 1;
        } else if ("--".equals(operator)) {
            newValue = longValue - 1;
        } else {
            throw new RuntimeException("Unknown increment/decrement operator: " + operator);
        }
        
        // Update the variable - prefer index-based access for variables stored by index
        if (id.getIndex() >= 0 && "int".equals(id.getType())) {
            boolean updated = context.getLocalContext().updateLongByIndex(id.getIndex(), newValue);
            if (!updated) {
                throw new RuntimeException("Cannot increment/decrement undefined or constant variable: " + varName);
            }
        } else {
            boolean updated = context.getLocalContext().updateVariable(varName, newValue);
            if (!updated) {
                updated = context.getGlobalContext().updateGlobalVariable(varName, newValue);
                if (!updated) {
                    throw new RuntimeException("Cannot increment/decrement undefined or constant variable: " + varName);
                }
            }
        }
        
        // Return appropriate value based on prefix/postfix
        if (isPrefix) {
            return newValue; // ++x returns new value
        } else {
            return longValue; // x++ returns old value
        }
    }
    
    @Override
    public long longValue(ExecutionContext context) throws Exception {
        // Optimized path for integer increment/decrement using index-based access (zero boxing!)
        if (!(operand instanceof IdentifierExprImpl)) {
            throw new RuntimeException("Increment/decrement operators can only be applied to variables");
        }
        
        IdentifierExprImpl id = (IdentifierExprImpl) operand;
        
        // Try index-based access if available (zero boxing!)
        if (id.getIndex() >= 0 && "int".equals(id.getType())) {
            int index = id.getIndex();
            long currentValue = context.getLocalContext().getLongByIndex(index);
            
            long newValue;
            if ("++".equals(operator)) {
                newValue = currentValue + 1;
            } else if ("--".equals(operator)) {
                newValue = currentValue - 1;
            } else {
                throw new RuntimeException("Unknown increment/decrement operator: " + operator);
            }
            
            // Update the variable using index (zero boxing!)
            context.getLocalContext().updateLongByIndex(index, newValue);
            
            // Return appropriate value based on prefix/postfix
            if (isPrefix) {
                return newValue; // ++x returns new value
            } else {
                return currentValue; // x++ returns old value
            }
        }
        
        // Fallback to name-based access
        return (long) evaluate(context);
    }
}
