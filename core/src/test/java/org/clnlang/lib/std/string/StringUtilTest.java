package org.clnlang.lib.std.string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the StringUtil standard library class.
 */
class StringUtilTest {

    private StringUtil stringUtil;
    private ExecutionContext context;
    private Registry registry;

    @BeforeEach
    void setUp() {
        stringUtil = new StringUtil();
        context = new ExecutionContext();
        registry = new Registry();
    }

    // ========== String Conversion Functions Tests ==========

    @Test
    void testIntToStr() {
        assertEquals("0", StringUtil.intToStr(0));
        assertEquals("42", StringUtil.intToStr(42));
        assertEquals("-42", StringUtil.intToStr(-42));
        assertEquals("2147483647", StringUtil.intToStr(2147483647));
        assertEquals("-2147483648", StringUtil.intToStr(-2147483648));
        assertEquals("9223372036854775807", StringUtil.intToStr(Long.MAX_VALUE));
        assertEquals("-9223372036854775808", StringUtil.intToStr(Long.MIN_VALUE));
    }

    @Test
    void testDecToString() {
        assertEquals("0", StringUtil.decToString(BigDecimal.ZERO));
        assertEquals("42.5", StringUtil.decToString(new BigDecimal("42.5")));
        assertEquals("-42.5", StringUtil.decToString(new BigDecimal("-42.5")));
        assertEquals("3.14159", StringUtil.decToString(new BigDecimal("3.14159")));
        assertEquals("0.001", StringUtil.decToString(new BigDecimal("0.001")));
        assertEquals("1000000.99", StringUtil.decToString(new BigDecimal("1000000.99")));
    }

    @Test
    void testStrToInt() {
        assertEquals(0, StringUtil.strToInt("0"));
        assertEquals(42, StringUtil.strToInt("42"));
        assertEquals(-42, StringUtil.strToInt("-42"));
        assertEquals(2147483647, StringUtil.strToInt("2147483647"));
        assertEquals(-2147483648, StringUtil.strToInt("-2147483648"));
        
        // Test error cases - should return 0
        assertEquals(0, StringUtil.strToInt(""));
        assertEquals(0, StringUtil.strToInt("abc"));
        assertEquals(0, StringUtil.strToInt("12.34"));
        assertEquals(0, StringUtil.strToInt("12abc"));
        assertEquals(0, StringUtil.strToInt("  "));
    }

    @Test
    void testBoolToStr() {
        assertEquals("true", StringUtil.boolToStr(true));
        assertEquals("false", StringUtil.boolToStr(false));
    }

    // ========== String Manipulation Functions Tests ==========

    @Test
    void testStrLen() {
        assertEquals(0, StringUtil.strLen(""));
        assertEquals(5, StringUtil.strLen("hello"));
        assertEquals(13, StringUtil.strLen("Hello, World!"));
        assertEquals(1, StringUtil.strLen(" "));
        assertEquals(3, StringUtil.strLen("   "));
        assertEquals(0, StringUtil.strLen(null));
    }

    @Test
    void testStrCat() {
        assertEquals("helloworld", StringUtil.strCat("hello", "world"));
        assertEquals("hello", StringUtil.strCat("hello", ""));
        assertEquals("world", StringUtil.strCat("", "world"));
        assertEquals("", StringUtil.strCat("", ""));
        assertEquals("hello", StringUtil.strCat("hello", null));
        assertEquals("world", StringUtil.strCat(null, "world"));
        assertEquals("", StringUtil.strCat(null, null));
        assertEquals("Hello World!", StringUtil.strCat("Hello ", "World!"));
    }

    @Test
    void testSubStr() {
        assertEquals("hello", StringUtil.subStr("hello", 0, 5));
        assertEquals("ell", StringUtil.subStr("hello", 1, 3));
        assertEquals("o", StringUtil.subStr("hello", 4, 1));
        assertEquals("", StringUtil.subStr("hello", 5, 1));
        assertEquals("lo", StringUtil.subStr("hello", 3, 10)); // Length exceeds string
        assertEquals("hello", StringUtil.subStr("hello", 0, 100)); // Length exceeds string
        
        // Edge cases
        assertEquals("", StringUtil.subStr("hello", -1, 3)); // Negative start
        assertEquals("", StringUtil.subStr("hello", 10, 3)); // Start out of bounds
        assertEquals("", StringUtil.subStr(null, 0, 5)); // Null string
        assertEquals("", StringUtil.subStr("hello", 0, 0)); // Zero length
    }

