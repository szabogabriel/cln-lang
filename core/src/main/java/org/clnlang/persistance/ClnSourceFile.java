package org.clnlang.persistance;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.clnlang.exception.ClnException;

/**
 * Represents a CLN source file or package in an abstract way.
 * This class abstracts the storage mechanism (filesystem, memory, database, etc.)
 * and provides a uniform interface for accessing source content.
 */
public class ClnSourceFile {
    private final String path;
    private final String name;
    private final SourceType type;
    private final String content; // For in-memory content (null if lazy-loaded)
    
    public enum SourceType {
        FILE,       // A .cln source file
        PACKAGE     // A package name
    }
    
    /**
     * Private constructor. Use factory methods to create instances.
     * 
     * @param path The canonical/absolute path to the source
     * @param name The display name (e.g., filename or package name)
     * @param type The type of source (file or package)
     * @param content The in-memory content (null for lazy-loaded sources)
     */
    private ClnSourceFile(String path, String name, SourceType type, String content) {
        this.path = path;
        this.name = name;
        this.type = type;
        this.content = content;
    }
    
    /**
     * Create a ClnSourceFile representing a file with a filesystem path.
     * Content will be read from the filesystem when getInputStream() is called.
     * 
     * @param path The canonical/absolute path to the source file
     * @param name The display name (filename)
     * @return A new ClnSourceFile instance
     */
    public static ClnSourceFile fromFilePath(String path, String name) {
        return new ClnSourceFile(path, name, SourceType.FILE, null);
    }
    
    /**
     * Create a ClnSourceFile representing a file with in-memory content.
     * 
     * @param name The display name (filename)
     * @param content The source code content
     * @return A new ClnSourceFile instance
     */
    public static ClnSourceFile fromContent(String name, String content) {
        return new ClnSourceFile(name, name, SourceType.FILE, content);
    }
    
    /**
     * Create a ClnSourceFile representing a package.
     * 
     * @param packageName The package name
     * @return A new ClnSourceFile instance
     */
    public static ClnSourceFile fromPackage(String packageName) {
        return new ClnSourceFile(packageName, packageName, SourceType.PACKAGE, null);
    }
    
    /**
     * Get the unique path identifier for this source.
     * For files, this is the canonical file path.
     * For packages, this is the package name.
     * 
     * @return The path identifier
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Get the display name of this source.
     * For files, this is the filename.
     * For packages, this is the package name.
     * 
     * @return The display name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Check if this source represents a .cln source file.
     * 
     * @return true if this is a source file, false if it's a package
     */
    public boolean isSourceFile() {
        return type == SourceType.FILE;
    }
    
    /**
     * Check if this source represents a package.
     * 
     * @return true if this is a package, false if it's a file
     */
    public boolean isPackage() {
        return type == SourceType.PACKAGE;
    }
    
    /**
     * Get the source type.
     * 
     * @return The source type
     */
    public SourceType getType() {
        return type;
    }

    /**
     * Get an InputStream for reading the source content.
     * The storage mechanism is abstracted - could be filesystem, memory, database, etc.
     * 
     * @return An InputStream for the source content
     * @throws ClnException If called on a package (packages don't have content to stream)
     * @throws IOException If there's an error reading from the storage backend
     */
    public InputStream getInputStream() throws ClnException, IOException {
        if (type == SourceType.PACKAGE) {
            throw new ClnException("Cannot get input stream for a package: " + name);
        }
        
        if (content != null) {
            // In-memory content - return a stream from the string
            return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        } else {
            // Filesystem-based content - read from file
            return new FileInputStream(path);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClnSourceFile that = (ClnSourceFile) o;
        return path.equals(that.path);
    }
    
    @Override
    public int hashCode() {
        return path.hashCode();
    }
    
    @Override
    public String toString() {
        return type == SourceType.FILE ? "file:" + name : "package:" + name;
    }
}
