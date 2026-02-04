# cln-lang

An interpreter for **cln**, a small embeddable scripting language with ANTLR4-based parsing, AST construction, compilation, and execution capabilities.

## Why cln exists

- **Embeddable DSL**: Designed to be embedded in larger applications as a domain-specific scripting language
- **Deterministic numeric behavior**: 64-bit signed integers and Java's BigDecimal provide consistent, predictable arithmetic across platforms
- **Module system with import resolution**: Package-based organization with wildcard import support
- **Domain data modeling**: Structs and unions enable clear representation of business domain entities
- **Pattern matching**: Switch/case on union types allows elegant handling of variant data
- **Simple, readable syntax**: Familiar C-like syntax with modern features like tuple destructuring and named returns

## Features

### ✅ Fully Implemented

- **Parser & Lexer**: Complete ANTLR4-based grammar for cln-lang
- **AST Construction**: Parse tree to structured Abstract Syntax Tree conversion
- **Type System**: Primitives (int, bool, string, dec), structs, and unions
- **Runtime Execution**: Full interpreter with execution context and function invocation
- **Module System**: Package declarations and imports (including wildcard imports)
- **Expressions**:
  - Binary operators: `+`, `-`, `*`, `/`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `&&`, `||`
  - Unary operators: `!`, `-`
  - Increment/decrement operators: `++`, `--` (both prefix and postfix)
  - Literals: integers, booleans, strings, decimals
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
- **Numeric Types**: 
  - **Integer**: 64-bit signed integers (using Java `long`, range: -2^63 to 2^63-1)
  - **Decimal**: Arbitrary precision decimal numbers (using Java `BigDecimal` for precise decimal arithmetic)
- **Standard Library**: Console I/O (`std.console.writeLine`, `std.console.write`, `std.console.readLine`)
- **String Utilities**: `intToStr` function for integer-to-string conversion
- **Error Handling**: Comprehensive exception handling with detailed error messages
- **Type Safety**: Runtime type checking for operators and conditionals with strict primitive type validation (case-sensitive: `int`, `bool`, `string`, `dec`)
- **Global Variables**: Full support for mutable global variables with `var` keyword
- **Union Types**: 
  - **Declaration**: Define union types with multiple struct members
  - **Pattern Matching**: Switch/case statements on union types with variable binding
  - **Implicit Upcasting**: Struct instances can be passed to functions expecting union types
  - **Type Matching**: Runtime type identification using `__type__` metadata

### 🚧 Partially Implemented

- **Arrays**: Grammar support exists, runtime implementation pending

### ❌ Not Yet Implemented

- **Index Access**: Array indexing (`[]` operator) not functional
- **Assignment to array elements**: Array element assignment not implemented
- **Array Operations**: No array creation, indexing, or manipulation
- **Type Checking**: Static type checking not implemented (runtime only)
- **Semantic Analysis**: No compile-time validation beyond basic type checks

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

- Java 21 or higher (LTS version)
- Maven 3.9 or higher

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

Execute a cln language program:

```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar <program.cln>
```

With verbose output (shows parsing, compilation, and execution details):

```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -v <program.cln>
```

## Language Features

### Basic Syntax

```cln
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
        writeLine("Sum is lesser or equal than 25");
    }
    return;
}
```
- **Basic elements**
  - Named return variables: `(var int result = 0) myFunc()`
  - Simple return types: `int add(int a, int b)`
  - Multiple return values with tuple destructuring
  - Exit codes from main function return value
- **Variables**: `var int x = 10;` or type-inferred `var x = 10;`
- **Data Types**:
  - Primitives: `int` (64-bit), `bool`, `string`, `dec` (BigDecimal)
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
  - Increment/Decrement: `++`, `--` (prefix: `++x`, postfix: `x++`)
- **Tuple assignment**: `(var x, var y) = functionReturningTwo();`
- **String operations**: String concatenation with `+`, `intToStr()` conversion

### Union Types and Pattern Matching

Union types allow you to define tagged unions where a value can be one of several struct types. The runtime automatically supports implicit upcasting from struct instances to union types:

```cln
struct Circle {
    var int radius;
};

struct Rectangle {
    var int width;
    var int height;
};

union Shape {
    Circle;
    Rectangle;
};

// Functions can accept union types and use pattern matching
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

int main() {
    // Create struct instances
    Circle myCircle = Circle(radius: 5);
    Rectangle myRect = Rectangle(width: 10, height: 20);
    
    // Implicit upcasting: pass structs to function expecting union type
    int circleArea = calculateArea(myCircle);  // Works!
    int rectArea = calculateArea(myRect);      // Works!
    
    writeLine("Circle area: " + intToStr(circleArea));
    writeLine("Rectangle area: " + intToStr(rectArea));
    
    return 0;
}
```

**How it works:**
- Each struct instance stores `__type__` metadata with its type name
- Functions accepting union types can receive any member struct
- Switch/case statements match on the struct's actual type
- Variables bound in case clauses have the correct struct type

### Increment and Decrement Operators

