# cln-lang

An interpreter for **cln**, a small embeddable scripting language with ANTLR4-based parsing, AST construction, compilation, and execution capabilities.

## Why cln exists

- **Embeddable DSL**: Designed to be embedded in larger applications as a domain-specific scripting language
- **Deterministic numeric behavior**: 64-bit signed integers and Java's BigDecimal provide consistent, predictable arithmetic across platforms
- **Module system with import resolution**: Package-based organization with wildcard import support
- **Domain data modeling**: Structs and unions enable clear representation of business domain entities
- **Pattern matching**: Switch/case on union types allows elegant handling of variant data
- **Simple, readable syntax**: Familiar syntax with modern features like tuple destructuring and named returns

## Features

### ✅ Fully Implemented

- **Parser & Lexer**: Complete ANTLR4-based grammar for cln-lang
- **AST Construction**: Parse tree to structured Abstract Syntax Tree conversion
- **Type System**: Primitives (int, bool, string, dec), structs, and unions
- **Arrays**: Arrays of primitive types (int[], dec[], bool[], string[])
  - Array literals: `[1, 2, 3]`
  - Array indexing: `arr[0]`
  - Array assignment: `arr[0] = 10`
  - Array length: `arr.length`
  - Bounds checking at runtime
  - Arrays as function parameters and return values
  - **Limitations**: Fixed-size only (no resizing besides copy function), 1D arrays only, primitive type arrays only (no structs/unions)
- **Runtime Execution**: Full interpreter with execution context and function invocation
- **Module System**: 
  - Package declarations with hierarchical namespacing
  - Wildcard imports (`import package.*;`)
  - Cross-package imports with visibility control
  - `expose` keyword for exporting symbols to other packages
  - Eager loading of all source files for comprehensive symbol resolution
  - Standard library imports (`std.console.*`, `std.str.*`, `std.array.*`, `std.math.*`)
  - Duplicate import handling (same package can be imported by multiple files)
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
  - **Decimal**: Arbitrary precision decimal numbers with optional precision control (using Java `BigDecimal`, supports `dec`, `dec(precision)`, `dec(precision, roundingMode)`)
- **Standard Library**: Console I/O, string utilities, array utilities, and math utilities (`std.console.*`, `std.str.*`, `std.array.*`, `std.math.*`)
- **String Utilities**: `intToStr` function for integer-to-string conversion
- **Array Utilities**: `std.array.*` (creation, copy, search, slice, concat)
- **Math Utilities**: `std.math.*` (trig, log/exp, pow/root, rounding, min/max)
- **Error Handling**: Comprehensive exception handling with detailed error messages
- **Type Safety**: Runtime type checking for operators and conditionals with strict primitive type validation (case-sensitive: `int`, `bool`, `string`, `dec`)
- **Global Variables**: Full support for mutable global variables with `var` keyword
- **Union Types**: 
  - **Declaration**: Define union types with multiple struct members
  - **Pattern Matching**: Switch/case statements on union types with variable binding
  - **Implicit Upcasting**: Struct instances can be passed to functions expecting union types
  - **Type Matching**: Runtime type identification using `__type__` metadata

### 🚧 Partially Implemented

- **Arrays of Structs/Unions**: Grammar supports it, but runtime implementation pending for next release

### ❌ Not Yet Implemented

- **Multi-dimensional Arrays**: Only 1D arrays currently supported
- **Array Resizing**: Arrays are fixed-size after creation. Resize only available via copy and creating a new array.
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

- Java 17 or higher (LTS version)
- Maven 3.9 or higher (or use the included Maven Wrapper - no installation needed)

### Building the Project

Build the project and generate the standalone fat JAR:

**Using Maven Wrapper (recommended - no Maven installation required):**
```bash
cd core
./mvnw clean package
```

**Or using your local Maven installation:**
```bash
cd core
mvn clean package
```

This creates:
- `target/core-1.0-SNAPSHOT.jar` - Regular JAR (requires classpath)
- `target/core-1.0-SNAPSHOT-fat.jar` - Standalone JAR with all dependencies (recommended)

### Running Programs

Execute a cln language program using either file-based or package-based startup:

**File-based execution** (for files without package declaration or in default package):
```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar <program.cln>
```

**Package-based execution** (runs main() function from specified package):
```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -cp <source_path> <package.name>

# Example: run main() from 'myapp' package
java -jar target/core-1.0-SNAPSHOT-fat.jar -cp . myapp

# Example: run main() from 'com.example.calculator' package  
java -jar target/core-1.0-SNAPSHOT-fat.jar -cp . com.example.calculator
```

**Note**: When using package-based startup, all `.cln` files in the source path are loaded to enable cross-package imports.

With verbose output (shows parsing, compilation, and execution details):

