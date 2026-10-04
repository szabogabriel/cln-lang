package org.clnlang.compreg.lib;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.runtime.StructValue;

public final class CalendarLibrary implements JavaLibrary {
    private static final String PACKAGE = "std.calendar";

    @Override
    public void register(LibraryRegistry registry) {
        registry.registerStruct("Timestamp", List.of(
                field("timestamp", "int"), field("year", "int"), field("month", "int"),
                field("day", "int"), field("hour", "int"), field("minute", "int"),
                field("second", "int"), field("millisecond", "int"), field("timezone", "string")));
        registry.registerStruct("Date", List.of(field("year", "int"), field("month", "int"), field("day", "int")));
        registry.registerStruct("Time", List.of(field("hour", "int"), field("minute", "int"),
                field("second", "int"), field("millisecond", "int"), field("timezone", "string")));
        registerFormatConstants(registry);

        registerNow(registry);
        registerDeltas(registry);
        registerDifferences(registry);
        registerConversions(registry);
        registerFormatting(registry);
        registry.registerStructuredFunction(PACKAGE, "dayOfWeek", List.of("Timestamp"), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(),
                        dateTime(memory, args.get(0).getStructValue()).getDayOfWeek().getValue()));
        registry.registerStructuredFunction(PACKAGE, "fromEpoch", List.of("int"), List.of("Timestamp"),
                (memory, args, results) -> writeTimestamp(memory, results.get(0).getStructValue(),
                        Instant.ofEpochMilli(memory.getInt(args.get(0).getOffset())).atZone(ZoneId.systemDefault())));
        registry.registerStructuredFunction(PACKAGE, "toTimezone", List.of("Timestamp", "string"),
                List.of("Timestamp"), (memory, args, results) -> writeTimestamp(memory,
                        results.get(0).getStructValue(), dateTime(memory, args.get(0).getStructValue())
                                .withZoneSameInstant(ZoneId.of(memory.getStr(args.get(1).getOffset())))));
        registry.registerStructuredFunction(PACKAGE, "isBefore", List.of("Timestamp", "Timestamp"), List.of("bool"),
                (memory, args, results) -> memory.setBool(results.get(0).getOffset(),
                        epoch(memory, args.get(0)) < epoch(memory, args.get(1))));
        registry.registerStructuredFunction(PACKAGE, "isAfter", List.of("Timestamp", "Timestamp"), List.of("bool"),
                (memory, args, results) -> memory.setBool(results.get(0).getOffset(),
                        epoch(memory, args.get(0)) > epoch(memory, args.get(1))));
    }

    private StructValue.FieldLayout field(String name, String type) {
        return new StructValue.FieldLayout(name, type, false, null);
    }

    private void registerFormatConstants(LibraryRegistry registry) {
        constant(registry, "FORMAT_DATETIME", "yyyy-MM-dd HH:mm:ss");
        constant(registry, "FORMAT_DATE", "yyyy-MM-dd");
        constant(registry, "FORMAT_TIME", "HH:mm:ss");
        constant(registry, "FORMAT_DATETIME_MILLIS", "yyyy-MM-dd HH:mm:ss.SSS");
        constant(registry, "FORMAT_TIME_MILLIS", "HH:mm:ss.SSS");
        constant(registry, "FORMAT_ISO_OFFSET_DATETIME", "yyyy-MM-dd'T'HH:mm:ssXXX");
        constant(registry, "FORMAT_ISO_OFFSET_DATETIME_MILLIS", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        constant(registry, "FORMAT_ISO_DATETIME", "yyyy-MM-dd'T'HH:mm:ss");
        constant(registry, "FORMAT_ISO_DATETIME_MILLIS", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        constant(registry, "FORMAT_DDMMYYYY_SLASH", "dd/MM/yyyy");
        constant(registry, "FORMAT_DDMMYYYY_SLASH_HHMM", "dd/MM/yyyy HH:mm");
        constant(registry, "FORMAT_DDMMYYYY_SLASH_HHMMSS", "dd/MM/yyyy HH:mm:ss");
        constant(registry, "FORMAT_DDMMYYYY_DOT", "dd.MM.yyyy");
        constant(registry, "FORMAT_DDMMYYYY_DOT_HHMM", "dd.MM.yyyy HH:mm");
        constant(registry, "FORMAT_DDMMYYYY_DOT_HHMMSS", "dd.MM.yyyy HH:mm:ss");
        constant(registry, "FORMAT_DDMMYYYY_DASH", "dd-MM-yyyy");
        constant(registry, "FORMAT_DDMMYYYY_DASH_HHMM", "dd-MM-yyyy HH:mm");
        constant(registry, "FORMAT_DDMMYYYY_DASH_HHMMSS", "dd-MM-yyyy HH:mm:ss");
        constant(registry, "FORMAT_MMDDYYYY_SLASH", "MM/dd/yyyy");
        constant(registry, "FORMAT_MMDDYYYY_SLASH_HHMM", "MM/dd/yyyy HH:mm");
        constant(registry, "FORMAT_MMDDYYYY_SLASH_HHMMSS", "MM/dd/yyyy HH:mm:ss");
        constant(registry, "FORMAT_YYYYMMDD_SLASH", "yyyy/MM/dd");
        constant(registry, "FORMAT_YYYYMMDD_SLASH_HHMM", "yyyy/MM/dd HH:mm");
        constant(registry, "FORMAT_YYYYMMDD_SLASH_HHMMSS", "yyyy/MM/dd HH:mm:ss");
        constant(registry, "FORMAT_YYYYMMDD_COMPACT", "yyyyMMdd");
        constant(registry, "FORMAT_YYYYMMDDHHMMSS_COMPACT", "yyyyMMddHHmmss");
        constant(registry, "FORMAT_HHMM_COMPACT", "HHmm");
        constant(registry, "FORMAT_HHMMSS_COMPACT", "HHmmss");
        constant(registry, "FORMAT_DATE_LONG", "MMMM d, yyyy");
        constant(registry, "FORMAT_DATE_MEDIUM", "MMM d, yyyy");
        constant(registry, "FORMAT_DATETIME_LONG", "MMMM d, yyyy HH:mm:ss");
        constant(registry, "FORMAT_DATETIME_MEDIUM", "MMM d, yyyy HH:mm:ss");
        constant(registry, "FORMAT_DAY_OF_WEEK_DATE", "EEEE, MMMM d, yyyy");
        constant(registry, "FORMAT_RFC1123", "EEE, dd MMM yyyy HH:mm:ss zzz");
    }

    private void constant(LibraryRegistry registry, String name, String value) {
        registry.registerConstant(PACKAGE, name, "string", value);
    }

    private void registerNow(LibraryRegistry registry) {
        registry.registerStructuredFunction(PACKAGE, "now", List.of(), List.of("Timestamp"),
                (memory, args, results) -> writeTimestamp(memory, results.get(0).getStructValue(), ZonedDateTime.now()));
        registry.registerStructuredFunction(PACKAGE, "nowDate", List.of(), List.of("Date"),
                (memory, args, results) -> writeDate(memory, results.get(0).getStructValue(), LocalDate.now()));
        registry.registerStructuredFunction(PACKAGE, "nowTime", List.of(), List.of("Time"),
                (memory, args, results) -> {
                    ZonedDateTime value = ZonedDateTime.now();
                    writeTime(memory, results.get(0).getStructValue(), value.toLocalTime(), value.getZone());
                });
    }

    private void registerDeltas(LibraryRegistry registry) {
        delta(registry, "plusYears", (date, n) -> date.plusYears(n));
        delta(registry, "plusMonths", (date, n) -> date.plusMonths(n));
        delta(registry, "plusDays", (date, n) -> date.plusDays(n));
        delta(registry, "plusHours", (date, n) -> date.plusHours(n));
        delta(registry, "plusMinutes", (date, n) -> date.plusMinutes(n));
        delta(registry, "plusSeconds", (date, n) -> date.plusSeconds(n));
        delta(registry, "plusMilliseconds", (date, n) -> date.plus(n, ChronoUnit.MILLIS));
        delta(registry, "minusYears", (date, n) -> date.minusYears(n));
        delta(registry, "minusMonths", (date, n) -> date.minusMonths(n));
        delta(registry, "minusDays", (date, n) -> date.minusDays(n));
        delta(registry, "minusHours", (date, n) -> date.minusHours(n));
        delta(registry, "minusMinutes", (date, n) -> date.minusMinutes(n));
        delta(registry, "minusSeconds", (date, n) -> date.minusSeconds(n));
        delta(registry, "minusMilliseconds", (date, n) -> date.minus(n, ChronoUnit.MILLIS));
        delta(registry, "withYear", (date, n) -> date.withYear((int) n));
        delta(registry, "withMonth", (date, n) -> date.withMonth((int) n));
        delta(registry, "withDay", (date, n) -> date.withDayOfMonth((int) n));
        delta(registry, "withHour", (date, n) -> date.withHour((int) n));
        delta(registry, "withMinute", (date, n) -> date.withMinute((int) n));
        delta(registry, "withSecond", (date, n) -> date.withSecond((int) n));
    }

    private void delta(LibraryRegistry registry, String name, DateDelta delta) {
        registry.registerStructuredFunction(PACKAGE, name, List.of("Timestamp", "int"), List.of("Timestamp"),
                (memory, args, results) -> writeTimestamp(memory, results.get(0).getStructValue(),
                        delta.apply(dateTime(memory, args.get(0).getStructValue()),
                                memory.getInt(args.get(1).getOffset()))));
    }

    private void registerDifferences(LibraryRegistry registry) {
        difference(registry, "diffMilliseconds", 1);
        difference(registry, "diffSeconds", 1_000);
        difference(registry, "diffMinutes", 60_000);
        difference(registry, "diffHours", 3_600_000);
        difference(registry, "diffDays", 86_400_000);
    }

    private void difference(LibraryRegistry registry, String name, long divisor) {
        registry.registerStructuredFunction(PACKAGE, name, List.of("Timestamp", "Timestamp"), List.of("int"),
                (memory, args, results) -> memory.setInt(results.get(0).getOffset(),
                        (epoch(memory, args.get(1)) - epoch(memory, args.get(0))) / divisor));
    }

    private void registerConversions(LibraryRegistry registry) {
        registry.registerStructuredFunction(PACKAGE, "timestampToDate", List.of("Timestamp"), List.of("Date"),
                (memory, args, results) -> writeDate(memory, results.get(0).getStructValue(),
                        dateTime(memory, args.get(0).getStructValue()).toLocalDate()));
        registry.registerStructuredFunction(PACKAGE, "timestampToTime", List.of("Timestamp"), List.of("Time"),
                (memory, args, results) -> {
                    ZonedDateTime value = dateTime(memory, args.get(0).getStructValue());
                    writeTime(memory, results.get(0).getStructValue(), value.toLocalTime(), value.getZone());
                });
        registry.registerStructuredFunction(PACKAGE, "dateToTimestamp", List.of("Date"), List.of("Timestamp"),
                (memory, args, results) -> writeTimestamp(memory, results.get(0).getStructValue(),
                        date(memory, args.get(0).getStructValue()).atStartOfDay(ZoneId.systemDefault())));
        registry.registerStructuredFunction(PACKAGE, "timeToTimestamp", List.of("Time"), List.of("Timestamp"),
                (memory, args, results) -> {
                    StructValue value = args.get(0).getStructValue();
                    ZoneId zone = zone(memory, value);
                    writeTimestamp(memory, results.get(0).getStructValue(),
                            LocalDate.now(zone).atTime(time(memory, value)).atZone(zone));
                });
        registry.registerStructuredFunction(PACKAGE, "dateTimeToTimestamp", List.of("Date", "Time"),
                List.of("Timestamp"), (memory, args, results) -> {
                    StructValue timeValue = args.get(1).getStructValue();
                    writeTimestamp(memory, results.get(0).getStructValue(), date(memory, args.get(0).getStructValue())
                            .atTime(time(memory, timeValue)).atZone(zone(memory, timeValue)));
                });
    }

    private void registerFormatting(LibraryRegistry registry) {
        registry.registerStructuredFunction(PACKAGE, "timestampToString", List.of("Timestamp", "string"),
                List.of("string"), (memory, args, results) -> memory.setStr(results.get(0).getOffset(),
                        DateTimeFormatter.ofPattern(memory.getStr(args.get(1).getOffset()))
                                .format(dateTime(memory, args.get(0).getStructValue()))));
        registry.registerStructuredFunction(PACKAGE, "dateToString", List.of("Date", "string"), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(), DateTimeFormatter
                        .ofPattern(memory.getStr(args.get(1).getOffset()))
                        .format(date(memory, args.get(0).getStructValue()))));
        registry.registerStructuredFunction(PACKAGE, "timeToString", List.of("Time", "string"), List.of("string"),
                (memory, args, results) -> memory.setStr(results.get(0).getOffset(), DateTimeFormatter
                        .ofPattern(memory.getStr(args.get(1).getOffset()))
                        .format(time(memory, args.get(0).getStructValue()))));
        registry.registerStructuredFunction(PACKAGE, "toTimestamp", List.of("string", "string"),
                List.of("Timestamp"), (memory, args, results) -> writeTimestamp(memory,
                        results.get(0).getStructValue(), parseTimestamp(memory.getStr(args.get(0).getOffset()),
                                memory.getStr(args.get(1).getOffset()))));
        registry.registerStructuredFunction(PACKAGE, "toDate", List.of("string", "string"), List.of("Date"),
                (memory, args, results) -> writeDate(memory, results.get(0).getStructValue(), LocalDate.parse(
                        memory.getStr(args.get(0).getOffset()),
                        DateTimeFormatter.ofPattern(memory.getStr(args.get(1).getOffset())))));
        registry.registerStructuredFunction(PACKAGE, "toTime", List.of("string", "string"), List.of("Time"),
                (memory, args, results) -> writeTime(memory, results.get(0).getStructValue(), LocalTime.parse(
                        memory.getStr(args.get(0).getOffset()),
                        DateTimeFormatter.ofPattern(memory.getStr(args.get(1).getOffset()))), ZoneId.systemDefault()));
    }

    private ZonedDateTime parseTimestamp(String value, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            return ZonedDateTime.parse(value, formatter);
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault());
            } catch (DateTimeParseException ignoredAgain) {
                return LocalDate.parse(value, formatter).atStartOfDay(ZoneId.systemDefault());
            }
        }
    }

    private ZonedDateTime dateTime(Memory memory, StructValue value) {
        long millis = intField(memory, value, "timestamp");
        return Instant.ofEpochMilli(millis).atZone(zone(memory, value));
    }

    private long epoch(Memory memory, Call.Register argument) {
        return intField(memory, argument.getStructValue(), "timestamp");
    }

    private LocalDate date(Memory memory, StructValue value) {
        return LocalDate.of((int) intField(memory, value, "year"), (int) intField(memory, value, "month"),
                (int) intField(memory, value, "day"));
    }

    private LocalTime time(Memory memory, StructValue value) {
        return LocalTime.of((int) intField(memory, value, "hour"), (int) intField(memory, value, "minute"),
                (int) intField(memory, value, "second"), (int) intField(memory, value, "millisecond") * 1_000_000);
    }

    private ZoneId zone(Memory memory, StructValue value) {
        String timezone = stringField(memory, value, "timezone");
        return timezone == null || timezone.isEmpty() ? ZoneId.systemDefault() : ZoneId.of(timezone);
    }

    private long intField(Memory memory, StructValue value, String name) {
        return requireField(value, name).getInt(memory);
    }

    private String stringField(Memory memory, StructValue value, String name) {
        return requireField(value, name).getString(memory);
    }

    private StructValue.FieldSlot requireField(StructValue value, String name) {
        StructValue.FieldSlot field = value.getField(name);
        if (field == null) {
            throw new IllegalArgumentException("Struct " + value.getTypeName() + " has no field '" + name + "'.");
        }
        return field;
    }

    private void writeTimestamp(Memory memory, StructValue target, ZonedDateTime value) {
        long millis = value.toInstant().toEpochMilli();
        setInt(memory, target, "timestamp", millis);
        setInt(memory, target, "year", value.getYear());
        setInt(memory, target, "month", value.getMonthValue());
        setInt(memory, target, "day", value.getDayOfMonth());
        setInt(memory, target, "hour", value.getHour());
        setInt(memory, target, "minute", value.getMinute());
        setInt(memory, target, "second", value.getSecond());
        setInt(memory, target, "millisecond", Math.floorMod(millis, 1000));
        setString(memory, target, "timezone", value.getZone().getId());
    }

    private void writeDate(Memory memory, StructValue target, LocalDate value) {
        setInt(memory, target, "year", value.getYear());
        setInt(memory, target, "month", value.getMonthValue());
        setInt(memory, target, "day", value.getDayOfMonth());
    }

    private void writeTime(Memory memory, StructValue target, LocalTime value, ZoneId zone) {
        setInt(memory, target, "hour", value.getHour());
        setInt(memory, target, "minute", value.getMinute());
        setInt(memory, target, "second", value.getSecond());
        setInt(memory, target, "millisecond", value.getNano() / 1_000_000);
        setString(memory, target, "timezone", zone.getId());
    }

    private void setInt(Memory memory, StructValue target, String name, long value) {
        requireField(target, name).setInt(memory, value);
    }

    private void setString(Memory memory, StructValue target, String name, String value) {
        requireField(target, name).setString(memory, value);
    }

    @FunctionalInterface
    private interface DateDelta { ZonedDateTime apply(ZonedDateTime value, long amount); }
}