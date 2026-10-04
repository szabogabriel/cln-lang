package org.clnlang.compreg.lib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.compreg.runtime.RegisterBank;
import org.clnlang.compreg.runtime.StructValue;

public final class LibraryRegistry {

    public static final class Constant {
        private final String packageName;
        private final String name;
        private final String type;
        private final Object value;

        private Constant(String packageName, String name, String type, Object value) {
            this.packageName = packageName;
            this.name = name;
            this.type = type;
            this.value = value;
        }

        public String getPackageName() { return packageName; }
        public String getName() { return name; }
        public String getType() { return type; }
        public Object getValue() { return value; }
    }

    private final List<CompiledFunction> functions = new ArrayList<>();
    private final List<Constant> constants = new ArrayList<>();
    private final Set<String> registeredNames = new HashSet<>();
    private final Map<String, List<StructValue.FieldLayout>> structLayouts = new LinkedHashMap<>();

    public void registerConstant(String packageName, String name, String type, Object value) {
        if (name == null || name.isBlank() || value == null) {
            throw new IllegalArgumentException("Library constant name and value are required.");
        }
        requireSupportedType(type);
        if (!(type.equals("int") && value instanceof Long)
                && !(type.equals("dec") && value instanceof java.math.BigDecimal)
                && !(type.equals("bool") && value instanceof Boolean)
                && !(type.equals("string") && value instanceof String)) {
            throw new IllegalArgumentException("Library constant '" + name + "' value does not match type '"
                    + type + "'.");
        }
        constants.add(new Constant(packageName == null ? "" : packageName, name, type, value));
    }

    public void registerStruct(String name, List<StructValue.FieldLayout> fields) {
        if (name == null || name.isBlank() || fields == null) {
            throw new IllegalArgumentException("Library struct name and fields are required.");
        }
        List<StructValue.FieldLayout> copiedFields = List.copyOf(fields);
        Set<String> fieldNames = new HashSet<>();
        for (StructValue.FieldLayout field : copiedFields) {
            requireScalarType(field.getType());
            if (!fieldNames.add(field.getName())) {
                throw new IllegalArgumentException("Duplicate field '" + field.getName()
                        + "' in library struct '" + name + "'.");
            }
        }
        if (structLayouts.putIfAbsent(name, copiedFields) != null) {
            throw new IllegalArgumentException("Library struct '" + name + "' is already registered.");
        }
    }

    public void registerFunction(String packageName, String name, List<String> parameterTypes,
            List<String> returnTypes, JavaFunction implementation) {
        registerFunction(packageName, name, parameterTypes, returnTypes, true, implementation);
    }

    public void registerFunction(String packageName, String name, List<String> parameterTypes,
            List<String> returnTypes, boolean exposed, JavaFunction implementation) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Library function name must not be blank.");
        }
        if (implementation == null) {
            throw new IllegalArgumentException("Library function implementation must not be null.");
        }
        String normalizedPackage = packageName == null ? "" : packageName;
        String key = normalizedPackage + "::" + name + parameterTypes;
        if (!registeredNames.add(key)) {
            throw new IllegalArgumentException("Library function '" + key + "' is already registered.");
        }

        List<CompiledFunction.Slot> parameters = createSlots(parameterTypes, "arg");
        List<CompiledFunction.Slot> results = createSlots(returnTypes, "result");
        int[] slotCounts = countSlots(parameterTypes, returnTypes);
        Command body = memory -> implementation.execute(memory, List.copyOf(parameters), List.copyOf(results));
        CompiledFunction function = CompiledFunction.createLibraryFunction(normalizedPackage, name, exposed,
            parameters, results, slotCounts, body);
        functions.add(function);
    }

    public List<CompiledFunction> getFunctions() {
        return List.copyOf(functions);
    }

    public List<Constant> getConstants() {
        return List.copyOf(constants);
    }

    public Map<String, List<StructValue.FieldLayout>> getStructLayouts() {
        return Map.copyOf(structLayouts);
    }

    public void registerStructuredFunction(String packageName, String name, List<String> parameterTypes,
            List<String> returnTypes, StructuredJavaFunction implementation) {
        if (name == null || name.isBlank() || implementation == null) {
            throw new IllegalArgumentException("Library function name and implementation are required.");
        }
        String normalizedPackage = packageName == null ? "" : packageName;
        String key = normalizedPackage + "::" + name + parameterTypes;
        if (!registeredNames.add(key)) {
            throw new IllegalArgumentException("Library function '" + key + "' is already registered.");
        }
        List<CompiledFunction.Slot> parameters = createStructuredSlots(parameterTypes, "arg");
        List<CompiledFunction.Slot> results = createStructuredSlots(returnTypes, "result");
        CompiledFunction function = CompiledFunction.createStructuredLibraryFunction(normalizedPackage, name, true,
                parameters, results, implementation::execute);
        functions.add(function);
    }

    private List<CompiledFunction.Slot> createSlots(List<String> types, String prefix) {
        if (types == null) {
            throw new IllegalArgumentException("Library function " + prefix + " types must not be null.");
        }
        List<CompiledFunction.Slot> slots = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            requireSupportedType(type);
            slots.add(new CompiledFunction.Slot(prefix + i, type, false));
        }
        return slots;
    }

    private List<CompiledFunction.Slot> createStructuredSlots(List<String> types, String prefix) {
        if (types == null) {
            throw new IllegalArgumentException("Library function " + prefix + " types must not be null.");
        }
        List<CompiledFunction.Slot> slots = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            if (bankIndex(type) < 0 && !structLayouts.containsKey(type)) {
                throw new IllegalArgumentException("Unknown structured library type '" + type + "'.");
            }
            slots.add(new CompiledFunction.Slot(prefix + i, type, false));
        }
        return slots;
    }

    private void requireScalarType(String type) {
        if (bankIndex(type) < 0) {
            throw new IllegalArgumentException("Library struct fields must use scalar register types, got '"
                    + type + "'.");
        }
    }

    private void requireSupportedType(String type) {
        if (bankIndex(type) < 0) {
            throw new IllegalArgumentException("Register library functions do not support type '" + type + "'.");
        }
    }

    private int[] countSlots(List<String> parameterTypes, List<String> returnTypes) {
        int[] counts = new int[4];
        for (String type : parameterTypes) {
            counts[bankIndex(type)]++;
        }
        for (String type : returnTypes) {
            counts[bankIndex(type)]++;
        }
        return counts;
    }

    private int bankIndex(String type) {
        RegisterBank bank = RegisterBank.forType(type);
        return bank == null ? -1 : bank.getIndex();
    }
}