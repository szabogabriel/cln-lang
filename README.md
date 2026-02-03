# Clean Language (cln-lang)

A complete interpreter for the Clean programming language with ANTLR4-based parsing, AST construction, compilation, and execution capabilities.

## Features

### ✅ Fully Implemented

- **Parser & Lexer**: Complete ANTLR4-based grammar for Clean language
- **AST Construction**: Parse tree to structured Abstract Syntax Tree conversion
- **Type System**: Primitives (int, bool, string), structs, and unions
- **Runtime Execution**: Full interpreter with execution context and function invocation
- **Module System**: Package declarations and imports (including wildcard imports)
- **Expressions**:
  - Binary operators: `+`, `-`, `*`, `/`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `&&`, `||`
  - Unary operators: `!`, `-`
  - Literals: integers, booleans, strings
  - Identifiers and function calls
  - String concatenation with `+` operator
  - **Struct literals**: Construction with field initialization (e.g., `Circle(radius: 5)`)
  - **Member access**: Read struct fields with `.` operator (e.g., `user.name`)
- **Statements**:
  - Variable declarations with type inference (`var`)
  - If/else conditionals with boolean validation
  - While loops
  - **Switch/case**: Pattern matching on union types with variable binding
  - Multiple return value assignments (tuple destructuring)
  - Return statements
  - Expression statements
  - **Assignment to struct fields**: Modify struct members (e.g., `user.name = "value"`)
- **Functions**:
  - Named return variables with simple return types (e.g., `int main()`)
  - Multiple return values
  - Call frame management
  - Parameter passing
  - Exit code support (main function return value becomes process exit code)
- **Data Structures**:
  - **Struct declaration and instantiation**: Full support for creating and using struct types
  - **Struct field access**: Read and write struct fields
  - **Union declarations**: Type definitions for tagged unions
- **Integer Type**: 64-bit signed integers (using Java `long`, range: -2^63 to 2^63-1)
- **Standard Library**: Console I/O (`std.console.writeLine`, `std.console.write`)
- **String Utilities**: `intToStr` function for integer-to-string conversion
- **Error Handling**: Comprehensive exception handling with detailed error messages
- **Type Safety**: Runtime type checking for operators and conditionals

### 🚧 Partially Implemented

- **Unions**: Declaration and switch/case matching work, but implicit upcasting from member types to union type not implemented
- **Arrays**: Grammar support exists, runtime implementation pending

### ❌ Not Yet Implemented

- **Index Access**: Array indexing (`[]` operator) not functional
- **Assignment to array elements**: Array element assignment not implemented
- **Global Variables**: Parsing exists but not handled in compilation
- **Array Operations**: No array creation, indexing, or manipulation
- **Type Checking**: Static type checking not implemented (runtime only)
- **Semantic Analysis**: No compile-time validation beyond basic type checks
- **Union type coercion**: Cannot pass struct instances to functions expecting union types

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

## Language Features

### Basic Syntax

