package org.clnlang.persistance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.ast.visitor.CompilerVisitor;
import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.compile.declaration.ProgramImpl;
import org.clnlang.exception.ClnException;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;
import org.clnlang.runtime.execution.Registry;

public class FileSystemLoader implements ClnLoader {
    private final List<String> clnPaths;
    private final List<String> sourceArgs;
    private final boolean verbose;

    public FileSystemLoader(List<String> clnPaths, List<String> sourceArgs, boolean verbose) {
        this.clnPaths = clnPaths;
        this.sourceArgs = sourceArgs;
        this.verbose = verbose;
    }

    @Override
    public int loadSources(Registry registry) throws Exception {
        log("Loading all .cln files from source paths...");
        int totalFiles = 0;
        
        for (String clnPath : clnPaths) {
            File rootDir = new File(clnPath);
            if (!rootDir.exists() || !rootDir.isDirectory()) {
                log("Warning: Source path does not exist or is not a directory: " + clnPath);
                continue;
            }
            
            int filesLoaded = loadClnFilesRecursively(rootDir, registry);
            totalFiles += filesLoaded;
            log("Loaded " + filesLoaded + " file(s) from " + clnPath);
        }
        
        log("Total files loaded: " + totalFiles);
        return totalFiles;
    }

    /**
     * Recursively load all .cln files from a directory.
     */
    private int loadClnFilesRecursively(File directory, Registry registry) throws Exception {
        int count = 0;
        File[] files = directory.listFiles();
        
        if (files == null) {
            return 0;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                count += loadClnFilesRecursively(file, registry);
            } else if (isClnFile(file.getName())) {
                try {
                    ProgramImpl program = compileFile(file, registry);
                    String declaredPackage = "default";
                    
                    if (program.getPackageDecl() != null && 
                        program.getPackageDecl().getPackageName() != null) {
                        declaredPackage = program.getPackageDecl().getPackageName();
                    }
                    
                    // Use canonical path for consistent lookups
                    String sourcePath;
                    try {
                        sourcePath = file.getCanonicalPath();
                    } catch (java.io.IOException e) {
                        sourcePath = file.getAbsolutePath();
                    }
                    
                    registry.addProgram(sourcePath, program, declaredPackage);
                    
                    // Also register all functions, types, and variables from this program
                    registerProgramSymbols(program, declaredPackage, registry);
                    
                    count++;
                    log("  Loading: " + file.getName() + " (package=" + declaredPackage + ")");
                } catch (Exception e) {
                    log("Warning: Failed to compile " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        
        return count;
    }

    /**
     * Compile a single .cln file into a ProgramImpl.
     */
    private ProgramImpl compileFile(File file, Registry registry) throws Exception {
        CharStream input = CharStreams.fromFileName(file.getAbsolutePath());
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        clnParser.ProgramContext programContext = parser.program();
        
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new ClnException("Parsing failed for " + file.getName() + 
                " with " + parser.getNumberOfSyntaxErrors() + " errors.");
        }
        
        CompilerVisitor compiler = new CompilerVisitor();
        registry.getAllStructTypes().keySet()
            .forEach(fqn -> compiler.addExternalTypes(java.util.List.of(fqn.getEntityName())));
        registry.getAllUnionTypes().keySet()
            .forEach(fqn -> compiler.addExternalTypes(java.util.List.of(fqn.getEntityName())));
        return compiler.compileProgram(programContext);
    }

    /**
     * Register all symbols (functions, types, variables) from a program into the registry.
     * This makes them available for import resolution.
     */
    private void registerProgramSymbols(ProgramImpl program, String packageName, Registry registry) {
        for (var decl : program.getDeclarations()) {
            if (decl instanceof FunctionDeclImpl) {
                FunctionDeclImpl funcDecl = (FunctionDeclImpl) decl;
                funcDecl.setPackageName(packageName);
                registry.registerFunction(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, funcDecl.getName()),
                    funcDecl
                );
            } else if (decl instanceof org.clnlang.compile.declaration.StructDeclImpl) {
                org.clnlang.compile.declaration.StructDeclImpl structDecl = 
                    (org.clnlang.compile.declaration.StructDeclImpl) decl;
                registry.registerStructType(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, structDecl.getName()),
                    structDecl.toStructDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.compile.declaration.UnionDeclImpl) {
                org.clnlang.compile.declaration.UnionDeclImpl unionDecl = 
                    (org.clnlang.compile.declaration.UnionDeclImpl) decl;
                registry.registerUnionType(
                    new org.clnlang.runtime.types.FullyQualifiedName(packageName, unionDecl.getName()),
                    unionDecl.toUnionDefinition(packageName)
                );
            } else if (decl instanceof org.clnlang.compile.declaration.GlobalVarDeclImpl) {
                org.clnlang.compile.declaration.GlobalVarDeclImpl varDecl = 
                    (org.clnlang.compile.declaration.GlobalVarDeclImpl) decl;
                varDecl.setPackageName(packageName);
                if (varDecl.isMutable()) {
                    registry.registerGlobalVariable(
                        new org.clnlang.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                } else {
                    registry.registerGlobalConstant(
                        new org.clnlang.runtime.types.FullyQualifiedName(packageName, varDecl.getName()),
                        varDecl
                    );
                }
            }
        }
    }

    @Override
    public StartupMode getSupportedStartupMode() {
        List<ClnSourceFile> sourceFiles = getSourceFiles();
        
        if (sourceFiles.isEmpty()) {
            throw new RuntimeException("No source files or package specified");
        }
        
        // Check if first source is a file or package
        ClnSourceFile first = sourceFiles.get(0);
        
        if (first.isSourceFile()) {
            return StartupMode.FILES;
        } else {
            return StartupMode.PACKAGE;
        }
    }

    @Override
    public List<ClnSourceFile> getSourceFiles() {
        List<ClnSourceFile> result = new ArrayList<>();
        for (String sourceArg : sourceArgs) {
            // Check if this contains multiple files separated by path separator
            if (sourceArg.contains(File.pathSeparator)) {
                String[] files = sourceArg.split(File.pathSeparator);
                for (String file : files) {
                    String trimmed = file.trim();
                    if (!trimmed.isEmpty()) {
                        result.add(createSourceFile(trimmed));
                    }
                }
            } else {
                // Single file or package definition
                result.add(createSourceFile(sourceArg));
            }
        }
        return result;
    }
    
    /**
     * Create a ClnSourceFile from a string path or package name.
     * Determines whether it's a file or package based on the .cln extension.
     */
    private ClnSourceFile createSourceFile(String source) {
        if (isClnFile(source)) {
            File file = new File(source);
            try {
                String canonicalPath = file.getCanonicalPath();
                return ClnSourceFile.fromFilePath(canonicalPath, file.getName());
            } catch (java.io.IOException e) {
                // Fallback to absolute path
                return ClnSourceFile.fromFilePath(file.getAbsolutePath(), file.getName());
            }
        } else {
            // It's a package name
            return ClnSourceFile.fromPackage(source);
        }
    }
    
    /**
     * Check if a path represents a CLN source file.
     * Package-private to allow access from other loader utilities.
     */
    boolean isClnFile(String path) {
        return path != null && path.endsWith(".cln");
    }

    private void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
    
}
