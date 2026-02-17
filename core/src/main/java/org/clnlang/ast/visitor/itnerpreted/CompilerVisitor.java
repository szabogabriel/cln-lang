package org.clnlang.ast.visitor.itnerpreted;

import org.clnlang.interpreted.compile.BlockImpl;
import org.clnlang.interpreted.compile.CompiledAction;
import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.compile.declaration.*;
import org.clnlang.interpreted.compile.expression.*;
import org.clnlang.interpreted.compile.statement.*;
import org.clnlang.interpreted.compile.types.DecimalTypeInfo;
import org.clnlang.parser.clnBaseVisitor;
import org.clnlang.parser.clnParser;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compiles ANTLR parse tree into executable CompiledAction objects.
 * This is the bridge between parsing and execution.
 */
public class CompilerVisitor extends clnBaseVisitor<Object> {
    
    /**
     * Tracks variable names, types, and indices during compilation for index-based runtime access.
     */
    private static class VariableScope {
        private final VariableScope parent;
        
        // Separate indices for each type (matching LocalContext structure)
        private final java.util.Map<String, Integer> longIndices = new java.util.HashMap<>();
        private final java.util.Map<String, Integer> boolIndices = new java.util.HashMap<>();
        private final java.util.Map<String, Integer> decimalIndices = new java.util.HashMap<>();
        private final java.util.Map<String, Integer> stringIndices = new java.util.HashMap<>();
        private final java.util.Map<String, Integer> objectIndices = new java.util.HashMap<>();
        
        // Track types for all variables
        private final java.util.Map<String, String> variableTypes = new java.util.HashMap<>();
        
        // Track decimal type info for decimal variables
        private final java.util.Map<String, DecimalTypeInfo> decimalTypeInfos = new java.util.HashMap<>();
        
        private int nextLongIndex = 0;
        private int nextBoolIndex = 0;
        private int nextDecimalIndex = 0;
        private int nextStringIndex = 0;
        private int nextObjectIndex = 0;
        
        public VariableScope() {
            this(null);
        }
        
        public VariableScope(VariableScope parent) {
            this.parent = parent;
        }
        
        /**
         * Register a variable and assign it an index based on its type.
         */
        public void registerVariable(String name, String type, DecimalTypeInfo decimalTypeInfo) {
            variableTypes.put(name, type);
            
            String baseType = type.replaceAll("\\[\\]", ""); // Strip array brackets
            
            switch (baseType) {
                case "int":
                    longIndices.put(name, nextLongIndex++);
                    break;
                case "bool":
                    boolIndices.put(name, nextBoolIndex++);
                    break;
                case "dec":
                    decimalIndices.put(name, nextDecimalIndex++);
                    decimalTypeInfos.put(name, decimalTypeInfo);
                    break;
                case "string":
                    stringIndices.put(name, nextStringIndex++);
                    break;
                default:
                    // Structs, unions, arrays of non-primitives
                    objectIndices.put(name, nextObjectIndex++);
                    break;
            }
        }
        
        /**
         * Backward compatibility method
         */
        public void registerVariable(String name, String type) {
            registerVariable(name, type, DecimalTypeInfo.DEFAULT);
        }
        
        /**
         * Get the index and type for a variable.
         * Returns null if not found in this scope or parent scopes.
         */
        public VarInfo getVariableInfo(String name) {
            // Check current scope
            if (variableTypes.containsKey(name)) {
                String type = variableTypes.get(name);
                String baseType = type.replaceAll("\\[\\]", "");
                
                Integer index = null;
                DecimalTypeInfo decimalTypeInfo = DecimalTypeInfo.DEFAULT;
                
                switch (baseType) {
                    case "int":
                        index = longIndices.get(name);
                        break;
                    case "bool":
                        index = boolIndices.get(name);
                        break;
                    case "dec":
                        index = decimalIndices.get(name);
                        decimalTypeInfo = decimalTypeInfos.getOrDefault(name, DecimalTypeInfo.DEFAULT);
                        break;
                    case "string":
                        index = stringIndices.get(name);
                        break;
                    default:
                        index = objectIndices.get(name);
                        break;
                }
                
                if (index != null) {
                    return new VarInfo(name, type, index, decimalTypeInfo);
                }
            }
            
            // Check parent scope
            if (parent != null) {
                return parent.getVariableInfo(name);
            }
            
            return null;
        }
    }
    
