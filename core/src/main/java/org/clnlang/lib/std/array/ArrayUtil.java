package org.clnlang.lib.std.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;

/**
 * Standard library array utility functions providing array operations for Cln.
 */
public class ArrayUtil implements ClnFunction {

    private final String packageName = "std.array";

    // ========== Array Creation and Copying ==========

    /**
     * Create a new array with specified size
     */
    private void executeNewArray(ExecutionContext context) {
        long size = (long) context.getLocalContext().getValue("size");
        List<Object> result = newArray(size);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Create a new 2D array (array of arrays) with specified dimensions
     */
    private void executeNewArray2D(ExecutionContext context) {
        long rows = (long) context.getLocalContext().getValue("rows");
        long cols = (long) context.getLocalContext().getValue("cols");
        List<Object> result = newArray2D(rows, cols);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Create a new 3D array (array of arrays of arrays) with specified dimensions
     */
    private void executeNewArray3D(ExecutionContext context) {
        long depth = (long) context.getLocalContext().getValue("depth");
        long rows = (long) context.getLocalContext().getValue("rows");
        long cols = (long) context.getLocalContext().getValue("cols");
        List<Object> result = newArray3D(depth, rows, cols);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Copy array (shallow copy)
     */
    private void executeCopy(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        List<Object> result = copy(arr);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Deep copy array (recursively copies nested arrays)
     */
    private void executeDeepCopy(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        List<Object> result = deepCopy(arr);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Copy array with specified range
     */
    private void executeCopyRange(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        long start = (long) context.getLocalContext().getValue("start");
        long end = (long) context.getLocalContext().getValue("end");
        List<Object> result = copyRange(arr, start, end);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Array Information ==========

    /**
     * Get array length
     */
    private void executeLength(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        long result = length(arr);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if array is empty
     */
    private void executeIsEmpty(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        boolean result = isEmpty(arr);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Array Search ==========

    /**
     * Find index of element in array
     */
    private void executeIndexOf(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        Object element = context.getLocalContext().getValue("element");
        long result = indexOf(arr, element);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Find last index of element in array
     */
    private void executeLastIndexOf(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        Object element = context.getLocalContext().getValue("element");
        long result = lastIndexOf(arr, element);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if array contains element
     */
    private void executeContains(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        Object element = context.getLocalContext().getValue("element");
        boolean result = contains(arr, element);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Array Modification ==========

    /**
     * Fill array with value
     */
    private void executeFill(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        Object value = context.getLocalContext().getValue("value");
        fill(arr, value);
    }

    /**
     * Reverse array
     */
    private void executeReverse(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        reverse(arr);
    }

    // ========== Array Comparison ==========

    /**
     * Check if two arrays are equal
     */
    private void executeEquals(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr1 = (List<Object>) context.getLocalContext().getValue("arr1");
        @SuppressWarnings("unchecked")
        List<Object> arr2 = (List<Object>) context.getLocalContext().getValue("arr2");
        boolean result = equals(arr1, arr2);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Array Concatenation ==========

    /**
     * Concatenate two arrays
     */
    private void executeConcat(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr1 = (List<Object>) context.getLocalContext().getValue("arr1");
        @SuppressWarnings("unchecked")
        List<Object> arr2 = (List<Object>) context.getLocalContext().getValue("arr2");
        List<Object> result = concat(arr1, arr2);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Array Slice ==========

    /**
     * Get slice of array
     */
    private void executeSlice(ExecutionContext context) {
        @SuppressWarnings("unchecked")
        List<Object> arr = (List<Object>) context.getLocalContext().getValue("arr");
        long start = (long) context.getLocalContext().getValue("start");
        long length = (long) context.getLocalContext().getValue("length");
        List<Object> result = slice(arr, start, length);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Static Implementation Methods ==========

    public static List<Object> newArray(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Array size cannot be negative");
        }
        List<Object> arr = new ArrayList<>((int) size);
        for (int i = 0; i < size; i++) {
            arr.add(null);
        }
        return arr;
    }

    public static List<Object> newArray2D(long rows, long cols) {
        if (rows < 0 || cols < 0) {
            throw new IllegalArgumentException("Array dimensions cannot be negative");
        }
        List<Object> outer = new ArrayList<>((int) rows);
        for (int i = 0; i < rows; i++) {
            outer.add(newArray(cols));
        }
        return outer;
    }

    public static List<Object> newArray3D(long depth, long rows, long cols) {
        if (depth < 0 || rows < 0 || cols < 0) {
            throw new IllegalArgumentException("Array dimensions cannot be negative");
        }
        List<Object> outermost = new ArrayList<>((int) depth);
        for (int i = 0; i < depth; i++) {
            outermost.add(newArray2D(rows, cols));
        }
        return outermost;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> deepCopy(List<Object> arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        List<Object> result = new ArrayList<>(arr.size());
        for (Object element : arr) {
            result.add(deepCopyElement(element));
        }
        return result;
    }

    /**
     * Recursively deep-copies a single element.
     * Handles nested arrays (List), struct/union instances (Map), and
     * primitive values (Long, Boolean, String, BigDecimal) which are immutable.
     */
    @SuppressWarnings("unchecked")
    private static Object deepCopyElement(Object element) {
        if (element instanceof List) {
            return deepCopy((List<Object>) element);
        }
        if (element instanceof Map) {
            Map<String, Object> original = (Map<String, Object>) element;
            Map<String, Object> copy = new HashMap<>(original.size());
            for (Map.Entry<String, Object> entry : original.entrySet()) {
                copy.put(entry.getKey(), deepCopyElement(entry.getValue()));
            }
            return copy;
        }
        // Primitives (Long, Boolean, String, BigDecimal) are immutable – share safely
        return element;
    }

    public static List<Object> copy(List<Object> arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(arr);
    }

    public static List<Object> copyRange(List<Object> arr, long start, long end) {
        if (arr == null || start < 0 || start > arr.size()) {
            return new ArrayList<>();
        }
        int startIdx = (int) start;
        int endIdx = (int) Math.min(end, arr.size());
        if (startIdx >= endIdx) {
            return new ArrayList<>();
        }
        return new ArrayList<>(arr.subList(startIdx, endIdx));
    }

    public static long length(List<Object> arr) {
        return arr == null ? 0 : arr.size();
    }

    public static boolean isEmpty(List<Object> arr) {
        return arr == null || arr.isEmpty();
    }

    public static long indexOf(List<Object> arr, Object element) {
        if (arr == null) {
            return -1;
        }
        for (int i = 0; i < arr.size(); i++) {
            Object item = arr.get(i);
            if ((item == null && element == null) || (item != null && item.equals(element))) {
                return i;
            }
        }
        return -1;
    }

    public static long lastIndexOf(List<Object> arr, Object element) {
        if (arr == null) {
            return -1;
        }
        for (int i = arr.size() - 1; i >= 0; i--) {
            Object item = arr.get(i);
            if ((item == null && element == null) || (item != null && item.equals(element))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(List<Object> arr, Object element) {
        return indexOf(arr, element) >= 0;
    }

    public static void fill(List<Object> arr, Object value) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            arr.set(i, value);
        }
    }

    public static void reverse(List<Object> arr) {
        if (arr == null || arr.size() <= 1) {
            return;
        }
        int left = 0;
        int right = arr.size() - 1;
        while (left < right) {
            Object temp = arr.get(left);
            arr.set(left, arr.get(right));
            arr.set(right, temp);
            left++;
            right--;
        }
    }

    public static boolean equals(List<Object> arr1, List<Object> arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        if (arr1.size() != arr2.size()) {
            return false;
        }
        for (int i = 0; i < arr1.size(); i++) {
            Object item1 = arr1.get(i);
            Object item2 = arr2.get(i);
            if ((item1 == null && item2 != null) || (item1 != null && !item1.equals(item2))) {
                return false;
            }
        }
        return true;
    }

    public static List<Object> concat(List<Object> arr1, List<Object> arr2) {
        List<Object> result = new ArrayList<>();
        if (arr1 != null) {
            result.addAll(arr1);
        }
        if (arr2 != null) {
            result.addAll(arr2);
        }
        return result;
    }

    public static List<Object> slice(List<Object> arr, long start, long length) {
        if (arr == null || start < 0 || start >= arr.size() || length <= 0) {
            return new ArrayList<>();
        }
        int startIdx = (int) start;
        int endIdx = (int) Math.min(start + length, arr.size());
        return new ArrayList<>(arr.subList(startIdx, endIdx));
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // Array creation and copying
        registerFunction(registry, "newArray", "Array", "result",
                        new String[]{"int"}, new String[]{"size"}, this::executeNewArray);
        
        registerFunction(registry, "newArray2D", "Array", "result",
                        new String[]{"int", "int"}, new String[]{"rows", "cols"}, this::executeNewArray2D);

        registerFunction(registry, "newArray3D", "Array", "result",
                        new String[]{"int", "int", "int"}, new String[]{"depth", "rows", "cols"}, this::executeNewArray3D);

        registerFunction(registry, "copy", "Array", "result",
                        new String[]{"Array"}, new String[]{"arr"}, this::executeCopy);
        
        registerFunction(registry, "deepCopy", "Array", "result",
                        new String[]{"Array"}, new String[]{"arr"}, this::executeDeepCopy);

        registerFunction(registry, "copyRange", "Array", "result",
                        new String[]{"Array", "int", "int"}, new String[]{"arr", "start", "end"}, this::executeCopyRange);

        // Array information
        registerFunction(registry, "length", "int", "result",
                        new String[]{"Array"}, new String[]{"arr"}, this::executeLength);
        
        registerFunction(registry, "isEmpty", "bool", "result",
                        new String[]{"Array"}, new String[]{"arr"}, this::executeIsEmpty);

        // Array search
        registerFunction(registry, "indexOf", "int", "result",
                        new String[]{"Array", "Object"}, new String[]{"arr", "element"}, this::executeIndexOf);
        
        registerFunction(registry, "lastIndexOf", "int", "result",
                        new String[]{"Array", "Object"}, new String[]{"arr", "element"}, this::executeLastIndexOf);
        
        registerFunction(registry, "contains", "bool", "result",
                        new String[]{"Array", "Object"}, new String[]{"arr", "element"}, this::executeContains);

        // Array modification (void functions)
        registerVoidFunction(registry, "fill",
                            new String[]{"Array", "Object"}, new String[]{"arr", "value"}, this::executeFill);
        
        registerVoidFunction(registry, "reverse",
                            new String[]{"Array"}, new String[]{"arr"}, this::executeReverse);

        // Array comparison
        registerFunction(registry, "equals", "bool", "result",
                        new String[]{"Array", "Array"}, new String[]{"arr1", "arr2"}, this::executeEquals);

        // Array concatenation
        registerFunction(registry, "concat", "Array", "result",
                        new String[]{"Array", "Array"}, new String[]{"arr1", "arr2"}, this::executeConcat);

        // Array slice
        registerFunction(registry, "slice", "Array", "result",
                        new String[]{"Array", "int", "int"}, new String[]{"arr", "start", "length"}, this::executeSlice);
    }

    /**
     * Helper method to register a function with return value
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

    /**
     * Helper method to register a void function (no return value)
     */
    private void registerVoidFunction(Registry registry, String functionName,
                                      String[] paramTypes, String[] paramNames,
                                      java.util.function.Consumer<ExecutionContext> executor) {
        FunctionDeclImpl func = new FunctionDeclImpl(functionName, true);

        // Add parameters
        for (int i = 0; i < paramTypes.length; i++) {
            func.addParameter(paramTypes[i], paramNames[i]);
        }

        // Set the execution block
        func.setBlock(executor::accept);

        // Register the function
        registry.registerFunction(new FullyQualifiedName(packageName, functionName), func);
    }
}
