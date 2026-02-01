package org.clnlang.parser;

import java.util.ArrayList;
import java.util.List;

import org.clnlang.ast.ASTNode;
import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.ImportDeclNode;
import org.clnlang.ast.declaration.PackageDeclNode;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.declaration.StructDeclNode;
import org.clnlang.ast.declaration.UnionDeclNode;
import org.clnlang.ast.expression.BinaryExpr;
import org.clnlang.ast.expression.BoolLiteralExpr;
import org.clnlang.ast.expression.CallExpr;
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
import org.clnlang.ast.statement.Stmt;
import org.clnlang.ast.statement.SwitchStmt;
import org.clnlang.ast.statement.TupleAssignStmt;
import org.clnlang.ast.statement.VarDeclStmt;
import org.clnlang.ast.statement.WhileStmt;

/**
 * ANTLR visitor that builds an AST from the parse tree.
 */
public class ClnASTBuilder extends clnBaseVisitor<ASTNode> {
    
    @Override
    public ASTNode visitProgram(clnParser.ProgramContext ctx) {
        ProgramNode program = new ProgramNode();
        
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.packageDecl() != null) {
                PackageDeclNode pkg = (PackageDeclNode) visitPackageDecl(topLevel.packageDecl());
                program.setPackageDecl(pkg);
            } else if (topLevel.importDecl() != null) {
                ImportDeclNode imp = (ImportDeclNode) visitImportDecl(topLevel.importDecl());
                program.addImport(imp);
            } else if (topLevel.decl() != null) {
                ASTNode decl = visitDecl(topLevel.decl());
                if (decl != null) {
                    program.addDeclaration(decl);
                }
            }
        }
        
        return program;
    }
    
    @Override
    public ASTNode visitPackageDecl(clnParser.PackageDeclContext ctx) {
        String packageName = getQualifiedName(ctx.qualifiedName());
        return new PackageDeclNode(packageName);
    }
    
    @Override
    public ASTNode visitImportDecl(clnParser.ImportDeclContext ctx) {
        String importPath = getQualifiedName(ctx.qualifiedName());
        boolean isWildcard = ctx.STAR() != null;
        return new ImportDeclNode(importPath, isWildcard);
    }
    
    @Override
    public ASTNode visitDecl(clnParser.DeclContext ctx) {
        boolean isExposed = ctx.EXPOSE() != null;
        
        if (ctx.structDecl() != null) {
            return visitStructDecl(ctx.structDecl(), isExposed);
        } else if (ctx.unionDecl() != null) {
            return visitUnionDecl(ctx.unionDecl(), isExposed);
        } else if (ctx.functionDecl() != null) {
            return visitFunctionDecl(ctx.functionDecl(), isExposed);
        }
        // TODO: Handle globalVarDecl
        
        return null;
    }
    
    public ASTNode visitStructDecl(clnParser.StructDeclContext ctx, boolean isExposed) {
        String structName = ctx.ID().getText();
        StructDeclNode struct = new StructDeclNode(structName, isExposed);
        
        for (clnParser.StructFieldDeclContext field : ctx.structFieldDecl()) {
            String fieldType = getTypeString(field.type());
            String fieldName = field.ID().getText();
            struct.addField(fieldType, fieldName);
        }
        
        return struct;
    }
    
    public ASTNode visitFunctionDecl(clnParser.FunctionDeclContext ctx, boolean isExposed) {
        String functionName = ctx.ID().getText();
        FunctionDeclNode function = new FunctionDeclNode(functionName, isExposed);
        
        // Process return variables
        clnParser.NamedReturnSigContext returnSig = ctx.namedReturnSig();
        if (returnSig != null) {
            for (clnParser.ReturnVarContext retVar : returnSig.returnVar()) {
                String type = getTypeString(retVar.type());
                String name = retVar.ID().getText();
                function.addReturnVar(type, name);
            }
        }
        
        // Process parameters
        clnParser.ParamListContext paramList = ctx.paramList();
        if (paramList != null) {
            for (clnParser.ParamContext param : paramList.param()) {
                String type = getTypeString(param.type());
                String name = param.ID().getText();
                function.addParameter(type, name);
            }
        }
        
        // Process block
        if (ctx.block() != null) {
            BlockNode block = visitBlock(ctx.block());
            function.setBlock(block);
        }
        
        return function;
    }
    
    public UnionDeclNode visitUnionDecl(clnParser.UnionDeclContext ctx, boolean isExposed) {
        String unionName = ctx.ID().getText();
        UnionDeclNode union = new UnionDeclNode(unionName, isExposed);
        
        for (clnParser.UnionMemberContext member : ctx.unionMember()) {
            String memberType = getQualifiedName(member.qualifiedName());
            union.addMember(memberType);
        }
        
        return union;
    }
    
    public BlockNode visitBlock(clnParser.BlockContext ctx) {
        BlockNode block = new BlockNode();
        for (clnParser.StmtContext stmtCtx : ctx.stmt()) {
            Stmt stmt = visitStmt(stmtCtx);
            if (stmt != null) {
                block.addStatement(stmt);
            }
        }
        return block;
    }
    
    public Stmt visitStmt(clnParser.StmtContext ctx) {
        if (ctx.block() != null) {
            // Block statement - but BlockNode is not a Stmt, so we skip it for now
            // Could wrap it in a BlockStmt if needed
            return null;
        } else if (ctx.varDeclStmt() != null) {
            return visitVarDeclStmt(ctx.varDeclStmt());
        } else if (ctx.assignStmt() != null) {
            return visitAssignStmt(ctx.assignStmt());
        } else if (ctx.tupleAssignStmt() != null) {
            return visitTupleAssignStmt(ctx.tupleAssignStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {
            return visitWhileStmt(ctx.whileStmt());
        } else if (ctx.switchStmt() != null) {
            return visitSwitchStmt(ctx.switchStmt());
        } else if (ctx.returnStmt() != null) {
            return visitReturnStmt(ctx.returnStmt());
        } else if (ctx.exprStmt() != null) {
            return visitExprStmt(ctx.exprStmt());
        } else if (ctx.SEMI() != null) {
            return new EmptyStmt();
        }
        return null;
    }
    
    public VarDeclStmt visitVarDeclStmt(clnParser.VarDeclStmtContext ctx) {
        clnParser.VarBindingContext binding = ctx.varBinding();
        boolean isVar = binding.VAR() != null;
        String type = getTypeString(binding.type());
        String name = binding.ID().getText();
        Expr initializer = visitExpr(binding.expr());
        return new VarDeclStmt(isVar, type, name, initializer);
    }
    
    public AssignStmt visitAssignStmt(clnParser.AssignStmtContext ctx) {
        Expr lvalue = visitLvalue(ctx.lvalue());
        Expr value = visitExpr(ctx.expr());
        return new AssignStmt(lvalue, value);
    }
    
    public TupleAssignStmt visitTupleAssignStmt(clnParser.TupleAssignStmtContext ctx) {
        List<TupleAssignStmt.TupleBind> bindings = new ArrayList<>();
        for (clnParser.TupleBindContext bindCtx : ctx.tupleBind()) {
            boolean isVar = bindCtx.VAR() != null;
            String type = getTypeString(bindCtx.type());
            String name = bindCtx.ID().getText();
            bindings.add(new TupleAssignStmt.TupleBind(isVar, type, name));
        }
        Expr value = visitExpr(ctx.expr());
        return new TupleAssignStmt(bindings, value);
    }
    
    public IfStmt visitIfStmt(clnParser.IfStmtContext ctx) {
        Expr condition = visitExpr(ctx.expr());
        BlockNode thenBlock = visitBlock(ctx.block(0));
        BlockNode elseBlock = ctx.block().size() > 1 ? visitBlock(ctx.block(1)) : null;
        return new IfStmt(condition, thenBlock, elseBlock);
    }
    
    public WhileStmt visitWhileStmt(clnParser.WhileStmtContext ctx) {
        Expr condition = visitExpr(ctx.expr());
        BlockNode body = visitBlock(ctx.block());
        return new WhileStmt(condition, body);
    }
    
    public SwitchStmt visitSwitchStmt(clnParser.SwitchStmtContext ctx) {
        Expr expression = visitExpr(ctx.expr());
        SwitchStmt switchStmt = new SwitchStmt(expression);
        
        for (clnParser.CaseClauseContext caseCtx : ctx.caseClause()) {
            if (caseCtx.CASE() != null) {
                String qualifiedName = getQualifiedName(caseCtx.qualifiedName());
                String varName = caseCtx.ID().getText();
                SwitchStmt.CaseClause caseClause = new SwitchStmt.CaseClause(qualifiedName, varName, false);
                for (clnParser.StmtContext stmtCtx : caseCtx.stmt()) {
                    Stmt stmt = visitStmt(stmtCtx);
                    if (stmt != null) {
                        caseClause.addStatement(stmt);
                    }
                }
                switchStmt.addCase(caseClause);
            } else if (caseCtx.DEFAULT() != null) {
                SwitchStmt.CaseClause defaultClause = new SwitchStmt.CaseClause(null, null, true);
                for (clnParser.StmtContext stmtCtx : caseCtx.stmt()) {
                    Stmt stmt = visitStmt(stmtCtx);
                    if (stmt != null) {
                        defaultClause.addStatement(stmt);
                    }
                }
                switchStmt.addCase(defaultClause);
            }
        }
        
        return switchStmt;
    }
    
    public ReturnStmt visitReturnStmt(clnParser.ReturnStmtContext ctx) {
        if (ctx.expr() != null) {
            // Single expression return
            return new ReturnStmt(visitExpr(ctx.expr()));
        } else if (ctx.exprList() != null) {
            // Tuple return
            List<Expr> exprs = new ArrayList<>();
            for (clnParser.ExprContext exprCtx : ctx.exprList().expr()) {
                exprs.add(visitExpr(exprCtx));
            }
            return new ReturnStmt(exprs);
        } else {
            // Empty return
            return new ReturnStmt();
        }
    }
    
    public ExprStmt visitExprStmt(clnParser.ExprStmtContext ctx) {
        Expr expr = visitExpr(ctx.expr());
        return new ExprStmt(expr);
    }
    
    // Expression visitors
    
    public Expr visitExpr(clnParser.ExprContext ctx) {
        return visitOrExpr(ctx.orExpr());
    }
    
    public Expr visitOrExpr(clnParser.OrExprContext ctx) {
        List<clnParser.AndExprContext> andExprs = ctx.andExpr();
        Expr result = visitAndExpr(andExprs.get(0));
        for (int i = 1; i < andExprs.size(); i++) {
            Expr right = visitAndExpr(andExprs.get(i));
            result = new BinaryExpr(result, "||", right);
        }
        return result;
    }
    
    public Expr visitAndExpr(clnParser.AndExprContext ctx) {
        List<clnParser.EqualityExprContext> eqExprs = ctx.equalityExpr();
        Expr result = visitEqualityExpr(eqExprs.get(0));
        for (int i = 1; i < eqExprs.size(); i++) {
            Expr right = visitEqualityExpr(eqExprs.get(i));
            result = new BinaryExpr(result, "&&", right);
        }
        return result;
    }
    
    public Expr visitEqualityExpr(clnParser.EqualityExprContext ctx) {
        List<clnParser.RelExprContext> relExprs = ctx.relExpr();
        Expr result = visitRelExpr(relExprs.get(0));
        
        for (int i = 1; i < relExprs.size(); i++) {
            String op = ctx.getChild(i * 2 - 1).getText(); // Get operator
            Expr right = visitRelExpr(relExprs.get(i));
            result = new BinaryExpr(result, op, right);
        }
        return result;
    }
    
    public Expr visitRelExpr(clnParser.RelExprContext ctx) {
        List<clnParser.AddExprContext> addExprs = ctx.addExpr();
        Expr result = visitAddExpr(addExprs.get(0));
        
        for (int i = 1; i < addExprs.size(); i++) {
            String op = ctx.getChild(i * 2 - 1).getText(); // Get operator
            Expr right = visitAddExpr(addExprs.get(i));
            result = new BinaryExpr(result, op, right);
        }
        return result;
    }
    
    public Expr visitAddExpr(clnParser.AddExprContext ctx) {
        List<clnParser.MulExprContext> mulExprs = ctx.mulExpr();
        Expr result = visitMulExpr(mulExprs.get(0));
        
        for (int i = 1; i < mulExprs.size(); i++) {
            String op = ctx.getChild(i * 2 - 1).getText(); // Get operator
            Expr right = visitMulExpr(mulExprs.get(i));
            result = new BinaryExpr(result, op, right);
        }
        return result;
    }
    
    public Expr visitMulExpr(clnParser.MulExprContext ctx) {
        List<clnParser.UnaryExprContext> unaryExprs = ctx.unaryExpr();
        Expr result = visitUnaryExpr(unaryExprs.get(0));
        
        for (int i = 1; i < unaryExprs.size(); i++) {
            String op = ctx.getChild(i * 2 - 1).getText(); // Get operator
            Expr right = visitUnaryExpr(unaryExprs.get(i));
            result = new BinaryExpr(result, op, right);
        }
        return result;
    }
    
    public Expr visitUnaryExpr(clnParser.UnaryExprContext ctx) {
        if (ctx.NOT() != null) {
            return new UnaryExpr("!", visitUnaryExpr(ctx.unaryExpr()));
        } else if (ctx.MINUS() != null) {
            return new UnaryExpr("-", visitUnaryExpr(ctx.unaryExpr()));
        } else {
            return visitPostfixExpr(ctx.postfixExpr());
        }
    }
    
    public Expr visitPostfixExpr(clnParser.PostfixExprContext ctx) {
        Expr result = visitPrimaryExpr(ctx.primaryExpr());
        
        for (clnParser.PostfixOpContext opCtx : ctx.postfixOp()) {
            if (opCtx.LPAREN() != null) {
                // Function call
                List<Expr> args = new ArrayList<>();
                if (opCtx.argList() != null) {
                    for (clnParser.ExprContext exprCtx : opCtx.argList().expr()) {
                        args.add(visitExpr(exprCtx));
                    }
                }
                result = new CallExpr(result, args);
            } else if (opCtx.DOT() != null) {
                // Member access
                String member = opCtx.ID().getText();
                result = new MemberAccessExpr(result, member);
            } else if (opCtx.LBRACK() != null) {
                // Index access
                Expr index = visitExpr(opCtx.expr());
                result = new IndexAccessExpr(result, index);
            }
        }
        
        return result;
    }
    
    public Expr visitPrimaryExpr(clnParser.PrimaryExprContext ctx) {
        if (ctx.INT_LIT() != null) {
            return new IntLiteralExpr(ctx.INT_LIT().getText());
        } else if (ctx.BOOL_LIT() != null) {
            return new BoolLiteralExpr(ctx.BOOL_LIT().getText().equals("true"));
        } else if (ctx.STRING_LIT() != null) {
            String text = ctx.STRING_LIT().getText();
            // Remove quotes and handle escape sequences
            String value = text.substring(1, text.length() - 1);
            return new StringLiteralExpr(value);
        } else if (ctx.structLiteral() != null) {
            return visitStructLiteral(ctx.structLiteral());
        } else if (ctx.ID() != null) {
            return new IdentifierExpr(ctx.ID().getText());
        } else if (ctx.expr() != null) {
            return visitExpr(ctx.expr());
        }
        return null;
    }
    
    public Expr visitStructLiteral(clnParser.StructLiteralContext ctx) {
        String typeName = getQualifiedName(ctx.qualifiedName());
        List<StructLiteralExpr.FieldInit> fields = new ArrayList<>();
        
        if (ctx.fieldInitList() != null) {
            for (clnParser.FieldInitContext fieldCtx : ctx.fieldInitList().fieldInit()) {
                String fieldName = fieldCtx.ID().getText();
                Expr value = visitExpr(fieldCtx.expr());
                fields.add(new StructLiteralExpr.FieldInit(fieldName, value));
            }
        }
        
        return new StructLiteralExpr(typeName, fields);
    }
    
    public Expr visitLvalue(clnParser.LvalueContext ctx) {
        Expr result = new IdentifierExpr(ctx.ID().getText());
        
        for (clnParser.LvalueSuffixContext suffix : ctx.lvalueSuffix()) {
            if (suffix.DOT() != null) {
                String member = suffix.ID().getText();
                result = new MemberAccessExpr(result, member);
            } else if (suffix.LBRACK() != null) {
                Expr index = visitExpr(suffix.expr());
                result = new IndexAccessExpr(result, index);
            }
        }
        
        return result;
    }
    
    /**
     * Extract a qualified name as a string (e.g., "foo.bar.baz").
     */
    private String getQualifiedName(clnParser.QualifiedNameContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ctx.ID().size(); i++) {
            if (i > 0) sb.append(".");
            sb.append(ctx.ID(i).getText());
        }
        return sb.toString();
    }
    
    /**
     * Extract type information as a string (including array brackets).
     */
    private String getTypeString(clnParser.TypeContext ctx) {
        StringBuilder sb = new StringBuilder();
        
        // Base type
        clnParser.BaseTypeContext baseType = ctx.baseType();
        if (baseType.primitiveType() != null) {
            sb.append(baseType.primitiveType().getText());
        } else if (baseType.qualifiedName() != null) {
            sb.append(getQualifiedName(baseType.qualifiedName()));
        }
        
        // Array brackets
        for (int i = 0; i < ctx.LBRACK().size(); i++) {
            sb.append("[]");
        }
        
        return sb.toString();
    }
}