```clean
package main;

import std.console.*;
import std.str.*;

// Struct definitions
struct Point {
    var int x;
    var int y;
};

// Function with simple return type
int add(int a, int b) {
    return a + b;
}

// Function with multiple return values
(var int sum = 0, var int product = 1) calculate(int a, int b) {
    sum = a + b;
    product = a * b;
    return;
}

// Main function entry point (return value becomes exit code)
(var int ret = 0) main() {
    writeLine("Hello, World!");
    
    // Struct creation and field access
    var Point p = Point(x: 10, y: 20);
    writeLine("Point x: " + intToStr(p.x));
    
    // Struct field assignment
    p.x = 15;
    p.y = 25;
    
    // Tuple assignment with multiple return values
    (var int s, var int product) = calculate(10, 20);
    
    writeLine("Sum: " + intToStr(s));
    writeLine("Product: " + intToStr(product));
    
    // Conditionals with proper boolean validation
    if (s > 25) {
        writeLine("Sum is greater than 25");
    } else {
  - Named return variables: `(var int result = 0) myFunc()`
  - Simple return types: `int add(int a, int b)`
  - Multiple return values with tuple destructuring
  - Exit codes from main function return value
- **Variables**: `var int x = 10;` or type-inferred `var x = 10;`
- **Data Types**:
  - Primitives: `int` (64-bit), `bool`, `string`
  - Structs: Declare, instantiate, access, and modify fields
  - Unions: Declare and pattern match with switch/case
- **Struct Operations**:
  - Declaration: `struct Point { var int x; var int y; };`
  - Instantiation: `var Point p = Point(x: 10, y: 20);`
  - Field access: `p.x`, `p.y`
  - Field assignment: `p.x = 15;`
- **Control flow**: 
  - `if/else` statements with boolean expressions
  - `while` loops
  - `switch/case` pattern matching on union types with variable binding
- **Operators**:
  - Arithmetic: `+`, `-`, `*`, `/`
  - Comparison: `<`, `<=`, `>`, `>=`, `==`, `!=`
  - Logical: `&&`, `||`, `!`
- **Tuple assignment**: `(var x, var y) = functionReturningTwo();`
- **String operations**: String concatenation with `+`, `intToStr()` conversion

### Known Limitations

- **Union type parameters**: Cannot pass struct instances directly to functions expecting union types (implicit upcasting not implemented)
- **Exit codes**: Process exit codes are 8-bit (0-255), so values > 255 are truncated via modulo operation
- **Structs**: Represented internally as `Map<String, Object>` with `__type__` metadata

struct Rectangle {
    var int width;
    var int height;
};

union Shape {
    Circle;
    Rectangle;
};

// Switch/case pattern matching (note: union parameter passing not yet supported)
(var int result = 0) calculateArea(Shape s) {
    switch (s) {
        case Circle c:
            result = c.radius * c.radius * 3;
        case Rectangle r:
            result = r.width * r.height;
        default:
            result = 0;
    }
    return;
}

(var int exitCode = 0) main() {
    // Create and manipulate structs
    var Circle myCircle = Circle(radius: 5);
    var int area = myCircle.radius * myCircle.radius * 3;
    
    // Modify struct fields
    myCircle.radius = 10;
    
    exitCode = 0;
    return;
}
```

### Currently Supported Language Constructs

- **Package declarations**: `package main;`
- **Imports**: `import std.console.writeLine;` or `import std.console.*;`
- **Functions**: With named return variables and multiple return values
- **Variables**: `var int x = 10;` or type-inferred `var x = 10;`
- **Control flow**: `if/else` statements and `while` loops
- **Operators**:
  - Arithmetic: `+`, `-`, `*`, `/`
  - Comparison: `<`, `<=`, `>`, `>=`, `==`, `!=`
  - Logical: `&&`, `||`, `!`
- **Tuple assignment**: `(var x, var y) = functionReturningTwo();`
- **String operations**: String concatenation with `+`, `intToStr()` conversion

### Language Constructs Not Yet Functional

- **Structs**: Defined in grammar but construction/access not implemented
  ```clean
  struct Point {
      var int x;
      var int y;
  };
  ```
- **Unions**: Defined but pattern matching not implemented
- **Switch statements**: Parsed but case matching not implemented
- **Arrays**: Grammar exists but no runtime support
- **Member access**: `.` operator for struct fields
- **Index access**: `[]` operator for arrays

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

Test programs in `core/src/test/resources/`:

- **test_hello.cln** - Hello world with console output
- **test_condition.cln** - If/else conditionals with boolean expressions
- **test_string_util.cln** - String manipulation with `intToStr()`
- **test_unary_expr.cln** - Unary operators (`!`, `-`)
- **test_compiler.cln** - Basic compilation tests
- **test_program.cln** - Multiple language features
- **test_union.cln** - Union type declarations

Working example programs in `core/`:

- **test_hello.cln** - Simple hello world
- **test_return_simple.cln** - Single return value functions
- **test_return_multi.cln** - Multiple return values with tupl - Package structure and organization
- [STATEMENTS_AND_EXPRESSIONS.md](changelog/STATEMENTS_AND_EXPRESSIONS.md) - Statement and expression implementations
- [VISITOR_PATTERN.md](changelog/VISITOR_PATTERN.md) - Visitor pattern usage

## Recent Updates

- ✅ **Operator Enum**: Refactored binary operators from strings to type-safe enum
- ✅ **Multiple Return Values**: Functions can now return multiple values with tuple destructuring
- ✅ **Boolean Validation**: Proper type checking for conditional expressions
- ✅ **Binary Expression Evaluation**: All operators fully implemented with type checking
- ✅ **Improved Error Messages**: Better runtime error reporting for type mismatches
   - Type inference improvements
   - Better error messages for type mismatches

6. **Global Variables**
   - Global variable compilation
   - Proper scope management

### Low Priority

7. **Standard Library Expansion**
   - String manipulation functions
   - Math operations
   - File I/O

8. **Optimization**
   - Performance improvements
   - Memory management

9. **Advanced Features**
   - Generics
   - Closures
   - Advanced pattern matching

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
