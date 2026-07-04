package org.clnlang.lib.std.calendar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.GlobalVarDeclImpl;
import org.clnlang.compile.expression.StringLiteralExprImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;
import org.clnlang.runtime.types.StructDefinition;

public class Calendar implements ClnFunction {

    private final String packageName = "std.calendar";

    // ========== Struct builders ==========

    private static Map<String, Object> buildTimestampStruct(ZonedDateTime zdt) {
        long millis = zdt.toInstant().toEpochMilli();
        Map<String, Object> s = new HashMap<>();
        s.put("__type__", "Timestamp");
        s.put("timestamp", millis);
        s.put("year", (long) zdt.getYear());
        s.put("month", (long) zdt.getMonthValue());
        s.put("day", (long) zdt.getDayOfMonth());
        s.put("hour", (long) zdt.getHour());
        s.put("minute", (long) zdt.getMinute());
        s.put("second", (long) zdt.getSecond());
        s.put("millisecond", (long) (millis % 1000));
        s.put("timezone", zdt.getZone().getId());
        return s;
    }

    private static Map<String, Object> buildDateStruct(LocalDate date) {
        Map<String, Object> s = new HashMap<>();
        s.put("__type__", "Date");
        s.put("year", (long) date.getYear());
        s.put("month", (long) date.getMonthValue());
        s.put("day", (long) date.getDayOfMonth());
        return s;
    }

    private static Map<String, Object> buildTimeStruct(LocalTime time, ZoneId zone) {
        Map<String, Object> s = new HashMap<>();
        s.put("__type__", "Time");
        s.put("hour", (long) time.getHour());
        s.put("minute", (long) time.getMinute());
        s.put("second", (long) time.getSecond());
        s.put("millisecond", (long) (time.getNano() / 1_000_000));
        s.put("timezone", zone.getId());
        return s;
    }

    // ========== Parameter helpers ==========

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getStruct(ExecutionContext context, String name) {
        return (Map<String, Object>) context.getLocalContext().getValue(name);
    }

    private static long getLong(ExecutionContext context, String name) {
        return (Long) context.getLocalContext().getValue(name);
    }

    private static String getString(ExecutionContext context, String name) {
        return (String) context.getLocalContext().getValue(name);
    }

    private static ZonedDateTime timestampToZdt(Map<String, Object> ts) {
        long millis = (Long) ts.get("timestamp");
        String tz = (String) ts.get("timezone");
        ZoneId zone = (tz != null && !tz.isEmpty()) ? ZoneId.of(tz) : ZoneId.systemDefault();
        return Instant.ofEpochMilli(millis).atZone(zone);
    }

    // ========== now ==========

    private void executeNow(ExecutionContext context) {
        long millis = System.currentTimeMillis();
        ZonedDateTime zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    private void executeNowDate(ExecutionContext context) {
        long millis = System.currentTimeMillis();
        ZonedDateTime zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());
        context.setReturnValues(Collections.singletonList(buildDateStruct(zdt.toLocalDate())));
    }