    /**
     * Variable information: name, type, index, and decimal type info.
     */
    private static class VarInfo {
        final String name;
        final String type;
        final int index;
        final DecimalTypeInfo decimalTypeInfo;
        
        VarInfo(String name, String type, int index, DecimalTypeInfo decimalTypeInfo) {
            this.name = name;
            this.type = type;
            this.index = index;
            this.decimalTypeInfo = decimalTypeInfo;
        }
        
        // Backward compatibility constructor
        VarInfo(String name, String type, int index) {
            this(name, type, index, DecimalTypeInfo.DEFAULT);
        }
    }
    
    // Track the current function being compiled to access return variables
    private FunctionDeclImpl currentFunction = null;
    
    // Track the current variable scope for index assignment
    private VariableScope currentScope = null;
    
    // Track defined types for validation
    private Set<String> definedTypes = new HashSet<>();
    
    /**
     * Compile a program from the parse tree
     */
    public ProgramImpl compileProgram(clnParser.ProgramContext ctx) {
        ProgramImpl program = new ProgramImpl();
        
        // First pass: collect all type definitions
        definedTypes.clear();
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.decl() != null) {
                clnParser.DeclContext decl = topLevel.decl();
                if (decl.structDecl() != null) {
                    definedTypes.add(decl.structDecl().ID().getText());
                } else if (decl.unionDecl() != null) {
                    definedTypes.add(decl.unionDecl().ID().getText());
                }
            }
        }
        
        // Second pass: compile declarations with type validation
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.packageDecl() != null) {
                PackageDeclImpl pkg = compilePackageDecl(topLevel.packageDecl());
                program.setPackageDecl(pkg);
            } else if (topLevel.importDecl() != null) {
                ImportDeclImpl imp = compileImportDecl(topLevel.importDecl());
                program.addImport(imp);
            } else if (topLevel.decl() != null) {
                CompiledAction decl = compileDeclaration(topLevel.decl());
                if (decl != null) {
                    program.addDeclaration(decl);
                }
            }
        }
        
        return program;
    }
    
    /**
     * Compile package declaration
     */
    private PackageDeclImpl compilePackageDecl(clnParser.PackageDeclContext ctx) {
        String packageName = ctx.qualifiedName().getText();
        return new PackageDeclImpl(packageName);
    }
    
    /**
     * Compile import declaration
     */
    private ImportDeclImpl compileImportDecl(clnParser.ImportDeclContext ctx) {
        String importPath = ctx.qualifiedName().getText();
        boolean isWildcard = ctx.STAR() != null;
        return new ImportDeclImpl(importPath, isWildcard);
    }
    
    /**
     * Compile any declaration (function, struct, union, global var)
     */
    private CompiledAction compileDeclaration(clnParser.DeclContext ctx) {
        boolean isExposed = ctx.EXPOSE() != null;
        
        if (ctx.functionDecl() != null) {
            return compileFunctionDecl(ctx.functionDecl(), isExposed);
        } else if (ctx.structDecl() != null) {
            return compileStructDecl(ctx.structDecl(), isExposed);
        } else if (ctx.unionDecl() != null) {
            return compileUnionDecl(ctx.unionDecl(), isExposed);
        } else if (ctx.globalVarDecl() != null) {
            return compileGlobalVarDecl(ctx.globalVarDecl(), isExposed);
        }
        return null;
    }
    
    /**
     * Compile global variable declaration
     */
    private GlobalVarDeclImpl compileGlobalVarDecl(clnParser.GlobalVarDeclContext ctx, boolean isExposed) {
        clnParser.VarBindingContext binding = ctx.varBinding();
        
        // Handle parser errors gracefully
        if (binding == null || binding.ID() == null || binding.type() == null || binding.expr() == null) {
            return null;
        }
        
        boolean isMutable = binding.VAR() != null;
        String type = binding.type().getText();
        String name = binding.ID().getText();
        
        // Validate type
        validateType(type, binding.type().getStart().getLine());
        
        CompiledExpr initializer = compileExpression(binding.expr());
        
        return new GlobalVarDeclImpl(isMutable, type, name, initializer, isExposed);
    }
    
    /**
     * Compile function declaration
     */
    private FunctionDeclImpl compileFunctionDecl(clnParser.FunctionDeclContext ctx, boolean isExposed) {
        String name = ctx.ID().getText();
        FunctionDeclImpl func = new FunctionDeclImpl(name, isExposed);
        
        // Set current function context and create new variable scope
        FunctionDeclImpl previousFunction = this.currentFunction;
        VariableScope previousScope = this.currentScope;
        this.currentFunction = func;
        this.currentScope = new VariableScope(); // New scope for function
        
        try {
            // Compile return type
            if (ctx.returnType() != null) {
                clnParser.ReturnTypeContext retType = ctx.returnType();
                
                if (retType.type() != null) {
                    // Simple return type: int main()
                    String simpleType = retType.type().getText();
                    validateType(simpleType, retType.type().getStart().getLine());
                    func.setSimpleReturnType(simpleType);
                } else if (retType.namedReturnSig() != null) {
                    // Named return signature: (var int x = 0) main()
                    for (clnParser.ReturnVarContext retVar : retType.namedReturnSig().returnVar()) {
                        String type = retVar.type().getText();
                        String varName = retVar.ID().getText();
                        
                        // Validate return type
                        validateType(type, retVar.type().getStart().getLine());
                        
                        func.addReturnVar(type, varName);
                        // Register return variable in scope
                        currentScope.registerVariable(varName, type);
                    }
                }
            }
            
            // Compile parameters
            if (ctx.paramList() != null) {
                for (clnParser.ParamContext param : ctx.paramList().param()) {
                    String type = param.type().getText();
                    String paramName = param.ID().getText();
                    
                    // Validate parameter type
                    validateType(type, param.type().getStart().getLine());
                    
                    func.addParameter(type, paramName);
                    // Register parameter in scope
                    currentScope.registerVariable(paramName, type);
                }
            }
            
            // Compile body
            BlockImpl body = compileBlock(ctx.block());
            func.setBlock(body);
            
            return func;
        } finally {
            // Restore previous function context and scope
            this.currentFunction = previousFunction;
            this.currentScope = previousScope;
        }
    }
    
    /**
     * Compile struct declaration
     */
    private StructDeclImpl compileStructDecl(clnParser.StructDeclContext ctx, boolean isExposed) {
        String name = ctx.ID().getText();
        StructDeclImpl struct = new StructDeclImpl(name, isExposed);
        
        for (clnParser.StructFieldDeclContext field : ctx.structFieldDecl()) {
            String fieldType = field.type().getText();
            String fieldName = field.ID().getText();
            boolean isVar = field.VAR() != null;
            
            // Validate field type
            validateType(fieldType, field.type().getStart().getLine());
            
            struct.addField(fieldType, fieldName, isVar);
        }
        
        return struct;
    }
    
    /**
     * Compile union declaration
     */
    private UnionDeclImpl compileUnionDecl(clnParser.UnionDeclContext ctx, boolean isExposed) {
        String name = ctx.ID().getText();
        UnionDeclImpl union = new UnionDeclImpl(name, isExposed);
        
        for (clnParser.UnionMemberContext member : ctx.unionMember()) {
            String memberType = member.qualifiedName().getText();
            union.addMember(memberType);
        }
        
        return union;
    }
    
    /**
     * Compile a block of statements
     */
    private BlockImpl compileBlock(clnParser.BlockContext ctx) {
        BlockImpl block = new BlockImpl();
        
        for (clnParser.StmtContext stmtCtx : ctx.stmt()) {
            CompiledAction stmt = compileStatement(stmtCtx);
            if (stmt != null) {
                block.addStatement(stmt);
            }
        }
        
        return block;
    }
    
    /**
     * Compile a statement
     */
    private CompiledAction compileStatement(clnParser.StmtContext ctx) {
        if (ctx.block() != null) {
            return compileBlock(ctx.block());
        } else if (ctx.varDeclStmt() != null) {
            return compileVarDeclStmt(ctx.varDeclStmt());
        } else if (ctx.assignStmt() != null) {
            return compileAssignStmt(ctx.assignStmt());
        } else if (ctx.tupleAssignStmt() != null) {
            return compileTupleAssignStmt(ctx.tupleAssignStmt());
        } else if (ctx.ifStmt() != null) {
            return compileIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {
            return compileWhileStmt(ctx.whileStmt());
        } else if (ctx.switchStmt() != null) {
            return compileSwitchStmt(ctx.switchStmt());
        } else if (ctx.returnStmt() != null) {
            return compileReturnStmt(ctx.returnStmt());
        } else if (ctx.exprStmt() != null) {
            return compileExprStmt(ctx.exprStmt());
        } else if (ctx.SEMI() != null) {
            return new EmptyStmtImpl();
        }
        return null;
    }
    
    /**
     * Compile variable declaration statement
     */
    private VarDeclStmtImpl compileVarDeclStmt(clnParser.VarDeclStmtContext ctx) {
        return compileVarBinding(ctx.varBinding());
    }
    
    /**
     * Compile variable binding (used in var decl and tuple assign)
     */
    private VarDeclStmtImpl compileVarBinding(clnParser.VarBindingContext ctx) {
        boolean isVar = ctx.VAR() != null;
        String type = ctx.type().getText();
        String name = ctx.ID().getText();
        
        // Normalize decimal type to "dec" (strip parameters)
        String normalizedType = type.replaceAll("^dec\\(.*\\)", "dec");
        
        // Validate type
        validateType(normalizedType, ctx.type().getStart().getLine());
        
        // Extract decimal type info if it's a decimal type
        DecimalTypeInfo decimalTypeInfo = extractDecimalTypeInfo(ctx.type());
        
        // Register variable in current scope and get the assigned index
        int index = -1;
        if (currentScope != null) {
            currentScope.registerVariable(name, normalizedType, decimalTypeInfo);
            VarInfo varInfo = currentScope.getVariableInfo(name);
            if (varInfo != null) {
                index = varInfo.index;
            }
        }
        
        CompiledExpr initializer = compileExpression(ctx.expr());
        
        return new VarDeclStmtImpl(isVar, normalizedType, name, initializer, index, decimalTypeInfo);
    }
    
    /**
     * Compile assignment statement
     */
    private AssignStmtImpl compileAssignStmt(clnParser.AssignStmtContext ctx) {
        CompiledExpr lvalue = compileLvalue(ctx.lvalue());
        CompiledExpr value = compileExpression(ctx.expr());
        
        return new AssignStmtImpl(lvalue, value);
    }
    
    /**
     * Compile lvalue (left-hand side of assignment)
     */
    private CompiledExpr compileLvalue(clnParser.LvalueContext ctx) {
        String baseName = ctx.ID().getText();
        
        // Try to resolve base identifier with index
        CompiledExpr base;
        if (currentScope != null) {
            VarInfo varInfo = currentScope.getVariableInfo(baseName);
            if (varInfo != null) {
                base = new IdentifierExprImpl(baseName, varInfo.type, varInfo.index);
            } else {
                // Not in local scope, use name-based lookup
                base = new IdentifierExprImpl(baseName);
            }
        } else {
            base = new IdentifierExprImpl(baseName);
        }
        
        for (clnParser.LvalueSuffixContext suffix : ctx.lvalueSuffix()) {
            if (suffix.DOT() != null) {
                String member = suffix.ID().getText();
                base = new MemberAccessExprImpl(base, member);
            } else if (suffix.LBRACK() != null) {
                CompiledExpr index = compileExpression(suffix.expr());
                base = new IndexAccessExprImpl(base, index);
            }
        }
        
        return base;
    }
    
    /**
     * Compile tuple assignment statement
     */
    private TupleAssignStmtImpl compileTupleAssignStmt(clnParser.TupleAssignStmtContext ctx) {
        List<TupleAssignStmtImpl.TupleBind> bindings = new ArrayList<>();
        
        for (clnParser.TupleBindContext bind : ctx.tupleBind()) {
            boolean isVar = bind.VAR() != null;
            String type = bind.type().getText();
            String name = bind.ID().getText();
            
            // Validate type
            validateType(type, bind.type().getStart().getLine());
            
            bindings.add(new TupleAssignStmtImpl.TupleBind(isVar, type, name));
            
            // Register new variables in scope (if VAR is present)
            if (isVar && currentScope != null) {
                currentScope.registerVariable(name, type);
            }
        }
        
        CompiledExpr value = compileExpression(ctx.expr());
        
        return new TupleAssignStmtImpl(bindings, value);
    }
    
    /**
     * Compile if statement
     */
    private IfStmtImpl compileIfStmt(clnParser.IfStmtContext ctx) {
        CompiledExpr condition = compileExpression(ctx.expr());
        CompiledAction thenBlock = compileBlock(ctx.block(0));
        CompiledAction elseBlock = ctx.block().size() > 1 ? compileBlock(ctx.block(1)) : null;
        
        return new IfStmtImpl(condition, thenBlock, elseBlock);
    }
    
    /**
     * Compile while statement
     */
    private WhileStmtImpl compileWhileStmt(clnParser.WhileStmtContext ctx) {
        CompiledExpr condition = compileExpression(ctx.expr());
        CompiledAction body = compileBlock(ctx.block());
        
        return new WhileStmtImpl(condition, body);
    }
    
    /**
     * Compile switch statement
     */
    private SwitchStmtImpl compileSwitchStmt(clnParser.SwitchStmtContext ctx) {
        CompiledExpr expr = compileExpression(ctx.expr());
        SwitchStmtImpl switchStmt = new SwitchStmtImpl(expr);
        
        for (clnParser.CaseClauseContext caseCtx : ctx.caseClause()) {
            if (caseCtx.DEFAULT() != null) {
                // Default case
                SwitchStmtImpl.CaseClause defaultCase = new SwitchStmtImpl.CaseClause(null, null, true);
                for (clnParser.StmtContext stmt : caseCtx.stmt()) {
                    CompiledAction compiledStmt = compileStatement(stmt);
                    if (compiledStmt != null) {
                        defaultCase.addStatement(compiledStmt);
                    }
                }
                switchStmt.addCase(defaultCase);
            } else {
                // Regular case
                String qualifiedName = caseCtx.qualifiedName().getText();
                String varName = caseCtx.ID().getText();
                SwitchStmtImpl.CaseClause caseClause = new SwitchStmtImpl.CaseClause(qualifiedName, varName, false);
                for (clnParser.StmtContext stmt : caseCtx.stmt()) {
                    CompiledAction compiledStmt = compileStatement(stmt);
                    if (compiledStmt != null) {
                        caseClause.addStatement(compiledStmt);
                    }
                }
                switchStmt.addCase(caseClause);
            }
        }
        
        return switchStmt;
    }
    
    /**
     * Compile return statement
     */
    private ReturnStmtImpl compileReturnStmt(clnParser.ReturnStmtContext ctx) {
        if (ctx.expr() != null) {
            // Single return value
            CompiledExpr value = compileExpression(ctx.expr());
            return new ReturnStmtImpl(value);
        } else if (ctx.exprList() != null) {
            // Multiple return values
            List<CompiledExpr> values = new ArrayList<>();
            for (clnParser.ExprContext exprCtx : ctx.exprList().expr()) {
                values.add(compileExpression(exprCtx));
            }
            return new ReturnStmtImpl(values);
        } else {
            // No explicit return value - need to look up named return variables
            // Get the names from the current function being compiled
            List<String> returnVarNames = new ArrayList<>();
            if (currentFunction != null) {
                for (FunctionDeclImpl.ReturnVar retVar : currentFunction.getReturnVars()) {
                    returnVarNames.add(retVar.getName());
                }
            }
            return ReturnStmtImpl.withNamedReturnVars(returnVarNames);
        }
    }
    
    /**
     * Compile expression statement
     */
    private ExprStmtImpl compileExprStmt(clnParser.ExprStmtContext ctx) {
        CompiledExpr expression = compileExpression(ctx.expr());
        return new ExprStmtImpl(expression);
    }
    
    /**
     * Compile an expression
     */
    private CompiledExpr compileExpression(clnParser.ExprContext ctx) {
        return compileOrExpr(ctx.orExpr());
    }
    
    /**
     * Compile OR expression
     */
    private CompiledExpr compileOrExpr(clnParser.OrExprContext ctx) {
        CompiledExpr left = compileAndExpr(ctx.andExpr(0));
        
        for (int i = 1; i < ctx.andExpr().size(); i++) {
            CompiledExpr right = compileAndExpr(ctx.andExpr(i));
            left = new BinaryExprImpl(left, Operator.OR, right);
        }
        
        return left;
    }
    
    /**
     * Compile AND expression
     */
    private CompiledExpr compileAndExpr(clnParser.AndExprContext ctx) {
        CompiledExpr left = compileEqualityExpr(ctx.equalityExpr(0));
        
        for (int i = 1; i < ctx.equalityExpr().size(); i++) {
            CompiledExpr right = compileEqualityExpr(ctx.equalityExpr(i));
            left = new BinaryExprImpl(left, Operator.AND, right);
        }
        
        return left;
    }
    
    /**
     * Compile equality expression
     */
    private CompiledExpr compileEqualityExpr(clnParser.EqualityExprContext ctx) {
        CompiledExpr left = compileRelExpr(ctx.relExpr(0));
        
        for (int i = 1; i < ctx.relExpr().size(); i++) {
            Operator op = ctx.EQ(i - 1) != null ? Operator.EQ : Operator.NEQ;
            CompiledExpr right = compileRelExpr(ctx.relExpr(i));
            left = new BinaryExprImpl(left, op, right);
        }
        
        return left;
    }
    
    /**
     * Compile relational expression
     */
    private CompiledExpr compileRelExpr(clnParser.RelExprContext ctx) {
        CompiledExpr left = compileAddExpr(ctx.addExpr(0));
        
        for (int i = 1; i < ctx.addExpr().size(); i++) {
            Operator op;
            if (ctx.LT(i - 1) != null) op = Operator.LT;
            else if (ctx.LTE(i - 1) != null) op = Operator.LTE;
            else if (ctx.GT(i - 1) != null) op = Operator.GT;
            else op = Operator.GTE;
            
            CompiledExpr right = compileAddExpr(ctx.addExpr(i));
            left = new BinaryExprImpl(left, op, right);
        }
        
        return left;
    }
    
    /**
     * Compile additive expression
     */
    private CompiledExpr compileAddExpr(clnParser.AddExprContext ctx) {
        CompiledExpr left = compileMulExpr(ctx.mulExpr(0));
        
        for (int i = 1; i < ctx.mulExpr().size(); i++) {
            Operator op = ctx.PLUS(i - 1) != null ? Operator.PLUS : Operator.MINUS;
            CompiledExpr right = compileMulExpr(ctx.mulExpr(i));
            left = new BinaryExprImpl(left, op, right);
        }
        
        return left;
    }
    
    /**
     * Compile multiplicative expression
     */
    private CompiledExpr compileMulExpr(clnParser.MulExprContext ctx) {
        CompiledExpr left = compileUnaryExpr(ctx.unaryExpr(0));
        
        for (int i = 1; i < ctx.unaryExpr().size(); i++) {
            Operator op = ctx.STAR(i - 1) != null ? Operator.STAR : Operator.SLASH;
            CompiledExpr right = compileUnaryExpr(ctx.unaryExpr(i));
            left = new BinaryExprImpl(left, op, right);
        }
        
        return left;
    }
    
    /**
     * Compile unary expression
     */
    private CompiledExpr compileUnaryExpr(clnParser.UnaryExprContext ctx) {
        if (ctx.NOT() != null) {
            CompiledExpr operand = compileUnaryExpr(ctx.unaryExpr());
            return new UnaryExprImpl("!", operand);
        } else if (ctx.MINUS() != null) {
            CompiledExpr operand = compileUnaryExpr(ctx.unaryExpr());
            return new UnaryExprImpl("-", operand);
        } else if (ctx.INC() != null) {
            CompiledExpr operand = compileUnaryExpr(ctx.unaryExpr());
            return new IncrementExprImpl(operand, "++", true);
        } else if (ctx.DEC() != null) {
            CompiledExpr operand = compileUnaryExpr(ctx.unaryExpr());
            return new IncrementExprImpl(operand, "--", true);
        } else {
            return compilePostfixExpr(ctx.postfixExpr());
        }
    }
    
    /**
     * Compile postfix expression (calls, member access, indexing)
     */
    private CompiledExpr compilePostfixExpr(clnParser.PostfixExprContext ctx) {
        CompiledExpr base = compilePrimaryExpr(ctx.primaryExpr());
        
        for (clnParser.PostfixOpContext op : ctx.postfixOp()) {
            if (op.LPAREN() != null) {
                // Function call
                List<CompiledExpr> arguments = new ArrayList<>();
                if (op.argList() != null) {
                    for (clnParser.ExprContext arg : op.argList().expr()) {
                        arguments.add(compileExpression(arg));
                    }
                }
                base = new CallExprImpl(base, arguments);
            } else if (op.DOT() != null) {
                // Member access
                String member = op.ID().getText();
                base = new MemberAccessExprImpl(base, member);
            } else if (op.LBRACK() != null) {
                // Index access
                CompiledExpr index = compileExpression(op.expr());
                base = new IndexAccessExprImpl(base, index);
            } else if (op.INC() != null) {
                // Postfix increment
                base = new IncrementExprImpl(base, "++", false);
            } else if (op.DEC() != null) {
                // Postfix decrement
                base = new IncrementExprImpl(base, "--", false);
            }
        }
        
        return base;
    }
    
    /**
     * Compile primary expression (literals, identifiers, parenthesized)
     */
    private CompiledExpr compilePrimaryExpr(clnParser.PrimaryExprContext ctx) {
        if (ctx.INT_LIT() != null) {
            String text = ctx.INT_LIT().getText();
            return new IntLiteralExprImpl(text);
        } else if (ctx.DEC_LIT() != null) {
            String text = ctx.DEC_LIT().getText();
            return new DecLiteralExprImpl(text);
        } else if (ctx.BOOL_LIT() != null) {
            boolean value = Boolean.parseBoolean(ctx.BOOL_LIT().getText());
            return new BoolLiteralExprImpl(value);
        } else if (ctx.STRING_LIT() != null) {
            String text = ctx.STRING_LIT().getText();
            // Remove quotes and handle escape sequences
            String value = text.substring(1, text.length() - 1);
            value = value.replace("\\n", "\n")
                         .replace("\\t", "\t")
                         .replace("\\\"", "\"")
                         .replace("\\\\", "\\");
            return new StringLiteralExprImpl(value);
        } else if (ctx.arrayLiteral() != null) {
            return compileArrayLiteral(ctx.arrayLiteral());
        } else if (ctx.structLiteral() != null) {
            return compileStructLiteral(ctx.structLiteral());
        } else if (ctx.ID() != null) {
            String idName = ctx.ID().getText();
            // Try to resolve with index from current scope
            if (currentScope != null) {
                VarInfo varInfo = currentScope.getVariableInfo(idName);
                if (varInfo != null) {
                    return new IdentifierExprImpl(idName, varInfo.type, varInfo.index);
                }
            }
            // Not in local scope or no scope, use name-based lookup
            return new IdentifierExprImpl(idName);
        } else if (ctx.expr() != null) {
            // Parenthesized expression
            return compileExpression(ctx.expr());
        }
        
        throw new IllegalArgumentException("Unknown primary expression type: " + ctx.getText());
    }
    
    /**
     * Compile struct literal
     */
    private CompiledExpr compileStructLiteral(clnParser.StructLiteralContext ctx) {
        String typeName = ctx.qualifiedName().getText();
        List<StructLiteralExprImpl.FieldInit> fields = new ArrayList<>();
        
        if (ctx.fieldInitList() != null) {
            for (clnParser.FieldInitContext fieldCtx : ctx.fieldInitList().fieldInit()) {
                String fieldName = fieldCtx.ID().getText();
                CompiledExpr value = compileExpression(fieldCtx.expr());
                fields.add(new StructLiteralExprImpl.FieldInit(fieldName, value));
            }
        }
        
        // WORKAROUND: If this is a simple identifier with no fields (e.g., "test()"),
        // it's actually a function call, not a struct literal. The grammar is ambiguous.
        // Struct literals should have at least one field or be a qualified name.
        if (fields.isEmpty() && !typeName.contains(".")) {
            // This is actually a function call with no arguments, not a struct literal
            // Try to resolve with index from current scope (though functions are usually global)
            CompiledExpr functionExpr;
            if (currentScope != null) {
                VarInfo varInfo = currentScope.getVariableInfo(typeName);
                if (varInfo != null) {
                    functionExpr = new IdentifierExprImpl(typeName, varInfo.type, varInfo.index);
                } else {
                    functionExpr = new IdentifierExprImpl(typeName);
                }
            } else {
                functionExpr = new IdentifierExprImpl(typeName);
            }
            return new CallExprImpl(functionExpr, new ArrayList<>());
        }
        
        return new StructLiteralExprImpl(typeName, fields);
    }
    
    /**
     * Compile array literal
     */
    private CompiledExpr compileArrayLiteral(clnParser.ArrayLiteralContext ctx) {
        List<CompiledExpr> elements = new ArrayList<>();
        
        if (ctx.exprList() != null) {
            for (clnParser.ExprContext exprCtx : ctx.exprList().expr()) {
                elements.add(compileExpression(exprCtx));
            }
        }
        
        return new ArrayLiteralExprImpl(elements);
    }
    
    /**
     * Check if a type name is a primitive type
     */
    private boolean isPrimitiveType(String typeName) {
        // Strip array brackets for checking base type
        String baseType = typeName.replaceAll("\\[\\]", "");
        return baseType.equals("int") || baseType.equals("bool") || baseType.equals("string") || baseType.equals("dec");
    }
    
    /**
     * Validate that a type exists (either primitive or user-defined)
     */
    private void validateType(String typeName, int lineNumber) {
        // Strip array brackets for checking base type
        String baseType = typeName.replaceAll("\\[\\]", "");
        
        if (!isPrimitiveType(baseType) && !definedTypes.contains(baseType)) {
            throw new IllegalArgumentException(
                "line " + lineNumber + ": Unknown type '" + baseType + "'. " +
                "Type must be one of the primitive types (int, bool, string, dec) or a declared struct/union."
            );
        }
    }
    
    /**
     * Extract DecimalTypeInfo from a type context
     */
    private DecimalTypeInfo extractDecimalTypeInfo(clnParser.TypeContext typeCtx) {
        if (typeCtx == null || typeCtx.baseType() == null || typeCtx.baseType().primitiveType() == null) {
            return DecimalTypeInfo.DEFAULT;
        }
        
        clnParser.PrimitiveTypeContext primitiveType = typeCtx.baseType().primitiveType();
        if (primitiveType.decimalType() == null) {
            return DecimalTypeInfo.DEFAULT;
        }
        
        clnParser.DecimalTypeContext decimalType = primitiveType.decimalType();
        
        // Check if it has parameters (precision)
        if (decimalType.INT_LIT() == null) {
            return DecimalTypeInfo.DEFAULT;
        }
        
        int precision = Integer.parseInt(decimalType.INT_LIT().getText());
        
        // Check if it has rounding mode
        if (decimalType.ID() != null) {
            String roundingModeStr = decimalType.ID().getText();
            try {
                RoundingMode roundingMode = RoundingMode.valueOf(roundingModeStr);
                return new DecimalTypeInfo(precision, roundingMode);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "line " + decimalType.ID().getSymbol().getLine() + 
                    ": Invalid rounding mode '" + roundingModeStr + "'. " +
                    "Valid values are: UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN, HALF_EVEN, UNNECESSARY"
                );
            }
        }
        
        return new DecimalTypeInfo(precision);
    }
}
