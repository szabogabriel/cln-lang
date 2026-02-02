package org.clnlang.runtime.lib.std.io;

import org.clnlang.compile.declaration.FunctionDeclImpl;
import org.clnlang.runtime.ExecutionContext;
import org.clnlang.runtime.FullyQualifiedName;
import org.clnlang.runtime.Registry;
import org.clnlang.runtime.lib.ClnFunction;

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
    }
    
}
