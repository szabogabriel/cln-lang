# Execution Context System

The execution context system is the core runtime infrastructure of the CLN language interpreter. It manages program execution state, variable storage, function calls, and scope resolution.

## Overview

The execution context provides a multi-layered architecture for managing runtime state:

- **ExecutionContext**: Top-level execution coordinator
- **GlobalContext**: Program-wide state (functions, types, global variables)
- **CallFrame**: Individual function call stack frames
- **LocalContext**: Function-local variable storage with scoping

Together, these components provide efficient variable storage, function invocation, and scope management with minimal boxing overhead.

## Architecture

### 1. ExecutionContext

The `ExecutionContext` class is the primary interface for program execution. It maintains the global context and manages the call stack for function invocations.

**Key Responsibilities:**
- Maintains a single `GlobalContext` shared across the entire program
- Manages a call stack (`Deque<CallFrame>`) for function invocations
- Tracks import declarations
- Provides methods for pushing/popping call frames
- Handles return value management

**Structure:**
```java
public class ExecutionContext {
    private final GlobalContext globalContext;
    private final Deque<CallFrame> callStack;
    private final List<ImportDeclImpl> imports;
}
```

**Key Methods:**
- `getGlobalContext()`: Access program-wide state
- `getLocalContext()`: Access current function's local variables
- `getCurrentFrame()`: Get the current call frame
- `pushCallFrame(String functionName)`: Enter a new function
- `popCallFrame()`: Exit current function and return values
- `setReturnValues(List<Object> values)`: Set function return values
- `hasReturned()`: Check if current function has returned

### 2. GlobalContext

The `GlobalContext` stores all program-wide state that persists throughout execution.

**Storage:**
- **Struct Types**: `Map<String, StructDefinition>`
- **Union Types**: `Map<String, UnionDefinition>`
- **Functions**: `Map<String, FunctionDeclImpl>`
- **Global Variables**: `Map<String, GlobalVariable>`
- **Package Name**: Current package identifier

**Key Methods:**
- `registerStructType(name, definition)`: Register a new struct type
- `registerUnionType(name, definition)`: Register a new union type
- `registerFunction(name, function)`: Register a function
- `setGlobalVariable(name, variable)`: Register a global variable
- `getFunction(name)`: Retrieve a function by name
- `getStructType(name)`: Retrieve a struct definition
- `getUnionType(name)`: Retrieve a union definition

**Note:** Global context does not support function overloading. Attempting to register a duplicate name throws `OverloadingNotSupportedException`.

### 3. CallFrame

A `CallFrame` represents a single function invocation on the call stack. Each frame maintains its own isolated local context.

**Components:**
- **Function Name**: Identifier for debugging and stack traces
- **Local Context**: Storage for function-local variables
- **Arguments**: Map of function parameters with metadata
- **Return Values**: List of values returned by the function

**Key Features:**
- Arguments can be immutable (constants) or mutable (variables)
- Return values support multiple return semantics
- Each frame has an isolated `LocalContext`
- Function calls do NOT inherit the caller's local variables (no implicit closure)

**Key Methods:**
- `setArgument(name, value, isMutable)`: Set a function parameter
- `getLocalContext()`: Access function-local variables
- `setReturnValues(values)`: Mark function return
- `hasReturned()`: Check if function has returned
- `getReturnValueObjects()`: Extract return values

### 4. LocalContext

The `LocalContext` is the most performance-critical component. It stores function-local variables using primitive arrays for zero-boxing storage.

**Storage Strategy:**

Variables are stored in separate arrays by type:
- **long[]**: Integer values (zero boxing!)
- **boolean[]**: Boolean values (zero boxing!)
- **BigDecimal[]**: Decimal values
- **String[]**: String values
- **Object[]**: Other object types

Each type has parallel arrays:
- `typeValues[]`: Actual values
- `typeMutable[]`: Mutability flags
- `typeNames[]`: Variable names (for backward compatibility)
- `typeCount`: Number of variables of that type

**Index-Based Access:**

