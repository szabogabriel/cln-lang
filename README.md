# Clean Language Parser - ANTLR4 Integration

This Maven project includes the ANTLR4 parser for the Clean programming language.

## Project Structure

```
core/
├── pom.xml                                    # Maven configuration with ANTLR4 plugin
├── src/
│   ├── main/
│   │   ├── antlr4/
│   │   │   └── org/clnlang/parser/
│   │   │       └── cln.g4                     # Grammar file
│   │   └── java/
│   │       └── org/clnlang/
│   │           └── Main.java                  # Example parser usage
│   └── test/java/
└── target/
    └── generated-sources/
        └── antlr4/                            # Generated parser classes
            └── org/clnlang/parser/
                ├── clnLexer.java              # Lexer
                ├── clnParser.java             # Parser
                ├── clnListener.java           # Listener interface
                ├── clnBaseListener.java       # Base listener implementation
                ├── clnVisitor.java            # Visitor interface
                └── clnBaseVisitor.java        # Base visitor implementation
```

## Generated Classes

The ANTLR4 Maven plugin generates the following Java classes from `cln.g4`:

1. **clnLexer.java** - Tokenizes the input source code
2. **clnParser.java** - Parses tokens according to grammar rules
3. **clnListener.java** - Interface for tree-walking listeners
4. **clnBaseListener.java** - Default implementation of the listener
5. **clnVisitor.java** - Interface for tree visitors  
6. **clnBaseVisitor.java** - Default implementation of the visitor

## Building the Project

To generate ANTLR4 classes and compile:

```bash
cd core
mvn clean compile
```

The ANTLR4 Maven plugin automatically:
- Processes the grammar file during the `generate-sources` phase
- Generates Java classes in `target/generated-sources/antlr4/`
- Makes them available for compilation

## Running the Parser

To parse a Clean language source file:

```bash
mvn exec:java -Dexec.mainClass="org.clnlang.Main" -Dexec.args="path/to/source.cln"
```

## Using the Parser in Your Code

### Basic Parser Usage

```java
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;

// Create a CharStream from input
CharStream input = CharStreams.fromFileName("program.cln");

// Create lexer and parser
clnLexer lexer = new clnLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
clnParser parser = new clnParser(tokens);

// Parse starting from the 'program' rule
ParseTree tree = parser.program();
```

### Using a Listener (for tree walking)

```java
import org.clnlang.parser.clnBaseListener;
import org.clnlang.parser.clnParser;

public class MyListener extends clnBaseListener {
    @Override
    public void enterFunctionDecl(clnParser.FunctionDeclContext ctx) {
        String functionName = ctx.ID().getText();
        System.out.println("Found function: " + functionName);
    }
}

// Use the listener
ParseTreeWalker walker = new ParseTreeWalker();
walker.walk(new MyListener(), tree);
```

### Using a Visitor (for computing values)

```java
import org.clnlang.parser.clnBaseVisitor;
import org.clnlang.parser.clnParser;

public class MyVisitor extends clnBaseVisitor<String> {
    @Override
    public String visitFunctionDecl(clnParser.FunctionDeclContext ctx) {
        String functionName = ctx.ID().getText();
        return "Function: " + functionName;
    }
}

// Use the visitor
MyVisitor visitor = new MyVisitor();
String result = visitor.visit(tree);
```

## Maven Configuration Details

The `pom.xml` includes:

- **ANTLR4 Runtime Dependency**: Required at runtime to execute generated parser code
- **ANTLR4 Maven Plugin**: Generates Java classes from `.g4` grammar files
- **Plugin Configuration**:
  - `listener=true`: Generates listener interfaces and base classes
  - `visitor=true`: Generates visitor interfaces and base classes
  - `outputDirectory`: Where generated sources are placed

## Next Steps

You can now:

1. Create custom listeners by extending `clnBaseListener`
2. Create custom visitors by extending `clnBaseVisitor`
3. Build an Abstract Syntax Tree (AST) from the parse tree
4. Implement semantic analysis, type checking, and code generation

## Example Program

See [test_program.cln](../test_program.cln) for a sample Clean language program.
