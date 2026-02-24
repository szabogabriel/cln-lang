package org.clnlang.interpreted.compile.statement;

import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.compile.types.DecimalTypeInfo;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

/**
 * Compiled representation of a variable declaration statement.
 */
public class VarDeclStmtImpl implements CompiledAction {
    private boolean isVar;
    private String type;
    private String name;
    private CompiledExpr initializer;
    private int index; // Compile-time resolved index (-1 if not resolved)
    private DecimalTypeInfo decimalTypeInfo; // For decimal types with precision/rounding

    public VarDeclStmtImpl(boolean isVar, String type, String name, CompiledExpr initializer, int index, DecimalTypeInfo decimalTypeInfo) {
        this.isVar = isVar;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
        this.index = index;
        this.decimalTypeInfo = decimalTypeInfo != null ? decimalTypeInfo : DecimalTypeInfo.DEFAULT;
    }
    
    public VarDeclStmtImpl(boolean isVar, String type, String name, CompiledExpr initializer, int index) {
        this(isVar, type, name, initializer, index, DecimalTypeInfo.DEFAULT);
    }
    
    // Backward compatibility constructor
    public VarDeclStmtImpl(boolean isVar, String type, String name, CompiledExpr initializer) {
        this(isVar, type, name, initializer, -1, DecimalTypeInfo.DEFAULT);
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

    public CompiledExpr getInitializer() {
        return initializer;
    }
    
    public int getIndex() {
        return index;
    }
    
    public DecimalTypeInfo getDecimalTypeInfo() {
        return decimalTypeInfo;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Try index-based storage first (zero boxing for primitives!)
        if (index >= 0) {
            switch (type) {
                case "int":
                    long longValue = initializer.longValue(context);
                    if (isVar) {
                        context.getLocalContext().setLongByIndex(index, longValue, true);
                    } else {
                        context.getLocalContext().setLongByIndex(index, longValue, false);
                    }
                    return;
                case "bool":
                    boolean boolValue = initializer.boolValue(context);
                    if (isVar) {
                        context.getLocalContext().setBoolByIndex(index, boolValue, true);
                    } else {
                        context.getLocalContext().setBoolByIndex(index, boolValue, false);
                    }
                    return;
                case "dec":
                case "decimal":  // Backward compatibility
                    java.math.BigDecimal decimalValue = initializer.decimalValue(context);
                    // Apply precision and rounding constraints
                    decimalValue = decimalTypeInfo.applyConstraints(decimalValue);
                    if (isVar) {
                        context.getLocalContext().setDecimalByIndex(index, decimalValue, true, decimalTypeInfo);
                    } else {
                        context.getLocalContext().setDecimalByIndex(index, decimalValue, false, decimalTypeInfo);
                    }
                    return;
                case "string":
                    String stringValue = initializer.stringValue(context);
                    if (isVar) {
                        context.getLocalContext().setStringByIndex(index, stringValue, true);
                    } else {
                        context.getLocalContext().setStringByIndex(index, stringValue, false);
                    }
                    return;
                default:
                    // Object types (structs, arrays, etc.)
                    Object objectValue = initializer.evaluate(context);
                    if (isVar) {
                        context.getLocalContext().setObjectByIndex(index, objectValue, true);
                    } else {
                        context.getLocalContext().setObjectByIndex(index, objectValue, false);
                    }
                    return;
            }
        }
        
        // Fallback to name-based storage (backward compatibility)
        Object value = initializer.evaluate(context);
        
        // Apply decimal constraints if applicable
        if ((type.equals("dec") || type.equals("decimal")) && value instanceof java.math.BigDecimal) {
            value = decimalTypeInfo.applyConstraints((java.math.BigDecimal) value);
        }
        
        if (isVar) {
            context.getLocalContext().setVariable(name, value);
        } else {
            context.getLocalContext().setConstant(name, value);
        }
    }
}
