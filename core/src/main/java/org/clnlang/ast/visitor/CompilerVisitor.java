package org.clnlang.ast.visitor;

import org.clnlang.compile.BlockImpl;
import org.clnlang.compile.CompiledAction;
import org.clnlang.compile.CompiledExpr;
import org.clnlang.compile.declaration.*;
import org.clnlang.compile.expression.*;
import org.clnlang.compile.statement.*;
import org.clnlang.parser.clnBaseVisitor;
import org.clnlang.parser.clnParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compiles ANTLR parse tree into executable CompiledAction objects.
 * This is the bridge between parsing and execution.
 */
public class CompilerVisitor extends clnBaseVisitor<Object> {
    
    // Track the current function being compiled to access return variables
    private FunctionDeclImpl currentFunction = null;
    
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
            }
        }
        
        // Set current function context for compiling return statements
        FunctionDeclImpl previousFunction = this.currentFunction;
        this.currentFunction = func;
        
        try {
            // Compile body
            BlockImpl body = compileBlock(ctx.block());
            func.setBlock(body);
        } finally {
            // Restore previous function context
            this.currentFunction = previousFunction;
        }
        
        return func;
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
            
            // Validate field type
            validateType(fieldType, field.type().getStart().getLine());
            
            struct.addField(fieldType, fieldName);
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
        
        // Validate type
        validateType(type, ctx.type().getStart().getLine());
        
        CompiledExpr initializer = compileExpression(ctx.expr());
        
        return new VarDeclStmtImpl(isVar, type, name, initializer);
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
        CompiledExpr base = new IdentifierExprImpl(ctx.ID().getText());
        
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
        } else if (ctx.structLiteral() != null) {
            return compileStructLiteral(ctx.structLiteral());
        } else if (ctx.ID() != null) {
            return new IdentifierExprImpl(ctx.ID().getText());
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
            CompiledExpr functionExpr = new IdentifierExprImpl(typeName);
            return new CallExprImpl(functionExpr, new ArrayList<>());
        }
        
        return new StructLiteralExprImpl(typeName, fields);
    }
    
    /**
     * Check if a type name is a primitive type
     */
    private boolean isPrimitiveType(String typeName) {
        // Strip array brackets for checking base type
        String baseType = typeName.replaceAll("\\[\\]", "");
        return baseType.equals("int") || baseType.equals("bool") || baseType.equals("string");
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
                "Type must be one of the primitive types (int, bool, string) or a declared struct/union."
            );
        }
    }
}