The primary access method is index-based, not name-based. During compilation, the `CompilerVisitor` assigns each variable a type-specific index. At runtime, variables are accessed directly by index, avoiding name lookups and hash map overhead.

```java
// Fast: zero-boxing access
long value = localContext.getLongByIndex(3);
localContext.setLongByIndex(3, value + 1, true);

// Slow: legacy name-based access (for backward compatibility)
Object value = localContext.getValue("myVar");
```

**Performance Features:**
- **Zero Boxing for Primitives**: `int` and `bool` types avoid boxing
- **Direct Array Access**: Variables accessed by index, not name
- **Dynamic Capacity**: Arrays grow automatically (doubling strategy)
- **Parent Chain**: Supports nested scopes via parent reference

**Scoping:**

Local contexts support nested scopes through a parent chain:
```java
LocalContext parent = new LocalContext();
LocalContext child = new LocalContext(parent);
```

When a variable is not found in the current context, the lookup traverses the parent chain.

**Key Methods:**

**Index-Based (Preferred):**
- `getLongByIndex(index)`: Get integer value by index
- `setLongByIndex(index, value, mutable)`: Set integer value
- `updateLongByIndex(index, value)`: Update existing integer
- Similar methods for `bool`, `decimal`, `string`, `object`

**Name-Based (Legacy):**
- `getValue(name)`: Get value by name (slower)
- `setVariable(name, value)`: Set mutable variable
- `setConstant(name, value)`: Set immutable variable
- `updateVariable(name, value)`: Update existing variable
- `hasValue(name)`: Check if variable exists
- `isMutable(name)`: Check if variable is mutable

## Execution Flow

### Program Initialization

1. **Create ExecutionContext**: `ExecutionContext context = new ExecutionContext();`
2. **Populate Global Context**: Register functions, types, and global variables
3. **Resolve Imports**: Use `Linker` to resolve cross-module dependencies
4. **Push Global Frame**: A `<global>` frame is automatically pushed at initialization

### Function Call

1. **Push Call Frame**: `context.pushCallFrame("myFunction")`
2. **Set Arguments**: `frame.setArgument("param1", value, false)`
3. **Execute Function Body**: Statements execute with local context
4. **Set Return Values**: `context.setReturnValues(Arrays.asList(result))`
5. **Pop Call Frame**: `List<Object> returns = context.popCallFrame()`

### Variable Access

**Compilation Phase:**
```java
// CompilerVisitor assigns indices during compilation
VariableScope scope = new VariableScope();
scope.registerVariable("counter", "int");  // Assigns index 0 in long array
```

**Runtime Phase:**
```java
// CompiledAction uses index-based access
LocalContext ctx = context.getLocalContext();
ctx.setLongByIndex(0, 10L, true);  // Set counter = 10
long value = ctx.getLongByIndex(0);  // Read counter (zero boxing!)
```

### Scope Resolution

Variables are resolved in the following order:
1. **Local Context**: Check current function's local variables
2. **Parent Context**: Check parent scope (if exists)
3. **Global Context**: Check global variables
4. **Imports**: Check imported modules

## Performance Considerations

### Zero-Boxing Optimization

The `LocalContext` uses primitive arrays (`long[]`, `boolean[]`) instead of object wrappers (`Long`, `Boolean`). This eliminates boxing overhead for primitive types:

```java
// Traditional approach: boxing overhead
Map<String, Object> vars = new HashMap<>();
vars.put("i", (Long) 42L);  // Boxing
Long value = (Long) vars.get("i");  // Unboxing

// CLN approach: zero boxing
long[] longValues = new long[10];
longValues[0] = 42L;  // Direct primitive access
long value = longValues[0];  // No boxing/unboxing
```

### Index-Based Access

Variable access by index avoids:
- Hash map lookups
- String comparisons
- Object allocation for keys

**Benchmark Impact:**

In loops with millions of iterations, index-based access provides significant speedup:
```cln
// This loop benefits from zero-boxing and index access
var i: int = 0;
while (i < 10000000) {
    i++;  // Direct primitive increment, no boxing
}
```