    private void executeNowTime(ExecutionContext context) {
        long millis = System.currentTimeMillis();
        ZonedDateTime zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());
        context.setReturnValues(Collections.singletonList(buildTimeStruct(zdt.toLocalTime(), zdt.getZone())));
    }

    // ========== Plus ==========

    private void executePlusYears(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusYears(amount))));
    }

    private void executePlusMonths(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusMonths(amount))));
    }

    private void executePlusDays(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusDays(amount))));
    }

    private void executePlusHours(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusHours(amount))));
    }

    private void executePlusMinutes(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusMinutes(amount))));
    }

    private void executePlusSeconds(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plusSeconds(amount))));
    }

    private void executePlusMilliseconds(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).plus(amount, ChronoUnit.MILLIS))));
    }

    // ========== Minus ==========

    private void executeMinusYears(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusYears(amount))));
    }

    private void executeMinusMonths(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusMonths(amount))));
    }

    private void executeMinusDays(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusDays(amount))));
    }

    private void executeMinusHours(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusHours(amount))));
    }

    private void executeMinusMinutes(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusMinutes(amount))));
    }

    private void executeMinusSeconds(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minusSeconds(amount))));
    }

    private void executeMinusMilliseconds(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long amount = getLong(context, "amount");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).minus(amount, ChronoUnit.MILLIS))));
    }

    // ========== Comparison ==========

    private void executeIsBefore(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        boolean result = (Long) a.get("timestamp") < (Long) b.get("timestamp");
        context.setReturnValues(Collections.singletonList(result));
    }

    private void executeIsAfter(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        boolean result = (Long) a.get("timestamp") > (Long) b.get("timestamp");
        context.setReturnValues(Collections.singletonList(result));
    }

    // ========== Formatting (toStr) ==========

    private void executeTimestampToString(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        String format = getString(context, "format");
        ZonedDateTime zdt = timestampToZdt(ts);
        context.setReturnValues(Collections.singletonList(DateTimeFormatter.ofPattern(format).format(zdt)));
    }

    private void executeDateToString(ExecutionContext context) {
        Map<String, Object> d = getStruct(context, "d");
        String format = getString(context, "format");
        LocalDate date = LocalDate.of(
            ((Long) d.get("year")).intValue(),
            ((Long) d.get("month")).intValue(),
            ((Long) d.get("day")).intValue()
        );
        context.setReturnValues(Collections.singletonList(DateTimeFormatter.ofPattern(format).format(date)));
    }

    private void executeTimeToString(ExecutionContext context) {
        Map<String, Object> t = getStruct(context, "t");
        String format = getString(context, "format");
        LocalTime time = LocalTime.of(
            ((Long) t.get("hour")).intValue(),
            ((Long) t.get("minute")).intValue(),
            ((Long) t.get("second")).intValue(),
            ((Long) t.get("millisecond")).intValue() * 1_000_000
        );
        context.setReturnValues(Collections.singletonList(DateTimeFormatter.ofPattern(format).format(time)));
    }

    // ========== Difference ==========

    private void executeDiffMilliseconds(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        context.setReturnValues(Collections.singletonList((Long) b.get("timestamp") - (Long) a.get("timestamp")));
    }

    private void executeDiffSeconds(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        long diff = ((Long) b.get("timestamp") - (Long) a.get("timestamp")) / 1000L;
        context.setReturnValues(Collections.singletonList(diff));
    }

    private void executeDiffMinutes(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        long diff = ((Long) b.get("timestamp") - (Long) a.get("timestamp")) / 60_000L;
        context.setReturnValues(Collections.singletonList(diff));
    }

    private void executeDiffHours(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        long diff = ((Long) b.get("timestamp") - (Long) a.get("timestamp")) / 3_600_000L;
        context.setReturnValues(Collections.singletonList(diff));
    }

    private void executeDiffDays(ExecutionContext context) {
        Map<String, Object> a = getStruct(context, "a");
        Map<String, Object> b = getStruct(context, "b");
        long diff = ((Long) b.get("timestamp") - (Long) a.get("timestamp")) / 86_400_000L;
        context.setReturnValues(Collections.singletonList(diff));
    }

    // ========== Field setters (with*) ==========

    private void executeWithYear(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withYear((int) value))));
    }

    private void executeWithMonth(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withMonth((int) value))));
    }

    private void executeWithDay(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withDayOfMonth((int) value))));
    }

    private void executeWithHour(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withHour((int) value))));
    }

    private void executeWithMinute(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withMinute((int) value))));
    }

    private void executeWithSecond(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        long value = getLong(context, "value");
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(timestampToZdt(ts).withSecond((int) value))));
    }

    // ========== Timezone ==========

    private void executeToTimezone(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        String tz = getString(context, "tz");
        ZonedDateTime zdt = timestampToZdt(ts).withZoneSameInstant(ZoneId.of(tz));
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    // ========== Utility ==========

    private void executeDayOfWeek(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        // ISO 8601: 1=Monday … 7=Sunday
        long dow = timestampToZdt(ts).getDayOfWeek().getValue();
        context.setReturnValues(Collections.singletonList(dow));
    }

    private void executeFromEpoch(ExecutionContext context) {
        long millis = getLong(context, "millis");
        ZonedDateTime zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    // ========== Struct conversions ==========

    private void executeTimestampToDate(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        context.setReturnValues(Collections.singletonList(buildDateStruct(timestampToZdt(ts).toLocalDate())));
    }

    private void executeTimestampToTime(ExecutionContext context) {
        Map<String, Object> ts = getStruct(context, "ts");
        ZonedDateTime zdt = timestampToZdt(ts);
        context.setReturnValues(Collections.singletonList(buildTimeStruct(zdt.toLocalTime(), zdt.getZone())));
    }

    private void executeDateToTimestamp(ExecutionContext context) {
        Map<String, Object> d = getStruct(context, "d");
        LocalDate date = LocalDate.of(
            ((Long) d.get("year")).intValue(),
            ((Long) d.get("month")).intValue(),
            ((Long) d.get("day")).intValue()
        );
        ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    private void executeTimeToTimestamp(ExecutionContext context) {
        Map<String, Object> t = getStruct(context, "t");
        String tz = (String) t.get("timezone");
        ZoneId zone = (tz != null && !tz.isEmpty()) ? ZoneId.of(tz) : ZoneId.systemDefault();
        LocalTime time = LocalTime.of(
            ((Long) t.get("hour")).intValue(),
            ((Long) t.get("minute")).intValue(),
            ((Long) t.get("second")).intValue(),
            ((Long) t.get("millisecond")).intValue() * 1_000_000
        );
        ZonedDateTime zdt = LocalDate.now(zone).atTime(time).atZone(zone);
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    private void executeDateTimeToTimestamp(ExecutionContext context) {
        Map<String, Object> d = getStruct(context, "d");
        Map<String, Object> t = getStruct(context, "t");
        String tz = (String) t.get("timezone");
        ZoneId zone = (tz != null && !tz.isEmpty()) ? ZoneId.of(tz) : ZoneId.systemDefault();
        LocalDate date = LocalDate.of(
            ((Long) d.get("year")).intValue(),
            ((Long) d.get("month")).intValue(),
            ((Long) d.get("day")).intValue()
        );
        LocalTime time = LocalTime.of(
            ((Long) t.get("hour")).intValue(),
            ((Long) t.get("minute")).intValue(),
            ((Long) t.get("second")).intValue(),
            ((Long) t.get("millisecond")).intValue() * 1_000_000
        );
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(date.atTime(time).atZone(zone))));
    }

    // ========== Parsing (toStruct) ==========

    private void executeToTimestamp(ExecutionContext context) {
        String value = getString(context, "value");
        String format = getString(context, "format");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        ZonedDateTime zdt;
        try {
            zdt = ZonedDateTime.parse(value, formatter);
        } catch (DateTimeParseException e1) {
            try {
                zdt = LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault());
            } catch (DateTimeParseException e2) {
                zdt = LocalDate.parse(value, formatter).atStartOfDay(ZoneId.systemDefault());
            }
        }
        context.setReturnValues(Collections.singletonList(buildTimestampStruct(zdt)));
    }

    private void executeToDate(ExecutionContext context) {
        String value = getString(context, "value");
        String format = getString(context, "format");
        LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
        context.setReturnValues(Collections.singletonList(buildDateStruct(date)));
    }

    private void executeToTime(ExecutionContext context) {
        String value = getString(context, "value");
        String format = getString(context, "format");
        LocalTime time = LocalTime.parse(value, DateTimeFormatter.ofPattern(format));
        context.setReturnValues(Collections.singletonList(buildTimeStruct(time, ZoneId.systemDefault())));
    }

    // ========== Registration ==========

    @Override
    public void register(Registry registry) {
        registerStructs(registry);
        registerConstants(registry);
        registerFunctions(registry);
    }

    private void registerConstants(Registry registry) {
        // ISO / dash-separated (yyyy-MM-dd)
        registerConstant(registry, "FORMAT_DATETIME",                  "yyyy-MM-dd HH:mm:ss");
        registerConstant(registry, "FORMAT_DATE",                      "yyyy-MM-dd");
        registerConstant(registry, "FORMAT_TIME",                      "HH:mm:ss");
        registerConstant(registry, "FORMAT_DATETIME_MILLIS",           "yyyy-MM-dd HH:mm:ss.SSS");
        registerConstant(registry, "FORMAT_TIME_MILLIS",               "HH:mm:ss.SSS");
        registerConstant(registry, "FORMAT_ISO_OFFSET_DATETIME",       "yyyy-MM-dd'T'HH:mm:ssXXX");
        registerConstant(registry, "FORMAT_ISO_OFFSET_DATETIME_MILLIS","yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        registerConstant(registry, "FORMAT_ISO_DATETIME",              "yyyy-MM-dd'T'HH:mm:ss");
        registerConstant(registry, "FORMAT_ISO_DATETIME_MILLIS",       "yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Day-first slash-separated (dd/MM/yyyy)
        registerConstant(registry, "FORMAT_DDMMYYYY_SLASH",            "dd/MM/yyyy");
        registerConstant(registry, "FORMAT_DDMMYYYY_SLASH_HHMM",       "dd/MM/yyyy HH:mm");
        registerConstant(registry, "FORMAT_DDMMYYYY_SLASH_HHMMSS",     "dd/MM/yyyy HH:mm:ss");

        // Day-first dot-separated (dd.MM.yyyy)
        registerConstant(registry, "FORMAT_DDMMYYYY_DOT",              "dd.MM.yyyy");
        registerConstant(registry, "FORMAT_DDMMYYYY_DOT_HHMM",         "dd.MM.yyyy HH:mm");
        registerConstant(registry, "FORMAT_DDMMYYYY_DOT_HHMMSS",       "dd.MM.yyyy HH:mm:ss");

        // Day-first dash-separated (dd-MM-yyyy)
        registerConstant(registry, "FORMAT_DDMMYYYY_DASH",             "dd-MM-yyyy");
        registerConstant(registry, "FORMAT_DDMMYYYY_DASH_HHMM",        "dd-MM-yyyy HH:mm");
        registerConstant(registry, "FORMAT_DDMMYYYY_DASH_HHMMSS",      "dd-MM-yyyy HH:mm:ss");

        // Month-first slash-separated (MM/dd/yyyy) — US style
        registerConstant(registry, "FORMAT_MMDDYYYY_SLASH",            "MM/dd/yyyy");
        registerConstant(registry, "FORMAT_MMDDYYYY_SLASH_HHMM",       "MM/dd/yyyy HH:mm");
        registerConstant(registry, "FORMAT_MMDDYYYY_SLASH_HHMMSS",     "MM/dd/yyyy HH:mm:ss");

        // Year-first slash-separated (yyyy/MM/dd)
        registerConstant(registry, "FORMAT_YYYYMMDD_SLASH",            "yyyy/MM/dd");
        registerConstant(registry, "FORMAT_YYYYMMDD_SLASH_HHMM",       "yyyy/MM/dd HH:mm");
        registerConstant(registry, "FORMAT_YYYYMMDD_SLASH_HHMMSS",     "yyyy/MM/dd HH:mm:ss");

        // Compact (no separators)
        registerConstant(registry, "FORMAT_YYYYMMDD_COMPACT",          "yyyyMMdd");
        registerConstant(registry, "FORMAT_YYYYMMDDHHMMSS_COMPACT",    "yyyyMMddHHmmss");
        registerConstant(registry, "FORMAT_HHMM_COMPACT",              "HHmm");
        registerConstant(registry, "FORMAT_HHMMSS_COMPACT",            "HHmmss");

        // Human-readable
        registerConstant(registry, "FORMAT_DATE_LONG",                 "MMMM d, yyyy");
        registerConstant(registry, "FORMAT_DATE_MEDIUM",               "MMM d, yyyy");
        registerConstant(registry, "FORMAT_DATETIME_LONG",             "MMMM d, yyyy HH:mm:ss");
        registerConstant(registry, "FORMAT_DATETIME_MEDIUM",           "MMM d, yyyy HH:mm:ss");
        registerConstant(registry, "FORMAT_DAY_OF_WEEK_DATE",          "EEEE, MMMM d, yyyy");

        // RFC / HTTP
        registerConstant(registry, "FORMAT_RFC1123",                   "EEE, dd MMM yyyy HH:mm:ss zzz");
    }

    private void registerConstant(Registry registry, String name, String value) {
        GlobalVarDeclImpl c = new GlobalVarDeclImpl(false, "string", name, new StringLiteralExprImpl(value), true);
        c.setPackageName(packageName);
        registry.registerGlobalConstant(new FullyQualifiedName(packageName, name), c);
    }

    private void registerFunctions(Registry registry) {
        // now
        func(registry, "now",     this::executeNow);
        func(registry, "nowDate", this::executeNowDate);
        func(registry, "nowTime", this::executeNowTime);

        // plus
        func2(registry, "plusYears",        "Timestamp", "ts", "int", "amount", this::executePlusYears);
        func2(registry, "plusMonths",       "Timestamp", "ts", "int", "amount", this::executePlusMonths);
        func2(registry, "plusDays",         "Timestamp", "ts", "int", "amount", this::executePlusDays);
        func2(registry, "plusHours",        "Timestamp", "ts", "int", "amount", this::executePlusHours);
        func2(registry, "plusMinutes",      "Timestamp", "ts", "int", "amount", this::executePlusMinutes);
        func2(registry, "plusSeconds",      "Timestamp", "ts", "int", "amount", this::executePlusSeconds);
        func2(registry, "plusMilliseconds", "Timestamp", "ts", "int", "amount", this::executePlusMilliseconds);

        // minus
        func2(registry, "minusYears",        "Timestamp", "ts", "int", "amount", this::executeMinusYears);
        func2(registry, "minusMonths",       "Timestamp", "ts", "int", "amount", this::executeMinusMonths);
        func2(registry, "minusDays",         "Timestamp", "ts", "int", "amount", this::executeMinusDays);
        func2(registry, "minusHours",        "Timestamp", "ts", "int", "amount", this::executeMinusHours);
        func2(registry, "minusMinutes",      "Timestamp", "ts", "int", "amount", this::executeMinusMinutes);
        func2(registry, "minusSeconds",      "Timestamp", "ts", "int", "amount", this::executeMinusSeconds);
        func2(registry, "minusMilliseconds", "Timestamp", "ts", "int", "amount", this::executeMinusMilliseconds);

        // difference
        func2(registry, "diffMilliseconds", "Timestamp", "a", "Timestamp", "b", this::executeDiffMilliseconds);
        func2(registry, "diffSeconds",      "Timestamp", "a", "Timestamp", "b", this::executeDiffSeconds);
        func2(registry, "diffMinutes",      "Timestamp", "a", "Timestamp", "b", this::executeDiffMinutes);
        func2(registry, "diffHours",        "Timestamp", "a", "Timestamp", "b", this::executeDiffHours);
        func2(registry, "diffDays",         "Timestamp", "a", "Timestamp", "b", this::executeDiffDays);

        // field setters
        func2(registry, "withYear",   "Timestamp", "ts", "int", "value", this::executeWithYear);
        func2(registry, "withMonth",  "Timestamp", "ts", "int", "value", this::executeWithMonth);
        func2(registry, "withDay",    "Timestamp", "ts", "int", "value", this::executeWithDay);
        func2(registry, "withHour",   "Timestamp", "ts", "int", "value", this::executeWithHour);
        func2(registry, "withMinute", "Timestamp", "ts", "int", "value", this::executeWithMinute);
        func2(registry, "withSecond", "Timestamp", "ts", "int", "value", this::executeWithSecond);

        // timezone
        func2(registry, "toTimezone", "Timestamp", "ts", "string", "tz", this::executeToTimezone);

        // utility
        func1(registry, "dayOfWeek", "Timestamp", "ts",   this::executeDayOfWeek);
        func1(registry, "fromEpoch", "int",        "millis", this::executeFromEpoch);

        // comparison
        func2(registry, "isBefore", "Timestamp", "a", "Timestamp", "b", this::executeIsBefore);
        func2(registry, "isAfter",  "Timestamp", "a", "Timestamp", "b", this::executeIsAfter);

        // formatting
        func2(registry, "timestampToString", "Timestamp", "ts", "string", "format", this::executeTimestampToString);
        func2(registry, "dateToString",      "Date",      "d",  "string", "format", this::executeDateToString);
        func2(registry, "timeToString",      "Time",      "t",  "string", "format", this::executeTimeToString);

        // struct conversions
        func1(registry, "timestampToDate",     "Timestamp", "ts", this::executeTimestampToDate);
        func1(registry, "timestampToTime",     "Timestamp", "ts", this::executeTimestampToTime);
        func1(registry, "dateToTimestamp",     "Date",      "d",  this::executeDateToTimestamp);
        func1(registry, "timeToTimestamp",     "Time",      "t",  this::executeTimeToTimestamp);
        func2(registry, "dateTimeToTimestamp", "Date",      "d",  "Time", "t", this::executeDateTimeToTimestamp);

        // parsing
        func2(registry, "toTimestamp", "string", "value", "string", "format", this::executeToTimestamp);
        func2(registry, "toDate",      "string", "value", "string", "format", this::executeToDate);
        func2(registry, "toTime",      "string", "value", "string", "format", this::executeToTime);
    }

    private void func1(Registry registry, String name,
                       String type1, String param1,
                       CompiledAction block) {
        FunctionDeclImpl f = new FunctionDeclImpl(name, true);
        f.addParameter(type1, param1);
        f.setBlock(block);
        registry.registerFunction(new FullyQualifiedName(packageName, name), f);
    }

    private void func(Registry registry, String name, CompiledAction block) {
        FunctionDeclImpl f = new FunctionDeclImpl(name, true);
        f.setBlock(block);
        registry.registerFunction(new FullyQualifiedName(packageName, name), f);
    }

    private void func2(Registry registry, String name,
                       String type1, String param1,
                       String type2, String param2,
                       CompiledAction block) {
        FunctionDeclImpl f = new FunctionDeclImpl(name, true);
        f.addParameter(type1, param1);
        f.addParameter(type2, param2);
        f.setBlock(block);
        registry.registerFunction(new FullyQualifiedName(packageName, name), f);
    }

    private void registerStructs(Registry registry) {
        StructDefinition timestampStruct = new StructDefinition("Timestamp", packageName, true);
        timestampStruct.addField("timestamp", "int", false);
        timestampStruct.addField("year", "int", false);
        timestampStruct.addField("month", "int", false);
        timestampStruct.addField("day", "int", false);
        timestampStruct.addField("hour", "int", false);
        timestampStruct.addField("minute", "int", false);
        timestampStruct.addField("second", "int", false);
        timestampStruct.addField("millisecond", "int", false);
        timestampStruct.addField("timezone", "string", false);
        registry.registerStructType(new FullyQualifiedName(packageName, "Timestamp"), timestampStruct);

        StructDefinition dateStruct = new StructDefinition("Date", packageName, true);
        dateStruct.addField("year", "int", false);
        dateStruct.addField("month", "int", false);
        dateStruct.addField("day", "int", false);
        registry.registerStructType(new FullyQualifiedName(packageName, "Date"), dateStruct);

        StructDefinition timeStruct = new StructDefinition("Time", packageName, true);
        timeStruct.addField("hour", "int", false);
        timeStruct.addField("minute", "int", false);
        timeStruct.addField("second", "int", false);
        timeStruct.addField("millisecond", "int", false);
        timeStruct.addField("timezone", "string", false);
        registry.registerStructType(new FullyQualifiedName(packageName, "Time"), timeStruct);
    }
}

