package org.clnlang.persistance;

import java.util.Optional;

/**
 * Known JDBC database vendors, used to select the correct DDL dialect when
 * auto-creating the {@code CLN_SOURCE} table.
 */
public enum DbVendors {

    H2         ("jdbc:h2:"),
    POSTGRESQL ("jdbc:postgresql:"),
    MYSQL      ("jdbc:mysql:"),
    MARIADB    ("jdbc:mariadb:"),
    SQLSERVER  ("jdbc:sqlserver:"),
    ORACLE     ("jdbc:oracle:"),
    DB2        ("jdbc:db2:"),
    SQLITE     ("jdbc:sqlite:");

    private final String jdbcPrefix;

    DbVendors(String jdbcPrefix) {
        this.jdbcPrefix = jdbcPrefix;
    }

    public String getJdbcPrefix() {
        return jdbcPrefix;
    }

    /**
     * Derives the vendor from a JDBC URL by matching the well-known URL prefix.
     *
     * @param jdbcUrl the full JDBC URL (e.g. {@code jdbc:postgresql://host/db})
     * @return the matching vendor, or {@link Optional#empty()} for unknown URLs
     */
    public static Optional<DbVendors> fromJdbc(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return Optional.empty();
        }
        String lower = jdbcUrl.toLowerCase();
        for (DbVendors vendor : values()) {
            if (lower.startsWith(vendor.jdbcPrefix)) {
                return Optional.of(vendor);
            }
        }
        return Optional.empty();
    }
}
