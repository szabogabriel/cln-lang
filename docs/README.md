# CLN Documentation

This directory contains comprehensive documentation for CLN standard library packages and internal architecture.

## Architecture & Internals

### [Execution Context System](EXECUTION_CONTEXT.md)
Deep dive into the runtime execution architecture, including how variables are stored, function calls are managed, and scope resolution works.

**Topics Covered:**
- ExecutionContext, GlobalContext, LocalContext, CallFrame architecture
- Zero-boxing optimization for primitive types
- Index-based variable access for performance
- Call stack management and function invocation
- Compilation to runtime integration

---

## Standard Libraries

The following standard library packages are available for use in CLN programs:

### [std.console](STD_CONSOLE.md) - Console I/O
Input and output operations for console interaction.

**Functions:**
- `write` - Write text without newline
- `writeLine` - Write text with newline
- `readLine` - Read user input

---

### [std.math](STD_MATH.md) - Mathematical Functions
Mathematical operations including trigonometry, logarithms, and more.

**Categories:**
- Trigonometric Functions (sin, cos, tan, asin, acos, atan, atan2)
- Exponential & Logarithmic (exp, log, log10)
- Power & Root (pow, sqrt, cbrt)
- Rounding (abs, ceil, floor, round)
- Min/Max (min, max)
- Utility (random, toRadians, toDegrees)

**Total Functions:** 23

---

### [std.str](STD_STRING.md) - String Utilities
C-like string operations for text manipulation.

**Categories:**
- Conversion (intToStr, decToStr, strToInt, boolToStr)
- Manipulation (strLen, strCat, subStr, charAt, indexOf, lastIndexOf)
- Comparison (strCmp, strEq, startsWith, endsWith)
- Transformation (toUpper, toLower, trim, replace, isEmpty)

**Total Functions:** 18

---

### [std.sys](STD_SYS.md) - System Functions
System-level operations including time, environment, and process control.

**Categories:**
- Time Functions (currentTimeMillis, nanoTime)
- Process Control (exit)
- Environment & Properties (getenv, getProperty, getPropertyWithDefault)
- Memory Management (freeMemory, totalMemory, maxMemory, gc)

**Total Functions:** 10

---

### [std.array](STD_ARRAY.md) - Array Utilities
Array operations for creating, manipulating, and querying arrays.

**Categories:**
- Creation & Copying (newArray, copy, copyRange)
- Information (length, isEmpty)
- Search (indexOf, lastIndexOf, contains)
- Modification (fill, reverse)
- Comparison (equals)
- Operations (concat, slice)

**Total Functions:** 13

---

### std.reflect - Reflection Utilities
Runtime inspection and dynamic access to struct, union, and primitive values.

**Categories:**
- Field Access (getField, setField)
- Type Checks (isStruct, isInt, isDec, isBool, isString)
- Struct Inspection (getStructName)
- Typed Getters (getInt, getDec, getBool, getString)

**Total Functions:** 11

**Quick example:**
```cln
import std.reflect.*;

struct Point { var int x; var int y; };

int main() {
    Point p = Point(x: 3, y: 7);

    // Dynamic field read / write
    Any xVal = getField(p, "x");       // → Any (holds 3)
    setField(p, "x", 99);

    // Type inspection
    bool yes = isStruct(p);             // true
    string n = getStructName(p);        // "Point"

    // Safe typed extraction
    int x = getInt(getField(p, "x")); // 99
    return 0;
}
```

**Notes:**
- Struct and union instances are passed as `Any`; the `__type__` metadata is preserved.
- `setField` mutates the instance in-place regardless of whether the field was declared `var`.
- Typed getters (`getInt`, `getString`, etc.) throw a runtime error if the actual type does not match.

---

## Quick Start

To use any standard library in your CLN code, import it using the package name:

```cln
// Import specific functions
import std.console.writeLine;
import std.math.sqrt;

// Import all functions from a package
import std.str.*;

// Use the functions
writeLine("Hello, World!");
decimal result = sqrt(16.0);
String text = toUpper("hello");
```

## Package Naming Convention

All standard library packages follow the naming pattern `std.<category>`:
- `std.console` - Console I/O
- `std.math` - Mathematical operations
- `std.str` - String utilities
- `std.sys` - System operations
- `std.array` - Array utilities
- `std.reflect` - Reflection utilities

## Notes

- All functions are implemented as exposed functions (public API)
- Most functions handle null/invalid inputs gracefully
- Numeric operations use `int` (Long) and `decimal` (BigDecimal) types
- String functions follow C-like conventions where applicable
