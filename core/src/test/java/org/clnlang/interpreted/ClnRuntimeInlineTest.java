package org.clnlang.interpreted;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.clnlang.exception.ClnException;
import org.clnlang.exception.OverloadingNotSupportedException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for inline script execution via ClnRuntime.
 */
class ClnRuntimeInlineTest {

    @Test
    void executeInlineReturnsExitCode() throws Exception {
        String script = """
            (var int result = 0) main() {
                result = 7;
                return;
            }
            """;

        int exitCode = ClnRuntime.executeInline(script);
        assertEquals(7, exitCode);
    }

    @Test
    void executeInlineSupportsStdLibImports() throws Exception {
        String script = """
            import std.str.intToStr;

            (var int result = 0) main() {
                var string s = intToStr(5);
                // Prevent unused variable optimization (if any)
                if (s == "") {
                    result = 1;
                }
                return;
            }
            """;

        int exitCode = ClnRuntime.executeInline(script);
        assertEquals(0, exitCode);
    }

    @Test
    void executeInlineNoMainThrows() {
        String script = """
            helper() {
                return;
            }
            """;

        ClnException ex = assertThrows(ClnException.class, () -> ClnRuntime.executeInline(script));
        assertTrue(ex.getMessage().contains("main"));
    }

    @Test
    void executeInlineMultipleMainThrows() {
        String script = """
            (var int result = 0) main() {
                result = 1;
                return;
            }

            (var int result = 0) main() {
                result = 2;
                return;
            }
            """;

        OverloadingNotSupportedException ex = assertThrows(OverloadingNotSupportedException.class, () -> ClnRuntime.executeInline(script));
        assertTrue(ex.getMessage().contains("is already defined"));
    }
}
