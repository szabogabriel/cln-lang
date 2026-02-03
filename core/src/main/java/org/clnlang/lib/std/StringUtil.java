package org.clnlang.lib.std;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.clnlang.runtime.execution.Registry;

/**
 * Standard library string utility functions providing C-like string operations.
 */
public class StringUtil implements ClnFunction {

    private final String packageName = "std.str";

    // ========== String Conversion Functions ==========

    /**
     * Convert integer to string
     */
    private void executeIntToStr(ExecutionContext context) {
        long value = (long) context.getLocalContext().getValue("value");
        String result = intToStr(value);
        // Set the return value in the local context AND mark as returned
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Convert string to integer
     */
    private void executeStrToInt(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        long result = strToInt(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Convert boolean to string
     */
    private void executeBoolToStr(ExecutionContext context) {
        boolean value = (boolean) context.getLocalContext().getValue("value");
        String result = boolToStr(value);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== String Manipulation Functions ==========

    /**
     * Get string length
     */
    private void executeStrLen(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        long result = strLen(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Concatenate two strings
     */
    private void executeStrCat(ExecutionContext context) {
        String str1 = (String) context.getLocalContext().getValue("str1");
        String str2 = (String) context.getLocalContext().getValue("str2");
        String result = strCat(str1, str2);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Get substring
     */
    private void executeSubStr(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        long start = (long) context.getLocalContext().getValue("start");
        long length = (long) context.getLocalContext().getValue("length");
        String result = subStr(str, start, length);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Get character at index
     */
    private void executeCharAt(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        long index = (long) context.getLocalContext().getValue("index");
        String result = charAt(str, index);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Find first occurrence of substring
     */
    private void executeIndexOf(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String search = (String) context.getLocalContext().getValue("search");
        long result = indexOf(str, search);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Find last occurrence of substring
     */
    private void executeLastIndexOf(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String search = (String) context.getLocalContext().getValue("search");
        long result = lastIndexOf(str, search);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== String Comparison Functions ==========

    /**
     * Compare two strings (returns 0 if equal, <0 if str1 < str2, >0 if str1 > str2)
     */
    private void executeStrCmp(ExecutionContext context) {
        String str1 = (String) context.getLocalContext().getValue("str1");
        String str2 = (String) context.getLocalContext().getValue("str2");
        long result = strCmp(str1, str2);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if strings are equal
     */
    private void executeStrEq(ExecutionContext context) {
        String str1 = (String) context.getLocalContext().getValue("str1");
        String str2 = (String) context.getLocalContext().getValue("str2");
        boolean result = strEq(str1, str2);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if string starts with prefix
     */
    private void executeStartsWith(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String prefix = (String) context.getLocalContext().getValue("prefix");
        boolean result = startsWith(str, prefix);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if string ends with suffix
     */
    private void executeEndsWith(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String suffix = (String) context.getLocalContext().getValue("suffix");
        boolean result = endsWith(str, suffix);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== String Transformation Functions ==========

    /**
     * Convert string to uppercase
     */
    private void executeToUpper(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String result = toUpper(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Convert string to lowercase
     */
    private void executeToLower(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String result = toLower(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Trim whitespace from both ends
     */
    private void executeTrim(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String result = trim(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Replace all occurrences of a substring
     */
    private void executeReplace(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        String oldStr = (String) context.getLocalContext().getValue("oldStr");
        String newStr = (String) context.getLocalContext().getValue("newStr");
        String result = replace(str, oldStr, newStr);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    /**
     * Check if string is empty
     */
    private void executeIsEmpty(ExecutionContext context) {
        String str = (String) context.getLocalContext().getValue("str");
        boolean result = isEmpty(str);
        context.getCurrentFrame().getLocalContext().setVariable("result", result);
        context.setReturnValues(java.util.Collections.singletonList(result));
    }

    // ========== Static Implementation Methods ==========

    public static String intToStr(long value) {
        return String.valueOf(value);
    }

    public static long strToInt(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0; // C-like behavior: return 0 on error
        }
    }

    public static String boolToStr(boolean value) {
        return value ? "true" : "false";
    }

    public static long strLen(String str) {
        return str == null ? 0 : str.length();
    }

    public static String strCat(String str1, String str2) {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";
        return str1 + str2;
    }

    public static String subStr(String str, long start, long length) {
        if (str == null || start < 0 || start >= str.length()) {
            return "";
        }
        int end = (int) Math.min(start + length, str.length());
        return str.substring((int) start, end);
    }

    public static String charAt(String str, long index) {
        if (str == null || index < 0 || index >= str.length()) {
            return "";
        }
        return String.valueOf(str.charAt((int) index));
    }

    public static long indexOf(String str, String search) {
        if (str == null || search == null) {
            return -1;
        }
        return str.indexOf(search);
    }

    public static long lastIndexOf(String str, String search) {
        if (str == null || search == null) {
            return -1;
        }
        return str.lastIndexOf(search);
    }

    public static long strCmp(String str1, String str2) {
        if (str1 == null && str2 == null) return 0;
        if (str1 == null) return -1;
        if (str2 == null) return 1;
        return str1.compareTo(str2);
    }

    public static boolean strEq(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2);
    }

    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) return false;
        return str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) return false;
        return str.endsWith(suffix);
    }

    public static String toUpper(String str) {
        return str == null ? "" : str.toUpperCase();
    }

    public static String toLower(String str) {
        return str == null ? "" : str.toLowerCase();
    }

    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    public static String replace(String str, String oldStr, String newStr) {
        if (str == null || oldStr == null || newStr == null) {
            return str == null ? "" : str;
        }
        return str.replace(oldStr, newStr);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        // Conversion functions
        registerFunction(registry, "intToStr", "String", "result", 
                        new String[]{"int"}, new String[]{"value"}, this::executeIntToStr);
        
        registerFunction(registry, "strToInt", "int", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeStrToInt);
        
        registerFunction(registry, "boolToStr", "String", "result",
                        new String[]{"bool"}, new String[]{"value"}, this::executeBoolToStr);

        // String manipulation
        registerFunction(registry, "strLen", "int", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeStrLen);
        
        registerFunction(registry, "strCat", "String", "result",
                        new String[]{"String", "String"}, new String[]{"str1", "str2"}, this::executeStrCat);
        
        registerFunction(registry, "subStr", "String", "result",
                        new String[]{"String", "int", "int"}, new String[]{"str", "start", "length"}, this::executeSubStr);
        
        registerFunction(registry, "charAt", "String", "result",
                        new String[]{"String", "int"}, new String[]{"str", "index"}, this::executeCharAt);
        
        registerFunction(registry, "indexOf", "int", "result",
                        new String[]{"String", "String"}, new String[]{"str", "search"}, this::executeIndexOf);
        
        registerFunction(registry, "lastIndexOf", "int", "result",
                        new String[]{"String", "String"}, new String[]{"str", "search"}, this::executeLastIndexOf);

        // String comparison
        registerFunction(registry, "strCmp", "int", "result",
                        new String[]{"String", "String"}, new String[]{"str1", "str2"}, this::executeStrCmp);
        
        registerFunction(registry, "strEq", "bool", "result",
                        new String[]{"String", "String"}, new String[]{"str1", "str2"}, this::executeStrEq);
        
        registerFunction(registry, "startsWith", "bool", "result",
                        new String[]{"String", "String"}, new String[]{"str", "prefix"}, this::executeStartsWith);
        
        registerFunction(registry, "endsWith", "bool", "result",
                        new String[]{"String", "String"}, new String[]{"str", "suffix"}, this::executeEndsWith);

        // String transformation
        registerFunction(registry, "toUpper", "String", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeToUpper);
        
        registerFunction(registry, "toLower", "String", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeToLower);
        
        registerFunction(registry, "trim", "String", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeTrim);
        
        registerFunction(registry, "replace", "String", "result",
                        new String[]{"String", "String", "String"}, new String[]{"str", "oldStr", "newStr"}, this::executeReplace);
        
        registerFunction(registry, "isEmpty", "bool", "result",
                        new String[]{"String"}, new String[]{"str"}, this::executeIsEmpty);
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
}