```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -v <program.cln>
# or
java -jar target/core-1.0-SNAPSHOT-fat.jar -v -cp <source_path> <package.name>
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
  - Primitives: `int` (64-bit), `bool`, `string`, `dec` (BigDecimal with optional precision: `dec`, `dec(2)`, `dec(2, HALF_UP)`)
  - Arrays: `int[]`, `string[]`, `bool[]`, `dec[]` with literal syntax `[1, 2, 3]`
  - Structs: Declare, instantiate, access, and modify fields
  - Unions: Declare and pattern match with switch/case
- **Array Operations**:
  - Creation: `var int[] arr = [1, 2, 3];`
  - Access: `arr[0]`, `arr[i]`
  - Assignment: `arr[0] = 10;`
  - Length: `arr.length`
  - String indexing: `"hello"[0]` returns `"h"`
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

#### Common Field Access

Union types automatically compute common fields that are shared across all member structs. You can access these common fields directly without using switch/case:

```cln
struct User {
    int id;
    string name;
    string email;
};

struct Company {
    int id;
    string name;
    string email;
};

union Entity {
    User;
    Company;
};

// Access common fields directly - no switch/case needed!
printEntityInfo(Entity e) {
    writeLine("ID: " + intToStr(e.id));
    writeLine("Name: " + e.name);
    writeLine("Email: " + e.email);
    return;
}

int main() {
    User john = User(id: 1, name: "John", email: "john@example.com");
    Company acme = Company(id: 2, name: "Acme", email: "info@acme.com");
    
    printEntityInfo(john);   // Works!
    printEntityInfo(acme);   // Works!
    
    return 0;
}
```

**Common field rules:**
- A field is "common" if it exists in all union member structs with the **same type**
- Common fields can be accessed directly on union-typed values
- Use switch/case for accessing type-specific fields
- Runtime validates field access and provides clear error messages

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
- **Mixed operations**: Can combine `int` and `dec` types in expressions always resulting in a `dec` type
- **All operators supported**: `+`, `-`, `*`, `/`, `<`, `<=`, `>`, `>=`, `==`, `!=`
- **Java BigDecimal**: Uses `java.math.BigDecimal` with `DECIMAL128` precision for division
- **Literal syntax**: Must include decimal point (e.g., `3.14`, `10.0`)
- **Precision control**: Optional precision and rounding mode specification (e.g., `dec(2)`, `dec(2, HALF_UP)`)

#### Decimal Type Variants

The `dec` type supports three declaration variants:

1. **Unlimited Precision** (default):
```cln
dec pi = 3.14159265;  // Full precision, no rounding
```

2. **Fixed Precision**:
```cln
dec(2) price = 19.999;  // Rounded to 2 decimal places: 20.00
// Uses HALF_UP rounding by default
```

3. **Fixed Precision with Custom Rounding Mode**:
```cln
dec(2, FLOOR) value = 2.125;  // Rounded down to 2 decimals: 2.12
```

**Supported Rounding Modes:**
- `HALF_UP` (default) - Round towards nearest neighbor, up if equidistant
- `HALF_DOWN` - Round towards nearest neighbor, down if equidistant
- `HALF_EVEN` - Round towards nearest neighbor, to even neighbor if equidistant
- `UP` - Round away from zero
- `DOWN` - Round towards zero
- `CEILING` - Round towards positive infinity
- `FLOOR` - Round towards negative infinity
- `UNNECESSARY` - Assert that no rounding is necessary (throws exception if rounding needed)

**Precision Inheritance:**

When a variable is declared with precision constraints, all assignments to that variable automatically apply the constraints:

```cln
var dec(2) price = 10.555;  // Stored as: 10.56
price = 15.999;             // Automatically rounded to: 16.00
price = 7.123;              // Automatically rounded to: 7.12
```

This ensures consistent precision throughout calculations, making it ideal for financial applications where you need to maintain a specific number of decimal places.

### Known Limitations

- **Arrays**:
  - Fixed-size only: cannot create pre-sized arrays or resize after creation
  - No multi-dimensional arrays (only 1D arrays supported)
  - Only primitive types: arrays of structs/unions not yet implemented
  - No array constructor syntax (e.g., `int[](100)` to create array of size 100)
- **Exit codes**: Process exit codes are 8-bit (0-255), so values > 255 are truncated via modulo operation
- **Structs**: Represented internally as `Map<String, Object>` with `__type__` metadata
- **Type names**: Primitive type names are case-sensitive and must be lowercase (`int`, `bool`, `string`)
- **Variable mutability**: Variables must be declared with `var` keyword to be modifiable

### Module System and Imports

cln-lang has a fully functional package system with cross-package import support:

**Package-Based Organization:**
- Files declare their package: `package com.example.myapp;`
- Directory structure (like in Java) reflects package hierarchy: `com/example/myapp/Main.cln`
- Packages can span multiple files

**Import Mechanisms:**
- **Wildcard imports**: `import std.console.*;` imports all accessible symbols from a package
- **Standard library imports**: `import std.console.*;`, `import std.str.*;`, `import std.array.*;`, `import std.math.*;`
- **Cross-package imports**: Import from other user-defined packages with visibility control
- **Duplicate imports**: Multiple files can safely import the same package

**Visibility Control with `expose`:**
- By default, symbols are package-scoped (only visible within the same package)
- Use `expose` keyword to make symbols accessible from other packages:
  ```cln
  // Helper.cln in package myapp
  package myapp;
  
  // This function is visible to other packages
  expose displayMessage(string msg) {
      writeLine("Helper says: " + msg);
      return;
  }
  
  // This function is only visible within myapp package
  int getConstant() {
      return 42;
  }
  ```

**Cross-Package Import Example:**
```cln
// calculator/Main.cln
package com.example.calculator;

