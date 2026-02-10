# Performance Analysis

This document outlines identified performance bottlenecks in the CLN language runtime and compiler.

## Overview

The primary performance issues stem from the generic, dynamically-typed nature of the current runtime implementation. While this design is flexible and correct, it introduces significant overhead in critical hot paths, particularly in tight loops with primitive operations.

## Critical Performance Issues

### 1. Boxing/Unboxing Overhead ⚠️ **MOST CRITICAL**

**Location**: Throughout entire runtime (BinaryExprImpl, IdentifierExprImpl, LocalContext, GlobalContext)

**Impact**: Every primitive operation incurs boxing/unboxing costs

**Example**: Simple `i++` in a loop:
```
1. Read `i` from Map<String, Object> → unbox Long
2. Increment → primitive operation  
3. Box back to Long → store in Map<String, Object>
```

**Cost**: 3+ boxing operations per simple arithmetic operation, resulting in millions of allocations in tight loops.

**Root Cause**: All values stored as `Object` in context maps:
- `Map<String, Object> variables` in LocalContext
- `Map<String, Object> constants` in LocalContext
- `GlobalVariable` stores `Object value`

**Code References**:
- `core/src/main/java/org/clnlang/runtime/context/LocalContext.java`
- `core/src/main/java/org/clnlang/runtime/context/GlobalContext.java`
- `core/src/main/java/org/clnlang/runtime/values/GlobalVariable.java`

### 2. Repeated instanceof Type Checks ⚠️ **HIGH**

**Location**: `BinaryExprImpl.evaluate()` - lines 36-218

**Impact**: Every binary operation performs redundant type checking on both operands

**Example**: For addition operator:
```java
if (leftVal instanceof Long && rightVal instanceof Long) { ... }
if (leftVal instanceof BigDecimal && rightVal instanceof BigDecimal) { ... }
if (leftVal instanceof BigDecimal && rightVal instanceof Long) { ... }
if (leftVal instanceof Long && rightVal instanceof BigDecimal) { ... }
if (leftVal instanceof String || rightVal instanceof String) { ... }
```

**Cost**: 2-8 instanceof checks per arithmetic operation. No type information is cached or available at compile time.

**Code References**:
- `core/src/main/java/org/clnlang/compile/expression/BinaryExprImpl.java`

### 3. Multiple HashMap Lookups per Variable Access ⚠️ **HIGH**

**Location**: `LocalContext.getValue()`, `IdentifierExprImpl.evaluate()`

**Impact**: Multiple hash map operations for every variable access:

**LocalContext Lookup Chain**:
1. `variables.containsKey(name)`
2. If not found: `constants.containsKey(name)`
3. If not found: parent context traversal (recursive lookup)

**IdentifierExprImpl Lookup Chain**:
1. `context.getLocalContext().hasValue(name)` → traverses local chain
2. `context.getLocalContext().getValue(name)` → traverses local chain again
3. If not found: `context.getGlobalContext().hasGlobalVariable(name)`
4. If not found: `context.getGlobalContext().getGlobalValue(name)`
5. If not found: `context.getGlobalContext().getFunction(name)`

**Cost**: 2-6 hash map operations per variable read. Variable updates require similar traversal.

**Code References**:
- `core/src/main/java/org/clnlang/runtime/context/LocalContext.java` (lines 47-60)
- `core/src/main/java/org/clnlang/compile/expression/IdentifierExprImpl.java` (lines 21-42)

### 4. No Type Specialization ⚠️ **HIGH**

**Location**: All expression evaluation uses `Object evaluate(ExecutionContext context)`

**Impact**: 
- All expressions return Object, requiring type checks and casts
- No fast paths for primitive operations
- Virtual method calls cannot be inlined
- Loop conditions re-box Boolean on every iteration

**Example**: `while (i < 10000000) { i++; }`
- Condition evaluates to Object, casts to Boolean, unboxes
- Increment treats `i` as Object throughout
- Both operations repeated 10 million times

**Suggested Solution**: Add typed evaluation methods:
```java
long longValue(ExecutionContext context);
boolean boolValue(ExecutionContext context);
BigDecimal decimalValue(ExecutionContext context);
```

**Code References**:
- `core/src/main/java/org/clnlang/compile/CompiledExpr.java`
- All expression implementations

### 5. Redundant Parent Chain Traversal 🔸 **MEDIUM-HIGH**

**Location**: `LocalContext` methods

**Impact**: Multiple traversals of the same parent chain

**Example**: `updateVariable(name, value)` traversal:
1. Check if `variables.containsKey(name)` in current scope
2. Check if `constants.containsKey(name)` in current scope
3. If neither: recurse to parent
4. Parent repeats steps 1-3

Additionally, `hasValue()` followed by `getValue()` traverses the chain twice.

**Cost**: O(n) operations repeated multiple times, where n = scope depth

**Code References**:
- `core/src/main/java/org/clnlang/runtime/context/LocalContext.java` (lines 72-87, 108-123)

### 6. Object Allocations in Hot Paths 🔸 **MEDIUM**

**Location**: Function calls, mixed-type arithmetic, string operations

**Impact**: Excessive object allocation in frequently executed code

