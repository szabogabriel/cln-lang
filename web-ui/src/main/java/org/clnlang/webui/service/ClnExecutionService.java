package org.clnlang.webui.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.clnlang.ClnDbMain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Executes CLN programs loaded from the H2 database and captures their output.
 */
@Service
public class ClnExecutionService {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username:sa}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    // Guard against concurrent executions swapping System.out under each other
    private final Object executionLock = new Object();

    /**
     * Executes the CLN program identified by {@code packageName}.
     * All standard output produced by the program is captured and returned.
     *
     * @param packageName the entry-point package name
     * @param verbose     whether to include verbose diagnostic messages
     * @return captured output lines (stdout + diagnostics)
     */
    public ExecutionResult execute(String packageName, boolean verbose) {
        StringBuilder output = new StringBuilder();

        // Redirect System.out so CLN's writeLine() calls are captured.
        // Synchronized to prevent concurrent executions from corrupting each other's streams.
        int exitCode;
        synchronized (executionLock) {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        ByteArrayOutputStream capturedErr = new ByteArrayOutputStream();

        PrintStream capturingOut = new PrintStream(capturedOut, true, StandardCharsets.UTF_8);
        PrintStream capturingErr = new PrintStream(capturedErr, true, StandardCharsets.UTF_8);

        try {
            System.setOut(capturingOut);
            System.setErr(capturingErr);

            // Embed credentials in the URL so JdbcLoader's DriverManager.getConnection(url)
            // authenticates as the same user Spring Boot uses (default: sa / empty password).
            String urlWithCreds = jdbcUrl + ";USER=" + dbUsername + ";PASSWORD=" + dbPassword;
            ClnDbMain runner = new ClnDbMain(
                    urlWithCreds,
                    null,              // use default org.h2.Driver
                    List.of(packageName),
                    verbose,
                    msg -> output.append("[DBG] ").append(msg).append("\n")
            );

            exitCode = runner.execute();

        } catch (Exception e) {
            exitCode = -1;
            output.append("[ERROR] ").append(e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                output.append("\n  caused by: ").append(cause.getMessage());
                cause = cause.getCause();
            }
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String stdout = capturedOut.toString(StandardCharsets.UTF_8);
        String stderr = capturedErr.toString(StandardCharsets.UTF_8);
        String diagnostics = output.toString();

        StringBuilder combined = new StringBuilder();
        if (!stdout.isBlank()) {
            combined.append(stdout);
        }
        if (!stderr.isBlank() && exitCode != 0) {
            combined.append("[STDERR]\n").append(stderr);
        }
        // Always show errors; show all diagnostics when verbose
        if (!diagnostics.isBlank() && (verbose || exitCode != 0)) {
            combined.append(diagnostics);
        }

        return new ExecutionResult(exitCode, combined.toString());
        } // end synchronized
    }

    /** Simple value holder for execution results. */
    public record ExecutionResult(int exitCode, String output) {}
}