### Call Stack Overhead

Each function call creates a new `CallFrame` and `LocalContext`. For deeply recursive functions, this can create memory pressure. The system uses efficient data structures:
- `ArrayDeque` for call stack (fast push/pop)
- Primitive arrays for local variables (compact memory)

### Parent Chain Traversal

Variable lookups traverse the parent chain when not found locally. Deep nesting increases lookup cost. Best practices:
- Keep scope nesting shallow
- Use index-based access when possible
- Cache frequently accessed variables

## Best Practices

### For Compiler/Interpreter Developers

1. **Prefer Index-Based Access**: Always use `*ByIndex()` methods in compiled code
2. **Assign Indices at Compile Time**: Use `CompilerVisitor.VariableScope` to track indices
3. **Minimize Parent Chain Depth**: Avoid deeply nested scopes
4. **Batch Variable Operations**: Group variable accesses to improve cache locality

### For Language Users

1. **Declare Variables Locally**: Avoid excessive global variables
2. **Use Appropriate Types**: Choose `int` over `dec` when possible for better performance
3. **Avoid Deep Recursion**: Consider iterative solutions for performance-critical code
4. **Mark Constants as const**: Helps the runtime optimize storage

## Integration with Compilation Pipeline

### 1. Lexing & Parsing (ANTLR4)
Source code → Parse Tree

### 2. AST Construction (ClnASTBuilder)
Parse Tree → Abstract Syntax Tree

### 3. Compilation (CompilerVisitor)
AST → Compiled Representations
- **VariableScope** assigns type-specific indices to variables
- Generates `CompiledAction` objects with index-based accesses

### 4. Linking (Linker)
- Resolves imports across modules
- Populates `ExecutionContext` with functions and types
- Connects dependencies in `GlobalContext`

### 5. Execution (FunctionInvoker)
- Uses `ExecutionContext` to manage execution
- Pushes/pops `CallFrame` objects
- Accesses variables via `LocalContext` by index

## Example: Variable Lifecycle

**Source Code:**
```cln
func calculate(x: int) -> int {
    const multiplier: int = 2;
    var result: int = x * multiplier;
    result++;
    return result;
}
```

**Compilation:**
```java
// CompilerVisitor phase
VariableScope scope = new VariableScope();
scope.registerVariable("x", "int");         // index 0
scope.registerVariable("multiplier", "int"); // index 1
scope.registerVariable("result", "int");     // index 2
```

**Execution:**
```java
// Runtime phase
context.pushCallFrame("calculate");
LocalContext local = context.getLocalContext();

// Set argument x = 5
local.setLongByIndex(0, 5L, false);

// const multiplier = 2
local.setLongByIndex(1, 2L, false);

// var result = x * multiplier
long x = local.getLongByIndex(0);
long mult = local.getLongByIndex(1);
local.setLongByIndex(2, x * mult, true);

// result++
long result = local.getLongByIndex(2);
local.updateLongByIndex(2, result + 1);

// return result
List<Object> returns = Arrays.asList(local.getLongByIndex(2));
context.setReturnValues(returns);
context.popCallFrame();
```

## Related Documentation

- [Performance Guide](../PERFORMANCE.md): Detailed performance analysis
- [Runtime Configuration](../changelog/RUNTIME_CONFIGURATION.md): System configuration
- [Startup Modes](../changelog/STARTUP_MODES_GUIDE.md): Execution modes and context lifecycle
- [Visitor Pattern](../changelog/VISITOR_PATTERN.md): AST traversal and compilation

## Code References

- `org.clnlang.runtime.context.ExecutionContext`: Main execution coordinator
- `org.clnlang.runtime.context.GlobalContext`: Global program state
- `org.clnlang.runtime.context.LocalContext`: Local variable storage
- `org.clnlang.runtime.context.CallFrame`: Function call frames
- `org.clnlang.ast.visitor.CompilerVisitor`: Compilation and index assignment
- `org.clnlang.runtime.FunctionInvoker`: Function execution
