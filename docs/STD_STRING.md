# std.str - String Utility Library

The `std.str` package provides C-like string operations including conversion, manipulation, comparison, and transformation functions.

## Package Name
`std.str`

## Functions

### String Conversion Functions

#### intToStr
Converts an integer to a string.

**Signature:**
```cln
intToStr(value: int) -> String
```

**Parameters:**
- `value` (int): Integer value to convert

**Returns:**
- String: String representation of the integer

---

#### decToStr
Converts a decimal to a string.

**Signature:**
```cln
decToStr(value: decimal) -> String
```

**Parameters:**
- `value` (decimal): Decimal value to convert

**Returns:**
- String: String representation of the decimal

---

#### strToInt
Converts a string to an integer. Returns 0 if conversion fails (C-like behavior).

**Signature:**
```cln
strToInt(str: String) -> int
```

**Parameters:**
- `str` (String): String to convert

**Returns:**
- int: Integer value, or 0 on error

---

#### boolToStr
Converts a boolean to a string ("true" or "false").

**Signature:**
```cln
boolToStr(value: bool) -> String
```

**Parameters:**
- `value` (bool): Boolean value to convert

**Returns:**
- String: "true" or "false"

---

### String Manipulation Functions

#### strLen
Returns the length of a string.

**Signature:**
```cln
strLen(str: String) -> int
```

**Parameters:**
- `str` (String): String to measure

**Returns:**
- int: Length of the string (0 if null)

---

#### strCat
Concatenates two strings.

**Signature:**
```cln
strCat(str1: String, str2: String) -> String
```

**Parameters:**
- `str1` (String): First string
- `str2` (String): Second string

**Returns:**
- String: Concatenated string

---

#### subStr
Extracts a substring from a string.

**Signature:**
```cln
subStr(str: String, start: int, length: int) -> String
```

**Parameters:**
- `str` (String): Source string
- `start` (int): Starting index (0-based)
- `length` (int): Length of substring

**Returns:**
- String: Extracted substring, or empty string if invalid parameters

---

#### charAt
Returns the character at a specific index.

**Signature:**
```cln
charAt(str: String, index: int) -> String
```

**Parameters:**
- `str` (String): Source string
- `index` (int): Character index (0-based)

**Returns:**
- String: Single character as string, or empty string if invalid index

---

#### indexOf
Finds the first occurrence of a substring.

**Signature:**
```cln
indexOf(str: String, search: String) -> int
```

**Parameters:**
- `str` (String): String to search in
- `search` (String): Substring to find

**Returns:**
- int: Index of first occurrence, or -1 if not found

---

#### lastIndexOf
Finds the last occurrence of a substring.

**Signature:**
```cln
lastIndexOf(str: String, search: String) -> int
```

**Parameters:**
- `str` (String): String to search in
- `search` (String): Substring to find

**Returns:**
- int: Index of last occurrence, or -1 if not found

---

### String Comparison Functions

#### strCmp
Compares two strings lexicographically.

**Signature:**
```cln
strCmp(str1: String, str2: String) -> int
```

**Parameters:**
- `str1` (String): First string
- `str2` (String): Second string

**Returns:**
- int: 0 if equal, <0 if str1 < str2, >0 if str1 > str2

---

#### strEq
Checks if two strings are equal.

**Signature:**
```cln
strEq(str1: String, str2: String) -> bool
```

**Parameters:**
- `str1` (String): First string
- `str2` (String): Second string

**Returns:**
- bool: true if strings are equal, false otherwise

---

#### startsWith
Checks if a string starts with a specific prefix.

**Signature:**
```cln
startsWith(str: String, prefix: String) -> bool
```

**Parameters:**
- `str` (String): String to check
- `prefix` (String): Prefix to look for

**Returns:**
- bool: true if string starts with prefix, false otherwise

---

#### endsWith
Checks if a string ends with a specific suffix.

**Signature:**
```cln
endsWith(str: String, suffix: String) -> bool
```

**Parameters:**
- `str` (String): String to check
- `suffix` (String): Suffix to look for

**Returns:**
- bool: true if string ends with suffix, false otherwise

---

### String Transformation Functions

#### toUpper
Converts a string to uppercase.

**Signature:**
```cln
toUpper(str: String) -> String
```

**Parameters:**
- `str` (String): String to convert

**Returns:**
- String: Uppercase version of the string

---

#### toLower
Converts a string to lowercase.

**Signature:**
```cln
toLower(str: String) -> String
```

**Parameters:**
- `str` (String): String to convert

**Returns:**
- String: Lowercase version of the string

---

#### trim
Removes whitespace from both ends of a string.

**Signature:**
```cln
trim(str: String) -> String
```

**Parameters:**
- `str` (String): String to trim

**Returns:**
- String: Trimmed string

---

#### replace
Replaces all occurrences of a substring with another substring.

**Signature:**
```cln
replace(str: String, oldStr: String, newStr: String) -> String
```

**Parameters:**
- `str` (String): Source string
- `oldStr` (String): Substring to replace
- `newStr` (String): Replacement substring

**Returns:**
- String: String with replacements made

---

#### isEmpty
Checks if a string is empty or null.

**Signature:**
```cln
isEmpty(str: String) -> bool
```

**Parameters:**
- `str` (String): String to check

**Returns:**
- bool: true if string is null or empty, false otherwise

---

## Usage Example

```cln
import std.str.*;

String name = "  John Doe  ";
String trimmed = trim(name);          // "John Doe"
String upper = toUpper(trimmed);      // "JOHN DOE"
int len = strLen(upper);              // 8
String first = charAt(upper, 0);      // "J"
bool hasJohn = startsWith(upper, "JOHN");  // true

int age = 25;
String ageStr = intToStr(age);        // "25"
String message = strCat("Age: ", ageStr);  // "Age: 25"

String replaced = replace(message, "Age", "Years");  // "Years: 25"
```
