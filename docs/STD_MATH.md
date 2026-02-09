# std.math - Mathematical Functions Library

The `std.math` package provides mathematical operations including trigonometric, exponential, logarithmic, power, rounding, and utility functions.

## Package Name
`std.math`

## Functions

### Trigonometric Functions

#### sin
Returns the sine of an angle (in radians).

**Signature:**
```cln
sin(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Angle in radians

**Returns:**
- decimal: Sine of x

---

#### cos
Returns the cosine of an angle (in radians).

**Signature:**
```cln
cos(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Angle in radians

**Returns:**
- decimal: Cosine of x

---

#### tan
Returns the tangent of an angle (in radians).

**Signature:**
```cln
tan(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Angle in radians

**Returns:**
- decimal: Tangent of x

---

#### asin
Returns the arc sine of a value (result in radians).

**Signature:**
```cln
asin(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Value between -1 and 1

**Returns:**
- decimal: Arc sine of x in radians

---

#### acos
Returns the arc cosine of a value (result in radians).

**Signature:**
```cln
acos(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Value between -1 and 1

**Returns:**
- decimal: Arc cosine of x in radians

---

#### atan
Returns the arc tangent of a value (result in radians).

**Signature:**
```cln
atan(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Any value

**Returns:**
- decimal: Arc tangent of x in radians

---

#### atan2
Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta).

**Signature:**
```cln
atan2(y: decimal, x: decimal) -> decimal
```

**Parameters:**
- `y` (decimal): Y coordinate
- `x` (decimal): X coordinate

**Returns:**
- decimal: Angle in radians

---

### Exponential and Logarithmic Functions

#### exp
Returns Euler's number e raised to the power of x.

**Signature:**
```cln
exp(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): The exponent

**Returns:**
- decimal: e^x

---

#### log
Returns the natural logarithm (base e) of x.

**Signature:**
```cln
log(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Value greater than 0

**Returns:**
- decimal: Natural logarithm of x

---

#### log10
Returns the base 10 logarithm of x.

**Signature:**
```cln
log10(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Value greater than 0

**Returns:**
- decimal: Base 10 logarithm of x

---

### Power and Root Functions

#### pow
Returns x raised to the power of y.

**Signature:**
```cln
pow(x: decimal, y: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Base value
- `y` (decimal): Exponent

**Returns:**
- decimal: x^y

---

#### sqrt
Returns the square root of x.

**Signature:**
```cln
sqrt(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Value greater than or equal to 0

**Returns:**
- decimal: Square root of x

---

#### cbrt
Returns the cube root of x.

**Signature:**
```cln
cbrt(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Any value

**Returns:**
- decimal: Cube root of x

---

### Rounding Functions

#### abs
Returns the absolute value of x (works with both int and decimal).

**Signature:**
```cln
abs(x: decimal) -> decimal
abs(x: int) -> int
```

**Parameters:**
- `x` (decimal or int): Any numeric value

**Returns:**
- decimal or int: Absolute value of x

---

#### ceil
Returns the smallest integer greater than or equal to x.

**Signature:**
```cln
ceil(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Any decimal value

**Returns:**
- decimal: Ceiling of x

---

#### floor
Returns the largest integer less than or equal to x.

**Signature:**
```cln
floor(x: decimal) -> decimal
```

**Parameters:**
- `x` (decimal): Any decimal value

**Returns:**
- decimal: Floor of x

---

#### round
Returns the closest integer to x.

**Signature:**
```cln
round(x: decimal) -> int
```

**Parameters:**
- `x` (decimal): Any decimal value

**Returns:**
- int: Rounded integer value

---

### Min/Max Functions

#### min
Returns the smaller of two values (works with int and decimal).

**Signature:**
```cln
min(a: decimal, b: decimal) -> decimal
min(a: int, b: int) -> int
```

**Parameters:**
- `a` (decimal or int): First value
- `b` (decimal or int): Second value

**Returns:**
- decimal or int: Smaller of the two values

---

#### max
Returns the larger of two values (works with int and decimal).

**Signature:**
```cln
max(a: decimal, b: decimal) -> decimal
max(a: int, b: int) -> int
```

**Parameters:**
- `a` (decimal or int): First value
- `b` (decimal or int): Second value

**Returns:**
- decimal or int: Larger of the two values

---

### Other Functions

#### random
Returns a random value between 0.0 (inclusive) and 1.0 (exclusive).

**Signature:**
```cln
random() -> decimal
```

**Returns:**
- decimal: Random value in range [0.0, 1.0)

---

#### toRadians
Converts an angle measured in degrees to radians.

**Signature:**
```cln
toRadians(degrees: decimal) -> decimal
```

**Parameters:**
- `degrees` (decimal): Angle in degrees

**Returns:**
- decimal: Angle in radians

---

#### toDegrees
Converts an angle measured in radians to degrees.

**Signature:**
```cln
toDegrees(radians: decimal) -> decimal
```

**Parameters:**
- `radians` (decimal): Angle in radians

**Returns:**
- decimal: Angle in degrees

---

## Usage Example

```cln
import std.math.*;

decimal angle = 45.0;
decimal radians = toRadians(angle);
decimal sineValue = sin(radians);
decimal result = pow(2.0, 3.0);  // 8.0
decimal root = sqrt(16.0);       // 4.0
int rounded = round(3.7);        // 4
decimal randNum = random();      // Random number between 0 and 1
```