    @Test
    void testCharAt() {
        assertEquals("h", StringUtil.charAt("hello", 0));
        assertEquals("e", StringUtil.charAt("hello", 1));
        assertEquals("o", StringUtil.charAt("hello", 4));
        
        // Edge cases
        assertEquals("", StringUtil.charAt("hello", -1)); // Negative index
        assertEquals("", StringUtil.charAt("hello", 5)); // Index out of bounds
        assertEquals("", StringUtil.charAt("hello", 100)); // Way out of bounds
        assertEquals("", StringUtil.charAt(null, 0)); // Null string
        assertEquals("", StringUtil.charAt("", 0)); // Empty string
    }

    @Test
    void testIndexOf() {
        assertEquals(0, StringUtil.indexOf("hello", "h"));
        assertEquals(1, StringUtil.indexOf("hello", "e"));
        assertEquals(2, StringUtil.indexOf("hello", "ll"));
        assertEquals(0, StringUtil.indexOf("hello", "hello"));
        assertEquals(-1, StringUtil.indexOf("hello", "world"));
        assertEquals(-1, StringUtil.indexOf("hello", "x"));
        assertEquals(0, StringUtil.indexOf("hello", ""));
        
        // Edge cases
        assertEquals(-1, StringUtil.indexOf(null, "hello"));
        assertEquals(-1, StringUtil.indexOf("hello", null));
        assertEquals(-1, StringUtil.indexOf(null, null));
        
        // Multiple occurrences - should return first
        assertEquals(0, StringUtil.indexOf("hello hello", "hello"));
        assertEquals(0, StringUtil.indexOf("abcabc", "abc"));
    }

    @Test
    void testLastIndexOf() {
        assertEquals(0, StringUtil.lastIndexOf("hello", "h"));
        assertEquals(1, StringUtil.lastIndexOf("hello", "e"));
        assertEquals(2, StringUtil.lastIndexOf("hello", "ll"));
        assertEquals(0, StringUtil.lastIndexOf("hello", "hello"));
        assertEquals(-1, StringUtil.lastIndexOf("hello", "world"));
        assertEquals(-1, StringUtil.lastIndexOf("hello", "x"));
        assertEquals(5, StringUtil.lastIndexOf("hello", ""));
        
        // Edge cases
        assertEquals(-1, StringUtil.lastIndexOf(null, "hello"));
        assertEquals(-1, StringUtil.lastIndexOf("hello", null));
        assertEquals(-1, StringUtil.lastIndexOf(null, null));
        
        // Multiple occurrences - should return last
        assertEquals(6, StringUtil.lastIndexOf("hello hello", "hello"));
        assertEquals(3, StringUtil.lastIndexOf("abcabc", "abc"));
    }

    // ========== String Comparison Functions Tests ==========

    @Test
    void testStrCmp() {
        assertEquals(0, StringUtil.strCmp("hello", "hello"));
        assertTrue(StringUtil.strCmp("hello", "world") < 0);
        assertTrue(StringUtil.strCmp("world", "hello") > 0);
        assertTrue(StringUtil.strCmp("abc", "abd") < 0);
        assertTrue(StringUtil.strCmp("abd", "abc") > 0);
        
        // Case sensitivity
        assertTrue(StringUtil.strCmp("Hello", "hello") < 0);
        assertTrue(StringUtil.strCmp("hello", "Hello") > 0);
        
        // Length differences
        assertTrue(StringUtil.strCmp("hello", "hello world") < 0);
        assertTrue(StringUtil.strCmp("hello world", "hello") > 0);
        
        // Edge cases
        assertEquals(0, StringUtil.strCmp(null, null));
        assertTrue(StringUtil.strCmp(null, "hello") < 0);
        assertTrue(StringUtil.strCmp("hello", null) > 0);
        assertEquals(0, StringUtil.strCmp("", ""));
        assertTrue(StringUtil.strCmp("", "a") < 0);
    }

    @Test
    void testStrEq() {
        assertTrue(StringUtil.strEq("hello", "hello"));
        assertFalse(StringUtil.strEq("hello", "world"));
        assertFalse(StringUtil.strEq("Hello", "hello")); // Case sensitive
        assertTrue(StringUtil.strEq("", ""));
        
        // Edge cases
        assertTrue(StringUtil.strEq(null, null));
        assertFalse(StringUtil.strEq(null, "hello"));
        assertFalse(StringUtil.strEq("hello", null));
        assertFalse(StringUtil.strEq("", null));
        assertFalse(StringUtil.strEq(null, ""));
    }

