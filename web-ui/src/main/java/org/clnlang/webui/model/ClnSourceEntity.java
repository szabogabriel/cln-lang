package org.clnlang.webui.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * JPA entity that maps to the CLN_SOURCE table.
 * Mirrors the schema used by {@code JdbcLoader} in the core module.
 */
@Entity
@Table(name = "CLN_SOURCE")
public class ClnSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package", nullable = false, length = 512)
    private String packageName;

    @Lob
    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "CREATEDAT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATEDAT", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "version", nullable = false)
    private int version = 1;

    protected ClnSourceEntity() {
    }

    public ClnSourceEntity(String packageName, String source) {
        this.packageName = packageName;
        this.source = source;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 1;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    // Getters and setters

    public Long getId() { return id; }

    public String getPackageName() { return packageName; }

    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public int getVersion() { return version; }
}
