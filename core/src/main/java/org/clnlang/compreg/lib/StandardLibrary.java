package org.clnlang.compreg.lib;

import java.math.BigDecimal;
import java.util.List;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.lib.std.math.DefaultMath;
import org.clnlang.lib.std.string.StringUtil;

/**
 * Register-native ports of standard-library functions whose signatures use scalar register types.
 * Array-, struct-, and dynamic-object-based libraries require those value kinds in library frames first.
 */
public final class StandardLibrary implements JavaLibrary {

    @Override
    public void register(LibraryRegistry registry) {
        registerConsole(registry);
        registerStrings(registry);
        registerMath(registry);
        registerSystem(registry);
                new CalendarLibrary().register(registry);
    }

    private void registerConsole(LibraryRegistry registry) {
        registry.registerFunction("std.console", "write", List.of("string"), List.of(),
                (memory, args, results) -> System.out.print(string(memory, args, 0)));
        registry.registerFunction("std.console", "writeLine", List.of("string"), List.of(),
                (memory, args, results) -> System.out.println(string(memory, args, 0)));
        registry.registerFunction("std.console", "readLine", List.of(), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(), readLine()));
    }

    private void registerStrings(LibraryRegistry registry) {
        unary(registry, "intToStr", "int", "string", (memory, args, results) ->
                setString(memory, results, StringUtil.intToStr(memory.getInt(args.get(0).getOffset()))));
        unary(registry, "decToStr", "dec", "string", (memory, args, results) ->
                setString(memory, results, StringUtil.decToString(memory.getDec(args.get(0).getOffset()))));
        unary(registry, "strToInt", "string", "int", (memory, args, results) ->
                memory.setInt(results.get(0).getOffset(), StringUtil.strToInt(string(memory, args, 0))));
        unary(registry, "boolToStr", "bool", "string", (memory, args, results) ->
                setString(memory, results, StringUtil.boolToStr(memory.getBool(args.get(0).getOffset()))));
        unary(registry, "strLen", "string", "int", (memory, args, results) ->
                memory.setInt(results.get(0).getOffset(), StringUtil.strLen(string(memory, args, 0))));
        binaryStrings(registry, "strCat", "string", (left, right) -> StringUtil.strCat(left, right));
        registry.registerFunction("std.str", "subStr", List.of("string", "int", "int"), List.of("string"),
                (memory, args, results) -> setString(memory, results, StringUtil.subStr(
                        string(memory, args, 0), memory.getInt(args.get(1).getOffset()),
                        memory.getInt(args.get(2).getOffset()))));
        registry.registerFunction("std.str", "charAt", List.of("string", "int"), List.of("string"),
                (memory, args, results) -> setString(memory, results, StringUtil.charAt(
                        string(memory, args, 0), memory.getInt(args.get(1).getOffset()))));
        registerStringSearch(registry, "indexOf", false);
        registerStringSearch(registry, "lastIndexOf", true);
        registry.registerFunction("std.str", "strCmp", List.of("string", "string"), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), StringUtil.strCmp(
                        string(memory, args, 0), string(memory, args, 1))));
        registerStringPredicate(registry, "strEq", StringUtil::strEq);
        registerStringPredicate(registry, "startsWith", StringUtil::startsWith);
        registerStringPredicate(registry, "endsWith", StringUtil::endsWith);
        unaryString(registry, "toUpper", StringUtil::toUpper);
        unaryString(registry, "toLower", StringUtil::toLower);
        unaryString(registry, "trim", StringUtil::trim);
        registry.registerFunction("std.str", "replace", List.of("string", "string", "string"), List.of("string"),
                (memory, args, results) -> setString(memory, results, StringUtil.replace(
                        string(memory, args, 0), string(memory, args, 1), string(memory, args, 2))));
        registry.registerFunction("std.str", "isEmpty", List.of("string"), List.of("bool"),
                (memory, args, results) -> memory.setBool(results.get(0).getOffset(),
                        StringUtil.isEmpty(string(memory, args, 0))));
    }

    private void registerMath(LibraryRegistry registry) {
        unaryDecimal(registry, "sin", DefaultMath::sin);
        unaryDecimal(registry, "cos", DefaultMath::cos);
        unaryDecimal(registry, "tan", DefaultMath::tan);
        unaryDecimal(registry, "asin", DefaultMath::asin);
        unaryDecimal(registry, "acos", DefaultMath::acos);
        unaryDecimal(registry, "atan", DefaultMath::atan);
        binaryDecimal(registry, "atan2", DefaultMath::atan2);
        unaryDecimal(registry, "exp", DefaultMath::exp);
        unaryDecimal(registry, "log", DefaultMath::log);
        unaryDecimal(registry, "log10", DefaultMath::log10);
        binaryDecimal(registry, "pow", DefaultMath::pow);
        unaryDecimal(registry, "sqrt", DefaultMath::sqrt);
        unaryDecimal(registry, "cbrt", DefaultMath::cbrt);
        unaryDecimal(registry, "ceil", DefaultMath::ceil);
        unaryDecimal(registry, "floor", DefaultMath::floor);
        registry.registerFunction("std.math", "round", List.of("dec"), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(),
                        DefaultMath.round(memory.getDec(args.get(0).getOffset()))));
        registry.registerFunction("std.math", "random", List.of(), List.of("dec"),
                (memory, args, results) -> memory.setDec(results.get(0).getOffset(), DefaultMath.random()));
        unaryDecimal(registry, "toRadians", DefaultMath::toRadians);
        unaryDecimal(registry, "toDegrees", DefaultMath::toDegrees);

        registry.registerFunction("std.math", "abs", List.of("int"), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(),
                        Math.abs(memory.getInt(args.get(0).getOffset()))));
        unaryDecimal(registry, "abs", DefaultMath::abs);
        registerNumericPair(registry, "min", true);
        registerNumericPair(registry, "max", false);
    }

    private void registerSystem(LibraryRegistry registry) {
        registry.registerFunction("std.sys", "currentTimeMillis", List.of(), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), System.currentTimeMillis()));
        registry.registerFunction("std.sys", "nanoTime", List.of(), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), System.nanoTime()));
        registry.registerFunction("std.sys", "exit", List.of("int"), List.of(),
                (memory, args, results) -> System.exit((int) memory.getInt(args.get(0).getOffset())));
        registry.registerFunction("std.sys", "getenv", List.of("string"), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(),
                        System.getenv(string(memory, args, 0))));
        registry.registerFunction("std.sys", "getProperty", List.of("string"), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(),
                        System.getProperty(string(memory, args, 0))));
        registry.registerFunction("std.sys", "getPropertyWithDefault", List.of("string", "string"), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(), System.getProperty(
                        string(memory, args, 0), string(memory, args, 1))));
        registry.registerFunction("std.sys", "freeMemory", List.of(), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), Runtime.getRuntime().freeMemory()));
        registry.registerFunction("std.sys", "totalMemory", List.of(), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), Runtime.getRuntime().totalMemory()));
        registry.registerFunction("std.sys", "maxMemory", List.of(), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), Runtime.getRuntime().maxMemory()));
        registry.registerFunction("std.sys", "gc", List.of(), List.of(),
                (memory, args, results) -> System.gc());
    }

    private void unary(LibraryRegistry registry, String name, String argumentType, String resultType,
            JavaFunction implementation) {
        registry.registerFunction("std.str", name, List.of(argumentType), List.of(resultType), implementation);
    }

    private void unaryString(LibraryRegistry registry, String name, StringTransform operation) {
        unary(registry, name, "string", "string", (memory, args, results) ->
                setString(memory, results, operation.apply(string(memory, args, 0))));
    }

    private void binaryStrings(LibraryRegistry registry, String name, String resultType, StringBinary operation) {
        registry.registerFunction("std.str", name, List.of("string", "string"), List.of(resultType),
                (memory, args, results) -> setString(memory, results,
                        operation.apply(string(memory, args, 0), string(memory, args, 1))));
    }

    private void registerStringSearch(LibraryRegistry registry, String name, boolean last) {
        registry.registerFunction("std.str", name, List.of("string", "string"), List.of("int"),
                                (memory, args, results) -> memory.setInt(results.get(0).getOffset(), last
                                                ? StringUtil.lastIndexOf(string(memory, args, 0), string(memory, args, 1))
                                                : StringUtil.indexOf(string(memory, args, 0), string(memory, args, 1))));
        }

        private void registerStringPredicate(LibraryRegistry registry, String name, StringPredicate predicate) {
                registry.registerFunction("std.str", name, List.of("string", "string"), List.of("bool"),
                                (memory, args, results) -> memory.setBool(results.get(0).getOffset(),
                                                predicate.test(string(memory, args, 0), string(memory, args, 1))));
        }

        private void unaryDecimal(LibraryRegistry registry, String name, DecimalUnary operation) {
                registry.registerFunction("std.math", name, List.of("dec"), List.of("dec"),
                                (memory, args, results) -> memory.setDec(results.get(0).getOffset(),
                                                operation.apply(memory.getDec(args.get(0).getOffset()))));
        }

        private void binaryDecimal(LibraryRegistry registry, String name, DecimalBinary operation) {
                registry.registerFunction("std.math", name, List.of("dec", "dec"), List.of("dec"),
                                (memory, args, results) -> memory.setDec(results.get(0).getOffset(), operation.apply(
                                                memory.getDec(args.get(0).getOffset()), memory.getDec(args.get(1).getOffset()))));
        }

        private void registerNumericPair(LibraryRegistry registry, String name, boolean minimum) {
                registry.registerFunction("std.math", name, List.of("int", "int"), List.of("int"),
                                (memory, args, results) -> {
                                        long left = memory.getInt(args.get(0).getOffset());
                                        long right = memory.getInt(args.get(1).getOffset());
                                        memory.setInt(results.get(0).getOffset(), minimum ? Math.min(left, right) : Math.max(left, right));
                                });
                registerDecimalPair(registry, name, "dec", "dec", minimum);
                registerDecimalPair(registry, name, "int", "dec", minimum);
                registerDecimalPair(registry, name, "dec", "int", minimum);
        }

        private void registerDecimalPair(LibraryRegistry registry, String name, String leftType,
                        String rightType, boolean minimum) {
                registry.registerFunction("std.math", name, List.of(leftType, rightType), List.of("dec"),
                                (memory, args, results) -> {
                                        BigDecimal left = decimalValue(memory, args.get(0));
                                        BigDecimal right = decimalValue(memory, args.get(1));
                                        memory.setDec(results.get(0).getOffset(), minimum
                                                        ? DefaultMath.min(left, right) : DefaultMath.max(left, right));
                                });
        }

        private BigDecimal decimalValue(Memory memory, CompiledFunction.Slot slot) {
                return slot.getRegisterBank() == org.clnlang.compreg.runtime.RegisterBank.DEC
                                ? memory.getDec(slot.getOffset())
                                : BigDecimal.valueOf(memory.getInt(slot.getOffset()));
        }

        private String string(Memory memory, List<CompiledFunction.Slot> slots, int index) {
                return memory.getStr(slots.get(index).getOffset());
        }

        private void setString(Memory memory, List<CompiledFunction.Slot> slots, String value) {
                memory.setStr(slots.get(0).getOffset(), value);
        }

        private String readLine() {
                try {
                        return System.console() == null ? new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).readLine()
                                        : System.console().readLine();
                } catch (java.io.IOException e) {
                        throw new IllegalStateException("Error reading line from console", e);
                }
        }

        @FunctionalInterface
        private interface StringTransform { String apply(String value); }

        @FunctionalInterface
        private interface StringBinary { String apply(String left, String right); }

        @FunctionalInterface
        private interface StringPredicate { boolean test(String left, String right); }

        @FunctionalInterface
        private interface DecimalUnary { BigDecimal apply(BigDecimal value); }

        @FunctionalInterface
        private interface DecimalBinary { BigDecimal apply(BigDecimal left, BigDecimal right); }
}
