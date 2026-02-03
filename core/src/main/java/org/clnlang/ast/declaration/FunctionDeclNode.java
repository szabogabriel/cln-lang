package org.clnlang.ast.declaration;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;
import org.clnlang.ast.BlockNode;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node representing a function declaration.
 */
public class FunctionDeclNode extends ASTNode {
    private String name;
    private List<Parameter> parameters;
    private List<ReturnVar> returnVars;
    private String simpleReturnType;  // Simple return type like "int" or null if using named returns
    private BlockNode block;
    private boolean isExposed;

    public FunctionDeclNode(String name, boolean isExposed) {
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

    public void setSimpleReturnType(String type) {
        this.simpleReturnType = type;
    }

    public String getSimpleReturnType() {
        return simpleReturnType;
    }

    public void setBlock(BlockNode block) {
        this.block = block;
    }

    public BlockNode getBlock() {
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
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((isExposed ? "expose " : "")).append("Function: ");

        // Return signature
        sb.append("(");
        for (int i = 0; i < returnVars.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(returnVars.get(i));
        }
        sb.append(") ");
        // Function name
        sb.append(name);
        // Parameters
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i));
        }
        if (block != null) {
            sb.append(block);
        }
        return sb.toString();
    }

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

        @Override
        public String toString() {
            return type + " " + name;
        }
    }

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