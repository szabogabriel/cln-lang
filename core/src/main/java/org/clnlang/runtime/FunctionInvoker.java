package org.clnlang.runtime;

import org.clnlang.compile.declaration.FunctionDeclImpl;

import java.util.List;

/**
 * Runtime utility for invoking functions with proper call frame management.
 * Handles the mechanics of function calls: pushing frames, mapping parameters,
 * executing function bodies, and returning values.
 */
public class FunctionInvoker {

    /**
     * Invoke a function with the given arguments.
     * 
     * @param funcDecl The function declaration to invoke
     * @param argValues The evaluated argument values
     * @param context The execution context
     * @return The first return value, or null if no return value
     * @throws Exception If execution fails
     */
    public static Object invoke(FunctionDeclImpl funcDecl, List<Object> argValues, ExecutionContext context) throws Exception {
        // Validate argument count
        List<FunctionDeclImpl.Parameter> parameters = funcDecl.getParameters();
        if (argValues.size() != parameters.size()) {
            throw new RuntimeException(
                String.format("Function '%s' expects %d arguments but got %d",
                    funcDecl.getName(), parameters.size(), argValues.size())
            );
        }
        
        // Push a new call frame for the function
        context.pushCallFrame(funcDecl.getName());
        
        try {
            // Map arguments to parameters in the new frame's local context
            for (int i = 0; i < parameters.size(); i++) {
                String paramName = parameters.get(i).getName();
                Object argValue = argValues.get(i);
                context.getLocalContext().setConstant(paramName, argValue);
            }
            
            // Initialize return variables in the local context
            List<FunctionDeclImpl.ReturnVar> returnVars = funcDecl.getReturnVars();
            for (FunctionDeclImpl.ReturnVar retVar : returnVars) {
                String retVarName = retVar.getName();
                Object defaultValue = getDefaultValue(retVar.getType());
                // Return variables are mutable, so use setVariable
                context.getLocalContext().setVariable(retVarName, defaultValue);
            }
            
            // Execute the function block
            funcDecl.getBlock().execute(context);
            
            // Get return values
            List<Object> returnValues = context.getReturnValues();
            
            // Clear the return flag for next call
            context.clearReturn();
            
            // Pop the call frame
            context.popCallFrame();
            
            // Return the first return value (or null if no return)
            if (returnValues != null && !returnValues.isEmpty()) {
                return returnValues.get(0);
            }
            return null;
            
        } catch (Exception e) {
            // Make sure to clean up the call frame on error
            context.popCallFrame();
            throw e;
        }
    }
    
    /**
     * Get the default value for a type.
     * 
     * @param type The type name
     * @return The default value for that type
     */
    private static Object getDefaultValue(String type) {
        return switch (type) {
            case "int" -> 0;
            case "float" -> 0.0;
            case "bool" -> false;
            case "String" -> "";
            default -> null;
        };
    }
}
