# CLN Startup Modes Guide

## Overview

The CLN language compiler now supports two distinct startup modes for executing programs:

1. **File-Based Startup** - Execute specific .cln files from the command line
2. **Package-Based Startup** - Execute all .cln files within a named package

## Lazy Loading Strategy

The startup system uses **lazy loading** to optimize performance and enable single-file testing:

### Principles

1. **No Eager Loading**: Files are not loaded from source paths (`CLN_PATH`, `-cp`) unless explicitly needed
2. **Only Load What's Needed**: Only explicitly specified files plus their imports are loaded
3. **Standard Library Always Available**: Standard library modules are always registered
4. **Recursive Import Resolution**: Imports are followed recursively to load all dependencies

### Benefits

- ✅ Faster startup times (only loads required files)
- ✅ Enables single-file testing without `-cp` option
- ✅ Avoids conflicts from unrelated files in source paths
- ✅ Supports script-like usage patterns

### How It Works

**File-Based Mode**:
1. Load explicitly specified .cln files
2. For each file, recursively load any `import` statements
3. Stop when all dependencies are resolved

**Package-Based Mode**:
1. Scan source paths to find files declaring the target package
2. For each package file, recursively load any `import` statements
3. Stop when all dependencies are resolved

## Architecture

The startup system uses a three-layer architecture:

```
Registry → StartupContext → ExecutionContext → GlobalContext
```

### Components

- **Registry**: Central registry of all compiled programs organized by package name, plus catalog of functions, types, variables, etc.
- **StartupContext**: Determines startup mode and prepares execution environment with lazy loading
- **ExecutionContext**: Program-specific runtime context (functions, types, variables)
- **GlobalContext**: Consolidated global view of all program elements

## File-Based Startup Mode

### Requirements

- Files specified as command-line arguments
- Files MUST be in the **default package** (no `package` declaration allowed)
- Files MUST exist in one of the configured source paths (`-cp` option)
- Files and their imports are loaded into the execution context

### Usage Examples

```bash
# Single file
java -jar cln.jar -cp /path/to/src file1.cln

# Multiple files
java -jar cln.jar -cp /path/to/src file1.cln file2.cln file3.cln

# With verbose output
java -jar cln.jar -v -cp /path/to/src file1.cln
```

### File Structure Example

```cln
// No package declaration - this is in default package

import std.console.writeLine;

(var int result = 0) main() {
    writeLine("Hello from file-based mode!");
    return;
}
```

### Validation Rules

1. ✅ Files must exist in configured source paths
2. ✅ Files must NOT have a package declaration
3. ✅ All imports are resolved and loaded
4. ✅ Exactly ONE `main()` function across all specified files
5. ❌ Error if file declares a package
6. ❌ Error if main() is missing or duplicated

## Package-Based Startup Mode

### Requirements

- Package name specified as command-line argument
- ALL .cln files in the package are loaded automatically
- Package directory structure must match package name
- Files MUST have matching `package` declaration
- Direct and transitive imports are resolved

### Usage Examples

```bash
# Simple package
java -jar cln.jar -cp /path/to/src myapp

# Nested package
java -jar cln.jar -cp /path/to/src com.example.calculator

# With verbose output
java -jar cln.jar -v -cp /path/to/src myapp
```

### Directory Structure Example

```
src/
  myapp/
    Main.cln       # contains: package myapp;
    Helper.cln     # contains: package myapp;
  com/
    example/
      calculator/
        Main.cln   # contains: package com.example.calculator;
```

### File Structure Example

```cln
package myapp;

import std.console.writeLine;

(var int result = 0) main() {
    writeLine("Hello from package-based mode!");
    return;
}

(var int x = 5) calculate() {
    return x * 2;
}
```

### Validation Rules

1. ✅ Package directory must exist in configured source paths
2. ✅ All .cln files in package are automatically loaded
3. ✅ Files must have matching package declaration
4. ✅ All imports (direct and transitive) are resolved
5. ✅ Exactly ONE `main()` function across entire package
6. ❌ Error if package not found
7. ❌ Error if main() is missing or duplicated

