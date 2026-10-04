package org.clnlang.compreg.compile;

import org.clnlang.compreg.CurrentExecution;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.compiler.RegisterCompiler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GlobalVariableRegisterCompilerTest {

    @Test
    void compilesSimpleGlobal() {
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse("int x = 5;"));
        Memory memory = new Memory();

        execution.executeGlobalInitializers(memory);

        assertEquals(5, memory.getInt(compiler.getGlobalOffsets().getIntOffset("x")));
    }

    @Test
    void compilesFlattenedGlobalArithmeticAndMutabilityMetadata() {
        String source = "expose var int y = 7; int x = 12 + y * 34; "
                + "dec amount = 2.5; dec total = amount + 4; int negative = -3;";
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
        Memory memory = new Memory();

        execution.executeGlobalInitializers(memory);

        assertEquals(7, memory.getInt(compiler.getGlobalOffsets().getIntOffset("y")));
        assertEquals(true, compiler.getGlobalOffsets().isMutable("y"));
        assertEquals(true, compiler.getGlobalOffsets().isExposed("y"));
        assertEquals(250, memory.getInt(compiler.getGlobalOffsets().getIntOffset("x")));
        assertEquals(new java.math.BigDecimal("6.5"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("total")));
        assertEquals(-3, memory.getInt(compiler.getGlobalOffsets().getIntOffset("negative")));
    }

    @Test
    void compilesStringBooleanAndDecimalInitializers() {
        String source = "dec amount = 12.75; string label = \"hello\\nregister\"; "
                + "bool enabled = true; bool disabled = false;";
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
        Memory memory = new Memory();

        execution.executeGlobalInitializers(memory);

        assertEquals(new java.math.BigDecimal("12.75"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("amount")));
        assertEquals("hello\nregister",
                memory.getStr(compiler.getGlobalOffsets().getStringOffset("label")));
        assertEquals(true, memory.getBool(compiler.getGlobalOffsets().getBooleanOffset("enabled")));
        assertEquals(false, memory.getBool(compiler.getGlobalOffsets().getBooleanOffset("disabled")));
    }

    @Test
    void appliesDecimalPrecisionAndRoundingModes() {
        String source = "dec(2) rounded = 1.235; dec(2, DOWN) down = 1.239;";
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
        Memory memory = new Memory();

        execution.executeGlobalInitializers(memory);

        assertEquals(new java.math.BigDecimal("1.24"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("rounded")));
        assertEquals(new java.math.BigDecimal("1.23"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("down")));
        assertEquals(2, compiler.getGlobalOffsets().getDecimalTypeInfo("rounded").getPrecision());
        assertEquals(java.math.RoundingMode.DOWN,
                compiler.getGlobalOffsets().getDecimalTypeInfo("down").getRoundingMode());
    }

    @Test
    void appliesDecimalPrecisionOnMutableGlobalAssignment() {
        String source = "var dec(2) amount = 0.0; int main() { amount = 1.235; return 0; }";
        RegisterCompiler compiler = new RegisterCompiler();
        CurrentExecution execution = compiler.compileProgram(RegisterCompilerTestSupport.parse(source));
        Memory memory = new Memory();

        execution.executeMain(memory);

        assertEquals(new java.math.BigDecimal("1.24"),
                memory.getDec(compiler.getGlobalOffsets().getDecOffset("amount")));
    }
}
