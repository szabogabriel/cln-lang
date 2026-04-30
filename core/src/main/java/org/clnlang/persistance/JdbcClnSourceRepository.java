package org.clnlang.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 * Plain-JDBC implementation of {@link ClnSourceRepository}.
 *
 * <p>Requires only a standard {@link DataSource} — no third-party libraries.
 * Connections are borrowed and returned for every operation; connection pooling
 * is the responsibility of the supplied {@code DataSource}.
 *
 * <p>The {@code CLN_SOURCE} table is created automatically on first use via
 * {@link JdbcLoader}'s vendor-aware DDL.  If you manage the schema yourself
 * (e.g. via Flyway / Liquibase), simply never call {@link #ensureSchema()}, or
 * construct the repository with a {@code DataSource} backed by an already-migrated
 * database.
 *
 * <pre>{@code
 * // Minimal setup with H2
 * JdbcDataSource ds = new JdbcDataSource();
 * ds.setURL("jdbc:h2:./mydb");
 * ClnSourceRepository repo = new JdbcClnSourceRepository(ds, "jdbc:h2:./mydb");
 * repo.save(ClnSource.of("com.example.myapp", clnSourceCode));
 * }</pre>
 */
public class JdbcClnSourceRepository implements ClnSourceRepository {

    private static final String INSERT_SQL =
            "INSERT INTO CLN_SOURCE (package, source, createdAt, updatedAt, version) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE CLN_SOURCE SET package = ?, source = ?, updatedAt = ?, version = ? " +
            "WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, package, source, createdAt, updatedAt, version " +
            "FROM CLN_SOURCE ORDER BY id";

    private static final String SELECT_BY_PACKAGE_SQL =
            "SELECT id, package, source, createdAt, updatedAt, version " +
            "FROM CLN_SOURCE WHERE package = ?";

    private static final String DELETE_BY_PACKAGE_SQL =
            "DELETE FROM CLN_SOURCE WHERE package = ?";

    private static final String COUNT_SQL =
            "SELECT COUNT(*) FROM CLN_SOURCE";

    private final DataSource dataSource;
    private final String     jdbcUrl;

    /**
     * Creates a new repository.
     *
     * @param dataSource the connection pool / data source to use
     * @param jdbcUrl    the JDBC URL corresponding to {@code dataSource}; used only
     *                   to determine the correct DDL dialect in {@link #ensureSchema()}
     */
    public JdbcClnSourceRepository(DataSource dataSource, String jdbcUrl) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource must not be null");
        }
        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            throw new IllegalArgumentException("jdbcUrl must not be null or blank");
        }
        this.dataSource = dataSource;
        this.jdbcUrl    = jdbcUrl.trim();
    }

    // -------------------------------------------------------------------------
    // Schema management
    // -------------------------------------------------------------------------

    /**
     * Creates the {@code CLN_SOURCE} table if it does not already exist.
     * Uses the vendor-specific DDL from {@link JdbcLoader}.
     *
     * <p>This is called automatically by {@link JdbcLoader} on startup.
     * Call it explicitly only when using this repository independently.
     *
     * @throws RuntimeException wrapping any {@link SQLException}
     */
    public void ensureSchema() {
        try (Connection conn = dataSource.getConnection()) {
            DbVendors vendor = DbVendors.fromJdbc(jdbcUrl).orElse(DbVendors.H2);
            String ddl = JdbcLoader.getDdlForVendor(vendor);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(ddl);
            } catch (SQLException e) {
                // Oracle ORA-00955: table already exists — tolerate it
                if (e.getErrorCode() != 955) {
                    throw e;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to ensure CLN_SOURCE schema", e);
        }
    }

    // -------------------------------------------------------------------------
    // ClnSourceRepository
    // -------------------------------------------------------------------------

    @Override
    public ClnSource save(ClnSource source) {
        if (source == null) {
            throw new IllegalArgumentException("source must not be null");
        }
        try {
            if (source.getId() == null) {
                return insert(source);
            } else {
                return update(source);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save ClnSource for package '" +
                    source.getPackageName() + "'", e);
        }
    }

    @Override
    public List<ClnSource> findAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<ClnSource> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return Collections.unmodifiableList(result);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load all CLN sources", e);
        }
    }

    @Override
    public Optional<ClnSource> findByPackageName(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            return Optional.empty();
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PACKAGE_SQL)) {

            ps.setString(1, packageName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find CLN source for package '" + packageName + "'", e);
        }
    }

    @Override
    public boolean deleteByPackageName(String packageName) {
        if (packageName == null || packageName.isBlank()) {
            return false;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_PACKAGE_SQL)) {

            ps.setString(1, packageName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete CLN source for package '" + packageName + "'", e);
        }
    }

    @Override
    public long count() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_SQL);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count CLN sources", e);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private ClnSource insert(ClnSource source) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, source.getPackageName());
            ps.setString(2, source.getSource());
            ps.setTimestamp(3, Timestamp.valueOf(now));
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setInt(5, source.getVersion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                Long generatedId = keys.next() ? keys.getLong(1) : null;
                return ClnSource.builder()
                        .id(generatedId)
                        .packageName(source.getPackageName())
                        .source(source.getSource())
                        .createdAt(now)
                        .updatedAt(now)
                        .version(source.getVersion())
                        .build();
            }
        }
    }

    private ClnSource update(ClnSource source) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, source.getPackageName());
            ps.setString(2, source.getSource());
            ps.setTimestamp(3, Timestamp.valueOf(now));
            ps.setInt(4, source.getVersion());
            ps.setLong(5, source.getId());
            ps.executeUpdate();

            return ClnSource.builder()
                    .id(source.getId())
                    .packageName(source.getPackageName())
                    .source(source.getSource())
                    .createdAt(source.getCreatedAt())
                    .updatedAt(now)
                    .version(source.getVersion())
                    .build();
        }
    }

    private static ClnSource mapRow(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("createdAt");
        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        return ClnSource.builder()
                .id(rs.getLong("id"))
                .packageName(rs.getString("package"))
                .source(rs.getString("source"))
                .createdAt(createdAt != null ? createdAt.toLocalDateTime() : null)
                .updatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null)
                .version(rs.getInt("version"))
                .build();
    }
}
