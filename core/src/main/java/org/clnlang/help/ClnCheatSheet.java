package org.clnlang.help;

import java.util.List;

/**
 * Single source of truth for the cln-lang quick-reference cheat sheet.
 * Each {@link Section} carries a plain-text title and an HTML body fragment
 * (wrapped in a single {@code <div>}) that can be rendered directly by
 * the web-UI without any additional dependencies.
 */
public final class ClnCheatSheet {

    public record Section(String title, String html) {}

    private ClnCheatSheet() {}

    public static List<Section> getSections() {
        return List.of(
            keywords(),
            types(),
            variables(),
            functions(),
            structs(),
            unions(),
            arrays(),
            stdConsole(),
            stdStr(),
            stdMath(),
            stdArray(),
            stdSys()
        );
    }

    // ── Sections ──────────────────────────────────────────────────────────────

    private static Section keywords() {
        return new Section("Keywords", """
            <div style="font-size:0.85em">
              <table cellpadding="3">
                <tr><td><code>package</code></td><td>declares the package</td></tr>
                <tr><td><code>import</code></td><td>imports a package</td></tr>
                <tr><td><code>expose</code></td><td>makes a symbol public</td></tr>
                <tr><td><code>struct</code></td><td>defines a record type</td></tr>
                <tr><td><code>union</code></td><td>defines a tagged union</td></tr>
                <tr><td><code>var</code></td><td>mutable binding</td></tr>
                <tr><td><code>if / else</code></td><td>conditional</td></tr>
                <tr><td><code>while</code></td><td>loop</td></tr>
                <tr><td><code>switch / case / default</code></td><td>union match</td></tr>
                <tr><td><code>return</code></td><td>returns from function</td></tr>
              </table>
            </div>
            """);
    }

    private static Section types() {
        return new Section("Basic Types", """
            <div style="font-size:0.85em">
              <table cellpadding="3">
                <tr><td><code>int</code></td><td>64-bit integer</td></tr>
                <tr><td><code>bool</code></td><td>true / false</td></tr>
                <tr><td><code>string</code></td><td>UTF-8 text</td></tr>
                <tr><td><code>dec</code></td><td>arbitrary-precision decimal</td></tr>
                <tr><td><code>T[]</code></td><td>1D array of T (any type)</td></tr>
                <tr><td><code>T[][]</code></td><td>2D array of T</td></tr>
                <tr><td><code>T[][][]</code></td><td>3D array of T</td></tr>
              </table>
              <p style="margin:4px 0 0">T may be a primitive, struct, or union type.</p>
            </div>
            """);
    }

    private static Section variables() {
        return new Section("Variables", """
            <div style="font-size:0.85em">
              <p><b>Mutable</b> (with <code>var</code>):</p>
              <pre>var int x = 42;
var string s = "hello";
var bool flag = true;
var dec d = 3.14;</pre>
              <p><b>Immutable</b> (without <code>var</code>):</p>
              <pre>int x = 42;
string s = "hello";</pre>
            </div>
            """);
    }

    private static Section functions() {
        return new Section("Functions & Returns", """
            <div style="font-size:0.85em">
              <p><b>Simple return:</b></p>
              <pre>int add(int a, int b) {
    return a + b;
}</pre>
              <p><b>Named / multiple returns:</b></p>
              <pre>(var int q = 0, var int r = 0) divmod(int a, int b) {
    q = a / b;
    r = a - q * b;
    return;
}</pre>
              <p><b>No return value:</b></p>
              <pre>greet(string msg) {
    writeLine(msg);
}</pre>
            </div>
            """);
    }

    private static Section structs() {
        return new Section("Structs", """
            <div style="font-size:0.85em">
              <p><b>Definition:</b></p>
              <pre>struct Point {
    var int x;
    var int y;
};</pre>
              <p><b>Instantiation:</b></p>
              <pre>Point p = Point(x: 1, y: 2);</pre>
              <p><b>Field access &amp; mutation:</b></p>
              <pre>int px = p.x;
p.y = 10;</pre>
            </div>
            """);
    }

    private static Section unions() {
        return new Section("Unions", """
            <div style="font-size:0.85em">
              <p><b>Definition:</b></p>
              <pre>struct Circle { var dec radius; };
struct Square { var dec side; };
union Shape = Circle | Square;</pre>
              <p><b>Pattern match:</b></p>
              <pre>switch s {
    case Circle c: writeLine("circle");
    case Square q: writeLine("square");
    default: writeLine("other");
}</pre>
            </div>
            """);
    }

