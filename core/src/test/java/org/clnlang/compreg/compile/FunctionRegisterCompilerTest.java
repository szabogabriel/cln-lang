package org.clnlang.compreg.compile;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.clnlang.compreg.CurrentExecution;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.compiler.RegisterCompiler;
import org.clnlang.compreg.lib.StandardLibrary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class FunctionRegisterCompilerTest {

    @Test
    void compilesMainWithLocalAndGlobalVariables() {
        String source = "int bias = 4; int main() { int x = 2 + 3; return x + bias; }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(9, execution.executeMain(new Memory()));
    }

        @Test
        void concatenatesStringsWithScalarValuesInEitherOrder() {
                String source = "int main() { var int i = 3; string text = \"Text: \" + i; "
                                + "string reverse = true + \"!\"; "
                                + "if (text == \"Text: 3\" && reverse == \"true!\") { return 1; } return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void compilesLocalStructLiteralAndMutableFieldAccess() {
                String source = "struct Point { var int x; int y; }; "
                                + "int main() { var Point point = Point(x: 3, y: 4); "
                                + "point.x = point.x + point.y; return point.x; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(7, execution.executeMain(new Memory()));
        }

        @Test
        void rejectsAssignmentToImmutableStructField() {
                String source = "struct Point { int x; }; "
                                + "int main() { var Point point = Point(x: 3); point.x = 4; return point.x; }";
                IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(source)));

                assertTrue(error.getMessage().contains("Cannot assign to immutable field 'x'"));
        }

        @Test
        void compilesGlobalStructAcrossTypedRegisterBanks() {
                String source = "struct Sample { var int count; dec weight; bool active; }; "
                                + "var Sample sample = Sample(count: 4, weight: 1.5, active: true); "
                                + "int main() { sample.count = 5; if (sample.active && sample.weight == 1.5) { "
                                + "return sample.count; } return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(5, execution.executeMain(new Memory()));
        }

        @Test
        void compilesStringStructFieldMutation() {
                String source = "struct Label { var string text; }; "
                                + "int main() { var Label label = Label(text: \"before\"); "
                                + "label.text = \"after\"; if (label.text == \"after\") { return 1; } return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void validatesStructLiteralFieldInitializers() {
                String declaration = "struct Point { int x; int y; }; ";
                assertStructCompilationFails(declaration
                                + "int main() { Point point = Point(x: 1); return 0; }", "Missing initializer");
                assertStructCompilationFails(declaration
                                + "int main() { Point point = Point(x: 1, x: 2); return 0; }",
                                "Duplicate initializer");
                assertStructCompilationFails(declaration
                                + "int main() { Point point = Point(x: 1, y: 2, z: 3); return 0; }",
                                "has no field 'z'");
                assertStructCompilationFails(declaration
                                + "int main() { Point point = Point(x: true, y: 2); return 0; }",
                                "Type mismatch in initializer for field 'x'");
        }

        @Test
        void compilesIntegerAndMixedDecimalModulo() {
                String source = "int main() { dec remainder = 5.5 % 2; "
                                + "if (17 % 5 == 2 && remainder == 1.5 && 2.5 % 1.5 == 1.0 "
                                + "&& 5 % 2.0 == 1.0 && -17 % 5 == -2 "
                                + "&& -5.5 % 2.0 == -1.5) { return 1; } return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void moduloByZeroThrowsArithmeticException() {
                String source = "int main() { return 5 % 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                ArithmeticException error = assertThrows(ArithmeticException.class,
                                () -> execution.executeMain(new Memory()));
                assertEquals("Modulo by zero", error.getMessage());
        }

        @Test
        void decimalModuloByZeroThrowsArithmeticException() {
                String source = "int main() { dec result = 5.5 % 0.0; return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                ArithmeticException error = assertThrows(ArithmeticException.class,
                                () -> execution.executeMain(new Memory()));
                assertEquals("Modulo by zero", error.getMessage());
        }

        @Test
        void compilesPrefixAndPostfixIncrementAndDecrement() {
                String source = "int main() { var int value = 2; int postInc = value++; "
                                + "int preInc = ++value; int postDec = value--; int preDec = --value; "
                                + "return postInc * 1000 + preInc * 100 + postDec * 10 + preDec + value; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(2444, execution.executeMain(new Memory()));
        }

        @Test
        void incrementsGlobalAndDecimalVariables() {
                String source = "var int counter = 4; var dec(1) amount = 1.2; "
                                + "int main() { int previous = counter++; if (++amount > 2.0) { "
                                + "return previous * 10 + counter; } return 0; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(45, execution.executeMain(new Memory()));
        }

        @Test
        void rejectsIncrementOfImmutableVariable() {
                String source = "int main() { int value = 1; value++; return value; }";
                IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(source)));

                assertTrue(error.getMessage().contains("Cannot assign to immutable variable 'value'"));
        }

        @Test
        void decrementsGlobalAndDecimalVariablesWithPrefixAndPostfixValues() {
                String source = "var int counter = 7; var dec amount = 5.5; "
                                + "int main() { int oldCounter = counter--; dec oldAmount = amount--; "
                                + "dec newAmount = --amount; var int valid = 0; "
                                + "if (oldAmount == 5.5 && newAmount == 3.5 && counter == 6) { valid = 1; } "
                                + "return oldCounter * 100 + valid; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(701, execution.executeMain(new Memory()));
        }

        @Test
        void rejectsIncrementOfImmutableGlobalAndBooleanVariable() {
                String immutableSource = "int value = 1; int main() { value++; return value; }";
                IllegalArgumentException immutableError = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(
                                                RegisterCompilerTestSupport.parse(immutableSource)));
                assertTrue(immutableError.getMessage().contains("Cannot assign to immutable global 'value'"));

                String booleanSource = "int main() { var bool enabled = true; ++enabled; return 0; }";
                IllegalArgumentException typeError = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(
                                                RegisterCompilerTestSupport.parse(booleanSource)));
                assertTrue(typeError.getMessage().contains("increment or decrement of 'bool'"));
        }

        @Test
        void compilesMultidimensionalArrayWithDynamicIndexes() {
                String source = "int main() { var int[][] matrix = [[1, 2, 3], [4, 5, 6]]; "
                                + "var int row = 1; var int col = 2; matrix[row][col] = matrix[0][1] + 10; "
                                + "return matrix[1][2]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(12, execution.executeMain(new Memory()));
        }

        @Test
        void compilesGlobalMultidimensionalArray() {
                String source = "var int[][] matrix = [[1, 2], [3, 4]]; "
                                + "int main() { matrix[0][1] = 9; return matrix[0][1] + matrix[1][1]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(13, execution.executeMain(new Memory()));
        }

        @Test
        void multidimensionalArrayChecksEachDimensionBounds() {
                String source = "int main() { int[][] matrix = [[1, 2], [3, 4]]; return matrix[0][2]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                IndexOutOfBoundsException boundsError = assertThrows(IndexOutOfBoundsException.class,
                                () -> execution.executeMain(new Memory()));
                assertTrue(boundsError.getMessage().contains("dimension 1 with length 2"));
        }

        @Test
        void rejectsNonRectangularArrayLiterals() {
                String source = "int main() { int[][] matrix = [[1, 2], [3]]; return 0; }";
                IllegalArgumentException shapeError = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(source)));

                assertTrue(shapeError.getMessage().contains("must be rectangular"));
        }

        @Test
        void compilesFixedLocalArrayWithRuntimeIndexedReadsAndWrites() {
                String source = "int main() { var int[] values = [10, 20, 30]; var int index = 1; "
                                + "values[index] = values[0] + 5; return values[index]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(15, execution.executeMain(new Memory()));
        }

        @Test
        void compilesFixedGlobalArrayWithIndexedReadsAndWrites() {
                String source = "var int[] values = [4, 8, 12]; "
                                + "int main() { values[2] = values[0] + values[1]; return values[2]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(12, execution.executeMain(new Memory()));
        }

        @Test
        void copyCreatesIndependentFixedArrayFromGlobalSource() {
                String source = "var int[] original = [4, 8, 12]; "
                                + "int main() { var int[] cloned = copy(original); cloned[0] = 99; "
                                + "return original[0] * 100 + cloned[0]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(499, execution.executeMain(new Memory()));
        }

        @Test
        void copyCanInitializeAnotherGlobalArray() {
                String source = "var int[] original = [4, 8]; var int[] cloned = copy(original); "
                                + "int main() { cloned[0] = 99; return original[0] * 100 + cloned[0]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(499, execution.executeMain(new Memory()));
        }

        @Test
        void copyCreatesIndependentFixedArrayFromLocalSource() {
                String source = "int main() { var int[] original = [4, 8]; "
                                + "var int[] cloned = copy(original); cloned[0] = 99; "
                                + "return original[0] * 100 + cloned[0]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(499, execution.executeMain(new Memory()));
        }

        @Test
        void copyReusesItsFrameSlotsAcrossLoopIterations() {
                String source = "int main() { var int i = 0; var int total = 0; "
                                + "var int[] source = [3]; while (i < 4) { "
                                + "int[] cloned = copy(source); total = total + cloned[0]; i = i + 1; } "
                                + "return total; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(12, execution.executeMain(new Memory()));
        }

        @Test
        void fixedArrayIndexingChecksBoundsAtRuntime() {
                String source = "int main() { int[] values = [4, 8]; return values[2]; }";
                CurrentExecution execution = new RegisterCompiler()
                                .compileProgram(RegisterCompilerTestSupport.parse(source));

                IndexOutOfBoundsException boundsError = assertThrows(IndexOutOfBoundsException.class,
                        () -> execution.executeMain(new Memory()));
                assertTrue(boundsError.getMessage().contains("out of bounds for length 2"));
        }

    @Test
    void compilesNestedSamePackageCalls() {
        String source = "package calc; int add(int a, int b) { return a + b; } "
                + "int plusOne(int value) { int sum = add(value, 1); return sum; } "
                + "int main() { int start = 40; return plusOne(start) + 1; }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(42, execution.executeMain(new Memory()));
    }

        @Test
        void invokesJavaLibraryFunctionThroughRegisterFrame() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(registry -> registry.registerFunction("lib.math", "twice",
                                java.util.List.of("int"), java.util.List.of("int"),
                                (memory, parameters, results) -> memory.setInt(results.get(0).getOffset(),
                                                memory.getInt(parameters.get(0).getOffset()) * 2)));

                String source = "package app; import lib.math.*; int main() { return twice(21); }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(42, execution.executeMain(new Memory()));
        }

        @Test
        void invokesRegisterNativeStandardStringAndMathLibraries() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "package app; import std.str.*; import std.math.*; "
                                + "int main() { int absolute = abs(-21); int smaller = min(9, 4); "
                                + "dec mixed = max(2, 2.5); string text = toUpper(strCat(\"cl\", \"n\")); "
                                + "if (absolute == 21 && smaller == 4 && mixed == 2.5 && text == \"CLN\") "
                                + "{ return strLen(text) + round(2.5); } return 0; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(6, execution.executeMain(new Memory()));
        }

        @Test
        void calendarLibraryPassesInlineStructValuesAcrossCalls() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "package app; import std.calendar.*; "
                        + "int main() { Date startDate = toDate(\"2024-04-05\", FORMAT_DATE); "
                        + "Timestamp start = dateToTimestamp(startDate); Timestamp next = plusDays(start, 1); "
                        + "Date nextDate = timestampToDate(next); string text = dateToString(nextDate, FORMAT_DATE); "
                        + "if (nextDate.day == 6 && text == \"2024-04-06\") { return 1; } return 0; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void calendarLibraryNowReturnsCurrentTimestamp() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "import std.console.writeLine; import std.calendar.*; "
                        + "int main() { Timestamp current = now(); "
                        + "if (current.timestamp > 0 && current.year >= 1970 && current.month >= 1 "
                        + "&& current.month <= 12 && current.day >= 1 && current.day <= 31 "
                        + "&& current.timezone != \"\") { return 1; } return 0; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void calendarLibrarySupportsParsingReplacementConversionAndDiff() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "package app; import std.calendar.*; "
                                + "int main() { Timestamp parsed = toTimestamp(\"2024-03-01 12:00:00\", "
                                + "\"yyyy-MM-dd HH:mm:ss\"); Timestamp changed = withMonth(parsed, 4); "
                                + "Date date = timestampToDate(changed); Time time = timestampToTime(changed); "
                                + "Timestamp combined = dateTimeToTimestamp(date, time); "
                                + "string output = timestampToString(combined, FORMAT_DATETIME); "
                                + "if (!isBefore(parsed, combined)) { return 10; } "
                                + "if (diffDays(parsed, combined) != 30) { return 20; } "
                                + "if (output != \"2024-04-01 12:00:00\") { return 30; } return 1; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

        @Test
        void writeLineConcatenatesStringAndInteger() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "package app; import std.console.*; "
                                + "int main() { var int i = 123; writeLine(\"Text: \" + i); return 0; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                PrintStream previous = System.out;
                try {
                        System.setOut(new PrintStream(output));
                        assertEquals(0, execution.executeMain(new Memory()));
                } finally {
                        System.setOut(previous);
                }

                assertEquals("Text: 123" + System.lineSeparator(), output.toString());
        }

        @Test
        void invokesRegisterNativeSystemLibrary() {
                RegisterCompiler compiler = new RegisterCompiler();
                compiler.addLibrary(new StandardLibrary());
                String source = "package app; import std.sys.*; "
                                + "int main() { string value = getPropertyWithDefault(\"cln.missing.property\", \"fallback\"); "
                                + "if (value == \"fallback\") { return 1; } return 0; }";
                CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));

                assertEquals(1, execution.executeMain(new Memory()));
        }

    @Test
    void rejectsAssignmentsToImmutableBindings() {
        String localSource = "int main() { int value = 1; value = 2; return value; }";
        IllegalArgumentException localError = assertThrows(IllegalArgumentException.class,
                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(localSource)));
        assertTrue(localError.getMessage().contains("Cannot assign to immutable variable 'value'"));

        String globalSource = "int value = 1; int main() { value = 2; return value; }";
        IllegalArgumentException globalError = assertThrows(IllegalArgumentException.class,
                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(globalSource)));
        assertTrue(globalError.getMessage().contains("Cannot assign to immutable global 'value'"));

        String parameterSource = "int set(int value) { value = 2; return value; } "
                + "int main() { return set(1); }";
        IllegalArgumentException parameterError = assertThrows(IllegalArgumentException.class,
                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(parameterSource)));
        assertTrue(parameterError.getMessage().contains("Cannot assign to immutable variable 'value'"));
    }

    @Test
    void compilesMutableNamedMultipleReturns() {
        String source = "package calc; "
                + "(var int sum = 0, var int diff = 0) split(int a, int b) { "
                + "sum = a + b; diff = a - b; return; } "
                + "int main() { (var int sum, var int diff) = split(9, 4); return sum * 10 + diff; }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(135, execution.executeMain(new Memory()));
    }

    @Test
    void importedCallsRequireExposureAcrossPackages() {
        String librarySource = "package lib; int hidden(int value) { return value; } "
                + "expose int visible(int value) { return value + 1; }";
        CurrentExecution library = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(librarySource));

        String callerSource = "package app; import lib.*; int main() { return visible(41); }";
        RegisterCompiler callerCompiler = new RegisterCompiler();
        callerCompiler.addExternalFunctions(library.getFunctions());
        CurrentExecution caller = callerCompiler.compileProgram(RegisterCompilerTestSupport.parse(callerSource));

        assertEquals(42, caller.executeMain(new Memory()));

        String hiddenCallerSource = "package app; import lib.*; int main() { return hidden(41); }";
        RegisterCompiler hiddenCompiler = new RegisterCompiler();
        hiddenCompiler.addExternalFunctions(library.getFunctions());
        IllegalArgumentException visibilityError = assertThrows(IllegalArgumentException.class,
                () -> hiddenCompiler.compileProgram(RegisterCompilerTestSupport.parse(hiddenCallerSource)));
        assertEquals(true, visibilityError.getMessage().contains("is not exposed"));
    }

    @Test
    void appliesDecimalPrecisionAcrossCallFrames() {
        String source = "(var dec(2) result = 0.0) round(dec(2) input) { result = input; return; } "
                + "dec(2) amount = round(1.235);";
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
        Memory memory = new Memory();

        execution.executeGlobalInitializers(memory);

        assertEquals(new java.math.BigDecimal("1.24"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("amount")));
    }

    @Test
    void compilesIfElseWithReturnFromEitherBranch() {
        String source = "int classify(int value) { "
                + "if (value < 0) { return 1; } else { return 2; } } "
                + "int main() { return classify(-1) + classify(1); }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(3, execution.executeMain(new Memory()));
    }

    @Test
    void compilesWhileLoopWithRegisterConditions() {
        String source = "int main() { var int i = 0; var int sum = 0; "
                + "while (i < 5) { sum = sum + i; i = i + 1; } return sum; }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(10, execution.executeMain(new Memory()));
    }

    @Test
    void logicalConditionsShortCircuit() {
        String source = "int main() { "
                + "if (false && (1 / 0 == 0)) { return 1; } "
                + "if (true || (1 / 0 == 0)) { return 7; } else { return 2; } }";
        CurrentExecution execution = new RegisterCompiler()
                .compileProgram(RegisterCompilerTestSupport.parse(source));

        assertEquals(7, execution.executeMain(new Memory()));
    }

        private void assertStructCompilationFails(String source, String expectedMessage) {
                IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                                () -> new RegisterCompiler().compileProgram(RegisterCompilerTestSupport.parse(source)));
                assertTrue(error.getMessage().contains(expectedMessage));
        }
}
