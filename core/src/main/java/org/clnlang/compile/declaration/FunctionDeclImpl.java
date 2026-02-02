package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.runtime.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a function declaration.
 */
public class FunctionDeclImpl implements CompiledAction {
    private String name;
    private List<Parameter> parameters;
    private List<ReturnVar> returnVars;
    private CompiledAction block;
    private boolean isExposed;

    public FunctionDeclImpl(String name, boolean isExposed) {
        this.name = name;
        this.isExposed = isExposed;
        this.parameters = new ArrayList<>();
        this.returnVars = new ArrayList<>();
    }

    public void addParameter(String type, String paramName) {
        parameters.add(new Parameter(type, paramName));
    }

    public void addReturnVar(String type, String varName) {
        returnVars.add(new ReturnVar(type, varName));
    }

    public void setBlock(CompiledAction block) {
        this.block = block;
    }

    public CompiledAction getBlock() {
        return block;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public List<ReturnVar> getReturnVars() {
        return returnVars;
    }

    public boolean isExposed() {
        return isExposed;
    }

    @Override
    public void execute(ExecutionContext context) throws Exception {
        // Function execution happens when called, not here
        // Registration is handled by ProgramImpl
    }

    /**
     * Function parameter
     */
    public static class Parameter {
        private String type;
        private String name;

        public Parameter(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Function return variable
     */
    public static class ReturnVar {
        private String type;
        private String name;

        public ReturnVar(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
