package org.clnlang.compiled.binary.statements;

import org.clnlang.compiled.binary.CStatement;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;

public class CReturnStatement extends CStatement {

    private int[] returnValueRegisters;
    private Types[] returnValueTypes;
    private int[] returnTargetRegisters;

    /**
     * Creates a return statement.
     * 
     * @param returnValueRegisters The local registers containing the values to return
     * @param returnValueTypes The types of the return values
     * @param returnTargetRegisters The target registers (return variable addresses) where values should be stored
     */
    public CReturnStatement(int[] returnValueRegisters, Types[] returnValueTypes, int[] returnTargetRegisters) {
        this.returnValueRegisters = returnValueRegisters;
        this.returnValueTypes = returnValueTypes;
        this.returnTargetRegisters = returnTargetRegisters;
    }

    @Override
    public void execute(ExecutionContext context) {
        LocalContext calleeContext = context.getCurrentLocalContext();
        
        // Step 1: Copy return values to the return variable slots (at the beginning of local context)
        for (int i = 0; i < returnValueRegisters.length; i++) {
            int sourceAddress = returnValueRegisters[i];
            int targetAddress = returnTargetRegisters[i];
            Types type = returnValueTypes[i];
            
            switch (type) {
                case INT:
                    long longValue = calleeContext.getLong(sourceAddress);
                    calleeContext.setLong(targetAddress, longValue);
                    break;
                case DEC:
                    calleeContext.setBigDecimal(targetAddress, calleeContext.getBigDecimal(sourceAddress));
                    break;
                case BOOL:
                    calleeContext.setBoolean(targetAddress, calleeContext.getBoolean(sourceAddress));
                    break;
                case STRING:
                    calleeContext.setString(targetAddress, calleeContext.getString(sourceAddress));
                    break;
                default:
                    throw new UnsupportedOperationException("Return type not yet supported: " + type);
            }
        }
        
        // Step 2: Copy return values from callee's return slots to caller's return registers
        LocalContext callerContext = calleeContext.getCallerContext();
        if (callerContext != null) {
            int[] callerReturnRegisters = calleeContext.getCallerReturnRegisters();
            Types[] callerReturnTypes = calleeContext.getCallerReturnTypes();
            
            for (int i = 0; i < callerReturnRegisters.length; i++) {
                int sourceAddress = returnTargetRegisters[i];
                int targetAddress = callerReturnRegisters[i];
                Types type = callerReturnTypes[i];
                
                switch (type) {
                    case INT:
                        long longValue = calleeContext.getLong(sourceAddress);
                        callerContext.setLong(targetAddress, longValue);
                        break;
                    case DEC:
                        callerContext.setBigDecimal(targetAddress, calleeContext.getBigDecimal(sourceAddress));
                        break;
                    case BOOL:
                        callerContext.setBoolean(targetAddress, calleeContext.getBoolean(sourceAddress));
                        break;
                    case STRING:
                        callerContext.setString(targetAddress, calleeContext.getString(sourceAddress));
                        break;
                    default:
                        throw new UnsupportedOperationException("Return type not yet supported: " + type);
                }
            }
        }
        
        // Step 3: Mark that we've returned and pop the context
        calleeContext.markReturned();
        context.popLocalContext();
    }

    @Override
    public int[] getResults() {
        return returnTargetRegisters;
    }

    @Override
    public Types[] getResultTypes() {
        return returnValueTypes;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
