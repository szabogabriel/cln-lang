# std.sys - System Functions Library

The `std.sys` package provides access to system-level functionality including time functions, environment access, process control, and memory management.

## Package Name
`std.sys`

## Functions

### Time Functions

#### currentTimeMillis
Returns the current time in milliseconds since the Unix epoch (January 1, 1970 UTC).

**Signature:**
```cln
currentTimeMillis() -> int
```

**Returns:**
- int: Current time in milliseconds

**Example:**
```cln
import std.sys.currentTimeMillis;

int startTime = currentTimeMillis();
// ... some operations ...
int endTime = currentTimeMillis();
int elapsed = endTime - startTime;
```

---

#### nanoTime
Returns the current value of the running JVM's high-resolution time source, in nanoseconds. Useful for measuring elapsed time with high precision.

**Signature:**
```cln
nanoTime() -> int
```

**Returns:**
- int: High-resolution time value in nanoseconds

**Note:** This value is only meaningful when comparing two readings to measure elapsed time.

---

### Process Control Functions

#### exit
Terminates the currently running program with the specified exit code.

**Signature:**
```cln
exit(code: int) -> void
```

**Parameters:**
- `code` (int): Exit code (0 for normal termination, non-zero for error)

**Example:**
```cln
import std.sys.exit;

if (errorCondition) {
    exit(1);  // Exit with error code
}
```

---

### Environment and System Properties Functions

#### getenv
Gets the value of the specified environment variable.

**Signature:**
```cln
getenv(name: String) -> String
```

**Parameters:**
- `name` (String): Name of the environment variable

**Returns:**
- String: Value of the environment variable, or null if not defined

**Example:**
```cln
import std.sys.getenv;

String home = getenv("HOME");
String path = getenv("PATH");
```

---

#### getProperty
Gets the system property indicated by the specified key.

**Signature:**
```cln
getProperty(key: String) -> String
```

**Parameters:**
- `key` (String): The property key

**Returns:**
- String: The system property value, or null if not found

**Common System Properties:**
- `"user.name"` - User's account name
- `"user.home"` - User's home directory
- `"user.dir"` - Current working directory
- `"os.name"` - Operating system name
- `"os.version"` - Operating system version
- `"java.version"` - Java Runtime Environment version

**Example:**
```cln
import std.sys.getProperty;

String userName = getProperty("user.name");
String osName = getProperty("os.name");
```

---

#### getPropertyWithDefault
Gets the system property indicated by the specified key, or returns the default value if the property is not found.

**Signature:**
```cln
getPropertyWithDefault(key: String, defaultValue: String) -> String
```

**Parameters:**
- `key` (String): The property key
- `defaultValue` (String): Default value to return if property not found

**Returns:**
- String: The system property value, or defaultValue if not found

**Example:**
```cln
import std.sys.getPropertyWithDefault;

String javaVersion = getPropertyWithDefault("java.version", "unknown");
```

---

### Memory Functions

#### freeMemory
Returns the amount of free memory in the Java Virtual Machine (in bytes).

**Signature:**
```cln
freeMemory() -> int
```

**Returns:**
- int: Free memory in bytes

---

#### totalMemory
Returns the total amount of memory in the Java Virtual Machine (in bytes).

**Signature:**
```cln
totalMemory() -> int
```

**Returns:**
- int: Total memory in bytes

---

#### maxMemory
Returns the maximum amount of memory that the Java Virtual Machine will attempt to use (in bytes).

**Signature:**
```cln
maxMemory() -> int
```

**Returns:**
- int: Maximum memory in bytes

---

#### gc
Runs the garbage collector. Calling this method suggests that the JVM expend effort toward recycling unused objects.

**Signature:**
```cln
gc() -> void
```

**Note:** This is a suggestion to the JVM; it may not run immediately.

---

## Usage Example

```cln
import std.sys.*;
import std.console.writeLine;
import std.str.intToStr;

// Timing example
int start = nanoTime();
// ... perform some operations ...
int end = nanoTime();
int elapsedNanos = end - start;

// System information
String osName = getProperty("os.name");
String userName = getProperty("user.name");
writeLine("OS: " + osName);
writeLine("User: " + userName);

// Memory information
int totalMem = totalMemory();
int freeMem = freeMemory();
int maxMem = maxMemory();

// Environment variable
String homePath = getenv("HOME");

// Exit with success code
exit(0);
```
