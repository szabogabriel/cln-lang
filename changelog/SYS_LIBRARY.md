# System Library (std.sys)

The `std.sys` package provides system-level functionality including time measurement, process control, environment access, and memory information.

## Time Functions

### currentTimeMillis() -> Long
Returns the current time in milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC).

**Use case:** Measuring elapsed time, timestamping events, basic timing operations.

**Example:**
```cln
import std.sys.currentTimeMillis;

var int startTime = currentTimeMillis();
// ... do some work ...
var int endTime = currentTimeMillis();
var int elapsed = endTime - startTime;
```

### nanoTime() -> Long
Returns the current value of the running JVM's high-resolution time source, in nanoseconds.

**Use case:** High-precision time measurement. Note: this is not related to wall-clock time and should only be used for measuring elapsed time differences.

**Example:**
```cln
import std.sys.nanoTime;

var int start = nanoTime();
// ... do some work ...
var int end = nanoTime();
var int elapsedNanos = end - start;
```

## Process Control

### exit(code: Long)
Terminates the currently running program with the specified exit code.

**Convention:** 
- 0 indicates normal/successful termination
- Non-zero indicates an error

**Example:**
```cln
import std.sys.exit;

// Exit with error code
exit(1);
```

## Environment Variables

### getenv(name: String) -> String
Gets the value of the specified environment variable. Returns `null` if the variable is not defined.

**Example:**
```cln
import std.sys.getenv;

var string path = getenv("PATH");
var string home = getenv("HOME");
```

## System Properties

### getProperty(key: String) -> String
Gets the system property indicated by the specified key. Returns `null` if there is no property with that key.

**Common Properties:**
- `"user.name"` - User's account name
- `"user.home"` - User's home directory  
- `"user.dir"` - Current working directory
- `"os.name"` - Operating system name
- `"os.version"` - Operating system version
- `"java.version"` - Java Runtime Environment version

**Example:**
```cln
import std.sys.getProperty;

var string userName = getProperty("user.name");
var string osName = getProperty("os.name");
```

### getPropertyWithDefault(key: String, defaultValue: String) -> String
Gets the system property indicated by the specified key, or returns the default value if the property is not found.

**Example:**
```cln
import std.sys.getPropertyWithDefault;

var string configPath = getPropertyWithDefault("app.config", "/etc/app/config");
```

## Memory Functions

### freeMemory() -> Long
Returns the amount of free memory in the Java Virtual Machine (in bytes).

**Example:**
```cln
import std.sys.freeMemory;

var int free = freeMemory();
var int freeMB = free / 1024 / 1024;  // Convert to MB
```

### totalMemory() -> Long
Returns the total amount of memory currently available to the Java Virtual Machine (in bytes).

**Example:**
```cln
import std.sys.totalMemory;

var int total = totalMemory();
```

### maxMemory() -> Long
Returns the maximum amount of memory that the Java Virtual Machine will attempt to use (in bytes).

**Example:**
```cln
import std.sys.maxMemory;

var int max = maxMemory();
```

### gc()
Runs the garbage collector. Calling this method suggests that the JVM should expend effort toward recycling unused objects.

**Note:** This is only a suggestion to the JVM. The garbage collector may or may not run immediately.

**Example:**
```cln
import std.sys.gc;

gc();  // Request garbage collection
```

## Complete Example

See [demo_sys.cln](../examples/demo_sys.cln) for a comprehensive demonstration of all sys library functions.

## Timing Example

See [demo_sys_timing.cln](../examples/demo_sys_timing.cln) for an example of using `currentTimeMillis()` to measure execution time.

## Exit Example

See [demo_sys_exit.cln](../examples/demo_sys_exit.cln) for an example of using the `exit()` function.
