# RuntimeConfiguration Class

The `RuntimeConfiguration` class provides a simple and structured way to parse command-line arguments for the CLN language interpreter.

## Features

### Verbose Mode
Enable verbose output with either:
- `-v` (short form)
- `--verbose` (long form)

### CLN Path
Specify paths for source files and libraries:
- `-cp <paths>` (short form)
- `--cln_path <paths>` (long form)

Multiple paths can be separated by the system's path separator:
- Linux/Mac: `:` (colon)
- Windows: `;` (semicolon)

### Source Files
Specify source files or package definitions:
- Single file: `hello.cln`
- Package definition: `myapp.main`
- Multiple files: `file1.cln:file2.cln:file3.cln` (Linux/Mac) or `file1.cln;file2.cln;file3.cln` (Windows)

### Environment Variables

#### CLN_HOME
The `CLN_HOME` environment variable specifies the home folder where out-of-the-box libraries live. When set during construction, the interpreter automatically adds `${CLN_HOME}/lib` to the library search path if the directory exists.

Example:
```bash
export CLN_HOME=/opt/cln
# This will automatically add /opt/cln/lib to the search path
java -jar cln.jar hello.cln
```

#### CLN_PATH
The `CLN_PATH` environment variable provides default library paths that are loaded during construction. Multiple paths are separated by the system's path separator (`:` on Linux/Mac, `;` on Windows). These paths are added after `CLN_HOME/lib` if both are present.

Example:
```bash
export CLN_PATH=/usr/local/lib/cln:/home/user/cln-libs
java -jar cln.jar hello.cln
```

**Loading order:**
1. `${CLN_HOME}/lib` is loaded first (if CLN_HOME is set and the lib directory exists)
2. Paths from `CLN_PATH` are loaded next (if CLN_PATH is set)
3. Paths from `-cp` or `--cln_path` command-line options are added during parsing

## Usage Examples

### Basic usage with a single file
```bash
java -jar cln.jar hello.cln
```

### With verbose output
```bash
java -jar cln.jar -v hello.cln
```

### With CLN path
```bash
java -jar cln.jar -cp /path/to/libs:/path/to/modules hello.cln
```

### Complex example with all options
```bash
java -jar cln.jar -v -cp /libs:/modules app.cln
```

### Package definition
```bash
java -jar cln.jar -v myapp.main
```

### Multiple source files
```bash
# Linux/Mac
java -jar cln.jar file1.cln:file2.cln:file3.cln

# Windows
java -jar cln.jar file1.cln;file2.cln;file3.cln
```

### Using environment variables
```bash
# Set CLN_HOME for out-of-the-box libraries
export CLN_HOME=/opt/cln
java -jar cln.jar hello.cln

# Set CLN_PATH for default library locations
export CLN_PATH=/usr/local/lib/cln:/home/user/cln-libs
java -jar cln.jar hello.cln

# Combine both - CLN_HOME/lib loads first, then CLN_PATH
export CLN_HOME=/opt/cln
export CLN_PATH=/usr/local/lib/cln
java -jar cln.jar hello.cln

# Add additional paths via command-line (added after CLN_HOME/lib and CLN_PATH)
export CLN_PATH=/default/path
java -jar cln.jar -cp /additional/path hello.cln
# All paths will be available: /opt/cln/lib, /default/path, and /additional/path
```

## API Usage

### Creating and parsing configuration
```java
RuntimeConfiguration config = new RuntimeConfiguration();
try {
    config.parse(args);
} catch (IllegalArgumentException e) {
    System.err.println("Error: " + e.getMessage());
    // Handle error
}
```

### Accessing configuration values
```java
// Check if verbose mode is enabled
boolean verbose = config.isVerbose();

// Get the first source file
String sourceFile = config.getFirstSourceFile();

// Get all CLN paths (includes CLN_HOME/lib, CLN_PATH, and -cp paths)
List<String> clnPaths = config.getClnPaths();

// Get all source files
List<String> sourceFiles = config.getSourceFiles();

// Check if values were specified
boolean hasSourceFiles = config.hasSourceFiles();
boolean hasClnPaths = config.hasClnPaths();

// Get CLN_HOME value
String clnHome = config.getClnHome();
```

### Setting values programmatically
```java
RuntimeConfiguration config = new RuntimeConfiguration();
config.setVerbose(true);
config.addClnPath("/path/to/libs");
config.setClnHome("/custom/cln/home");  // For testing purposes
```

## Error Handling

The `parse()` method throws `IllegalArgumentException` for:
- Unknown options (e.g., `-unknown`)
- Missing required values (e.g., `-cp` without a path)

## Implementation Details

- **OS-independent**: Uses `File.pathSeparator` to handle path separators correctly on all operating systems
- **Immutable lists**: Getter methods return new copies of internal lists to prevent external modification
- **Trimming**: Automatically trims whitespace from paths and file names
- **Empty handling**: Ignores empty strings when parsing paths or files
- **Environment variables**:
  - Both `CLN_HOME` and `CLN_PATH` are read and processed during construction
  - `${CLN_HOME}/lib` is added first if the directory exists
  - `CLN_PATH` paths are added next
  - Command-line `-cp` paths are added during parsing (appended to the list)
  - All paths are accumulated in the order they are encountered
