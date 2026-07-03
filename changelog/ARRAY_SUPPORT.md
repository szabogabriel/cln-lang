# Array Support in CLN

## Overview

CLN now supports arrays of primitive types (int, dec, boolean, and string). Arrays are dynamic, mutable collections that use zero-based indexing.

## Syntax

### Array Declaration and Initialization

```cln
// Declare and initialize an array
var int[] numbers = [1, 2, 3, 4, 5];
var string[] names = ["Alice", "Bob", "Charlie"];
var bool[] flags = [true, false, true];
var dec[] prices = [19.99, 25.50, 12.75];

// Empty array
var int[] empty = [];
```

### Array Access

```cln
// Read element at index
var int first = numbers[0];
var int last = numbers[4];

// Index must be an integer expression
var int i = 2;
var int elem = numbers[i];
var int middle = numbers[numbers.length / 2];
```

### Array Assignment

```cln
// Modify element at index
numbers[0] = 100;
numbers[i] = 200;

// Works with expressions
numbers[i + 1] = numbers[i] * 2;
```

### Array Length

```cln
// Access array length using .length property
var int size = numbers.length;

// Use in loops
var int i = 0;
while (i < numbers.length) {
    writeLine(intToStr(numbers[i]));
    i++;
}
```

## Type System

### Array Types

Arrays are declared with the element type followed by `[]`. Multiple `[]` suffixes create multi-dimensional arrays:
- `int[]` - array of integers
- `string[]` - array of strings
- `bool[]` - array of booleans
- `dec[]` - array of decimal numbers
- `int[][]` - 2D array of integers (array of int arrays)
- `int[][][]` - 3D array of integers
- `MyStruct[][]` - 2D array of struct values

### Runtime Representation

Arrays are implemented as Java `List<Object>`, which provides:
- Dynamic sizing
- Efficient element access
- Automatic bounds checking

### Type Safety

- Array element type is not enforced at runtime (dynamic typing)
- All primitive types use their Java representations:
  - `int` → `Long`
  - `dec` → `BigDecimal`
  - `bool` → `Boolean`
  - `string` → `String`

## Features

### Multi-dimensional Arrays

CLN supports multi-dimensional arrays natively. An `int[][]` is simply an array whose elements are themselves `int[]` arrays.

#### Literal syntax

```cln
var int[][] matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]];
var int[][][] cube = [[[1, 2], [3, 4]], [[5, 6], [7, 8]]];
```

#### Access and assignment

```cln
var int v = matrix[1][2];   // read element at row 1, col 2
matrix[0][0] = 99;          // write
cube[1][0][1] = 42;         // 3D write
```

#### Dynamic allocation

Use `newArray2D` and `newArray3D` from `std.array`:

```cln
import std.array.*;

var int[][] grid = newArray2D(rows, cols);
var int[][][] vol = newArray3D(depth, rows, cols);
```

#### `.length` at every dimension

```cln
writeLine(intToStr(matrix.length));       // number of rows
writeLine(intToStr(matrix[0].length));    // number of cols in row 0
```

#### Passing rows to 1D functions

A row of a 2D array is a plain `int[]` and can be passed anywhere a 1D array is accepted:

```cln
reverse(matrix[0]);   // reverses the first row in place
```

#### deepCopy vs copy

`copy` from `std.array` is a **shallow** copy. For multi-dimensional arrays use `deepCopy` to get an independent clone:

```cln
var int[][] a = [[1, 2], [3, 4]];
var int[][] b = deepCopy(a);   // completely independent
b[0][0] = 99;
// a[0][0] is still 1
```

---

### Bounds Checking

Array access is automatically bounds-checked:

```cln
var int[] arr = [1, 2, 3];
var int x = arr[10];  // Runtime error: Array index out of bounds
```

### String Indexing

Strings can be indexed like arrays, returning single-character strings:

```cln
var string text = "hello";
var string ch = text[0];  // Returns "h"
var int len = text.length;  // Returns 5
```

### Arrays as Function Parameters

Arrays can be passed to and returned from functions:

```cln
(var int sum = 0) sumArray(int[] arr) {
    var int i = 0;
    while (i < arr.length) {
        sum = sum + arr[i];
        i++;
    }
    return;
}

int main() {
    var int[] numbers = [1, 2, 3, 4, 5];
    int total = sumArray(numbers);
    writeLine("Total: " + intToStr(total));
    return 0;
}
```

## Examples

### Example 1: Array Iteration

