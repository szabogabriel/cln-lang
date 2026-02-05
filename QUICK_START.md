# CLN Quick Start Guide

## Building the Project

```bash
cd core
mvn clean package
```

This creates two jar files:
- `target/core-1.0-SNAPSHOT.jar` - Regular jar
- `target/core-1.0-SNAPSHOT-fat.jar` - Fat jar with all dependencies (use this one!)

## Running CLN Programs

### Basic Syntax

```bash
java -jar core/target/core-1.0-SNAPSHOT-fat.jar -cp <source-path> <file-or-package>
```

**Important**: The `-cp` option specifies WHERE to find your .cln source files, NOT the Java classpath!

### Example 1: Run a Single File

```bash
# From the project root
cd examples
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -cp . hello_world.cln
```

Output:
```
Hello, World!
```

### Example 2: Run from Any Directory

```bash
# From anywhere
java -jar /path/to/core/target/core-1.0-SNAPSHOT-fat.jar -cp /path/to/examples hello_world.cln
```

### Example 3: Run a Package

```bash
# Package-based execution
cd examples
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -cp package_demo myapp
```

### Example 4: Verbose Mode

```bash
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -v -cp . hello_world.cln
```

Shows:
- Files being loaded
- Startup mode (FILES or PACKAGE)
- Compilation progress
- Execution details

## File Requirements

### For File-Based Startup

- Files MUST be in the **default package** (no `package` declaration)
- Specify files on command line: `file1.cln file2.cln`
- Exactly ONE `main()` function across all specified files

Example file structure:
```cln
import std.console.writeLine;

(var int ret = 0) main() {
    writeLine("Hello!");
    return;
}
```

### For Package-Based Startup

- Files MUST have matching `package` declaration
- Directory structure must match package name
- Specify package name on command line: `myapp` or `com.example.myapp`
- Exactly ONE `main()` function across entire package

Example directory structure:
```
src/
  myapp/
    Main.cln      # contains: package myapp;
```

## Common Issues

### Error: "File not found in registry"

**Problem**: The file wasn't loaded because it's not in the source path.

**Solution**: Make sure the `-cp` option points to the directory containing your .cln files:

```bash
# ❌ Wrong
java -jar cln.jar hello_world.cln

# ✅ Correct
java -jar cln.jar -cp . hello_world.cln
```

### Error: "File-based startup requires files in default package"

**Problem**: Your file has a `package` declaration but you're using file-based mode.

**Solution**: Either:
1. Remove the package declaration from the file, OR
2. Use package-based startup instead

### Error: "Package not found"

**Problem**: The package directory doesn't exist or isn't in the source path.

**Solution**: Check that:
1. Directory structure matches package name (`myapp` → `myapp/` folder)
2. `-cp` points to the PARENT directory of your package folder

## Command-Line Options

| Option | Description | Example |
|--------|-------------|---------|
| `-cp <path>` | Source path for .cln files | `-cp /path/to/sources` |
| `-v` or `--verbose` | Enable verbose output | `-v` |
| `file.cln` | Run specific file (default package) | `hello.cln` |
| `package.name` | Run package | `myapp` or `com.example.app` |

## Examples Directory

Try these examples:

```bash
cd examples

# Simple hello world
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -cp . hello_world.cln

# Package example
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -cp package_demo myapp

# With verbose output
java -jar ../core/target/core-1.0-SNAPSHOT-fat.jar -v -cp package_demo myapp
```

## Next Steps

- See [STARTUP_MODES_GUIDE.md](STARTUP_MODES_GUIDE.md) for detailed documentation
- Check `/examples` directory for more examples
- Read `/examples/package_demo/README.md` for package usage examples