import std.console.*;
import myapp.*;  // Import from another package

int main() {
    displayMessage("Hello from calculator!");  // Works - displayMessage is exposed
    // getConstant();  // ERROR - not exposed
    return 0;
}
```

**Startup Modes:**
- **Package-based**: `java -jar cln.jar -cp . myapp` - Runs main() from specified package
- **File-based**: `java -jar cln.jar program.cln` - Runs main() from default package file
- **Eager loading**: All `.cln` files from source paths are loaded at startup for import resolution

### Currently Supported Language Constructs

- **Package declarations**: `package main;` or `package com.example.myapp;`
- **Imports**: 
  - Standard library: `import std.console.*;`
  - User packages: `import myapp.*;`
  - Cross-package imports with visibility enforcement
- **Visibility control**: `expose` keyword for cross-package symbol access
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

- **Static type checking**: Only runtime type validation is performed
- **Advanced array features**: Multi-dimensional arrays, array resizing, arrays of structs/unions

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

**Package Demos** (`examples/package_demo/`):
- `myapp/` - Simple package with multiple files demonstrating cross-file organization
  - `Main.cln` - Entry point with main() function
  - `Helper.cln` - Helper functions with `expose` visibility control
- `com/example/calculator/` - Nested package demonstrating cross-package imports
  - `Main.cln` - Imports and uses symbols from `myapp` package

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
  - ✅ Standard library (console I/O, string utilities, array utilities, math utilities)

2. **Type System**
   - ✅ Primitive types (int, bool, string, dec)
   - ✅ Struct types with field access
   - ✅ Union types for tagged variants
   - ✅ Type-safe operators with runtime validation
   - ✅ Case-sensitive type names
   - ✅ Mixed numeric operations (int and dec)

3. **Arrays** (NEW!)
   - ✅ Array literals: `[1, 2, 3]`
   - ✅ Array types: `int[]`, `string[]`, `bool[]`, `dec[]`
   - ✅ Index access: `arr[0]`
   - ✅ Array assignment: `arr[0] = 10`
   - ✅ Array length property: `arr.length`
   - ✅ String indexing: `str[0]`
   - ✅ Bounds checking
   - ✅ Arrays as function parameters and return values
   - ❌ Multi-dimensional arrays
   - ❌ Array resizing/pre-sizing
   - ❌ Arrays of structs/unions

4. **Java 21 Upgrade**
   - ✅ Migrated from Java 17 to Java 21 LTS
   - ✅ All tests passing (113 unit tests)

### 🚧 In Progress / Planned

5. **Advanced Arrays**
   - Arrays of structs and unions
   - Multi-dimensional arrays
   - Array initialization only by size with default values
   - Built-in array utilities (sort, filter, map)

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
- ✅ **Array Support**: Added arrays for primitive types (int[], dec[], bool[], string[])
  - Array literals, indexing, assignment, and length property
  - Arrays as function parameters and return values
  - Bounds checking at runtime
  - Fixed-size 1D arrays only (no resizing, no multi-dimensional, no struct/union arrays yet)
- ✅ **Cross-Package Imports**: Full implementation of cross-package imports with `expose` visibility control
  - Wildcard imports: `import myapp.*;`
  - Visibility enforcement: symbols require `expose` keyword to be accessible from other packages
  - Registry-based symbol resolution for import linking
  - Eager loading: all `.cln` files loaded at startup for comprehensive symbol availability
- ✅ **Package-Based Startup**: Dual-mode startup supporting both file-based and package-based execution
  - `java -jar cln.jar -cp . myapp` runs main() from specified package
  - `java -jar cln.jar program.cln` runs main() from file
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
