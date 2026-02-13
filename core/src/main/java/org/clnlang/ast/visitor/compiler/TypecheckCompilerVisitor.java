package org.clnlang.ast.visitor.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.FunctionDeclNode.ReturnVar;
import org.clnlang.ast.declaration.ImportDeclNode;
import org.clnlang.ast.declaration.PackageDeclNode;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.declaration.StructDeclNode;
import org.clnlang.ast.declaration.UnionDeclNode;
import org.clnlang.ast.expression.BinaryExpr;
import org.clnlang.ast.expression.BoolLiteralExpr;
import org.clnlang.ast.expression.CallExpr;
import org.clnlang.ast.expression.IdentifierExpr;
import org.clnlang.ast.expression.IndexAccessExpr;
import org.clnlang.ast.expression.IntLiteralExpr;
import org.clnlang.ast.expression.MemberAccessExpr;
import org.clnlang.ast.expression.StringLiteralExpr;
import org.clnlang.ast.expression.StructLiteralExpr;
import org.clnlang.ast.expression.UnaryExpr;
import org.clnlang.ast.statement.AssignStmt;
import org.clnlang.ast.statement.EmptyStmt;
import org.clnlang.ast.statement.ExprStmt;
import org.clnlang.ast.statement.IfStmt;
import org.clnlang.ast.statement.ReturnStmt;
import org.clnlang.ast.statement.SwitchStmt;
import org.clnlang.ast.statement.TupleAssignStmt;
import org.clnlang.ast.statement.VarDeclStmt;
import org.clnlang.ast.statement.WhileStmt;
import org.clnlang.ast.visitor.ASTVisitor;
import org.clnlang.compiled.CFunction;
import org.clnlang.compiled.Instruction;
import org.clnlang.compiled.types.Types;

public class TypecheckCompilerVisitor implements ASTVisitor {

    private static enum Variable { INT, DEC, BOOL, STRING, STRUCT, UNION; };

    private String packageName = null;

    private Set<String> imports = new HashSet<>();
    
    private List<String> structNames = new ArrayList<>();
    private List<String> unionNames = new ArrayList<>();
    private List<String> functionNames = new ArrayList<>();
    private List<String> globalVariableNames = new ArrayList<>();
    
    // Compiled functions
    private List<CFunction> compiledFunctions = new ArrayList<>();
    
    // Current function being compiled
    private List<Instruction> currentFunctionInstructions = new ArrayList<>();

    // Global variable tracking
    private Map<String, Variable> globalVariableTypes = new HashMap<>();
    private Map<String, Integer> globalVariableAddresses = new HashMap<>();
    private int globalVarAddrCounterInt = 0;
    private int globalVarAddrCounterDec = 0;
    private int globalVarAddrCounterBool = 0;
    private int globalVarAddrCounterString = 0;
    private int globalVarAddrCounterStruct = 0;
    private int globalVarAddrCounterUnion = 0;

    // Local variable tracking (reset per function)
    private Map<String, Variable> localVariableTypes = new HashMap<>();
    private Map<String, Integer> localVariableAddresses = new HashMap<>();
    private int localVarAddrCounterInt = 0;
    private int localVarAddrCounterDec = 0;
    private int localVarAddrCounterBool = 0;
    private int localVarAddrCounterString = 0;
    private int localVarAddrCounterStruct = 0;
    private int localVarAddrCounterUnion = 0;

    // Track if we're currently in a function (for scoping)
    private boolean inFunction = false;

    @Override
    public void visit(ProgramNode node) {
        // Visit package declaration
        if (node.getPackageDecl() != null) {
            node.getPackageDecl().accept(this);
        }
        
        // Visit imports
        for (ImportDeclNode importNode : node.getImports()) {
            importNode.accept(this);
        }
        
        // Visit declarations (structs, unions, functions)
        for (Object decl : node.getDeclarations()) {
            if (decl instanceof StructDeclNode) {
                ((StructDeclNode) decl).accept(this);
            } else if (decl instanceof UnionDeclNode) {
                ((UnionDeclNode) decl).accept(this);
            } else if (decl instanceof FunctionDeclNode) {
                ((FunctionDeclNode) decl).accept(this);
            }
        }
    }

    @Override
    public void visit(PackageDeclNode node) {
        this.packageName = node.getPackageName();
    }

    @Override
    public void visit(ImportDeclNode node) {
        imports.add(node.getImportPath());
    }

    @Override
    public void visit(StructDeclNode node) {
        structNames.add(node.getName());
        // TODO: Process struct fields
    }

