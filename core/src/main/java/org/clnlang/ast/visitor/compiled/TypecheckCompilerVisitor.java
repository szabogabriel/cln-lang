package org.clnlang.ast.visitor.compiled;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import org.clnlang.ast.expression.DecLiteralExpr;
import org.clnlang.ast.expression.Expr;
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
import org.clnlang.compiled.binary.CExecutable;
import org.clnlang.compiled.binary.CFunction;
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.binary.expressions.CExpression;
import org.clnlang.compiled.binary.expressions.CExpression.ExpressionType;
import org.clnlang.compiled.binary.expressions.binary.BinaryOperators;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionBoolBool;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionBoolString;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionDecDec;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionDecInt;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionDecString;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionIntDec;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionIntInt;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionIntString;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionStringBool;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionStringDec;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionStringInt;
import org.clnlang.compiled.binary.expressions.binary.CBinaryExpressionStringString;
import org.clnlang.compiled.binary.expressions.identifier.CIdentifierExpressionBool;
import org.clnlang.compiled.binary.expressions.identifier.CIdentifierExpressionDec;
import org.clnlang.compiled.binary.expressions.identifier.CIdentifierExpressionInt;
import org.clnlang.compiled.binary.expressions.identifier.CIdentifierExpressionString;
import org.clnlang.compiled.binary.expressions.literal.CBoolLiteralExpression;
import org.clnlang.compiled.binary.expressions.literal.CDecLiteralExpression;
import org.clnlang.compiled.binary.expressions.literal.CIntLiteralExpression;
import org.clnlang.compiled.binary.expressions.literal.CStringLiteralExpression;
import org.clnlang.compiled.context.MemoryAllocatorDescription;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.exception.ClnException;
import org.clnlang.exception.IncompatibleTypesException;
import org.clnlang.exception.VariableNotDeclaredException;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnParser;

public class TypecheckCompilerVisitor implements ASTVisitor {

    // Compiled functions
    private List<CFunction> compiledFunctions = new ArrayList<>();
    
    private GlobalRegistry globalRegistry;
    private ClnASTBuilder astBuilder;
    private CompilerContext compilerContext = new CompilerContext();
    
    /**
     * Creates a TypecheckCompilerVisitor with a GlobalRegistry.
     * The GlobalRegistry is used to resolve function and structure signatures.
     */
    public TypecheckCompilerVisitor(GlobalRegistry globalRegistry) {
        this(globalRegistry, new CompilerContext());
    }

    public TypecheckCompilerVisitor(GlobalRegistry globalRegistry, CompilerContext compilerContext) {
        this.globalRegistry = globalRegistry;
        this.astBuilder = new ClnASTBuilder();
        this.compilerContext = compilerContext;        
    }

    public CompilerContext getCompilerContext() {
        return compilerContext;
    }

    /**
     * Compiles a program from the parser context.
     * This method processes the parsed program and generates compiled functions.
     * 
     * @param ctx The parser context for the program
     * @param currentFile The source file being compiled
     */
    public void compileProgram(clnParser.ProgramContext ctx, File currentFile) {
        try {
            ProgramNode programNode = (ProgramNode) astBuilder.visitProgram(ctx);
            visit(programNode);
        } catch (Exception e) {
            throw new ClnException("Failed to compile file: " + currentFile.getName() + " due to: " + e.getMessage());
        }
    }

