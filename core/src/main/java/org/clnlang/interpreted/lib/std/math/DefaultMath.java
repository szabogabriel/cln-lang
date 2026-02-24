package org.clnlang.interpreted.lib.std.math;

import java.math.BigDecimal;

import org.clnlang.interpreted.compile.declaration.FunctionDeclImpl;
import org.clnlang.interpreted.lib.ClnFunction;
import org.clnlang.interpreted.runtime.context.ExecutionContext;
import org.clnlang.interpreted.runtime.execution.Registry;
import org.clnlang.interpreted.runtime.types.FullyQualifiedName;

/**
 * Standard library math functions providing mathematical operations for Cln.
 * Wraps Java's Math class functions.
 */
public class DefaultMath implements ClnFunction {

    private final String packageName = "std.math";

    // ========== Trigonometric Functions ==========

    /**
     * Returns the sine of an angle
     */
    private void executeSin(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = sin(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the cosine of an angle
     */
    private void executeCos(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = cos(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the tangent of an angle
     */
    private void executeTan(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = tan(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the arc sine of a value
     */
    private void executeAsin(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = asin(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the arc cosine of a value
     */
    private void executeAcos(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = acos(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the arc tangent of a value
     */
    private void executeAtan(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = atan(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the angle theta from polar coordinates (r, theta)
     */
    private void executeAtan2(ExecutionContext context) {
        BigDecimal y = (BigDecimal) context.getLocalContext().getValue("y");
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = atan2(y, x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Exponential and Logarithmic Functions ==========

    /**
     * Returns Euler's number e raised to the power of x
     */
    private void executeExp(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = exp(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the natural logarithm (base e) of x
     */
    private void executeLog(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = log(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the base 10 logarithm of x
     */
    private void executeLog10(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = log10(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Power and Root Functions ==========

    /**
     * Returns x raised to the power of y
     */
    private void executePow(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal y = (BigDecimal) context.getLocalContext().getValue("y");
        BigDecimal result = pow(x, y);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the square root of x
     */
    private void executeSqrt(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = sqrt(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the cube root of x
     */
    private void executeCbrt(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = cbrt(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Rounding Functions ==========

    /**
     * Returns the absolute value of x
     */
    private void executeAbs(ExecutionContext context) {
        Object xo = context.getLocalContext().getValue("x");
        if (xo instanceof BigDecimal) {
            BigDecimal x = (BigDecimal) xo;
            BigDecimal result = abs(x);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (xo instanceof Long) {
            long x = (Long) xo;
            long result = Math.abs(x);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else {
            throw new RuntimeException("Unsupported type for abs function: " + xo.getClass());
        }
    }

    /**
     * Returns the smallest integer greater than or equal to x
     */
    private void executeCeil(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = ceil(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the largest integer less than or equal to x
     */
    private void executeFloor(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        BigDecimal result = floor(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Returns the closest integer to x
     */
    private void executeRound(ExecutionContext context) {
        BigDecimal x = (BigDecimal) context.getLocalContext().getValue("x");
        long result = round(x);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Min/Max Functions ==========

    /**
     * Returns the smaller of two values
     */
    private void executeMin(ExecutionContext context) {
        Object ao = context.getLocalContext().getValue("a");
        Object bo = context.getLocalContext().getValue("b");
        if (ao instanceof BigDecimal && bo instanceof BigDecimal) {
            BigDecimal a = (BigDecimal) ao;
            BigDecimal b = (BigDecimal) bo;
            BigDecimal result = min(a, b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof Long && bo instanceof Long) {
            long a = (Long) ao;
            long b = (Long) bo;
            long result = Math.min(a, b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof Long && bo instanceof BigDecimal) {
            long a = (Long) ao;
            BigDecimal b = (BigDecimal) bo;
            BigDecimal result = min(BigDecimal.valueOf(a), b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof BigDecimal && bo instanceof Long) {
            BigDecimal a = (BigDecimal) ao;
            long b = (Long) bo;
            BigDecimal result = min(a, BigDecimal.valueOf(b));
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else {
            throw new RuntimeException("Unsupported types for min function: " +
                ao.getClass() + " and " + bo.getClass());
        }
    }
        

    /**
     * Returns the larger of two values
     */
    private void executeMax(ExecutionContext context) {
        Object ao = context.getLocalContext().getValue("a");
        Object bo = context.getLocalContext().getValue("b");
        if (ao instanceof BigDecimal && bo instanceof BigDecimal) {
            BigDecimal a = (BigDecimal) ao;
            BigDecimal b = (BigDecimal) bo;
            BigDecimal result = max(a, b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof Long && bo instanceof Long) {
            long a = (Long) ao;
            long b = (Long) bo;
            long result = Math.max(a, b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof Long && bo instanceof BigDecimal) {
            long a = (Long) ao;
            BigDecimal b = (BigDecimal) bo;
            BigDecimal result = max(BigDecimal.valueOf(a), b);
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else if (ao instanceof BigDecimal && bo instanceof Long) {
            BigDecimal a = (BigDecimal) ao;
            long b = (Long) bo;
            BigDecimal result = max(a, BigDecimal.valueOf(b));
            context.getCurrentFrame().getLocalContext().setVariable("result", result);
            context.setReturnValues(java.util.Collections.singletonList(result));
        } else {
            throw new RuntimeException("Unsupported types for max function: " +
                ao.getClass() + " and " + bo.getClass());
        }
    }

    // ========== Other Functions ==========

    /**
     * Returns a random value between 0.0 (inclusive) and 1.0 (exclusive)
     */
    private void executeRandom(ExecutionContext context) {
        BigDecimal result = random();
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Converts an angle measured in degrees to radians
     */
    private void executeToRadians(ExecutionContext context) {
        BigDecimal degrees = (BigDecimal) context.getLocalContext().getValue("degrees");
        BigDecimal result = toRadians(degrees);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Converts an angle measured in radians to degrees
     */
    private void executeToDegrees(ExecutionContext context) {
        BigDecimal radians = (BigDecimal) context.getLocalContext().getValue("radians");
        BigDecimal result = toDegrees(radians);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Static Implementation Methods ==========

    public static BigDecimal sin(BigDecimal x) {
        return BigDecimal.valueOf(Math.sin(x.doubleValue()));
    }

    public static BigDecimal cos(BigDecimal x) {
        return BigDecimal.valueOf(Math.cos(x.doubleValue()));
    }

    public static BigDecimal tan(BigDecimal x) {
        return BigDecimal.valueOf(Math.tan(x.doubleValue()));
    }

    public static BigDecimal asin(BigDecimal x) {
        return BigDecimal.valueOf(Math.asin(x.doubleValue()));
    }

    public static BigDecimal acos(BigDecimal x) {
        return BigDecimal.valueOf(Math.acos(x.doubleValue()));
    }

    public static BigDecimal atan(BigDecimal x) {
        return BigDecimal.valueOf(Math.atan(x.doubleValue()));
    }

    public static BigDecimal atan2(BigDecimal y, BigDecimal x) {
        return BigDecimal.valueOf(Math.atan2(y.doubleValue(), x.doubleValue()));
    }

    public static BigDecimal exp(BigDecimal x) {
        return BigDecimal.valueOf(Math.exp(x.doubleValue()));
    }

    public static BigDecimal log(BigDecimal x) {
        return BigDecimal.valueOf(Math.log(x.doubleValue()));
    }

    public static BigDecimal log10(BigDecimal x) {
        return BigDecimal.valueOf(Math.log10(x.doubleValue()));
    }

    public static BigDecimal pow(BigDecimal x, BigDecimal y) {
        return BigDecimal.valueOf(Math.pow(x.doubleValue(), y.doubleValue()));
    }

    public static BigDecimal sqrt(BigDecimal x) {
        return BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
    }

    public static BigDecimal cbrt(BigDecimal x) {
        return BigDecimal.valueOf(Math.cbrt(x.doubleValue()));
    }

    public static BigDecimal abs(BigDecimal x) {
        return x.abs();
    }

    public static BigDecimal ceil(BigDecimal x) {
        return x.setScale(0, java.math.RoundingMode.CEILING);
    }

    public static BigDecimal floor(BigDecimal x) {
        return x.setScale(0, java.math.RoundingMode.FLOOR);
    }

    public static long round(BigDecimal x) {
        return x.setScale(0, java.math.RoundingMode.HALF_UP).longValue();
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static BigDecimal random() {
        return BigDecimal.valueOf(Math.random());
    }

    public static BigDecimal toRadians(BigDecimal degrees) {
        return BigDecimal.valueOf(Math.toRadians(degrees.doubleValue()));
    }

    public static BigDecimal toDegrees(BigDecimal radians) {
        return BigDecimal.valueOf(Math.toDegrees(radians.doubleValue()));
    }

    // ========== Helper Methods for Registration ==========

    /**
     * Helper method to register a math function with a return value
     */
    private void registerFunction(Registry registry, String functionName,
                                  String returnType, String returnName,
                                  String[] paramTypes, String[] paramNames,
                                  java.util.function.Consumer<ExecutionContext> executor) {
        FunctionDeclImpl func = new FunctionDeclImpl(functionName, true);

        // Add parameters
        for (int i = 0; i < paramTypes.length; i++) {
            func.addParameter(paramTypes[i], paramNames[i]);
        }

        // Add return variable
        func.addReturnVar(returnType, returnName);

        // Set the execution block
        func.setBlock(executor::accept);

        // Register the function
        registry.registerFunction(new FullyQualifiedName(packageName, functionName), func);
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // Trigonometric functions - single parameter (x)
        registerFunction(registry, "sin", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeSin);
        registerFunction(registry, "cos", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeCos);
        registerFunction(registry, "tan", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeTan);
        registerFunction(registry, "asin", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeAsin);
        registerFunction(registry, "acos", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeAcos);
        registerFunction(registry, "atan", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeAtan);
        
        // atan2 - two parameters (y, x)
        registerFunction(registry, "atan2", "decimal", "result",
                        new String[]{"decimal", "decimal"}, new String[]{"y", "x"}, this::executeAtan2);

        // Exponential and logarithmic functions - single parameter
        registerFunction(registry, "exp", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeExp);
        registerFunction(registry, "log", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeLog);
        registerFunction(registry, "log10", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeLog10);

        // Power and root functions
        registerFunction(registry, "pow", "decimal", "result",
                        new String[]{"decimal", "decimal"}, new String[]{"x", "y"}, this::executePow);
        registerFunction(registry, "sqrt", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeSqrt);
        registerFunction(registry, "cbrt", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeCbrt);

        // Rounding functions
        registerFunction(registry, "abs", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeAbs);
        registerFunction(registry, "ceil", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeCeil);
        registerFunction(registry, "floor", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeFloor);
        registerFunction(registry, "round", "int", "result",
                        new String[]{"decimal"}, new String[]{"x"}, this::executeRound);

        // Min/Max functions - two parameters
        registerFunction(registry, "min", "decimal", "result",
                        new String[]{"decimal", "decimal"}, new String[]{"a", "b"}, this::executeMin);
        registerFunction(registry, "max", "decimal", "result",
                        new String[]{"decimal", "decimal"}, new String[]{"a", "b"}, this::executeMax);

        // Other functions
        registerFunction(registry, "random", "decimal", "result",
                        new String[]{}, new String[]{}, this::executeRandom);
        registerFunction(registry, "toRadians", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"degrees"}, this::executeToRadians);
        registerFunction(registry, "toDegrees", "decimal", "result",
                        new String[]{"decimal"}, new String[]{"radians"}, this::executeToDegrees);
    }
}