    @Override
    public void visit(UnionDeclNode node) {
        unionNames.add(node.getName());
        // TODO: Process union members
    }

    /**
     * Reset local variable context when entering a new function.
     */
    private void resetLocalContext() {
        localVariableTypes.clear();
        localVariableAddresses.clear();
        localVarAddrCounterInt = 0;
        localVarAddrCounterDec = 0;
        localVarAddrCounterBool = 0;
        localVarAddrCounterString = 0;
        localVarAddrCounterStruct = 0;
        localVarAddrCounterUnion = 0;
        currentFunctionInstructions.clear();
    }

    @Override
    public void visit(FunctionDeclNode node) {
        functionNames.add(node.getName());

        // Reset local context for this function
        resetLocalContext();
        inFunction = true;

        List<FunctionDeclNode.Parameter> parameters = node.getParameters();
        List<ReturnVar> returnVars = node.getReturnVars();

        int[] mappedParameters = new int[parameters.size()];
        Types[] mappedParameterTypes = new Types[parameters.size()];
        
        // Register parameters as local variables
        for (int i = 0; i < parameters.size(); i++) {
            FunctionDeclNode.Parameter param = parameters.get(i);
            String paramName = param.getName();
            String paramType = param.getType();
            Types type = Types.fromString(paramType);
            mappedParameterTypes[i] = type;
            
            // Register parameter in local context and get its address
            registerVariable(paramName, paramType, true);
            mappedParameters[i] = localVariableAddresses.get(paramName);
        }
        
        // Process return types
        int[] mappedReturns = new int[returnVars.size()];
        Types[] mappedReturnTypes = new Types[returnVars.size()];
        
        for (int i = 0; i < returnVars.size(); i++) {
            ReturnVar retVar = returnVars.get(i);
            String retName = retVar.getName();
            String retType = retVar.getType();
            Types type = Types.fromString(retType);
            mappedReturnTypes[i] = type;
            
            // Register return variable in local context and get its address
            registerVariable(retName, retType, true);
            mappedReturns[i] = localVariableAddresses.get(retName);
        }
        
        // Visit function body to compile instructions
        if (node.getBlock() != null) {
            node.getBlock().accept(this);
        }
        
        // Create CFunction instance with compiled data
        Instruction[] instructions = currentFunctionInstructions.toArray(new Instruction[0]);
        CFunction cFunction = new CFunction(
            node.getName(),
            mappedParameters,
            mappedParameterTypes,
            mappedReturns,
            mappedReturnTypes,
            instructions
        );
        
        compiledFunctions.add(cFunction);
        
        inFunction = false;
    }

    @Override
    public void visit(BlockNode node) {
        // Visit all statements in the block
        for (Object stmt : node.getStatements()) {
            if (stmt instanceof AssignStmt) {
                ((AssignStmt) stmt).accept(this);
            } else if (stmt instanceof VarDeclStmt) {
                ((VarDeclStmt) stmt).accept(this);
            } else if (stmt instanceof ReturnStmt) {
                ((ReturnStmt) stmt).accept(this);
            } else if (stmt instanceof IfStmt) {
                ((IfStmt) stmt).accept(this);
            } else if (stmt instanceof WhileStmt) {
                ((WhileStmt) stmt).accept(this);
            } else if (stmt instanceof ExprStmt) {
                ((ExprStmt) stmt).accept(this);
            } else if (stmt instanceof EmptyStmt) {
                ((EmptyStmt) stmt).accept(this);
            }
        }
    }

    @Override
    public void visit(AssignStmt node) {
        // TODO: Process assignment
        // Visit lvalue and value expressions
    }

    @Override
    public void visit(EmptyStmt node) {
        // No-op for empty statements
    }

    @Override
    public void visit(ExprStmt node) {
        // TODO: Process expression statement
        if (node.getExpression() != null) {
            // node.getExpression().accept(this);
        }
    }

    @Override
    public void visit(IfStmt node) {
        // TODO: Process if statement
        // Visit condition, then block, else block
    }

    @Override
    public void visit(ReturnStmt node) {
        // TODO: Process return statement
        // Visit return value expressions
        System.out.println("Processing return statement");
    }

    @Override
    public void visit(SwitchStmt node) {
        // TODO: Process switch statement
    }

    @Override
    public void visit(TupleAssignStmt node) {
        // TODO: Process tuple assignment
    }

