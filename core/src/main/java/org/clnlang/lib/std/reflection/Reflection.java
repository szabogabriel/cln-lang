package org.clnlang.lib.std.reflection;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;

public class Reflection implements ClnFunction {

    private final String packageName = "std.reflect";

    // ========== getField ==========

    private void executeGetField(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        String fieldName = (String) context.getLocalContext().getValue("fieldName");

        Object result = null;
        if (s instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> structMap = (Map<String, Object>) s;
            result = structMap.get(fieldName);
        }

        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== setField ==========

    private void executeSetField(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        String fieldName = (String) context.getLocalContext().getValue("fieldName");
        Object value = context.getLocalContext().getValue("value");

        if (!(s instanceof Map)) {
            throw new RuntimeException("setField: argument 's' is not a struct or union instance");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> structMap = (Map<String, Object>) s;
        structMap.put(fieldName, value);
    }

    // ========== Type checks ==========

    private void executeIsStruct(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        boolean result = s instanceof Map;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeGetStructName(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        String result = "";
        if (s instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> structMap = (Map<String, Object>) s;
            Object typeName = structMap.get("__type__");
            if (typeName instanceof String) {
                result = (String) typeName;
            }
        }
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeIsInt(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        boolean result = s instanceof Long;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeIsDec(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        boolean result = s instanceof BigDecimal;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeIsBool(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        boolean result = s instanceof Boolean;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeIsString(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        boolean result = s instanceof String;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== Typed getters ==========

    private void executeGetInt(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        if (!(s instanceof Long)) {
            throw new RuntimeException("getInt: value is not an int");
        }
        long result = (Long) s;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeGetDec(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        if (!(s instanceof BigDecimal)) {
            throw new RuntimeException("getDec: value is not a dec");
        }
        BigDecimal result = (BigDecimal) s;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeGetBool(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        if (!(s instanceof Boolean)) {
            throw new RuntimeException("getBool: value is not a bool");
        }
        boolean result = (Boolean) s;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeGetString(ExecutionContext context) {
        Object s = context.getLocalContext().getValue("s");
        if (!(s instanceof String)) {
            throw new RuntimeException("getString: value is not a string");
        }
        String result = (String) s;
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // getField(Any s, string fieldName) → Any result
        registerFunction(registry, "getField", "Any", "result",
                new String[]{"Any", "string"}, new String[]{"s", "fieldName"},
                this::executeGetField);

        // setField(Any s, string fieldName, Any value) → void
        registerVoidFunction(registry, "setField",
                new String[]{"Any", "string", "Any"}, new String[]{"s", "fieldName", "value"},
                this::executeSetField);

        // isStruct(Any s) → bool
        registerFunction(registry, "isStruct", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeIsStruct);

        // getStructName(Any s) → string
        registerFunction(registry, "getStructName", "string", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeGetStructName);

        // isInt(Any s) → bool
        registerFunction(registry, "isInt", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeIsInt);

        // isDec(Any s) → bool
        registerFunction(registry, "isDec", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeIsDec);

        // isBool(Any s) → bool
        registerFunction(registry, "isBool", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeIsBool);

        // isString(Any s) → bool
        registerFunction(registry, "isString", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeIsString);

        // getInt(Any s) → int
        registerFunction(registry, "getInt", "int", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeGetInt);

        // getDec(Any s) → dec
        registerFunction(registry, "getDec", "dec", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeGetDec);

        // getBool(Any s) → bool
        registerFunction(registry, "getBool", "bool", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeGetBool);

        // getString(Any s) → string
        registerFunction(registry, "getString", "string", "result",
                new String[]{"Any"}, new String[]{"s"},
                this::executeGetString);
    }

    private void registerFunction(Registry registry, String functionName,
                                  String returnType, String returnName,
                                  String[] paramTypes, String[] paramNames,
                                  java.util.function.Consumer<ExecutionContext> executor) {
        FunctionDeclImpl func = new FunctionDeclImpl(functionName, true);
        for (int i = 0; i < paramTypes.length; i++) {
            func.addParameter(paramTypes[i], paramNames[i]);
        }
        func.addReturnVar(returnType, returnName);
        func.setBlock(executor::accept);
        registry.registerFunction(new FullyQualifiedName(packageName, functionName), func);
    }

    private void registerVoidFunction(Registry registry, String functionName,
                                      String[] paramTypes, String[] paramNames,
                                      java.util.function.Consumer<ExecutionContext> executor) {
        FunctionDeclImpl func = new FunctionDeclImpl(functionName, true);
        for (int i = 0; i < paramTypes.length; i++) {
            func.addParameter(paramTypes[i], paramNames[i]);
        }
        func.setBlock(executor::accept);
        registry.registerFunction(new FullyQualifiedName(packageName, functionName), func);
    }
}
