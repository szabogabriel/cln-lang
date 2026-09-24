package org.clnlang.compile.declaration;

import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.runtime.context.ExecutionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiled representation of a function declaration.
 */
public class FunctionDeclImpl implements CompiledAction {
    private String name;
    private String packageName;
    private List<Parameter> parameters;
    private List<ReturnVar> returnVars;
    private String simpleReturnType;  // Simple return type like "int" or null if using named returns
    private CompiledAction block;
    private boolean isExposed;

    public FunctionDeclImpl(String name, boolean isExposed) {
        this.name = name;
        this.packageName = null; // Will be set later
        this.isExposed = isExposed;
        this.parameters = new ArrayList<>();
        this.returnVars = new ArrayList<>();
    }

    public void addParameter(String type, String paramName) {
        parameters.add(new Parameter(type, paramName));
    }

    public void addParameter(String type, String paramName, int registryIndex, DecimalTypeInfo decimalTypeInfo) {
        parameters.add(new Parameter(type, paramName, registryIndex, decimalTypeInfo));
    }

    public void addReturnVar(String type, String varName) {
        returnVars.add(new ReturnVar(type, varName));
    }

    public void addReturnVar(String type, String varName, int registryIndex, DecimalTypeInfo decimalTypeInfo) {
        returnVars.add(new ReturnVar(type, varName, registryIndex, decimalTypeInfo));
    }

    public void setSimpleReturnType(String type) {
        this.simpleReturnType = type;
    }

    public String getSimpleReturnType() {
        return simpleReturnType;
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
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getPackageName() {
        return packageName;
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
     * Function parameter. Carries the registry slot assigned by CompilerVisitor so
     * FunctionInvoker can bind arguments directly by index instead of by name.
     */
    public static class Parameter {
        private String type;
        private String name;
        private final int registryIndex; // -1 if unresolved at compile time
        private final DecimalTypeInfo decimalTypeInfo;

        public Parameter(String type, String name) {
            this(type, name, -1, DecimalTypeInfo.DEFAULT);
        }

        public Parameter(String type, String name, int registryIndex, DecimalTypeInfo decimalTypeInfo) {
            this.type = type;
            this.name = name;
            this.registryIndex = registryIndex;
            this.decimalTypeInfo = decimalTypeInfo != null ? decimalTypeInfo : DecimalTypeInfo.DEFAULT;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getRegistryIndex() {
            return registryIndex;
        }

        public DecimalTypeInfo getDecimalTypeInfo() {
            return decimalTypeInfo;
        }
    }

    /**
     * Function return variable. Carries the registry slot assigned by CompilerVisitor so
     * FunctionInvoker can initialize/read it directly by index instead of by name.
     */
    public static class ReturnVar {
        private String type;
        private String name;
        private final int registryIndex; // -1 if unresolved at compile time
        private final DecimalTypeInfo decimalTypeInfo;

        public ReturnVar(String type, String name) {
            this(type, name, -1, DecimalTypeInfo.DEFAULT);
        }

        public ReturnVar(String type, String name, int registryIndex, DecimalTypeInfo decimalTypeInfo) {
            this.type = type;
            this.name = name;
            this.registryIndex = registryIndex;
            this.decimalTypeInfo = decimalTypeInfo != null ? decimalTypeInfo : DecimalTypeInfo.DEFAULT;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getRegistryIndex() {
            return registryIndex;
        }

        public DecimalTypeInfo getDecimalTypeInfo() {
            return decimalTypeInfo;
        }
    }
}