    /**
     * Register a variable (global or local) with its type and address.
     */
    private void registerVariable(String name, String type, boolean isLocal) {
        Variable varType = mapTypeToVariable(type);
        
        if (isLocal) {
            localVariableTypes.put(name, varType);
            int address = getNextLocalAddress(varType);
            localVariableAddresses.put(name, address);
        } else {
            globalVariableTypes.put(name, varType);
            globalVariableNames.add(name);
            int address = getNextGlobalAddress(varType);
            globalVariableAddresses.put(name, address);
        }
    }
    
    /**
     * Map type string to Variable enum.
     */
    private Variable mapTypeToVariable(String type) {
        Types parsedType = Types.fromString(type);
        switch (parsedType) {
            case INT:
                return Variable.INT;
            case DEC:
                return Variable.DEC;
            case BOOL:
                return Variable.BOOL;
            case STRING:
                return Variable.STRING;
            case STRUCT:
                return Variable.STRUCT;
            case UNION:
                return Variable.UNION;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
    
    /**
     * Get next local variable address for the given type.
     */
    private int getNextLocalAddress(Variable varType) {
        switch (varType) {
            case INT:
                return localVarAddrCounterInt++;
            case DEC:
                return localVarAddrCounterDec++;
            case BOOL:
                return localVarAddrCounterBool++;
            case STRING:
                return localVarAddrCounterString++;
            case STRUCT:
                return localVarAddrCounterStruct++;
            case UNION:
                return localVarAddrCounterUnion++;
            default:
                throw new IllegalArgumentException("Unknown variable type: " + varType);
        }
    }
    
    /**
     * Get next global variable address for the given type.
     */
    private int getNextGlobalAddress(Variable varType) {
        switch (varType) {
            case INT:
                return globalVarAddrCounterInt++;
            case DEC:
                return globalVarAddrCounterDec++;
            case BOOL:
                return globalVarAddrCounterBool++;
            case STRING:
                return globalVarAddrCounterString++;
            case STRUCT:
                return globalVarAddrCounterStruct++;
            case UNION:
                return globalVarAddrCounterUnion++;
            default:
                throw new IllegalArgumentException("Unknown variable type: " + varType);
        }
    }

    @Override
    public void visit(VarDeclStmt node) {
        String name = node.getName();
        String type = node.getType();
        
        System.out.println("Processing variable declaration: " + name + " of type " + type);
        
        // Register variable in the appropriate scope
        registerVariable(name, type, inFunction);
        
        // TODO: Visit initializer expression if present
        // if (node.getInitializer() != null) {
        //     node.getInitializer().accept(this);
        // }
    }

    @Override
    public void visit(WhileStmt node) {
        // TODO: Process while statement
    }

    @Override
    public void visit(BinaryExpr node) {
        // TODO: Process binary expression
        System.out.println("Processing binary expression with operator: " + node.getOperator());
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        // TODO: Process boolean literal
    }

    @Override
    public void visit(CallExpr node) {
        // TODO: Process function call
    }

    @Override
    public void visit(IdentifierExpr node) {
        // TODO: Process identifier
        System.out.println("Processing identifier: " + node.getName());
    }

    @Override
    public void visit(IndexAccessExpr node) {
        // TODO: Process array/index access
    }

    @Override
    public void visit(IntLiteralExpr node) {
        // TODO: Process integer literal
        System.out.println("Processing int literal: " + node.getLongValue());
    }

    @Override
    public void visit(MemberAccessExpr node) {
        // TODO: Process member access
    }

    @Override
    public void visit(StringLiteralExpr node) {
        // TODO: Process string literal
    }

    @Override
    public void visit(StructLiteralExpr node) {
        // TODO: Process struct literal
    }

    @Override
    public void visit(UnaryExpr node) {
        // TODO: Process unary expression
    }
    
    // ==================== Getters for compiled results ====================
    
    /**
     * Get the list of compiled functions.
     */
    public List<CFunction> getCompiledFunctions() {
        return compiledFunctions;
    }
    
    /**
     * Get the package name.
     */
    public String getPackageName() {
        return packageName;
    }
    
    /**
     * Get the imported packages.
     */
    public Set<String> getImports() {
        return imports;
    }
    
    /**
     * Get global variable addresses.
     */
    public Map<String, Integer> getGlobalVariableAddresses() {
        return globalVariableAddresses;
    }
    
    /**
     * Get global variable types.
     */
    public Map<String, Variable> getGlobalVariableTypes() {
        return globalVariableTypes;
    }
    
}
