package org.clnlang.compiled.binary.statements;

import java.math.BigDecimal;

import org.clnlang.compiled.binary.CStatement;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.context.ExecutionContext;

/**
 * Statement for assigning values (from constants or registers) to target registers.
 */
public class CAssignStatement extends CStatement {

    private int sourceRegister;
    private int targetRegister;
    private Types type;
    private boolean sourceIsGlobal;
    private boolean targetIsGlobal;
    
    // For constant assignments
    private boolean isConstant;
    private Object constantValue;

    /**
     * Creates an assignment from one register to another.
     */
    public CAssignStatement(int sourceRegister, int targetRegister, Types type, boolean sourceIsGlobal, boolean targetIsGlobal) {
        this.sourceRegister = sourceRegister;
        this.targetRegister = targetRegister;
        this.type = type;
        this.sourceIsGlobal = sourceIsGlobal;
        this.targetIsGlobal = targetIsGlobal;
        this.isConstant = false;
    }

    /**
     * Creates an assignment from a constant value to a register.
     */
    public CAssignStatement(Object constantValue, int targetRegister, Types type, boolean targetIsGlobal) {
        this.constantValue = constantValue;
        this.targetRegister = targetRegister;
        this.type = type;
        this.targetIsGlobal = targetIsGlobal;
        this.isConstant = true;
    }

    @Override
    public void execute(ExecutionContext context) {
        if (isConstant) {
            // Assign constant to target
            switch (type) {
                case INT:
                    long longValue = (Long) constantValue;
                    if (targetIsGlobal) {
                        context.getGlobalContext().setLong(targetRegister, longValue);
                    } else {
                        context.getCurrentLocalContext().setLong(targetRegister, longValue);
                    }
                    break;
                case DEC:
                    BigDecimal decValue = (BigDecimal) constantValue;
                    if (targetIsGlobal) {
                        context.getGlobalContext().setBigDecimal(targetRegister, decValue);
                    } else {
                        context.getCurrentLocalContext().setBigDecimal(targetRegister, decValue);
                    }
                    break;
                case BOOL:
                    boolean boolValue = (Boolean) constantValue;
                    if (targetIsGlobal) {
                        context.getGlobalContext().setBoolean(targetRegister, boolValue);
                    } else {
                        context.getCurrentLocalContext().setBoolean(targetRegister, boolValue);
                    }
                    break;
                case STRING:
                    String stringValue = (String) constantValue;
                    if (targetIsGlobal) {
                        context.getGlobalContext().setString(targetRegister, stringValue);
                    } else {
                        context.getCurrentLocalContext().setString(targetRegister, stringValue);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Type not yet supported: " + type);
            }
        } else {
            // Copy from source register to target register
            switch (type) {
                case INT:
                    long longValue = sourceIsGlobal ?
                        context.getGlobalContext().getLong(sourceRegister) :
                        context.getCurrentLocalContext().getLong(sourceRegister);
                    if (targetIsGlobal) {
                        context.getGlobalContext().setLong(targetRegister, longValue);
                    } else {
                        context.getCurrentLocalContext().setLong(targetRegister, longValue);
                    }
                    break;
                case DEC:
                    BigDecimal decValue = sourceIsGlobal ?
                        context.getGlobalContext().getBigDecimal(sourceRegister) :
                        context.getCurrentLocalContext().getBigDecimal(sourceRegister);
                    if (targetIsGlobal) {
                        context.getGlobalContext().setBigDecimal(targetRegister, decValue);
                    } else {
                        context.getCurrentLocalContext().setBigDecimal(targetRegister, decValue);
                    }
                    break;
                case BOOL:
                    boolean boolValue = sourceIsGlobal ?
                        context.getGlobalContext().getBoolean(sourceRegister) :
                        context.getCurrentLocalContext().getBoolean(sourceRegister);
                    if (targetIsGlobal) {
                        context.getGlobalContext().setBoolean(targetRegister, boolValue);
                    } else {
                        context.getCurrentLocalContext().setBoolean(targetRegister, boolValue);
                    }
                    break;
                case STRING:
                    String stringValue = sourceIsGlobal ?
                        context.getGlobalContext().getString(sourceRegister) :
                        context.getCurrentLocalContext().getString(sourceRegister);
                    if (targetIsGlobal) {
                        context.getGlobalContext().setString(targetRegister, stringValue);
                    } else {
                        context.getCurrentLocalContext().setString(targetRegister, stringValue);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Type not yet supported: " + type);
            }
        }
    }

    @Override
    public int[] getResults() {
        return new int[]{targetRegister};
    }

    @Override
    public Types[] getResultTypes() {
        return new Types[]{type};
    }

    @Override
    public boolean isGlobal() {
        return targetIsGlobal;
    }
}
