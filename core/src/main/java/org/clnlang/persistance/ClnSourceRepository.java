package org.clnlang.persistance;

import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction for the {@code CLN_SOURCE} table.
 *
 * <p>Integrating applications can implement this interface using any persistence
 * technology (JPA, jOOQ, MyBatis, plain JDBC, …).  A default plain-JDBC
 * implementation is provided by {@link JdbcClnSourceRepository}.
 *
 * <p>All operations that modify state ({@link #save}, {@link #deleteByPackageName})
 * throw {@link java.sql.SQLException} wrapped in a {@link RuntimeException} rather
 * than declaring a checked exception, so they can be used without try/catch in
 * frameworks that prefer unchecked persistence exceptions.  Implementations may
 * choose to declare their own checked exceptions.
 */
public interface ClnSourceRepository {

    /**
     * Persists a {@link ClnSource} record.
     *
     * <ul>
     *   <li>If {@code source.getId()} is {@code null} a new row is inserted and the
     *       returned record carries the generated id.</li>
     *   <li>If {@code source.getId()} is non-null the existing row is updated.</li>
     * </ul>
     *
     * @param source the record to persist; must not be {@code null}
     * @return the persisted record (with id and timestamps populated)
     */
    ClnSource save(ClnSource source);

    /**
     * Returns all source records ordered by {@code id} ascending.
     *
     * @return a (possibly empty) immutable-view list
     */
    List<ClnSource> findAll();

    /**
     * Finds the source record for the given package name.
     *
     * @param packageName CLN package name (e.g. {@code "com.example.myapp"})
     * @return the record, or {@link Optional#empty()} if not found
     */
    Optional<ClnSource> findByPackageName(String packageName);

    /**
     * Deletes the source record for the given package name.
     *
     * @param packageName CLN package name to delete
     * @return {@code true} if a row was deleted, {@code false} if none existed
     */
    boolean deleteByPackageName(String packageName);

    /**
     * Returns the total number of source records in the table.
     */
    long count();
}