```cln
var int[] numbers = [10, 20, 30, 40, 50];

// Print all elements
var int i = 0;
while (i < numbers.length) {
    writeLine("numbers[" + intToStr(i) + "] = " + intToStr(numbers[i]));
    i++;
}
```

### Example 2: Array Sum

```cln
var int[] values = [5, 10, 15, 20];
var int sum = 0;
var int i = 0;
while (i < values.length) {
    sum = sum + values[i];
    i++;
}
writeLine("Sum: " + intToStr(sum));  // Output: Sum: 50
```

### Example 3: Finding Maximum

```cln
var int[] data = [15, 8, 23, 4, 42, 16];
var int max = data[0];
var int i = 1;
while (i < data.length) {
    if (data[i] > max) {
        max = data[i];
    }
    i++;
}
writeLine("Maximum: " + intToStr(max));  // Output: Maximum: 42
```

### Example 4: Array Reversal

```cln
var int[] original = [1, 2, 3, 4, 5];
var int[] reversed = [0, 0, 0, 0, 0];

var int i = 0;
while (i < original.length) {
    reversed[i] = original[original.length - 1 - i];
    i++;
}
```

### Example 5: String Array Concatenation

```cln
var string[] words = ["hello", "world", "from", "CLN"];
var string result = "";
var int i = 0;
while (i < words.length) {
    result = result + words[i];
    if (i < words.length - 1) {
        result = result + " ";
    }
    i++;
}
writeLine(result);  // Output: hello world from CLN
```

### Example 6: Decimal Array Operations

```cln
var dec[] prices = [19.99, 25.50, 12.75];
var dec total = 0.0;
var int i = 0;
while (i < prices.length) {
    total = total + prices[i];
    i++;
}
var dec average = total / prices.length;
writeLine("Average price: " + average);
```

## Current Limitations

1. **No Multi-dimensional Arrays**: Only 1D arrays are supported
   - `int[][]` is not supported
   - Use arrays of custom types for more complex structures

2. **No Array Resizing**: Arrays are fixed-size after creation
   - Cannot append or remove elements
   - Must create new array and copy elements

3. **No Array Literals in Expressions**: Array literals can only be used in variable initialization
   - Valid: `var int[] arr = [1, 2, 3];`
   - Not valid: `functionCall([1, 2, 3])`

4. **No Struct/Union Arrays**: Arrays of user-defined types not yet supported
   - `Point[]` is not supported (planned for future release)
   - Only primitive types supported: `int[]`, `dec[]`, `bool[]`, `string[]`

5. **No Array Copying Built-in**: Must manually copy arrays
   - No `clone()` or copy methods
   - Manual element-by-element copying required

## Implementation Details

### Grammar Changes

Added array literal syntax to the grammar:

```antlr
primaryExpr
  : INT_LIT
  | DEC_LIT
  | BOOL_LIT
  | STRING_LIT
  | arrayLiteral    // NEW
  | structLiteral
  | ID
  | LPAREN expr RPAREN
  ;

arrayLiteral
  : LBRACK exprList? RBRACK
  ;
```

The grammar already supported array types and index access:
```antlr
type
  : baseType (LBRACK RBRACK)*   // e.g., int[], string[]
  ;

postfixOp
  : LPAREN argList? RPAREN
  | DOT ID
  | LBRACK expr RBRACK           // Index access
  | INC
  | DEC
  ;
```

### Core Components

1. **ArrayLiteralExprImpl**: Compiles array literal expressions into Java Lists
2. **IndexAccessExprImpl**: Handles array element access with bounds checking
3. **MemberAccessExprImpl**: Extended to support `.length` property on arrays
4. **AssignStmtImpl**: Extended to support array element assignment

### Runtime Behavior

- Arrays are stored as `List<Object>` in Java
- Array elements are boxed Java objects (Long, String, Boolean, BigDecimal)
- Index bounds are checked at runtime
- Type compatibility is checked during operations

## Test Coverage

Comprehensive test suite includes:
- `test_array_basic.cln`: Basic array operations
- `test_array_loops.cln`: Iteration and algorithms
- `test_array_functions.cln`: Arrays as function parameters
- `test_array_strings.cln`: String array operations
- `test_array_decimals.cln`: Decimal arithmetic with arrays

All tests pass successfully!

## Future Enhancements

### Planned for Next Release
- Arrays of structs and unions
- Multi-dimensional arrays
- Array slicing operations
- Array copying and cloning utilities
- Built-in array manipulation functions (sort, filter, map)

### Under Consideration
- Array literals as function arguments
- Dynamic array resizing
- Array comprehensions
- Typed arrays for better type safety
