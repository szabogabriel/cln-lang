package org.clnlang.compreg;

import java.util.HashMap;
import java.util.Map;

import org.clnlang.compreg.commands.Command;

public class GlobalRegister {

    private final Map<ObjectDescriptor, Command> structs = new HashMap<>();
    private final Map<ObjectDescriptor, Command> unions = new HashMap<>();
    private final Map<ObjectDescriptor, Command> vars = new HashMap<>();
    private final Map<ObjectDescriptor, Command> functions = new HashMap<>();

    public void register(ObjectDescriptor descriptor, Command command) {
        switch (descriptor.getType()) {
            case STRUCT -> structs.put(descriptor, command);
            case UNION -> unions.put(descriptor, command);
            case GLOBAL_VARIABLE -> vars.put(descriptor, command);
            case FUNCTION -> functions.put(descriptor, command);
        }
    }

    public Command getMain(String packageName) {
        return getFunction(packageName, "main");
    }

    public Command getFunction(String packageName, String functionName) {
        for (Map.Entry<ObjectDescriptor, Command> entry : functions.entrySet()) {
            if (entry.getKey().getPackageName().equals(packageName) && entry.getKey().getName().equals(functionName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Command getStruct(String packageName, String structName) {
        for (Map.Entry<ObjectDescriptor, Command> entry : structs.entrySet()) {
            if (entry.getKey().getPackageName().equals(packageName) && entry.getKey().getName().equals(structName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Command getUnion(String packageName, String unionName) {
        for (Map.Entry<ObjectDescriptor, Command> entry : unions.entrySet()) {
            if (entry.getKey().getPackageName().equals(packageName) && entry.getKey().getName().equals(unionName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Command getGlobalVariable(String packageName, String varName) {
        for (Map.Entry<ObjectDescriptor, Command> entry : vars.entrySet()) {
            if (entry.getKey().getPackageName().equals(packageName) && entry.getKey().getName().equals(varName)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
}
