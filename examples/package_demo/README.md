# Package-Based Startup Examples

This directory contains examples demonstrating the new package-based startup feature.

## Directory Structure

```
package_demo/
├── myapp/                      # Simple package example
│   ├── Main.cln               # Contains main() function
│   └── Helper.cln             # Helper functions
└── com/example/calculator/     # Nested package example
    └── Main.cln               # Calculator application with main()
```

## Running Examples

### Running with Package Name

You can run a package by specifying its package name:

```bash
# Run myapp package
java -jar cln.jar -cp examples/package_demo myapp

# Run calculator package
java -jar cln.jar -cp examples/package_demo com.example.calculator
```

### Running with File Names (Default Package Only)

Files without package declarations can be run directly:

```bash
# This works for files in default package
java -jar cln.jar examples/hello_world.cln

# But NOT for files with package declarations
java -jar cln.jar examples/package_demo/myapp/Main.cln  # ERROR!
```

## Key Rules

1. **File-based startup** (specifying .cln files):
   - Files MUST be in the default package (no `package` declaration)
   - Can specify multiple files
   - All files will be loaded together
   - Must have exactly one `main()` function across all files

2. **Package-based startup** (specifying package name):
   - Specify the package name (e.g., `myapp` or `com.example.calculator`)
   - All files in that package will be loaded automatically
   - Must have exactly one `main()` function in the package
   - Files MUST have matching `package` declarations

3. **Main function requirements**:
   - Must be named `main`
   - Must have no parameters
   - Can optionally return an int (exit code)

## Examples

### Example 1: Simple Package

File: `myapp/Main.cln`
```cln
package myapp;

import std.console.*;

(var int exitCode = 0) main() {
    write("Hello from myapp!");
    return;
}
```

Run with:
```bash
java -jar cln.jar -cp examples/package_demo myapp
```

### Example 2: Multiple Files in Package

Files:
- `myapp/Main.cln` - contains main() and uses helper functions
- `myapp/Helper.cln` - contains utility functions

Both files will be loaded automatically when running the package.

### Example 3: Nested Package

File: `com/example/calculator/Main.cln`
```cln
package com.example.calculator;

// ... calculator code ...
```

Run with:
```bash
java -jar cln.jar -cp examples/package_demo com.example.calculator
```
