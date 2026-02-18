package org.clnlang.compiled;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.clnlang.RuntimeConfiguration;
import org.clnlang.ast.visitor.compiled.register.GlobalMemberRegistratorVisitor;
import org.clnlang.compiled.library.NativeLibraryManager;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.exception.ClnException;
import org.clnlang.parser.clnLexer;
import org.clnlang.parser.clnParser;

public class ClnOrchestrator {

    private RuntimeConfiguration config;

    //Package private for testing purposes
    GlobalRegistry globalRegistry;

    NativeLibraryManager nativeLibraryManager;

    List<File> sourceFilesToLoad = new ArrayList<>();

    public ClnOrchestrator(RuntimeConfiguration config) {
        this.config = config;
        this.globalRegistry = new GlobalRegistry();
        this.nativeLibraryManager = new NativeLibraryManager();
    }

    public ClnOrchestrator(RuntimeConfiguration config, GlobalRegistry globalRegistry,
            NativeLibraryManager nativeLibraryManager) {
        this.config = config;
        this.globalRegistry = globalRegistry;
        this.nativeLibraryManager = nativeLibraryManager;
    }

    public void orchestrate() throws IOException {
        registerNativeLibs();
        loadTargetClnFiles();
        registerRuntimeLibs();
        compileRuntimeLibs();
        executeMainFunction();
    }

    private void registerNativeLibs() {
        nativeLibraryManager.registerNativeLibs(globalRegistry);
    }

    private void loadTargetClnFiles() throws IOException {
        if (config.isSourceFileExecution()) {
            sourceFilesToLoad = config.getSourceFilesAsFiles();
        } else {
            sourceFilesToLoad = config.loadAllSourceFilesAsFiles();
        }
    }

    private void registerRuntimeLibs() {
        for (File sourceFile : sourceFilesToLoad) {
            try {
                registerFile(sourceFile);
            } catch (Exception e) {
                throw new ClnException("Failed to register file: " + sourceFile.getName() + " due to: " + e.getMessage());
            }
        }
    }

    private void compileRuntimeLibs() {

    }

    private void executeMainFunction() {

    }

    private void registerFile(File file) throws Exception {
        CharStream input = CharStreams.fromFileName(file.getAbsolutePath());
        clnLexer lexer = new clnLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        clnParser parser = new clnParser(tokens);
        clnParser.ProgramContext programContext = parser.program();
        
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new ClnException("Parsing failed for " + file.getName() + 
                " with " + parser.getNumberOfSyntaxErrors() + " errors.");
        }
        
        GlobalMemberRegistratorVisitor compiler = new GlobalMemberRegistratorVisitor(globalRegistry);
        compiler.compileProgram(programContext, file);
    }

}
