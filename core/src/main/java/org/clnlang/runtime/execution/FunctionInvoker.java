package org.clnlang.runtime.execution;

import java.math.BigDecimal;
import java.util.List;

import org.clnlang.compile.CompiledExpr;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.context.ExecutionContext;

/**
 * Runtime utility for invoking functions with proper call frame management.
 * Handles the mechanics of function calls: pushing frames, mapping parameters,
 * executing function bodies, and returning values.
 *
 * Parameters/return variables are bound using the type-specific registry index
 * assigned by the compiler (CompilerVisitor.VariableScope) rather than by name,
 * avoiding hash-map lookups and boxing on every call.
 */
public class FunctionInvoker {

    /**
     * Invoke a function, evaluating its arguments in the caller's context and
     * binding them directly into the callee's local registry by index.
     *
     * @param funcDecl The function declaration to invoke
     * @param argExprs The (not yet evaluated) argument expressions, evaluated in the caller's context
     * @param context The execution context
     * @return The first return value, or null if no return value
     * @throws Exception If execution fails
     */
    public static Object invoke(FunctionDeclImpl funcDecl, List<CompiledExpr> argExprs, ExecutionContext context) throws Exception {
        List<FunctionDeclImpl.Parameter> parameters = funcDecl.getParameters();
        if (argExprs.size() != parameters.size()) {
            throw new RuntimeException(
                String.format("Function '%s' expects %d arguments but got %d",
                    funcDecl.getName(), parameters.size(), argExprs.size())
            );
        }

        // Evaluate arguments in the CALLER's context/frame before pushing the new frame.
        // Values are staged per-type to avoid boxing primitives.
        int count = parameters.size();
        long[] longArgs = new long[count];
        boolean[] boolArgs = new boolean[count];
        BigDecimal[] decArgs = new BigDecimal[count];
        String[] stringArgs = new String[count];
        Object[] objArgs = new Object[count];

        for (int i = 0; i < count; i++) {
            FunctionDeclImpl.Parameter param = parameters.get(i);
            CompiledExpr argExpr = argExprs.get(i);
            String type = param.getType();
            if (type != null && !type.contains("[]")) {
                switch (type) {
                    case "int":
                        longArgs[i] = argExpr.longValue(context);
                        continue;
                    case "bool":
                        boolArgs[i] = argExpr.boolValue(context);
                        continue;
                    case "dec":
                    case "decimal":
                        decArgs[i] = argExpr.decimalValue(context);
                        continue;
                    case "string":
                        stringArgs[i] = argExpr.stringValue(context);
                        continue;
                }
            }
            objArgs[i] = argExpr.evaluate(context);
        }

        // Push a new call frame for the function
        context.pushCallFrame(funcDecl.getName());

        try {
            // Initialize return variables to their zero value (registered before params in scope)
            for (FunctionDeclImpl.ReturnVar retVar : funcDecl.getReturnVars()) {
                bindDefaultByIndex(context, retVar);
            }

            // Bind arguments to parameters directly by registry index (constants)
            for (int i = 0; i < count; i++) {
                FunctionDeclImpl.Parameter param = parameters.get(i);
                bindArgByIndex(context, param, longArgs[i], boolArgs[i], decArgs[i], stringArgs[i], objArgs[i]);
            }

            // Execute the function block
            funcDecl.getBlock().execute(context);

            // Get return values
            List<Object> returnValues = context.getReturnValues();

            // Clear the return flag for next call
            context.clearReturn();

            // Pop the call frame
            context.popCallFrame();

            // Return based on number of return values
            if (returnValues != null && !returnValues.isEmpty()) {
                // If multiple return values, return the list
                // If single return value, return just the value
                if (returnValues.size() > 1) {
                    return returnValues;
                } else {
                    return returnValues.get(0);
                }
            }
            return null;

        } catch (Exception e) {
            // Make sure to clean up the call frame on error
            context.popCallFrame();
            throw e;
        }
    }

    private static void bindDefaultByIndex(ExecutionContext context, FunctionDeclImpl.ReturnVar retVar) {
        String type = retVar.getType();
        int index = retVar.getRegistryIndex();
        if (index < 0 || type == null) {
            // Native/stdlib return vars never got a compile-time registry slot; fall
            // back to the legacy name-based path.
            context.getLocalContext().setVariable(retVar.getName(), getDefaultValue(type));
            return;
        }
        if (type.contains("[]")) {
            context.getLocalContext().setObjectByIndex(index, null, true);
            return;
        }
        org.clnlang.compile.types.DecimalTypeInfo decimalTypeInfo = retVar.getDecimalTypeInfo();
        switch (type) {
            case "int":
                context.getLocalContext().setLongByIndex(index, 0L, true);
                break;
            case "bool":
                context.getLocalContext().setBoolByIndex(index, false, true);
                break;
            case "dec":
            case "decimal":
                context.getLocalContext().setDecimalByIndex(index, BigDecimal.ZERO, true, decimalTypeInfo);
                break;
            case "string":
                context.getLocalContext().setStringByIndex(index, "", true);
                break;
            default:
                context.getLocalContext().setObjectByIndex(index, null, true);
                break;
        }
    }

    private static void bindArgByIndex(ExecutionContext context, FunctionDeclImpl.Parameter param,
            long longVal, boolean boolVal, BigDecimal decVal, String stringVal, Object objVal) {
        int index = param.getRegistryIndex();
        String type = param.getType();
        if (index < 0 || type == null) {
            // Native/stdlib parameters never got a compile-time registry slot; fall
            // back to the legacy name-based path.
            Object boxed = type == null ? objVal : switch (type) {
                case "int" -> longVal;
                case "bool" -> boolVal;
                case "dec", "decimal" -> decVal;
                case "string" -> stringVal;
                default -> objVal;
            };
            context.getLocalContext().setConstant(param.getName(), boxed);
            return;
        }
        if (type.contains("[]")) {
            context.getLocalContext().setObjectByIndex(index, objVal, false);
            return;
        }
        switch (type) {
            case "int":
                context.getLocalContext().setLongByIndex(index, longVal, false);
                break;
            case "bool":
                context.getLocalContext().setBoolByIndex(index, boolVal, false);
                break;
            case "dec":
            case "decimal":
                context.getLocalContext().setDecimalByIndex(index, decVal, false, param.getDecimalTypeInfo());
                break;
            case "string":
                context.getLocalContext().setStringByIndex(index, stringVal, false);
                break;
            default:
                context.getLocalContext().setObjectByIndex(index, objVal, false);
                break;
        }
    }

    /**
     * Default (zero) value for a type, used for the name-based fallback path only.
     */
    private static Object getDefaultValue(String type) {
        return switch (type) {
            case "int" -> 0L;
            case "bool" -> false;
            case "string" -> "";
            case "dec", "decimal" -> BigDecimal.ZERO;
            default -> null;
        };
    }
}
