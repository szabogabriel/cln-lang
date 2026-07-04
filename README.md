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
- **Arrays**: Arrays of primitives, structs, and unions; multi-dimensional support
  - Array types: `int[]`, `string[]`, `bool[]`, `dec[]`, `MyStruct[]`, `MyUnion[]`; multi-dimensional: `int[][]`, `int[][][]`
  - Array literals: `[1, 2, 3]`, `[[1, 2], [3, 4]]`
  - Array indexing: `arr[0]`, `matrix[1][2]`
  - Array assignment: `arr[0] = 10`, `matrix[0][1] = 5`
  - Array length: `arr.length` (available at each dimension level)
  - Bounds checking at runtime
  - Arrays as function parameters and return values
  - Dynamic allocation via `std.array`: `newArray(n)`, `newArray2D(rows, cols)`, `newArray3D(depth, rows, cols)`
  - Deep copy for multi-dimensional arrays: `deepCopy(arr)` from `std.array`
  - **Limitations**: Fixed-size only (no resizing after creation)
- **Runtime Execution**: Full interpreter with execution context and function invocation
- **Module System**: 
  - Package declarations with hierarchical namespacing
  - Wildcard imports (`import package.*;`)
  - Cross-package imports with visibility control
  - `expose` keyword for exporting symbols to other packages
  - Eager loading of all source files for comprehensive symbol resolution
  - Standard library imports (`std.console.*`, `std.str.*`, `std.array.*`, `std.math.*`, `std.calendar.*`, `std.reflect.*`)
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
- **Standard Library**: Console I/O, string utilities, array utilities, math utilities, date/time support, and reflection (`std.console.*`, `std.str.*`, `std.array.*`, `std.math.*`, `std.calendar.*`, `std.reflect.*`)
- **Calendar / Date-Time** (`std.calendar.*`):
  - Structs: `Timestamp` (9 fields), `Date` (3 fields), `Time` (5 fields)
  - Current snapshots: `now()`, `nowDate()`, `nowTime()`
  - Arithmetic: `plus/minusYears/Months/Days/Hours/Minutes/Seconds/Milliseconds`
  - Comparison: `isBefore`, `isAfter`
  - Difference: `diffDays/Hours/Minutes/Seconds/Milliseconds`
  - Field setters: `withYear/Month/Day/Hour/Minute/Second`
  - Timezone conversion: `toTimezone(ts, tz)`
  - Utility: `dayOfWeek` (ISO 8601, 1=Mon…7=Sun), `fromEpoch`
  - Struct conversions: `timestampToDate`, `timestampToTime`, `dateToTimestamp`, `timeToTimestamp`, `dateTimeToTimestamp`
  - Formatting: `timestampToString`, `dateToString`, `timeToString`
  - Parsing: `toTimestamp`, `toDate`, `toTime`
  - 34 format constants (e.g. `FORMAT_DATETIME`, `FORMAT_ISO_OFFSET_DATETIME`, `FORMAT_RFC1123`, …)
- **String Utilities**: `intToStr` function for integer-to-string conversion
- **Array Utilities**: `std.array.*` (creation, copy, search, slice, concat)
- **Math Utilities**: `std.math.*` (trig, log/exp, pow/root, rounding, min/max)
- **Reflection Utilities**: `std.reflect.*` (field access, type checks, typed getters)
- **Error Handling**: Comprehensive exception handling with detailed error messages
- **Type Safety**: Runtime type checking for operators and conditionals with strict primitive type validation (case-sensitive: `int`, `bool`, `string`, `dec`)
- **Global Variables**: Full support for mutable global variables with `var` keyword
- **Union Types**: 
  - **Declaration**: Define union types with multiple struct members
  - **Pattern Matching**: Switch/case statements on union types with variable binding
  - **Implicit Upcasting**: Struct instances can be passed to functions expecting union types
  - **Type Matching**: Runtime type identification using `__type__` metadata