```cln
(var int ret = 0) main() {
    var int x = 5;
    
    // Prefix increment - returns new value
    writeLine("++x = " + intToStr(++x));  // Outputs: ++x = 6
    writeLine("x = " + intToStr(x));      // Outputs: x = 6
    
    // Postfix increment - returns old value
    writeLine("x++ = " + intToStr(x++));  // Outputs: x++ = 6
    writeLine("x = " + intToStr(x));      // Outputs: x = 7
    
    // Works in expressions
    var int y = 10 + x++;  // y gets 17, x becomes 8
    
    return;
}
```

### Global Variables

```cln
package main;

import std.console.*;
import std.str.*;

// Global mutable variable
var int globalCounter = 0;

// Global constant
string greeting = "Hello";

increment() {
    globalCounter = globalCounter + 1;  // Can modify with 'var' keyword
    return;
}

int main() {
    writeLine("Counter: " + intToStr(globalCounter));
    increment();
    writeLine("Counter: " + intToStr(globalCounter));
    return 0;
}
```

### Decimal Type (BigDecimal)

The `dec` type provides arbitrary precision decimal arithmetic, ideal for financial calculations and situations requiring exact decimal representation:

```cln
package main;

import std.console.*;

int main() {
    // Decimal literals with fractional parts
    dec price = 19.99;
    dec tax_rate = 0.08;
    dec quantity = 3.5;
    
    // Arithmetic operations with decimals
    dec subtotal = price * quantity;           // 69.965
    dec tax = subtotal * tax_rate;             // 5.59720
    dec total = subtotal + tax;                // 75.56220
    
    writeLine("Total: " + total);
    
    // Mixed operations (int and dec)
    var int whole_units = 10;
    dec unit_price = 5.75;
    dec mixed_total = whole_units * unit_price;  // 57.50
    
    writeLine("Mixed calculation: " + mixed_total);
    
    // Precise division (no rounding errors)
    dec dividend = 10.0;
    dec divisor = 3.0;
    dec result = dividend / divisor;  // 3.333333333333333333333333333333333
    
    writeLine("Precise division: " + result);
    
    // Comparison operations
    dec value1 = 5.5;
    dec value2 = 6.0;
    
    if (value1 < value2) {
        writeLine("5.5 < 6.0 is true");
    }
    
    return 0;
}
```

**Key Features:**
- **Precise arithmetic**: No floating-point rounding errors
- **Mixed operations**: Can combine `int` and `dec` types in expressions
- **All operators supported**: `+`, `-`, `*`, `/`, `<`, `<=`, `>`, `>=`, `==`, `!=`
- **Java BigDecimal**: Uses `java.math.BigDecimal` with `DECIMAL128` precision for division
- **Literal syntax**: Must include decimal point (e.g., `3.14`, `10.0`)

### Known Limitations


- **Exit codes**: Process exit codes are 8-bit (0-255), so values > 255 are truncated via modulo operation
- **Structs**: Represented internally as `Map<String, Object>` with `__type__` metadata
- **Type names**: Primitive type names are case-sensitive and must be lowercase (`int`, `bool`, `string`)
- **Variable mutability**: Variables must be declared with `var` keyword to be modifiable

### Currently Supported Language Constructs

- **Package declarations**: `package main;`
- **Imports**: `import std.console.writeLine;` or `import std.console.*;`
- **Functions**: With named return variables and multiple return values
- **Variables**: 
  - Mutable: `var int x = 10;` or type-inferred `var x = 10;`
  - Constant: `int x = 10;` or `string name = "value";`
- **Structs**: Full declaration, instantiation, field access, and modification support
- **Unions**: Full declaration, switch/case pattern matching, and implicit upcasting from struct members
- **Control flow**: `if/else` statements, `while` loops, and `switch/case` on union types
- **Operators**:
  - Arithmetic: `+`, `-`, `*`, `/` (supports both integer and decimal types)
  - Comparison: `<`, `<=`, `>`, `>=`, `==`, `!=` (supports both integer and decimal types)
  - Logical: `&&`, `||`, `!`
  - Increment/Decrement: `++`, `--`
- **Tuple assignment**: `(var x, var y) = functionReturningTwo();`
- **String operations**: String concatenation with `+`, `intToStr()` conversion
- **Global variables**: Both mutable (`var`) and constant declarations
- **Decimal arithmetic**: Precise decimal calculations using BigDecimal (supports mixed int/dec operations)

### Language Constructs Not Yet Functional

- **Arrays**: Grammar exists but no runtime support for creation, indexing, or manipulation
- **Static type checking**: Only runtime type validation is performed

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

Example programs in `examples/` directory:

**Demonstrations:**
- `hello_world.cln` - Simple hello world program
- `demo_console.cln` - Console I/O with `readLine()` and `write()`
- `demo_functions.cln` - Function declarations with multiple return values
- `demo_simple_return.cln` - Simple return value functions
- `demo_string_util.cln` - String operations and `intToStr()`
- `demo_struct.cln` - Struct declaration, instantiation, and field access
- `demo_switch.cln` - Switch/case pattern matching on union types
- `demo_while.cln` - While loop examples
- `demo_exit_code.cln` - Main function exit codes
- `demo_both_syntaxes.cln` - Different syntax variations
- `demo_decimal.cln` - Decimal type (BigDecimal) arithmetic and operations
- `comprehensive_demo.cln` - Complete showcase of all language features

