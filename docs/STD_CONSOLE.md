# std.console - Console I/O Library

The `std.console` package provides functions for console input and output operations.

## Package Name
`std.console`

## Functions

### write
Writes a message to the console without a newline.

**Signature:**
```cln
write(message: String) -> void
```

**Parameters:**
- `message` (String): The text to write to the console

**Example:**
```cln
import std.console.write;
write("Hello");
write(" World");  // Output: Hello World
```

---

### writeLine
Writes a message to the console followed by a newline character.

**Signature:**
```cln
writeLine(message: String) -> void
```

**Parameters:**
- `message` (String): The text to write to the console

**Example:**
```cln
import std.console.writeLine;
writeLine("Hello World");  // Output: Hello World\n
```

---

### readLine
Reads a line of text from the console input.

**Signature:**
```cln
readLine() -> String
```

**Returns:**
- String: The line of text read from console input

**Example:**
```cln
import std.console.*;

writeLine("Enter your name:");
String name = readLine();
writeLine("Hello, " + name);
```

---

## Usage Example

```cln
import std.console.*;

writeLine("Welcome to the program!");
write("Enter your age: ");
String ageStr = readLine();
writeLine("You entered: " + ageStr);
```
