package org.clnlang.compiled.binary.statements;

import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.CFunction;
import org.clnlang.compiled.binary.CStatement;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.context.ExecutionContext;
import org.clnlang.compiled.context.LocalContext;

public class CCallStatement extends CStatement {

    private CFunction fromFunction;
    private CFunction toFunction;
    private int [] argumentRegisters;
    private Types [] argumentTypes;
    private int [] returnRegisters;
    private Types [] returnTypes;

    public CCallStatement(CFunction fromFunction, CFunction toFunction, int[] argumentRegisters, Types[] argumentTypes, int[] returnRegisters, Types[] returnTypes) {
        this.fromFunction = fromFunction;
        this.toFunction = toFunction;
        this.argumentRegisters = argumentRegisters;
        this.argumentTypes = argumentTypes;
        this.returnRegisters = returnRegisters;
        this.returnTypes = returnTypes;
    }

    @Override
    public void execute(ExecutionContext context) {
        // Get caller's local context
        LocalContext callerContext = context.getCurrentLocalContext();
        
        // Step 1: Push new local context for callee
        context.pushLocalContext(toFunction.getMemoryAllocatorDescription());
        LocalContext calleeContext = context.getCurrentLocalContext();
        
        // Store caller return info in callee context for CReturnStatement to use
        calleeContext.setCallerReturnInfo(callerContext, returnRegisters, returnTypes);
        
        // Step 2: Copy arguments from caller to callee's parameter slots
        int[] parameterAddresses = toFunction.getParameters();
        
        for (int i = 0; i < argumentRegisters.length; i++) {
            int sourceAddress = argumentRegisters[i];
            int targetAddress = parameterAddresses[i];
            Types type = argumentTypes[i];
            
            switch (type) {
                case INT:
                    long longValue = callerContext.getLong(sourceAddress);
                    calleeContext.setLong(targetAddress, longValue);
                    break;
                case DEC:
                    calleeContext.setBigDecimal(targetAddress, callerContext.getBigDecimal(sourceAddress));
                    break;
                case BOOL:
                    calleeContext.setBoolean(targetAddress, callerContext.getBoolean(sourceAddress));
                    break;
                case STRING:
                    calleeContext.setString(targetAddress, callerContext.getString(sourceAddress));
                    break;
                default:
                    throw new UnsupportedOperationException("Parameter type not yet supported: " + type);
            }
        }
        
        // Step 3: Execute function instructions
        // CReturnStatement will handle copying return values and popping context
        CExecutable[] instructions = toFunction.getInstructions();
        for (CExecutable instruction : instructions) {
            instruction.execute(context);
            
            // Check if we've returned (context was popped by CReturnStatement)
            if (calleeContext.hasReturned()) {
                break;
            }
        }
        
        // If no explicit return, handle implicit return (copy values and pop context)
        if (!calleeContext.hasReturned()) {
            int[] returnAddresses = toFunction.getReturns();
            Types[] functionReturnTypes = toFunction.getReturnTypes();
            
            for (int i = 0; i < returnRegisters.length; i++) {
                int sourceAddress = returnAddresses[i];
                int targetAddress = returnRegisters[i];
                Types type = functionReturnTypes[i];
                
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
            
            context.popLocalContext();
        }
    }

    @Override
    public int[] getResults() {
        return returnRegisters;
    }

    @Override
    public Types[] getResultTypes() {
        return returnTypes;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
    
}
