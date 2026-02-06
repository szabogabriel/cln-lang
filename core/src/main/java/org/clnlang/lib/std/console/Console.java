package org.clnlang.lib.std.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.lib.ClnFunction;
import org.clnlang.runtime.context.ExecutionContext;
import org.clnlang.runtime.execution.Registry;
import org.clnlang.runtime.types.FullyQualifiedName;

public class Console implements ClnFunction{

    private final String packageName = "std.console";

    private void executeWrite(ExecutionContext context) {
        String message = (String) context.getLocalContext().getValue("message");
        write(message);
    }

    private void executeWriteLine(ExecutionContext context) {
        String message = (String) context.getLocalContext().getValue("message");
        writeLine(message);
    }

    private void executeReadLine(ExecutionContext context) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String input = in.readLine();
            List<Object> retValues = new ArrayList<>();
            retValues.add(input);
            context.setReturnValues(retValues);
        } catch (Exception e) {
            throw new RuntimeException("Error reading line from console", e);
        }
    }   

    public static void write(String message) {
        System.out.print(message);
    }

    public static void writeLine(String message) {
        System.out.println(message);
    }

    @Override
    public void register(Registry registry) {
        FunctionDeclImpl writeFunc = new FunctionDeclImpl("write", true);
        writeFunc.addParameter("String", "message");
        writeFunc.setBlock(this::executeWrite);
        registry.registerFunction(new FullyQualifiedName(packageName, "write"), writeFunc);
        
        FunctionDeclImpl writeLineFunc = new FunctionDeclImpl("writeLine", true);
        writeLineFunc.addParameter("String", "message");
        writeLineFunc.setBlock(this::executeWriteLine);
        registry.registerFunction(new FullyQualifiedName(packageName, "writeLine"), writeLineFunc);

        FunctionDeclImpl readLineFunc = new FunctionDeclImpl("readLine", true);
        readLineFunc.setBlock(this::executeReadLine);
        registry.registerFunction(new FullyQualifiedName(packageName, "readLine"), readLineFunc);
    }
    
}
