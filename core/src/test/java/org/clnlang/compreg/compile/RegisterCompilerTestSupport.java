package org.clnlang.compreg.compile;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;

final class RegisterCompilerTestSupport {

    private RegisterCompilerTestSupport() {
    }

    static clnParser.ProgramContext parse(String source) {
        clnLexer lexer = new clnLexer(CharStreams.fromString(source));
        return new clnParser(new CommonTokenStream(lexer)).program();
    }
}