**Examples**:
- `new ArrayList<>()` created for every function call's arguments (CallExprImpl:47)
- `BigDecimal.valueOf(Long)` creates objects for mixed arithmetic operations
- `String.valueOf()` creates strings for concatenation operations

**Cost**: GC pressure, especially in loops with function calls or string operations

**Code References**:
- `core/src/main/java/org/clnlang/compile/expression/CallExprImpl.java` (line 47)
- `core/src/main/java/org/clnlang/compile/expression/BinaryExprImpl.java` (mixed arithmetic operations)

### 7. GlobalVariable Wrapper Indirection 🔸 **MEDIUM**

**Location**: `GlobalContext.getGlobalValue()`

**Impact**: Extra indirection for every global variable access

**Access Chain**:
1. `Map.get(name)` → GlobalVariable wrapper
2. `wrapper.getValue()` → Object value
3. Unboxing (if primitive)

**Cost**: 2x overhead compared to direct storage, plus additional mutability checks on updates

**Code References**:
- `core/src/main/java/org/clnlang/runtime/context/GlobalContext.java` (lines 114-117)
- `core/src/main/java/org/clnlang/runtime/values/GlobalVariable.java`

### 8. WhileStmt Condition Evaluation Overhead 🔸 **MEDIUM**

**Location**: `WhileStmtImpl.execute()` - line 28

**Impact**: Loop condition evaluated generically on every iteration

**Example**:
```java
while ((Boolean) condition.evaluate(context)) { ... }
```

For a loop that executes 10 million times:
- 10M `evaluate()` calls returning Object
- 10M casts to Boolean
- 10M unboxing operations

**Code References**:
- `core/src/main/java/org/clnlang/compile/statement/WhileStmtImpl.java` (line 28)

### 9. Increment/Decrement Operation Overhead 🔸 **MEDIUM**

**Location**: `IncrementExprImpl.evaluate()`

**Impact**: Simple `i++` operation requires excessive work

**Operation Breakdown**:
1. Check if operand is IdentifierExprImpl (instanceof check - could be compile-time)
2. Evaluate operand as Object
3. Check if value is Integer or Long (instanceof checks)
4. Unbox value
5. Perform increment
6. Box result
7. Update variable through generic updateVariable path (additional map lookups)
8. Final type check for mutability

**Cost**: ~8-10 operations for a single increment, most could be eliminated

**Code References**:
- `core/src/main/java/org/clnlang/compile/expression/IncrementExprImpl.java`

### 10. Struct/Array Access via Generic Interfaces 🔹 **LOW-MEDIUM**

**Location**: `MemberAccessExprImpl`, `IndexAccessExprImpl`

**Impact**: Struct and array access uses generic Map/List interfaces

**Struct Access**:
- `Map.containsKey(member)` + `Map.get(member)` = 2 hash operations
- Runtime type checking with string-based type names
- No specialization for known struct types at compile time

**Array Access**:
- Generic List interface with bounds checking
- Exception handling overhead for out-of-bounds
- No specialization for primitive arrays

**Code References**:
- `core/src/main/java/org/clnlang/compile/expression/MemberAccessExprImpl.java`
- `core/src/main/java/org/clnlang/compile/expression/IndexAccessExprImpl.java`

## Performance Impact Summary

### Top 3 Bottlenecks (in order of impact):

1. **Boxing/unboxing** - Affects EVERY primitive operation throughout the runtime
2. **instanceof type checks** - Repeated for every arithmetic/comparison operation  
3. **HashMap lookups** - Multiple lookups per variable access, compounded by scope traversal

### Compounding Effect

These issues compound multiplicatively in tight loops. A simple loop like:

```cln
var int i = 0;
while (i < 10000000) {
    i++;
}
```

Requires per iteration:
- 2 variable lookups (condition + increment) × hash map operations
- 2-4 unboxing operations
- 2-4 boxing operations
- 4-8 instanceof checks
- 2 Object allocations

**Result**: ~20-30 overhead operations per simple loop iteration

## Benchmark Results

From `examples/benchmark_loop.cln` (10 million iterations):
- Current performance: ~800-850ms
- Estimated overhead: ~70-80% from boxing/lookups/type checks
- Theoretical best case with optimizations: ~150-250ms

## Recommended Optimization Priorities

### Phase 1: Type-Specialized Storage (Highest Impact)
- Implement typed storage maps in LocalContext/GlobalContext
- Separate `Map<String, Long>`, `Map<String, Boolean>`, etc.
- Eliminates boxing for primitive operations

### Phase 2: Type-Specialized Evaluation (High Impact)
- Add typed evaluation methods to CompiledExpr interface
- Implement fast paths in expressions
- Reduces type checking and allows inlining

### Phase 3: Lookup Optimization (Medium Impact)
- Cache lookup results in hot paths
- Combine existence checks with retrieval
- Optimize parent chain traversal

### Phase 4: Specialized Operations (Medium Impact)
- Specialize increment/decrement for known types
- Optimize loop condition evaluation
- Pre-allocate common objects

## Notes

- These optimizations would maintain semantic correctness
- Backward compatibility can be preserved with fallback paths
- JIT compiler can optimize better with typed operations
- Performance gains would be most visible in compute-intensive code