### ❌ Not Yet Implemented

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

## Web UI Demo

A browser-based demo lets you write, save and execute cln-lang programs against an H2 database without installing anything locally.

### Running with Podman (or Docker)

**Build the image** (from the repository root):
```bash
podman build -t cln-lang-demo .
```

**Run — ephemeral (fresh database each start):**
```bash
podman run -p 8080:8080 cln-lang-demo
```

**Run — persistent (database survives container restarts):**
```bash
podman run -p 8080:8080 -v cln-data:/app/data cln-lang-demo
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

Replace `podman` with `docker` if you prefer Docker — both work with the provided `Containerfile`.

## JDBC Database Backend

Instead of storing `.cln` source files on the filesystem, you can keep them in any JDBC-compatible relational database. This is useful when source code lives in a managed store, is deployed as part of a service, or needs versioning and metadata in a structured way.

### Supported Databases

The vendor is detected automatically from the JDBC URL prefix. A vendor-specific `CREATE TABLE` statement is used when the schema is created automatically:

| Vendor | JDBC URL prefix | Default driver class |
|---|---|---|
| H2 | `jdbc:h2:` | `org.h2.Driver` *(bundled)* |
| PostgreSQL | `jdbc:postgresql:` | `org.postgresql.Driver` |
| MySQL | `jdbc:mysql:` | `com.mysql.cj.jdbc.Driver` |
| MariaDB | `jdbc:mariadb:` | `org.mariadb.jdbc.Driver` |
| SQL Server | `jdbc:sqlserver:` | `com.microsoft.sqlserver.jdbc.SQLServerDriver` |
| Oracle | `jdbc:oracle:` | `oracle.jdbc.OracleDriver` |
| DB2 | `jdbc:db2:` | `com.ibm.db2.jcc.DB2Driver` |
| SQLite | `jdbc:sqlite:` | `org.sqlite.JDBC` |

For any vendor other than H2, place the driver JAR on the classpath and supply the driver class via `-cdd` or `CLN_DB_DRIVER` (see below).

### How It Works

The interpreter detects that a JDBC URL is supplied (any value starting with `jdbc:`) for the `-cp` / `--cln_path` option **or** the `CLN_PATH` environment variable and automatically switches from the filesystem loader to the `JdbcLoader`. All other behavior (package resolution, import linking, execution) stays identical.

### Schema

The loader automatically creates the following table the first time it connects (exact DDL is vendor-specific, but the logical structure is the same for all vendors):

```sql
-- Example: H2 / DB2 syntax
CREATE TABLE IF NOT EXISTS CLN_SOURCE (
    id        BIGINT       GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    package   VARCHAR(512) NOT NULL,
    source    CLOB         NOT NULL,
    createdAt TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version   INT          NOT NULL DEFAULT 1
);
```

| Column | Type | Purpose |
|---|---|---|
| `id` | `BIGINT` | Auto-generated primary key; rows are loaded in `id` order |
| `package` | `VARCHAR(512)` | Package name this source belongs to (e.g. `myapp`, `com.example.util`) |
| `source` | `CLOB` | Full CLN source code |
| `createdAt` | `TIMESTAMP` | Row creation time (filled automatically) |
| `updatedAt` | `TIMESTAMP` | Last update time (fill/update in your tooling) |
| `version` | `INT` | Informational version counter; not interpreted by the runtime |

### JDBC Driver

The H2 driver is **bundled in the fat JAR** (`core-1.0-SNAPSHOT-fat.jar`) and loaded explicitly at startup. The driver class is resolved in the following order:

1. `-cdd` / `--cln-db-driver` runtime argument
2. `CLN_DB_DRIVER` environment variable
3. Built-in default: `org.h2.Driver`

For all other databases, add the vendor's JDBC driver JAR to the classpath and supply the driver class name via one of the options above.

### Storing Source Code

Insert your CLN source files into the `CLN_SOURCE` table before running:

```sql
INSERT INTO CLN_SOURCE (package, source) VALUES
    ('myapp', '
package myapp;

import std.console.*;
import std.str.*;

(var int result = 0) main() {
    writeLine("Hello from the database!");
    result = 0;
    return;
}
');
```

Each row corresponds to one logical CLN file. Multiple rows can share the same package name (the loader registers all of them).

### Running from the Database

**H2 file-based database:**
```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -cp jdbc:h2:./mydb myapp
```

**H2 in-memory database** (useful for testing and ephemeral scenarios):
```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -cp "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1" myapp
```

**With verbose output:**
```bash
java -jar target/core-1.0-SNAPSHOT-fat.jar -v -cp jdbc:h2:./mydb myapp
```

**Via the CLN_PATH environment variable:**
```bash
export CLN_PATH=jdbc:h2:./mydb
java -jar target/core-1.0-SNAPSHOT-fat.jar myapp
```

**PostgreSQL:**
```bash
java -cp target/core-1.0-SNAPSHOT-fat.jar:postgresql-42.x.x.jar \
    org.clnlang.Main \
    -cdd org.postgresql.Driver \
    -cp jdbc:postgresql://localhost:5432/clnsrc \
    myapp
```

**MySQL / MariaDB:**
```bash
java -cp target/core-1.0-SNAPSHOT-fat.jar:mysql-connector-j-8.x.x.jar \
    org.clnlang.Main \
    -cdd com.mysql.cj.jdbc.Driver \
    -cp jdbc:mysql://localhost:3306/clnsrc \
    myapp
```

### Configuration Reference

| Option | Description |
|---|---|
| `-cp jdbc:<url>` / `--cln_path jdbc:<url>` | JDBC URL; triggers the database loader |
| `CLN_PATH=jdbc:<url>` | Same, via environment variable |
| `-cdd <class>` / `--cln-db-driver <class>` | JDBC driver class name to load explicitly |
| `CLN_DB_DRIVER=<class>` | Same, via environment variable |

### Full Example

1. **Build the fat JAR:**
   ```bash
   cd core && ./mvnw clean package
   ```

2. **Seed the database** (using H2's built-in shell or any JDBC client):
   ```bash
   java -cp target/core-1.0-SNAPSHOT-fat.jar org.h2.tools.Shell \
       -url jdbc:h2:./demodb -sql "
   INSERT INTO CLN_SOURCE (package, source) VALUES
       ('hello', 'package hello;
import std.console.*;
(var int result = 0) main() { writeLine(\"Hello from the database!\"); return; }
');"
   ```
   *(The schema is created automatically on first run — no manual `CREATE TABLE` needed.)*

3. **Run:**
   ```bash
   java -jar target/core-1.0-SNAPSHOT-fat.jar -cp jdbc:h2:./demodb hello
   # Output: Hello from the database!
   ```

### Java Integration

When embedding cln-lang in a Java application that already uses a database, two dedicated classes make it straightforward to manage CLN source records programmatically — with no third-party framework required.

#### `ClnSource` — the row POJO

`ClnSource` is an immutable POJO that mirrors a row in `CLN_SOURCE`. It is intentionally annotation-free so you can use it directly or wrap it in your own JPA/jOOQ entities.

```java
// Create a new (unsaved) record
ClnSource newRecord = ClnSource.of("com.example.myapp", clnSourceCode);

// Create with full control via the builder
ClnSource existing = ClnSource.builder()
        .id(1L)
        .packageName("com.example.myapp")
        .source(clnSourceCode)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .version(2)
        .build();
```

#### `ClnSourceRepository` — the persistence interface

`ClnSourceRepository` is a framework-neutral interface covering the common CRUD operations:

```java
public interface ClnSourceRepository {
    ClnSource          save(ClnSource source);               // insert or update
    List<ClnSource>    findAll();                            // all rows, ordered by id
    Optional<ClnSource> findByPackageName(String pkg);       // lookup by package
    boolean            deleteByPackageName(String pkg);      // returns true if deleted
    long               count();
}
```

You can implement this interface with any persistence technology (JPA, jOOQ, MyBatis, …). A plain-JDBC implementation is provided out of the box.

#### `JdbcClnSourceRepository` — plain-JDBC implementation

`JdbcClnSourceRepository` requires only a `javax.sql.DataSource`. It reuses the same vendor-specific DDL as the runtime loader, so the schema definition stays in one place.

```java
// Any DataSource works — connection pool, simple JDBC DataSource, etc.
JdbcDataSource ds = new JdbcDataSource();
ds.setURL("jdbc:h2:./mydb");

ClnSourceRepository repo = new JdbcClnSourceRepository(ds, "jdbc:h2:./mydb");
repo.ensureSchema();   // creates CLN_SOURCE if absent (vendor-specific DDL)

// Store a CLN program
ClnSource saved = repo.save(ClnSource.of("com.example.myapp", clnSourceCode));
System.out.println("Saved with id: " + saved.getId());

// Look it up later
repo.findByPackageName("com.example.myapp").ifPresent(r ->
    System.out.println("Source: " + r.getSource()));

// Then execute it via ClnDbMain
ClnDbMain runner = new ClnDbMain("jdbc:h2:./mydb", List.of("com.example.myapp"));
int exitCode = runner.execute();
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
  - Arrays: `int[]`, `string[]`, `bool[]`, `dec[]`, `MyStruct[]`, `MyUnion[]`; multi-dimensional: `int[][]`, `int[][][]`; literals: `[1, 2, 3]`, `[[1, 2], [3, 4]]`
  - Structs: Declare, instantiate, access, and modify fields
  - Unions: Declare and pattern match with switch/case
- **Array Operations**:
  - Creation: `var int[] arr = [1, 2, 3];`, `var int[][] mat = [[1, 2], [3, 4]];`
  - Dynamic allocation: `newArray(n)`, `newArray2D(rows, cols)`, `newArray3D(depth, rows, cols)` via `std.array`
  - Access: `arr[0]`, `arr[i]`, `mat[1][2]`
  - Assignment: `arr[0] = 10;`, `mat[0][1] = 5;`
  - Length: `arr.length`, `mat[0].length` (at each dimension)
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
  - Fixed-size only: cannot resize arrays after creation (use `newArray`/`newArray2D`/`newArray3D` from `std.array` for pre-sized allocation)
  - No typed array constructor syntax (e.g., `int[](100)` to create a typed array of size 100)
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
- **Standard library imports**: `import std.console.*;`, `import std.str.*;`, `import std.array.*;`, `import std.math.*;`, `import std.calendar.*;`, `import std.reflect.*;`
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
  - Standard library: `import std.console.*;`, `import std.calendar.*;`
  - User packages: `import myapp.*;`
  - Cross-package imports with visibility enforcement
- **Visibility control**: `expose` keyword for cross-package symbol access
- **Functions**: With named return variables and multiple return values
- **Variables**: 
  - Mutable: `var int x = 10;` or type-inferred `var x = 10;`
  - Constant: `int x = 10;` or `string name = "value";`
- **Structs**: Full declaration, instantiation, field access, and modification support
- **Unions**: Full declaration, switch/case pattern matching, and implicit upcasting from struct members
- **Arrays**: 1D and multi-dimensional; primitive, struct, and union element types
  - Literal syntax: `[1, 2, 3]`, `[[1, 2], [3, 4]]`
  - Types: `int[]`, `int[][]`, `MyStruct[]`, `MyUnion[]`, etc.
  - Dynamic allocation via `std.array`: `newArray(n)`, `newArray2D(rows, cols)`, `newArray3D(depth, rows, cols)`
  - Deep copy for multi-dimensional arrays: `deepCopy(arr)`
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
- **Advanced array features**: Array resizing after creation

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
- **ClnLoader / FileSystemLoader**: Load and compile `.cln` files from the filesystem
- **JdbcLoader**: Load and compile CLN source stored in any JDBC-compatible database; vendor detected automatically from the URL prefix
- **DbVendors**: Enum of supported JDBC vendors; provides `fromJdbc(url)` for vendor detection and drives DDL dialect selection
- **ClnSource**: Immutable POJO representing one `CLN_SOURCE` row; use `ClnSource.of(pkg, src)` or `ClnSource.builder()` to construct
- **ClnSourceRepository**: Framework-neutral interface for CRUD operations on `CLN_SOURCE`
- **JdbcClnSourceRepository**: Plain-JDBC implementation of `ClnSourceRepository`; requires only a `DataSource`
- **ClnLoaderFactory**: Selects the appropriate loader based on the configured path (filesystem vs. JDBC URL)

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
- **H2 Database** (`com.h2database:h2`): Bundled at compile scope so the database loader works out of the box from the fat JAR
- **Maven Shade Plugin**: Creates fat JAR with all dependencies (including H2)
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
- `demo_calendar.cln` - Full showcase of `std.calendar.*`: snapshots, arithmetic, comparison, difference, field setters, timezone conversion, struct conversions, formatting, and parsing
- `demo_reflection.cln` - Full showcase of `std.reflect.*`: `getField`, `setField`, `isStruct`, `getStructName`, type checks (`isInt`, `isDec`, `isBool`, `isString`), and typed getters
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
  - ✅ Standard library (console I/O, string utilities, array utilities, math utilities, calendar/date-time)

2. **Type System**
   - ✅ Primitive types (int, bool, string, dec)
   - ✅ Struct types with field access
   - ✅ Union types for tagged variants
   - ✅ Type-safe operators with runtime validation
   - ✅ Case-sensitive type names
   - ✅ Mixed numeric operations (int and dec)

3. **Arrays**
   - ✅ Array literals: `[1, 2, 3]`, `[[1, 2], [3, 4]]`
   - ✅ Array types: `int[]`, `string[]`, `bool[]`, `dec[]`, `MyStruct[]`, `MyUnion[]`
   - ✅ Multi-dimensional arrays: `int[][]`, `int[][][]` with literal and dynamic allocation
   - ✅ Index access: `arr[0]`, `matrix[1][2]`
   - ✅ Array assignment: `arr[0] = 10`, `matrix[0][1] = 5`
   - ✅ Array length property: `arr.length` (at each dimension)
   - ✅ String indexing: `str[0]`
   - ✅ Bounds checking
   - ✅ Arrays as function parameters and return values
   - ✅ Dynamic allocation: `newArray(n)`, `newArray2D(rows, cols)`, `newArray3D(depth, rows, cols)`
   - ✅ Deep copy for multi-dimensional arrays: `deepCopy(arr)`
   - ❌ Array resizing after creation

4. **Java 21 Upgrade**
   - ✅ Migrated from Java 17 to Java 21 LTS
   - ✅ All tests passing (113 unit tests)

### 🚧 In Progress / Planned

5. **Advanced Arrays**
   - Array initialization with typed default values
   - Built-in array utilities (sort, filter, map)

5. **Union Type Enhancements**
   - Type coercion for function parameters

6. **Static Type Checking**
   - Compile-time type validation
   - Type inference improvements
   - Better error messages for type mismatches

### 📋 Future Enhancements

7. **Standard Library Expansion**
   - ✅ String manipulation functions
   - ✅ Math operations (sqrt, pow, abs, etc.)
   - ✅ Calendar / date-time (`std.calendar.*`)
   - ✅ Reflection utilities (`std.reflect.*`)
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

### July 2026
- ✅ **Multi-dimensional Arrays and Struct/Union Arrays**: Extended array support beyond 1D primitives
  - Multi-dimensional array literals: `[[1, 2], [3, 4]]`, `[[[1, 2], [3, 4]], [[5, 6], [7, 8]]]`
  - Multi-dimensional array types: `int[][]`, `int[][][]`, `MyStruct[][]`, etc.
  - Arrays of structs and unions: `Point[]`, `Shape[]` with full indexing and assignment
  - Dynamic allocation via `std.array`: `newArray2D(rows, cols)`, `newArray3D(depth, rows, cols)`
  - Deep copy for multi-dimensional arrays: `deepCopy(arr)` from `std.array`
  - Chained indexing: `matrix[1][2]`, `cube[0][1][0]`
- ✅ **Calendar / Date-Time Standard Library** (`std.calendar.*`): Full date and time support
  - Three structs exposed to CLN programs: `Timestamp` (epoch ms + 8 fields), `Date` (year/month/day), `Time` (hour/minute/second/millisecond/timezone)
  - Current-time snapshots: `now()`, `nowDate()`, `nowTime()`
  - Arithmetic on `Timestamp`: `plus/minus` × 7 units (years, months, days, hours, minutes, seconds, milliseconds)
  - Comparison: `isBefore(a, b)`, `isAfter(a, b)`
  - Difference: `diffDays/Hours/Minutes/Seconds/Milliseconds(a, b)` → `int`
  - Field setters: `withYear/Month/Day/Hour/Minute/Second(ts, value)` → `Timestamp`
  - Timezone conversion: `toTimezone(ts, tz)` → `Timestamp` (same instant, new zone)
  - Utility: `dayOfWeek(ts)` → `int` (ISO 8601: 1=Mon…7=Sun), `fromEpoch(millis)` → `Timestamp`
  - Struct conversions: `timestampToDate`, `timestampToTime`, `dateToTimestamp`, `timeToTimestamp`, `dateTimeToTimestamp`
  - Formatting: `timestampToString`, `dateToString`, `timeToString` (pattern-based)
  - Parsing: `toTimestamp`, `toDate`, `toTime` (pattern-based, multi-format fallback for `toTimestamp`)
  - 34 named format constants (ISO, locale-aware, compact, RFC 1123, and more)
- ✅ **Reflection Standard Library** (`std.reflect.*`): Runtime inspection and dynamic field access
  - `getField(Any s, string fieldName) → Any` — read any field from a struct or union by name
  - `setField(Any s, string fieldName, Any value)` — write any field by name (mutates in-place)
  - `isStruct(Any s) → bool` — check whether a value is a struct or union instance
  - `getStructName(Any s) → string` — return the `__type__` name of a struct/union instance
  - Primitive type checks: `isInt`, `isDec`, `isBool`, `isString` — each `(Any) → bool`
  - Typed getters: `getInt`, `getDec`, `getBool`, `getString` — each `(Any) → T`, throws on type mismatch
  - `Any` wildcard type accepted in parameter and variable positions
  - Compiler extended to resolve stdlib struct types (`Timestamp`, `Date`, `Time`) in user programs
  - Constants resolved at link time so format strings are available as plain `string` values

### April 2026
- ✅ **H2 Database Backend**: CLN source files can now be stored in and loaded from an H2 relational database
  - Set `-cp` (or `CLN_PATH`) to any JDBC URL (e.g. `jdbc:h2:./mydb`) to activate the database loader automatically
  - `CLN_SOURCE` table is created automatically on first connection
  - JDBC driver loaded explicitly via `-cdd` / `--cln-db-driver` argument or `CLN_DB_DRIVER` environment variable; defaults to the bundled `org.h2.Driver`
  - H2 driver (`com.h2database:h2`) included in the fat JAR — no extra setup required
  - All existing features (package resolution, imports, cross-package visibility) work identically with the database backend

### February 2026
- ✅ **Array Support**: Added arrays for primitive types (int[], dec[], bool[], string[])
  - Array literals, indexing, assignment, and length property
  - Arrays as function parameters and return values
  - Bounds checking at runtime
  - Fixed-size only (no resizing)
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