    @Test
    void testStartsWith() {
        assertTrue(StringUtil.startsWith("hello", "h"));
        assertTrue(StringUtil.startsWith("hello", "he"));
        assertTrue(StringUtil.startsWith("hello", "hello"));
        assertTrue(StringUtil.startsWith("hello", ""));
        assertFalse(StringUtil.startsWith("hello", "e"));
        assertFalse(StringUtil.startsWith("hello", "world"));
        assertFalse(StringUtil.startsWith("hello", "Hello")); // Case sensitive
        
        // Edge cases
        assertFalse(StringUtil.startsWith(null, "hello"));
        assertFalse(StringUtil.startsWith("hello", null));
        assertFalse(StringUtil.startsWith(null, null));
        assertTrue(StringUtil.startsWith("", ""));
        assertFalse(StringUtil.startsWith("", "a"));
    }

    @Test
    void testEndsWith() {
        assertTrue(StringUtil.endsWith("hello", "o"));
        assertTrue(StringUtil.endsWith("hello", "lo"));
        assertTrue(StringUtil.endsWith("hello", "hello"));
        assertTrue(StringUtil.endsWith("hello", ""));
        assertFalse(StringUtil.endsWith("hello", "l"));
        assertFalse(StringUtil.endsWith("hello", "world"));
        assertFalse(StringUtil.endsWith("hello", "LO")); // Case sensitive
        
        // Edge cases
        assertFalse(StringUtil.endsWith(null, "hello"));
        assertFalse(StringUtil.endsWith("hello", null));
        assertFalse(StringUtil.endsWith(null, null));
        assertTrue(StringUtil.endsWith("", ""));
        assertFalse(StringUtil.endsWith("", "a"));
    }

    // ========== String Transformation Functions Tests ==========

    @Test
    void testToUpper() {
        assertEquals("HELLO", StringUtil.toUpper("hello"));
        assertEquals("HELLO", StringUtil.toUpper("HELLO"));
        assertEquals("HELLO WORLD!", StringUtil.toUpper("Hello World!"));
        assertEquals("123ABC", StringUtil.toUpper("123abc"));
        assertEquals("", StringUtil.toUpper(""));
        assertEquals("", StringUtil.toUpper(null));
    }

    @Test
    void testToLower() {
        assertEquals("hello", StringUtil.toLower("HELLO"));
        assertEquals("hello", StringUtil.toLower("hello"));
        assertEquals("hello world!", StringUtil.toLower("Hello World!"));
        assertEquals("123abc", StringUtil.toLower("123ABC"));
        assertEquals("", StringUtil.toLower(""));
        assertEquals("", StringUtil.toLower(null));
    }

    @Test
    void testTrim() {
        assertEquals("hello", StringUtil.trim("hello"));
        assertEquals("hello", StringUtil.trim("  hello"));
        assertEquals("hello", StringUtil.trim("hello  "));
        assertEquals("hello", StringUtil.trim("  hello  "));
        assertEquals("hello world", StringUtil.trim("  hello world  "));
        assertEquals("", StringUtil.trim(""));
        assertEquals("", StringUtil.trim("   "));
        assertEquals("", StringUtil.trim(null));
    }

    @Test
    void testReplace() {
        assertEquals("hxllo", StringUtil.replace("hello", "e", "x"));
        assertEquals("Hi", StringUtil.replace("Hello", "ello", "i"));
        assertEquals("aaa", StringUtil.replace("bbb", "b", "a"));
        assertEquals("hello", StringUtil.replace("hello", "x", "y"));
        assertEquals("xhxexlxlxox", StringUtil.replace("hello", "", "x")); // Empty search replaces at each char boundary
        assertEquals("", StringUtil.replace("hello", "hello", ""));
        
        // Multiple occurrences
        assertEquals("xellx", StringUtil.replace("hello", "h", "x").replace("o", "x"));
        assertEquals("he11o", StringUtil.replace("hello", "l", "1"));
        
        // Edge cases
        assertEquals("", StringUtil.replace(null, "a", "b"));
        assertEquals("hello", StringUtil.replace("hello", null, "x"));
        assertEquals("hello", StringUtil.replace("hello", "x", null));
    }

