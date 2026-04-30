package org.clnlang.persistance;

import java.time.LocalDateTime;

/**
 * POJO representing one row in the {@code CLN_SOURCE} table.
 *
 * <p>This class is intentionally dependency-free so it can be used directly
 * by integrating applications regardless of their persistence framework.
 * Framework-specific annotations (JPA {@code @Entity}, Jackson {@code @JsonProperty},
 * etc.) can be added in the consuming project by subclassing or by wrapping.
 *
 * <p>Instances are created via the static factory methods:
 * <ul>
 *   <li>{@link #of(String, String)} — for new records (id / timestamps unset)</li>
 *   <li>{@link #builder()} — when all fields need to be populated (e.g. when
 *       mapping a {@link java.sql.ResultSet})</li>
 * </ul>
 */
public final class ClnSource {

    private final Long          id;
    private final String        packageName;
    private final String        source;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final int           version;

    private ClnSource(Builder b) {
        this.id          = b.id;
        this.packageName = b.packageName;
        this.source      = b.source;
        this.createdAt   = b.createdAt;
        this.updatedAt   = b.updatedAt;
        this.version     = b.version;
    }

    // -------------------------------------------------------------------------
    // Factory helpers
    // -------------------------------------------------------------------------

    /**
     * Creates a new (unsaved) source record with only the mandatory fields set.
     * {@code id}, {@code createdAt}, and {@code updatedAt} are {@code null};
     * {@code version} defaults to {@code 1}.
     *
     * @param packageName the CLN package name (e.g. {@code "com.example.myapp"})
     * @param source      the CLN source code
     */
    public static ClnSource of(String packageName, String source) {
        return builder()
                .packageName(packageName)
                .source(source)
                .version(1)
                .build();
    }

    /** Returns a new {@link Builder}. */
    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    /** Database-generated primary key; {@code null} for unsaved records. */
    public Long getId() { return id; }

    /** CLN package name stored in this row (maps to the {@code package} column). */
    public String getPackageName() { return packageName; }

    /** Full CLN source code. */
    public String getSource() { return source; }

    /** Timestamp of first insertion; {@code null} if not yet persisted. */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /** Timestamp of last update; {@code null} if not yet persisted. */
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /** Optimistic-lock version counter; starts at {@code 1}. */
    public int getVersion() { return version; }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    public static final class Builder {

        private Long          id;
        private String        packageName;
        private String        source;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int           version = 1;

        private Builder() {}

        public Builder id(Long id)                      { this.id = id;                   return this; }
        public Builder packageName(String packageName)  { this.packageName = packageName; return this; }
        public Builder source(String source)            { this.source = source;           return this; }
        public Builder createdAt(LocalDateTime t)       { this.createdAt = t;             return this; }
        public Builder updatedAt(LocalDateTime t)       { this.updatedAt = t;             return this; }
        public Builder version(int version)             { this.version = version;         return this; }

        public ClnSource build() {
            if (packageName == null || packageName.isBlank()) {
                throw new IllegalStateException("packageName must not be null or blank");
            }
            if (source == null) {
                throw new IllegalStateException("source must not be null");
            }
            return new ClnSource(this);
        }
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ClnSource{id=" + id +
               ", packageName='" + packageName + '\'' +
               ", version=" + version +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt + '}';
    }
}