    private static Section arrays() {
        return new Section("Arrays", """
            <div style="font-size:0.85em">
              <p><b>1D – primitive:</b></p>
              <pre>var int[] nums = [1, 2, 3];
int len   = nums.length;
int first = nums[0];
nums[1]   = 99;</pre>
              <p><b>1D – struct / union:</b></p>
              <pre>var Point[] pts = [Point(x:1, y:2), Point(x:3, y:4)];
pts[0].x = 99;
var Shape[] shapes = [Circle(radius:5), Rectangle(w:3, h:4)];</pre>
              <p><b>2D literal &amp; access:</b></p>
              <pre>var int[][] m = [[1, 2], [3, 4]];
int v = m[1][0];
m[0][1] = 7;</pre>
              <p><b>Dynamic allocation (std.array):</b></p>
              <pre>import std.array.*;
var int[][] grid = newArray2D(rows, cols);
var int[][][] vol = newArray3D(depth, rows, cols);</pre>
              <p><b>Iterate:</b></p>
              <pre>var int i = 0;
while (i &lt; nums.length) {
    writeLine(intToStr(nums[i]));
    i++;
}</pre>
            </div>
            """);
    }

    private static Section stdConsole() {
        return new Section("std.console", """
            <div style="font-size:0.85em">
              <pre>import std.console.*;

write(string msg)
writeLine(string msg)
string s = readLine()</pre>
            </div>
            """);
    }

    private static Section stdStr() {
        return new Section("std.str", """
            <div style="font-size:0.85em">
              <pre>import std.str.*;

intToStr(int n) → string
decToStr(dec d) → string
strToInt(string s) → int
boolToStr(bool b) → string
strLen(string s) → int
strCat(string a, string b) → string
subStr(string s, int from, int to) → string
charAt(string s, int i) → string
indexOf(string s, string sub) → int
lastIndexOf(string s, string sub) → int
strCmp(string a, string b) → int
strEq(string a, string b) → bool
startsWith(string s, string pfx) → bool
endsWith(string s, string sfx) → bool
toUpper(string s) → string
toLower(string s) → string
trim(string s) → string
replace(string s, string old, string nw) → string
isEmpty(string s) → bool</pre>
            </div>
            """);
    }

    private static Section stdMath() {
        return new Section("std.math", """
            <div style="font-size:0.85em">
              <pre>import std.math.*;

sin/cos/tan(dec x) → dec
asin/acos/atan(dec x) → dec
atan2(dec y, dec x) → dec
exp/log/log10(dec x) → dec
pow(dec base, dec exp) → dec
sqrt/cbrt(dec x) → dec
abs/ceil/floor(dec x) → dec
round(dec x) → int
min/max(dec a, dec b) → dec
random() → dec
toRadians(dec deg) → dec
toDegrees(dec rad) → dec</pre>
            </div>
            """);
    }

    private static Section stdArray() {
        return new Section("std.array", """
            <div style="font-size:0.85em">
              <pre>import std.array.*;

// Creation
newArray(int size) → T[]
newArray2D(int rows, int cols) → T[][]      // NEW
newArray3D(int d, int r, int c) → T[][][]   // NEW

// Copying
copy(T[] a) → T[]           // shallow copy
deepCopy(T[] a) → T[]       // deep copy (arrays + structs/unions) NEW
copyRange(T[] a, int from, int to) → T[]

// Information
length(T[] a) → int
isEmpty(T[] a) → bool

// Search
indexOf(T[] a, T v) → int
lastIndexOf(T[] a, T v) → int
contains(T[] a, T v) → bool

// Modification
fill(T[] a, T v)       // mutates
reverse(T[] a)         // mutates

// Comparison &amp; set ops
equals(T[] a, T[] b) → bool
concat(T[] a, T[] b) → T[]
slice(T[] a, int from, int len) → T[]</pre>
            </div>
            """);
    }

    private static Section stdSys() {
        return new Section("std.sys", """
            <div style="font-size:0.85em">
              <pre>import std.sys.*;

currentTimeMillis() → int
nanoTime() → int
exit(int code)
getenv(string name) → string
getProperty(string key) → string
getPropertyWithDefault(string key, string def) → string
freeMemory() → int
totalMemory() → int
maxMemory() → int
gc()</pre>
            </div>
            """);
    }
}