## Main Function Requirements

Both startup modes share the same requirements for the `main()` function:

### Signature

```cln
(var int result = 0) main() {
    // function body
    return;
}
```

### Rules

- **Name**: Must be exactly `main`
- **Parameters**: Must have NO parameters
- **Uniqueness**: Exactly ONE main function in the startup scope
  - File mode: across all specified files
  - Package mode: across entire package
- **Location**: 
  - File mode: In any of the specified files (default package only)
  - Package mode: In any file within the package

## Error Handling

### Common Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `No source files or package specified` | No arguments provided | Specify files or package name |
| `File not found: <file>` | File doesn't exist or not in source path | Check file path and `-cp` option |
| `File not found in registry: <file>` | File not compiled/loaded | Check source paths configuration |
| `File-based startup requires files in default package` | File has package declaration | Remove package declaration or use package mode |
| `No 'main' function found` | No main() in specified scope | Add main() function |
| `Multiple 'main' functions found` | More than one main() in scope | Keep only one main() function |
| `Package not found: <package>` | Package directory doesn't exist | Check package name and source paths |

## Configuration

### Source Paths (`-cp` option)

Specify where to find .cln source files:

```bash
# Single path
-cp /home/user/project/src

# Multiple paths (platform-specific delimiter)
-cp /home/user/project/src:/home/user/lib/src  # Linux/Mac
-cp C:\project\src;C:\lib\src                  # Windows
```

### Verbose Mode (`-v` or `--verbose`)

Enable detailed output:

```bash
java -jar cln.jar -v -cp /path/to/src myapp
```

Verbose output includes:
- Loading progress
- Startup mode detected (FILES or PACKAGE)
- Files being compiled
- Main function discovery
- Execution details

## Examples

### Example 1: Simple File-Based Execution

**File: hello.cln**
```cln
import std.console.writeLine;

(var int ret = 0) main() {
    writeLine("Hello, World!");
    return;
}
```

**Command:**
```bash
java -jar cln.jar -cp . hello.cln
```

### Example 2: Package-Based Execution

**Directory Structure:**
```
src/
  myapp/
    Main.cln
    Helper.cln
```

**File: myapp/Main.cln**
```cln
package myapp;

import std.console.writeLine;

(var int result = 0) main() {
    var int value = calculate();
    writeLine("Result: " + value);
    return;
}
```

**File: myapp/Helper.cln**
```cln
package myapp;

export (var int x = 10) calculate() {
    return x * 2;
}
```

**Command:**
```bash
java -jar cln.jar -cp src myapp
```

### Example 3: Multiple Files with Imports

**Files:**
- `app.cln` (no package)
- `util.cln` (no package)

**Command:**
```bash
java -jar cln.jar -cp . app.cln util.cln
```

## Migration from Old System

### Before (Old System)

```bash
# Auto-detected single file in current directory
java -jar cln.jar

# Explicit file
java -jar cln.jar myfile.cln
```

### After (New System)

```bash
# Must specify source path and file
java -jar cln.jar -cp . myfile.cln

# Or use package mode
java -jar cln.jar -cp . packagename
```

### Key Changes

1. **Source paths required**: Must use `-cp` option
2. **No auto-detection**: Must explicitly specify files or package
3. **Package separation**: File mode = default package only, Package mode = named packages only
4. **All imports resolved**: System automatically loads dependencies

## Testing

Comprehensive test suites are available:

- `FileBasedStartupTest.java` - 6 tests for file-based mode
- `PackageBasedStartupTest.java` - 7 tests for package-based mode
- `MainTest.java` - 14 integration tests

Run tests:
```bash
mvn test -Dtest=FileBasedStartupTest,PackageBasedStartupTest
```

## See Also

- Example files: `/examples/package_demo/`
- Registry implementation: `org.clnlang.runtime.execution.Registry`
- Startup logic: `org.clnlang.startup.StartupContext`
- Main entry point: `org.clnlang.Main`