    @Test
    void testIsEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty("hello"));
        assertFalse(StringUtil.isEmpty(" "));
        assertFalse(StringUtil.isEmpty("  "));
        assertFalse(StringUtil.isEmpty("a"));
    }

    // ========== Integration Tests with ExecutionContext ==========

    @Test
    void testIntToStrWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "intToStr"));
        assertNotNull(func, "intToStr function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("value", 42L);
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("42", result);
    }

    @Test
    void testDecToStrWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "decToStr"));
        assertNotNull(func, "decToStr function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("value", new BigDecimal("3.14"));
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("3.14", result);
    }

    @Test
    void testStrToIntWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "strToInt"));
        assertNotNull(func, "strToInt function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "42");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(42L, result);
    }

    @Test
    void testBoolToStrWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "boolToStr"));
        assertNotNull(func, "boolToStr function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("value", true);
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("true", result);
    }

    @Test
    void testStrLenWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "strLen"));
        assertNotNull(func, "strLen function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(5L, result);
    }

    @Test
    void testStrCatWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "strCat"));
        assertNotNull(func, "strCat function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str1", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("str2", "world");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("helloworld", result);
    }

    @Test
    void testSubStrWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "subStr"));
        assertNotNull(func, "subStr function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("start", 1L);
        context.getCurrentFrame().getLocalContext().setVariable("length", 3L);
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("ell", result);
    }

    @Test
    void testCharAtWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "charAt"));
        assertNotNull(func, "charAt function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("index", 1L);
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("e", result);
    }

    @Test
    void testIndexOfWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "indexOf"));
        assertNotNull(func, "indexOf function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("search", "ll");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(2L, result);
    }

    @Test
    void testLastIndexOfWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "lastIndexOf"));
        assertNotNull(func, "lastIndexOf function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("search", "l");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(3L, result);
    }

    @Test
    void testStrCmpWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "strCmp"));
        assertNotNull(func, "strCmp function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str1", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("str2", "hello");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(0L, result);
    }

    @Test
    void testStrEqWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "strEq"));
        assertNotNull(func, "strEq function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str1", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("str2", "hello");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(true, result);
    }

    @Test
    void testStartsWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "startsWith"));
        assertNotNull(func, "startsWith function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("prefix", "he");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(true, result);
    }

    @Test
    void testEndsWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "endsWith"));
        assertNotNull(func, "endsWith function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("suffix", "lo");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(true, result);
    }

    @Test
    void testToUpperWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "toUpper"));
        assertNotNull(func, "toUpper function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("HELLO", result);
    }

    @Test
    void testToLowerWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "toLower"));
        assertNotNull(func, "toLower function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "HELLO");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("hello", result);
    }

    @Test
    void testTrimWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "trim"));
        assertNotNull(func, "trim function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "  hello  ");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("hello", result);
    }

    @Test
    void testReplaceWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "replace"));
        assertNotNull(func, "replace function should be registered");

        // Set parameters
        context.getCurrentFrame().getLocalContext().setVariable("str", "hello");
        context.getCurrentFrame().getLocalContext().setVariable("oldStr", "l");
        context.getCurrentFrame().getLocalContext().setVariable("newStr", "1");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals("he11o", result);
    }

    @Test
    void testIsEmptyWithContext() throws Exception {
        stringUtil.register(registry);
        FunctionDeclImpl func = registry.getFunction(new FullyQualifiedName("std.str", "isEmpty"));
        assertNotNull(func, "isEmpty function should be registered");

        // Set parameter
        context.getCurrentFrame().getLocalContext().setVariable("str", "");
        
        // Execute
        func.getBlock().execute(context);
        
        // Verify result
        Object result = context.getCurrentFrame().getLocalContext().getValue("result");
        assertEquals(true, result);
    }

    @Test
    void testAllFunctionsRegistered() {
        stringUtil.register(registry);
        
        // Conversion functions
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "intToStr")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "decToStr")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "strToInt")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "boolToStr")));
        
        // Manipulation functions
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "strLen")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "strCat")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "subStr")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "charAt")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "indexOf")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "lastIndexOf")));
        
        // Comparison functions
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "strCmp")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "strEq")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "startsWith")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "endsWith")));
        
        // Transformation functions
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "toUpper")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "toLower")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "trim")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "replace")));
        assertNotNull(registry.getFunction(new FullyQualifiedName("std.str", "isEmpty")));
    }
}
