# std.array - Array Utility Library

The `std.array` package provides array operations including creation, copying, searching, modification, and comparison functions.

## Package Name
`std.array`

## Functions

### Array Creation and Copying

#### newArray
Creates a new array with the specified size, initialized with null values.

**Signature:**
```cln
newArray(size: int) -> Array
```

**Parameters:**
- `size` (int): Size of the array (must be non-negative)

**Returns:**
- Array: New array of specified size

**Throws:**
- IllegalArgumentException if size is negative

---

#### newArray2D
Creates a new 2D array (array of arrays) with the specified dimensions. Each row is an independent array of `cols` null-initialized elements.

**Signature:**
```cln
newArray2D(rows: int, cols: int) -> Array
```

**Parameters:**
- `rows` (int): Number of rows (must be non-negative)
- `cols` (int): Number of columns per row (must be non-negative)

**Returns:**
- Array: New 2D array of shape `rows × cols`

**Example:**
```cln
var int[][] grid = newArray2D(3, 4);
grid[2][3] = 42;
writeLine(intToStr(grid.length));       // 3
writeLine(intToStr(grid[0].length));    // 4
```

---

#### newArray3D
Creates a new 3D array (array of arrays of arrays) with the specified dimensions.

**Signature:**
```cln
newArray3D(depth: int, rows: int, cols: int) -> Array
```

**Parameters:**
- `depth` (int): Number of planes (must be non-negative)
- `rows` (int): Number of rows per plane (must be non-negative)
- `cols` (int): Number of columns per row (must be non-negative)

**Returns:**
- Array: New 3D array of shape `depth × rows × cols`

**Example:**
```cln
var int[][][] cube = newArray3D(2, 3, 4);
cube[1][2][3] = 99;
writeLine(intToStr(cube.length));           // 2
writeLine(intToStr(cube[0].length));        // 3
writeLine(intToStr(cube[0][0].length));     // 4
```

---

#### copy
Creates a **shallow** copy of an array. For 1D arrays this is equivalent to a full copy. For multi-dimensional arrays the inner arrays are shared, so mutating an inner element via the copy also changes the original.

**Signature:**
```cln
copy(arr: Array) -> Array
```

**Parameters:**
- `arr` (Array): Array to copy

**Returns:**
- Array: Shallow copy of the array

**Note:** Use `deepCopy` when you need an independent copy of a multi-dimensional array.

---

#### deepCopy
Creates a **deep** copy of an array, recursively copying all nested arrays. Mutating any element of the result (including inner arrays at any depth) does not affect the original.

**Signature:**
```cln
deepCopy(arr: Array) -> Array
```

**Parameters:**
- `arr` (Array): Array to deep-copy (may be multi-dimensional)

**Returns:**
- Array: Independent deep copy of the array

**Example:**
```cln
var int[][] a = [[1, 2], [3, 4]];
var int[][] b = deepCopy(a);
b[0][0] = 99;
writeLine(intToStr(a[0][0]));   // still 1
writeLine(intToStr(b[0][0]));   // 99
```

---

#### copyRange
Copies a range of elements from an array.

**Signature:**
```cln
copyRange(arr: Array, start: int, end: int) -> Array
```

**Parameters:**
- `arr` (Array): Source array
- `start` (int): Starting index (inclusive, 0-based)
- `end` (int): Ending index (exclusive)

**Returns:**
- Array: New array containing elements from start to end-1

---

### Array Information

#### length
Returns the length of an array.

**Signature:**
```cln
length(arr: Array) -> int
```

**Parameters:**
- `arr` (Array): Array to measure

**Returns:**
- int: Number of elements in the array (0 if null)

---

#### isEmpty
Checks if an array is empty.

**Signature:**
```cln
isEmpty(arr: Array) -> bool
```

**Parameters:**
- `arr` (Array): Array to check

**Returns:**
- bool: true if array is null or empty, false otherwise

---

### Array Search

#### indexOf
Finds the index of the first occurrence of an element in an array.

**Signature:**
```cln
indexOf(arr: Array, element: Object) -> int
```

**Parameters:**
- `arr` (Array): Array to search in
- `element` (Object): Element to find

**Returns:**
- int: Index of first occurrence, or -1 if not found

---

#### lastIndexOf
Finds the index of the last occurrence of an element in an array.

**Signature:**
```cln
lastIndexOf(arr: Array, element: Object) -> int
```

**Parameters:**
- `arr` (Array): Array to search in
- `element` (Object): Element to find

**Returns:**
- int: Index of last occurrence, or -1 if not found

---

#### contains
Checks if an array contains a specific element.

**Signature:**
```cln
contains(arr: Array, element: Object) -> bool
```

**Parameters:**
- `arr` (Array): Array to search in
- `element` (Object): Element to find

**Returns:**
- bool: true if element is found, false otherwise

---

### Array Modification

#### fill
Fills all elements of an array with a specified value.

**Signature:**
```cln
fill(arr: Array, value: Object) -> void
```

**Parameters:**
- `arr` (Array): Array to fill
- `value` (Object): Value to set for all elements

**Note:** This function modifies the array in place.

---

#### reverse
Reverses the order of elements in an array.

**Signature:**
```cln
reverse(arr: Array) -> void
```

**Parameters:**
- `arr` (Array): Array to reverse

**Note:** This function modifies the array in place.

---

### Array Comparison

#### equals
Checks if two arrays are equal (same length and equal elements at each position).

**Signature:**
```cln
equals(arr1: Array, arr2: Array) -> bool
```

**Parameters:**
- `arr1` (Array): First array
- `arr2` (Array): Second array

**Returns:**
- bool: true if arrays are equal, false otherwise

---

### Array Concatenation

#### concat
Concatenates two arrays into a new array.

**Signature:**
```cln
concat(arr1: Array, arr2: Array) -> Array
```

**Parameters:**
- `arr1` (Array): First array
- `arr2` (Array): Second array

**Returns:**
- Array: New array containing all elements from arr1 followed by all elements from arr2

---

### Array Slice

#### slice
Extracts a slice of an array.

**Signature:**
```cln
slice(arr: Array, start: int, length: int) -> Array
```

**Parameters:**
- `arr` (Array): Source array
- `start` (int): Starting index (0-based)
- `length` (int): Number of elements to include

**Returns:**
- Array: New array containing the slice

---

## Usage Example

```cln
import std.array.*;
import std.console.writeLine;
import std.str.intToStr;

// Create and initialize array
Array numbers = newArray(5);
fill(numbers, 0);

// Set some values
numbers[0] = 10;
numbers[1] = 20;
numbers[2] = 30;
numbers[3] = 40;
numbers[4] = 50;

// Get array information
int len = length(numbers);          // 5
bool empty = isEmpty(numbers);      // false

// Search operations
int idx = indexOf(numbers, 30);     // 2
bool has40 = contains(numbers, 40); // true

// Copy operations
Array copy1 = copy(numbers);
Array range = copyRange(numbers, 1, 4);  // [20, 30, 40]

// Modification
fill(range, 0);                     // range is now [0, 0, 0]
reverse(numbers);                   // numbers is now [50, 40, 30, 20, 10]

// Concatenation
Array arr1 = newArray(2);
arr1[0] = 1;
arr1[1] = 2;
Array arr2 = newArray(2);
arr2[0] = 3;
arr2[1] = 4;
Array combined = concat(arr1, arr2);  // [1, 2, 3, 4]

// Comparison
bool areEqual = equals(arr1, arr2);  // false

// Slicing
Array sliced = slice(combined, 1, 2);  // [2, 3]
```