**Tests:**
- `test_global_var.cln` - Mutable global variables
- `test_increment.cln` - Increment/decrement operators (++/--)
- `test_struct.cln` - Struct operations
- `test_member_assign.cln` - Struct field assignment
- `test_switch_simple.cln` - Simple switch/case
- `test_union_upcast.cln` - Union type upcasting from structs
- `test_union_complete.cln` - Complete union type functionality test
- `test_math.cln` - Mathematical operations
- `test_long_range.cln` - 64-bit integer range testing
- `test_simple_add.cln` - Basic arithmetic
- `test_no_return.cln` - Functions without return values
- `test_exit_code.cln` - Exit code behavior
- `test_triangle.cln` - Triangle calculations
- `test_one_shape.cln` - Single shape example

Additional test files in `core/src/test/resources/`:
- `test_hello.cln` - Basic hello test
- `test_condition.cln` - Conditional statements
- `test_string_util.cln` - String utility tests
- `test_unary_expr.cln` - Unary operator tests
- `test_compiler.cln` - Compilation tests
- `test_program.cln` - Comprehensive feature tests
- `test_union.cln` - Union type tests## Development Roadmap

### ✅ Completed

1. **Core Language Features**
   - ✅ ANTLR4 parser and lexer
   - ✅ AST construction and compilation
   - ✅ Runtime execution engine
   - ✅ Function calls and multiple return values
   - ✅ Binary operators (arithmetic, comparison, logical)
   - ✅ Unary operators (!, -)
   - ✅ Increment/decrement operators (++, --)
   - ✅ Control flow (if/else, while, switch/case)
   - ✅ Struct declaration, instantiation, and field operations
   - ✅ Union declarations and pattern matching
   - ✅ Global variables (mutable with `var`, constant without)
   - ✅ Type validation with runtime checking
   - ✅ String concatenation and utilities
   - ✅ Standard library (console I/O, string conversion)

2. **Type System**
   - ✅ Primitive types (int, bool, string, dec)
   - ✅ Struct types with field access
   - ✅ Union types for tagged variants
   - ✅ Type-safe operators with runtime validation
   - ✅ Case-sensitive type names
   - ✅ Mixed numeric operations (int and dec)

3. **Java 21 Upgrade**
   - ✅ Migrated from Java 17 to Java 21 LTS
   - ✅ All tests passing (62 unit tests)

### 🚧 In Progress / Planned

4. **Arrays**
   - Grammar support exists
   - Runtime implementation needed:
     - Array creation and initialization
     - Index access operator (`[]`)
     - Array element assignment
     - Array methods (length, iteration)

5. **Union Type Enhancements**
   - Implicit upcasting from struct to union types
   - Type coercion for function parameters

6. **Static Type Checking**
   - Compile-time type validation
   - Type inference improvements
   - Better error messages for type mismatches

### 📋 Future Enhancements

7. **Standard Library Expansion**
   - String manipulation functions
   - Math operations (sqrt, pow, abs, etc.)
   - File I/O operations
   - Collection utilities

8. **Language Features**
   - For loops with iterators
   - Break and continue statements
   - Enhanced pattern matching
   - String interpolation

9. **Optimization**
   - Performance improvements
   - Memory management optimizations
   - Bytecode compilation (optional)

10. **Developer Experience**
    - Better error messages with line/column numbers
    - Debug support
    - Language server protocol implementation
    - Syntax highlighting for editors

## Recent Updates

### February 2026
- ✅ **Increment/Decrement Operators**: Added `++` and `--` operators with both prefix and postfix support
- ✅ **Global Variable Support**: Full implementation of mutable global variables with `var` keyword
- ✅ **Unary Operator Fixes**: Fixed negation (`!`) and unary minus (`-`) operators
- ✅ **Type Validation**: Strict case-sensitive type checking for primitive types
- ✅ **Java 21 Upgrade**: Migrated from Java 17 to Java 21 LTS

### Earlier Updates
- ✅ **Operator Enum**: Refactored binary operators from strings to type-safe enum
- ✅ **Multiple Return Values**: Functions can return multiple values with tuple destructuring
- ✅ **Boolean Validation**: Proper type checking for conditional expressions
- ✅ **Binary Expression Evaluation**: All operators fully implemented with type checking
- ✅ **Struct Operations**: Complete struct field access and modification
- ✅ **Switch/Case Pattern Matching**: Union type pattern matching with variable binding

## Changelog

See [changelog/](changelog/) directory for detailed change history:
- [PACKAGE_ORGANIZATION.md](changelog/PACKAGE_ORGANIZATION.md) - Package structure and organization
- [STATEMENTS_AND_EXPRESSIONS.md](changelog/STATEMENTS_AND_EXPRESSIONS.md) - Statement and expression implementations
- [VISITOR_PATTERN.md](changelog/VISITOR_PATTERN.md) - Visitor pattern usage

## License

See [LICENSE](LICENSE) file for details.
