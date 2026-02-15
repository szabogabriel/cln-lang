package org.clnlang.exception;

public class VariableNotDeclaredException extends RuntimeException {

    public VariableNotDeclaredException(String variableName) {
        super("Variable [" + variableName + "] not declared.");
    }
    
}
