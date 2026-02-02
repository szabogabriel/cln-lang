# Clean Language (cln-lang)

A complete interpreter for the Clean programming language with ANTLR4-based parsing, AST construction, compilation, and execution capabilities.

## Features

- ✅ **Full Parser**: ANTLR4-based lexer and parser for Clean language
- ✅ **AST Construction**: Converts parse trees into a structured Abstract Syntax Tree
- ✅ **Type System**: Support for primitives (int, bool, string), structs, and unions
- ✅ **Runtime Execution**: Complete interpreter with execution context and function invocation
- ✅ **Standard Library**: Built-in console I/O functions (writeLine)
- ✅ **Module System**: Package declarations and imports (including wildcard imports)
- ✅ **Control Flow**: If/else statements, loops, and function calls
- ✅ **Expressions**: Binary operations, member access, struct literals, and more
- ✅ **Error Handling**: Comprehensive exception handling with detailed error messages

## Project Structure

```
core/
├── pom.xml                                    # Maven configuration with ANTLR4 and Shade plugins
├── src/
│   ├── main/
│   │   ├── antlr4/
│   │   │   └── org/clnlang/parser/
│   │   │       └── cln.g4                     # Grammar file
│   │   └── java/
│   │       └── org/clnlang/
│   │           ├── Main.java                  # Main entry point & CLI
│   │           ├── ast/                       # Abstract Syntax Tree nodes
│   │           │   ├── declaration/           # Program, function, struct declarations
│   │           │   ├── expression/            # All expression types
│   │           │   ├── statement/             # Statements (if, return, var decl, etc.)
│   │           │   └── visitor/               # AST visitors (printer, compiler)
│   │           ├── compile/                   # Compiled representations
│   │           │   ├── declaration/           # Compiled declarations
│   │           │   ├── expression/            # Compiled expressions
│   │           │   └── statement/             # Compiled statements
│   │           ├── exception/                 # Custom exceptions
│   │           ├── parser/                    # AST builder from parse trees
│   │           └── runtime/                   # Execution engine
│   │               ├── lib/                   # Standard library
│   │               ├── ExecutionContext.java  # Variable and function contexts
│   │               ├── FunctionInvoker.java   # Function execution
│   │               ├── Linker.java            # Import resolution
│   │               └── Registry.java          # Symbol registry
│   └── test/
│       ├── java/                              # Unit tests
│       └── resources/                         # Test .cln files
└── target/
    ├── core-1.0-SNAPSHOT-fat.jar              # Standalone executable JAR
    └── generated-sources/
        └── antlr4/                            # Generated parser classes
```

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Building the Project

Build the project and generate the standalone fat JAR:

```bash
cd core
mvn clean package
```

This creates:
- `target/core-1.0-SNAPSHOT.jar` - Regular JAR (requires classpath)
- `target/core-1.0-SNAPSHOT-fat.jar` - Standalone JAR with all dependencies (recommended)

### Running Programs

Execute a Clean language program:

```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar <program.cln>
```

With verbose output (shows parsing, compilation, and execution details):

```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -v <program.cln>
```
Language Features

### Basic Syntax

```clean
package main;

import std.console.writeLine;

// Struct definition
struct Point {
    var int x;
    var int y;
};

// Function with return variables
(var int result = 0) add(int a, int b) {
    result = a + b;
    return;
}

// Main function entry point
(var int ret = 0) main() {
    writeLine("Hello, World!");
    var int sum = add(10, 20);
    return;
}
```

### Supported Language Constructs

- **Package declarations**: `package main;`
- **Imports**: `import std.console.writeLine;` or `import utils.*;`
- **Structs**: User-defined types with fields
- **Unions**: Tagged union types
- **Functions**: With named return variables
- **Variables**: `var int x = 10;`
- **Control flow**: `if/else` statements
- **Expressions**: Binary ops (+, -, *, /, >, <, ==, etc.), member access, function calls
- **Struct literals**: `Point(x: 10, y: 20)`

## Architecture

The interpreter follows a multi-stage pipeline:

1. **Lexing & Parsing** (ANTLR4): Source code → Parse Tree
2. **AST Construction** (ClnASTBuilder): Parse Tree → Abstract Syntax Tree
3. **Compilation** (CompilerVisitor): AST → Compiled Representations
4. **Linking** (Linker): Resolve imports and populate execution context
5. **Execution** (FunctionInvoker): Execute the main function

### Key Components

- **ClnASTBuilder**: Converts ANTLR parse trees to AST nodes
- **CompilerVisitor**: Transforms AST into executable compiled forms
- **ExecutionContext**: Manages variable scopes and function registry
- **Linker**: Resolves imports and connects modules
- **StandardLibrary**: Provides built-in functions (console I/O)
- **FunctionInvoker**: Executes functions with proper context

## Development

### Building from Source

```bash
# Clone the repository
git clone https://github.com/szabogabriel/cln-lang.git
cd cln-lang/core

# Build the project
mvn clean install

# Run tests
mvn test
```

### Maven Configuration

The `pom.xml` includes:

- **ANTLR4 Runtime**: Parser runtime dependency
- **ANTLR4 Maven Plugin**: Generates lexer/parser from grammar
- **Maven Shade Plugin**: Creates fat JAR with all dependencies
- **JUnit 5**: Testing framework

### Grammar File

The language grammar is defined in `src/main/antlr4/org/clnlang/parser/cln.g4`

## Example Programs

See the `src/test/resources/` directory for example programs:

- `test_hello.cln` - Hello world with console output
- `test_program.cln` - Structs, unions, and control flow
- `test_union.cln` - Union types and expressions
- `test_compiler.cln` - Struct and function declarations

## License

See [LICENSE](LICENSE) file for details.

## Changelog

See [changelog/](changelog/) directory for detailed change history:
- [PACKAGE_ORGANIZATION.md](changelog/PACKAGE_ORGANIZATION.md)
- [STATEMENTS_AND_EXPRESSIONS.md](changelog/STATEMENTS_AND_EXPRESSIONS.md)
- [VISITOR_PATTERN.md](changelog/VISITOR_PATTERN.md)
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