    /**
     * Returns the list of compiled functions.
     * 
     * @return List of compiled CFunction objects
     */
    public List<CFunction> getCompiledFunctions() {
        return new ArrayList<>(compiledFunctions);
    }

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
        compilerContext.setPackageName(node.getPackageName());
    }

    @Override
    public void visit(ImportDeclNode node) {
        compilerContext.addImport(node.getImportPath());
    }

    @Override
    public void visit(StructDeclNode node) {
        compilerContext.addStructName(node.getName());
        // TODO: Process struct fields
    }

    @Override
    public void visit(UnionDeclNode node) {
        compilerContext.addUnionName(node.getName());
        // TODO: Process union members
    }

    @Override
    public void visit(FunctionDeclNode node) {
        compilerContext.addFunctionName(node.getName());

        // Reset local context for this function
        compilerContext.newLocalContext();
        compilerContext.setInFunction(true);

        List<FunctionDeclNode.Parameter> parameters = node.getParameters();
        List<ReturnVar> returnVars = node.getReturnVars();
        if (returnVars == null || returnVars.isEmpty()) {
            returnVars = new ArrayList<>();
            String simpleReturnType = node.getSimpleReturnType();
            if (simpleReturnType != null && !"".equals(simpleReturnType.trim())) {
                returnVars.add(new ReturnVar(simpleReturnType, "__ret__"));
            }
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
            compilerContext.getCurrentLocalContext().registerVariable(retName, type);
            mappedReturns[i] = compilerContext.getCurrentLocalContext().getVariableAddress(retName);
        }

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
            compilerContext.getCurrentLocalContext().registerVariable(paramName, type);
            mappedParameters[i] = compilerContext.getCurrentLocalContext().getVariableAddress(paramName);
        }
        
        // Visit function body to compile instructions
        if (node.getBlock() != null) {
            node.getBlock().accept(this);
        }
        
        // Create CFunction instance with compiled data
        CExecutable[] instructions = compilerContext.getCurrentLocalContext().getCurrentFunctionInstructions().toArray(new CExecutable[0]);
        MemoryAllocatorDescription memoryAllocatorDescription = compilerContext.getCurrentLocalContext().createMemoryAllocatorDescription();
        CFunction cFunction = new CFunction(
            node.getName(),
            mappedParameters,
            mappedParameterTypes,
            mappedReturns,
            mappedReturnTypes,
            instructions,
            memoryAllocatorDescription
        );
        
        compiledFunctions.add(cFunction);
        
        compilerContext.setInFunction(false);
        compilerContext.popLocalContext();
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
            Expr expr = node.getExpression();
            if (expr instanceof BinaryExpr) {
                visit((BinaryExpr) expr);
            } else if (expr instanceof BoolLiteralExpr) {
                visit((BoolLiteralExpr) expr);
            } else if (expr instanceof CallExpr) {
                visit((CallExpr) expr);
            } else if (expr instanceof IdentifierExpr) {
                visit((IdentifierExpr) expr);
            } else if (expr instanceof IndexAccessExpr) {
                visit((IndexAccessExpr) expr);
            } else if (expr instanceof IntLiteralExpr) {
                visit((IntLiteralExpr) expr);
            } else if (expr instanceof MemberAccessExpr) {
                visit((MemberAccessExpr) expr);
            } else if (expr instanceof StringLiteralExpr) {
                visit((StringLiteralExpr) expr);
            } else if (expr instanceof StructLiteralExpr) {
                visit((StructLiteralExpr) expr);
            } else if (expr instanceof UnaryExpr) {
                visit((UnaryExpr) expr);
            }
        }
    }

    @Override
    public void visit(IfStmt node) {
        // TODO: Process if statement
        // Visit condition, then block, else block
    }

    @Override
    public void visit(ReturnStmt node) {
        System.out.println("Processing return statement");
        List<Expr> expresions = node.getReturnValues();
        List<CExpression> compiledReturnValues = new ArrayList<>();
        for (Expr expr : expresions) {
            compiledReturnValues.add(compileExpression(expr));
        }

        //TODO: handle compiled return values, e.g. generate instructions to set return variables and jump to function epilogue
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
        if (isLocal) {
            compilerContext.getCurrentLocalContext().registerVariable(name, Types.fromString(type));
        } else {
            compilerContext.getGlobalContext().registerVariable(name, Types.fromString(type));
        }
    }

    @Override
    public void visit(VarDeclStmt node) {
        String name = node.getName();
        String type = node.getType();
        
        System.out.println("Processing variable declaration: " + name + " of type " + type);
        
        // Register variable in the appropriate scope
        registerVariable(name, type, compilerContext.isInFunction());
        
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
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        // TODO: Process boolean literal
    }

    @Override
    public void visit(DecLiteralExpr node) {
        // TODO: Process decimal literal
    }

    @Override
    public void visit(CallExpr node) {
        List<Expr> arguments = node.getArguments();

        List<CExpression> compiledArguments = new ArrayList<>();
        for (Expr arg : arguments) {
            compiledArguments.add(compileExpression(arg));
        }

        Expr function = node.getFunction();
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

    // ---------- Helper methods ----------

    private CExpression compileExpression(Expr expr) {
        CExpression ret = null;
        if (expr instanceof BinaryExpr) {
            ret = compileBinaryExpression((BinaryExpr) expr);
        } else if (expr instanceof BoolLiteralExpr) {
            ret = new CBoolLiteralExpression(((BoolLiteralExpr) expr).getValue());
        } else if (expr instanceof CallExpr) {
            //TODO
        } else if (expr instanceof DecLiteralExpr) {
            ret = new CDecLiteralExpression(((DecLiteralExpr) expr).getDecimalValue());
        } else if (expr instanceof IdentifierExpr) {
            ret = compileIdentifierExpression((IdentifierExpr) expr);
        } else if (expr instanceof IndexAccessExpr) {
            //TODO
        } else if (expr instanceof IntLiteralExpr) {
            ret = new CIntLiteralExpression(((IntLiteralExpr) expr).getLongValue());
        } else if (expr instanceof MemberAccessExpr) {
            //TODO
        } else if (expr instanceof StringLiteralExpr) {
            ret = new CStringLiteralExpression(((StringLiteralExpr) expr).getValue());
        } else if (expr instanceof StructLiteralExpr) {
            //TODO
        } else if (expr instanceof UnaryExpr) {
            //TODO
        }
        return ret;
    }

    private CExpression compileIdentifierExpression(IdentifierExpr idEx) {
        CExpression ret;
        
        String name = idEx.getName();
    
        if (compilerContext.getCurrentLocalContext().hasVariable(name)) {
            int offset = compilerContext.getCurrentLocalContext().getVariableAddress(name);
            Types type = compilerContext.getCurrentLocalContext().getVariableType(name);
            
            // Create type-specific expression
            switch (type) {
                case INT:
                    ret = new CIdentifierExpressionInt(offset, false);
                    break;
                case DEC:
                    ret = new CIdentifierExpressionDec(offset, false);
                    break;
                case BOOL:
                    ret = new CIdentifierExpressionBool(offset, false);
                    break;
                case STRING:
                    ret = new CIdentifierExpressionString(offset, false);
                    break;
                default:
                    throw new RuntimeException("Unsupported variable type: " + type);
            }
        } else {
            // Check global context
            if (compilerContext.getGlobalContext().hasVariable(name)) {
                int offset = compilerContext.getGlobalContext().getVariableAddress(name);
                Types type = compilerContext.getGlobalContext().getVariableType(name);
                
                // Create type-specific expression
                switch (type) {
                    case INT:
                        ret = new CIdentifierExpressionInt(offset, true);
                        break;
                    case DEC:
                        ret = new CIdentifierExpressionDec(offset, true);
                        break;
                    case BOOL:
                        ret = new CIdentifierExpressionBool(offset, true);
                        break;
                    case STRING:
                        ret = new CIdentifierExpressionString(offset, true);
                        break;
                    default:
                        throw new RuntimeException("Unsupported variable type: " + type);
                }
            } else {
                throw new VariableNotDeclaredException("Variable '" + name + "' is not declared in the current scope.");
            }
        }

        return ret;
    }

    private CExpression compileBinaryExpression(BinaryExpr node) {
        CExpression ret = null;
        CExpression left = compileExpression(node.getLeft());
        CExpression right = compileExpression(node.getRight());
        String operator = node.getOperator();

        if (isPrimitiveType(left, right)) {
            // Handle primitive binary expressions (int, dec, bool, string)
            ret = performConstantFolding(left, right, operator);
        } else {
            Types leftType = left.getResultTypes()[0];
            Types rightType = right.getResultTypes()[0];

            if (leftType == null || rightType == null) {
                throw new RuntimeException("Unable to determine types of binary expression operands");
            }

            Types returnType = getResultType(leftType, rightType, operator);
            String uniqueVarName = "__binary_result__" + System.nanoTime();
            compilerContext.getCurrentLocalContext().registerVariable(uniqueVarName, returnType);
            int resultAddress = compilerContext.getCurrentLocalContext().getVariableAddress(uniqueVarName);

            if (leftType == Types.STRING) {
                switch (rightType) {
                    case INT:
                        ret = new CBinaryExpressionStringInt(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                        break;
                    case DEC:
                        ret = new CBinaryExpressionStringDec(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                        break;
                    case BOOL:    
                        ret = new CBinaryExpressionStringBool(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                        break;
                    case STRING:
                        ret = new CBinaryExpressionStringString(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                        break;
                    default:
                        throw new IncompatibleTypesException("Cannot apply operator '" + operator + "' to types STRING and " + rightType);
                }
            } else if (leftType == Types.BOOL) {
                if (rightType == Types.STRING) {
                    ret = new CBinaryExpressionBoolString(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else if (rightType == Types.BOOL) {
                    ret = new CBinaryExpressionBoolBool(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else {
                    throw new IncompatibleTypesException("Cannot apply operator '" + operator + "' to types BOOL and " + rightType);
                }
            } else if (leftType == Types.INT) {
                if (rightType == Types.STRING) {
                    ret = new CBinaryExpressionIntString(right.getResults()[0], left.getResults()[0], resultAddress, right.isGlobal(), left.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else if (rightType == Types.INT) {
                    ret = new CBinaryExpressionIntInt(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else if (rightType == Types.DEC) {
                    ret = new CBinaryExpressionIntDec(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else {
                    throw new IncompatibleTypesException("Cannot apply operator '" + operator + "' to types INT and " + rightType);
                }
            } else if (leftType == Types.DEC) {
                if (rightType == Types.STRING) {
                    ret = new CBinaryExpressionDecString(right.getResults()[0], left.getResults()[0], resultAddress, right.isGlobal(), left.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else if (rightType == Types.INT) {
                    ret = new CBinaryExpressionDecInt(right.getResults()[0], left.getResults()[0], resultAddress, right.isGlobal(), left.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else if (rightType == Types.DEC) {
                    ret = new CBinaryExpressionDecDec(left.getResults()[0], right.getResults()[0], resultAddress, left.isGlobal(), right.isGlobal(), !compilerContext.isInFunction(), BinaryOperators.fromSymbol(operator));
                } else {
                    throw new IncompatibleTypesException("Cannot apply operator '" + operator + "' to types DEC and " + rightType); 
                }
            } else {
                throw new IncompatibleTypesException("Unsupported types for binary expression: " + leftType + " and " + rightType);
            }

        }
        
        return ret;
    }

    private boolean isPrimitiveType(CExpression left, CExpression right) {
        boolean leftIsLiteral = left.getExpressionType() == ExpressionType.INT_LITERAL 
            || left.getExpressionType() == ExpressionType.DEC_LITERAL 
            || left.getExpressionType() == ExpressionType.BOOL_LITERAL 
            || left.getExpressionType() == ExpressionType.STRING_LITERAL;
        boolean rightIsLiteral = right.getExpressionType() == ExpressionType.INT_LITERAL 
            || right.getExpressionType() == ExpressionType.DEC_LITERAL 
            || right.getExpressionType() == ExpressionType.BOOL_LITERAL 
            || right.getExpressionType() == ExpressionType.STRING_LITERAL;
        
        boolean ret = leftIsLiteral && rightIsLiteral;
        return ret;
    }

    private CExpression performConstantFolding(CExpression left, CExpression right, String operator) {
        ExpressionType leftType = left.getExpressionType();
        ExpressionType rightType = right.getExpressionType();
        
        // String concatenation - string converts everything to string
        if (operator.equals("+")) {
            if (leftType == ExpressionType.STRING_LITERAL || rightType == ExpressionType.STRING_LITERAL) {
                String leftStr = left.getStringValue();
                String rightStr = right.getStringValue();
                return new CStringLiteralExpression(leftStr + rightStr);
            }
        }
        
        // Arithmetic operations
        if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/") || operator.equals("%")) {
            // Int + Int = Int
            if (leftType == ExpressionType.INT_LITERAL && rightType == ExpressionType.INT_LITERAL) {
                long leftVal = left.getIntValue();
                long rightVal = right.getIntValue();
                long result;
                
                switch (operator) {
                    case "+":
                        result = leftVal + rightVal;
                        break;
                    case "-":
                        result = leftVal - rightVal;
                        break;
                    case "*":
                        result = leftVal * rightVal;
                        break;
                    case "/":
                        if (rightVal == 0) {
                            throw new ArithmeticException("Division by zero in constant folding");
                        }
                        result = leftVal / rightVal;
                        break;
                    case "%":
                        if (rightVal == 0) {
                            throw new ArithmeticException("Division by zero in constant folding");
                        }
                        result = leftVal % rightVal;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CIntLiteralExpression(result);
            }
            
            // Dec + Dec = Dec, Int + Dec = Dec, Dec + Int = Dec
            if ((leftType == ExpressionType.DEC_LITERAL || leftType == ExpressionType.INT_LITERAL) 
                && (rightType == ExpressionType.DEC_LITERAL || rightType == ExpressionType.INT_LITERAL)) {
                BigDecimal leftVal = leftType == ExpressionType.DEC_LITERAL ? left.getDecValue() : BigDecimal.valueOf(left.getIntValue());
                BigDecimal rightVal = rightType == ExpressionType.DEC_LITERAL ? right.getDecValue() : BigDecimal.valueOf(right.getIntValue());
                BigDecimal result;
                
                switch (operator) {
                    case "+":
                        result = leftVal.add(rightVal);
                        break;
                    case "-":
                        result = leftVal.subtract(rightVal);
                        break;
                    case "*":
                        result = leftVal.multiply(rightVal);
                        break;
                    case "/":
                        if (rightVal.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ArithmeticException("Division by zero in constant folding");
                        }
                        result = leftVal.divide(rightVal, java.math.MathContext.DECIMAL128);
                        break;
                    case "%":
                        if (rightVal.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ArithmeticException("Division by zero in constant folding");
                        }
                        result = leftVal.remainder(rightVal);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CDecLiteralExpression(result);
            }
        }
        
        // Comparison operators - return boolean
        if (operator.equals("==") || operator.equals("!=") || operator.equals("<") || operator.equals(">") 
            || operator.equals("<=") || operator.equals(">=")) {
            
            // Int comparison
            if (leftType == ExpressionType.INT_LITERAL && rightType == ExpressionType.INT_LITERAL) {
                long leftVal = left.getIntValue();
                long rightVal = right.getIntValue();
                boolean result;
                
                switch (operator) {
                    case "==":
                        result = leftVal == rightVal;
                        break;
                    case "!=":
                        result = leftVal != rightVal;
                        break;
                    case "<":
                        result = leftVal < rightVal;
                        break;
                    case ">":
                        result = leftVal > rightVal;
                        break;
                    case "<=":
                        result = leftVal <= rightVal;
                        break;
                    case ">=":
                        result = leftVal >= rightVal;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CBoolLiteralExpression(result);
            }
            
            // Dec comparison (including int-dec mixed)
            if ((leftType == ExpressionType.DEC_LITERAL || leftType == ExpressionType.INT_LITERAL) 
                && (rightType == ExpressionType.DEC_LITERAL || rightType == ExpressionType.INT_LITERAL)) {
                BigDecimal leftVal = leftType == ExpressionType.DEC_LITERAL ? left.getDecValue() : BigDecimal.valueOf(left.getIntValue());
                BigDecimal rightVal = rightType == ExpressionType.DEC_LITERAL ? right.getDecValue() : BigDecimal.valueOf(right.getIntValue());
                boolean result;
                
                switch (operator) {
                    case "==":
                        result = leftVal.compareTo(rightVal) == 0;
                        break;
                    case "!=":
                        result = leftVal.compareTo(rightVal) != 0;
                        break;
                    case "<":
                        result = leftVal.compareTo(rightVal) < 0;
                        break;
                    case ">":
                        result = leftVal.compareTo(rightVal) > 0;
                        break;
                    case "<=":
                        result = leftVal.compareTo(rightVal) <= 0;
                        break;
                    case ">=":
                        result = leftVal.compareTo(rightVal) >= 0;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CBoolLiteralExpression(result);
            }
            
            // String comparison (only == and !=)
            if (leftType == ExpressionType.STRING_LITERAL && rightType == ExpressionType.STRING_LITERAL) {
                String leftVal = left.getStringValue();
                String rightVal = right.getStringValue();
                boolean result;
                
                switch (operator) {
                    case "==":
                        result = leftVal.equals(rightVal);
                        break;
                    case "!=":
                        result = !leftVal.equals(rightVal);
                        break;
                    case "<":
                        result = leftVal.compareTo(rightVal) < 0;
                        break;
                    case ">":
                        result = leftVal.compareTo(rightVal) > 0;
                        break;
                    case "<=":
                        result = leftVal.compareTo(rightVal) <= 0;
                        break;
                    case ">=":
                        result = leftVal.compareTo(rightVal) >= 0;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CBoolLiteralExpression(result);
            }
            
            // Boolean comparison (only == and !=)
            if (leftType == ExpressionType.BOOL_LITERAL && rightType == ExpressionType.BOOL_LITERAL) {
                boolean leftVal = left.getBoolValue();
                boolean rightVal = right.getBoolValue();
                boolean result;
                
                switch (operator) {
                    case "==":
                        result = leftVal == rightVal;
                        break;
                    case "!=":
                        result = leftVal != rightVal;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected comparison operator for booleans: " + operator);
                }
                return new CBoolLiteralExpression(result);
            }
        }
        
        // Logical operators
        if (operator.equals("&&") || operator.equals("||")) {
            if (leftType == ExpressionType.BOOL_LITERAL && rightType == ExpressionType.BOOL_LITERAL) {
                boolean leftVal = left.getBoolValue();
                boolean rightVal = right.getBoolValue();
                boolean result;
                
                switch (operator) {
                    case "&&":
                        result = leftVal && rightVal;
                        break;
                    case "||":
                        result = leftVal || rightVal;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected operator: " + operator);
                }
                return new CBoolLiteralExpression(result);
            }
        }
        
        throw new IllegalStateException("Cannot perform constant folding for types: " + leftType + " " + operator + " " + rightType);
    }

    private Types getResultType(Types left, Types right, String operator) {
        Types ret = null;

        switch (operator) {
            case "+":
                if (Types.STRING.equals(left) || Types.STRING.equals(right)) {
                    ret = Types.STRING;
                } else if (Types.INT.equals(left) && Types.INT.equals(right)) {
                    ret = Types.INT; // Int + Int = Int
                } else if ((Types.DEC.equals(left) && Types.DEC.equals(right)) 
                    || (Types.INT.equals(left) && Types.DEC.equals(right)) || (Types.DEC.equals(left) && Types.INT.equals(right))) {
                    ret = Types.DEC; // Int + Dec = Dec, Dec + Dec = Dec
                }
                break;
            case "-":
            case "*":
            case "/":
                if (Types.INT.equals(left) && Types.INT.equals(right)) {
                    ret = Types.INT; // Int - Int = Int
                } else if ((Types.DEC.equals(left) && Types.DEC.equals(right)) 
                    || (Types.INT.equals(left) && Types.DEC.equals(right)) || (Types.DEC.equals(left) && Types.INT.equals(right))) {
                    ret = Types.DEC; // Int - Dec = Dec, Dec - Dec = Dec
                }
                break;
            case "==":
            case "!=":
            case "<":
            case ">":
            case "<=":
            case ">=":
                if ((Types.INT.equals(left) && Types.INT.equals(right)) 
                    || (Types.DEC.equals(left) && Types.DEC.equals(right)) 
                    || (Types.INT.equals(left) && Types.DEC.equals(right)) || (Types.DEC.equals(left) && Types.INT.equals(right))) {
                    ret = Types.BOOL; // Int and Dec comparisons return bool
                } else if (Types.STRING.equals(left) && Types.STRING.equals(right)) {
                    ret = Types.BOOL; // String comparisons return bool
                } else if (Types.BOOL.equals(left) && Types.BOOL.equals(right)) {
                    ret = Types.BOOL; // Bool comparisons return bool
                }
                break;
            case "&&":
            case "||":
                if (Types.BOOL.equals(left) && Types.BOOL.equals(right)) {
                    ret = Types.BOOL;
                }
                break;
        }

        if (ret == null) {
            throw new IncompatibleTypesException("The types '" + left + "'' and '" + right + "' are not compatible with operator '" + operator + "'");
        }

        return ret;
    }

}
