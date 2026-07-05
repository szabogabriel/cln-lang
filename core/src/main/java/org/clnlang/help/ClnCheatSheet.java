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
            stdArray(),
            stdCalendar(),
            stdConsole(),
            stdJson(),
            stdMath(),
            stdReflect(),
            stdStr(),
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

    private static Section stdCalendar() {
        return new Section("std.calendar", """
            <div style="font-size:0.85em">
              <pre>import std.calendar.*;</pre>

              <p><b>Structs:</b></p>
              <pre>Timestamp  { timestamp, year, month, day, hour, minute, second, millisecond, timezone }
Date       { year, month, day }
Time       { hour, minute, second, millisecond, timezone }</pre>

              <p><b>Current snapshots:</b></p>
              <pre>now()      → Timestamp
nowDate()  → Date
nowTime()  → Time</pre>

              <p><b>Arithmetic (Timestamp):</b></p>
              <pre>plus/minusYears(Timestamp ts, int n)        → Timestamp
plus/minusMonths(Timestamp ts, int n)       → Timestamp
plus/minusDays(Timestamp ts, int n)         → Timestamp
plus/minusHours(Timestamp ts, int n)        → Timestamp
plus/minusMinutes(Timestamp ts, int n)      → Timestamp
plus/minusSeconds(Timestamp ts, int n)      → Timestamp
plus/minusMilliseconds(Timestamp ts, int n) → Timestamp</pre>

              <p><b>Comparison:</b></p>
              <pre>isBefore(Timestamp a, Timestamp b) → bool
isAfter(Timestamp a, Timestamp b)  → bool</pre>

              <p><b>Difference:</b></p>
              <pre>diffDays(Timestamp a, Timestamp b)         → int
diffHours(Timestamp a, Timestamp b)        → int
diffMinutes(Timestamp a, Timestamp b)      → int
diffSeconds(Timestamp a, Timestamp b)      → int
diffMilliseconds(Timestamp a, Timestamp b) → int</pre>

              <p><b>Field setters:</b></p>
              <pre>withYear(Timestamp ts, int v)   → Timestamp
withMonth(Timestamp ts, int v)  → Timestamp
withDay(Timestamp ts, int v)    → Timestamp
withHour(Timestamp ts, int v)   → Timestamp
withMinute(Timestamp ts, int v) → Timestamp
withSecond(Timestamp ts, int v) → Timestamp</pre>

              <p><b>Timezone:</b></p>
              <pre>toTimezone(Timestamp ts, string tz) → Timestamp
// tz examples: "UTC", "America/New_York", "Asia/Tokyo"</pre>

              <p><b>Utility:</b></p>
              <pre>dayOfWeek(Timestamp ts) → int   // 1=Mon … 7=Sun (ISO 8601)
fromEpoch(int millis)   → Timestamp</pre>

              <p><b>Struct conversions:</b></p>
              <pre>timestampToDate(Timestamp ts)                    → Date
timestampToTime(Timestamp ts)                    → Time
dateToTimestamp(Date d)                          → Timestamp  // midnight local
timeToTimestamp(Time t)                          → Timestamp  // today + given time
dateTimeToTimestamp(Date d, Time t)              → Timestamp</pre>

              <p><b>Formatting:</b></p>
              <pre>timestampToString(Timestamp ts, string fmt) → string
dateToString(Date d, string fmt)            → string
timeToString(Time t, string fmt)            → string</pre>

              <p><b>Parsing:</b></p>
              <pre>toTimestamp(string s, string fmt) → Timestamp
toDate(string s, string fmt)      → Date
toTime(string s, string fmt)      → Time</pre>

              <p><b>Format constants (import std.calendar.*):</b></p>
              <pre>FORMAT_DATETIME                  "yyyy-MM-dd HH:mm:ss"
FORMAT_DATE                      "yyyy-MM-dd"
FORMAT_TIME                      "HH:mm:ss"
FORMAT_DATETIME_MILLIS           "yyyy-MM-dd HH:mm:ss.SSS"
FORMAT_TIME_MILLIS               "HH:mm:ss.SSS"
FORMAT_ISO_OFFSET_DATETIME       "yyyy-MM-dd'T'HH:mm:ssxxx"
FORMAT_ISO_OFFSET_DATETIME_MILLIS "yyyy-MM-dd'T'HH:mm:ss.SSSxxx"
FORMAT_ISO_DATETIME              "yyyy-MM-dd'T'HH:mm:ss"
FORMAT_ISO_DATETIME_MILLIS       "yyyy-MM-dd'T'HH:mm:ss.SSS"
FORMAT_DDMMYYYY_SLASH / _HHMM / _HHMMSS
FORMAT_DDMMYYYY_DOT   / _HHMM / _HHMMSS
FORMAT_DDMMYYYY_DASH  / _HHMM / _HHMMSS
FORMAT_MMDDYYYY_SLASH / _HHMM / _HHMMSS
FORMAT_YYYYMMDD_SLASH / _HHMM / _HHMMSS
FORMAT_YYYYMMDD_COMPACT          "yyyyMMdd"
FORMAT_YYYYMMDDHHMMSS_COMPACT    "yyyyMMddHHmmss"
FORMAT_HHMM_COMPACT              "HHmm"
FORMAT_HHMMSS_COMPACT            "HHmmss"
FORMAT_DATE_LONG                 "MMMM d, yyyy"
FORMAT_DATE_MEDIUM               "MMM d, yyyy"
FORMAT_DATETIME_LONG
FORMAT_DATETIME_MEDIUM
FORMAT_DAY_OF_WEEK_DATE          "EEEE, MMMM d, yyyy"
FORMAT_RFC1123                   "EEE, dd MMM yyyy HH:mm:ss z"</pre>
            </div>
            """);
    }

    private static Section stdReflect() {
        return new Section("std.reflect", """
            <div style="font-size:0.85em">
              <pre>import std.reflect.*;

// Field access
getFields(Any s) → string[]
getField(Any s, string fieldName) → Any
setField(Any s, string fieldName, Any value)

// Type checks
isStruct(Any s) → bool
isInt(Any s)    → bool
isDec(Any s)    → bool
isBool(Any s)   → bool
isString(Any s) → bool

// Struct inspection
getStructName(Any s) → string

// Typed getters (throw if wrong type)
getInt(Any s)    → int
getDec(Any s)    → dec
getBool(Any s)   → bool
getString(Any s) → string</pre>
              <p><b>Example:</b></p>
              <pre>Any xVal = getField(p, "x");
setField(p, "x", 99);
bool yes = isStruct(p);
string n  = getStructName(p);
int x     = getInt(xVal);</pre>
            </div>
            """);
    }

    private static Section stdJson() {
        return new Section("std.json", """
            <div style="font-size:0.85em">
              <pre>import std.json.*;
string toJson(Any value) → string
Any fromJson(string json) → Any</pre>
              <p><b>Example:</b></p>
              <pre>var Point p = Point(x: 1, y: 2);
string jsonStr = toJson(p);
Any obj = fromJson(jsonStr);</pre>
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
