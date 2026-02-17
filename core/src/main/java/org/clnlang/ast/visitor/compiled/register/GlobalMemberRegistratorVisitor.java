package org.clnlang.ast.visitor.compiled.register;

import java.io.File;
import java.util.List;

import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.FunctionDeclNode.Parameter;
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
import org.clnlang.compiled.binary.Types;
import org.clnlang.compiled.register.GlobalRegistry;
import org.clnlang.compiled.register.elements.FunctionSignature;
import org.clnlang.compiled.register.elements.StructSignature;
import org.clnlang.compiled.register.elements.UnionSignature;
import org.clnlang.compiled.register.elements.VariableSignature;
import org.clnlang.parser.ClnASTBuilder;
import org.clnlang.parser.clnParser;

public class GlobalMemberRegistratorVisitor implements ASTVisitor {

    private GlobalRegistry globalRegistry;
    private ClnASTBuilder astBuilder;

    private File currentFile;
    private String currentPackage;

    public GlobalMemberRegistratorVisitor(GlobalRegistry globalRegistry){
        this.globalRegistry = globalRegistry;
        this.astBuilder = new ClnASTBuilder();
    }

    public void compileProgram(clnParser.ProgramContext ctx, File currentFile) {
        this.currentFile = currentFile;
        // First pass: collect all type definitions
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.decl() != null) {
                clnParser.DeclContext decl = topLevel.decl();
                boolean isExposed = decl.EXPOSE() != null;
                
                if (decl.structDecl() != null) {
                    StructDeclNode structNode = (StructDeclNode) astBuilder.visitStructDecl(decl.structDecl(), isExposed);
                    visit(structNode);
                } else if (decl.unionDecl() != null) {
                    UnionDeclNode unionNode = (UnionDeclNode) astBuilder.visitUnionDecl(decl.unionDecl(), isExposed);
                    visit(unionNode);
                } else if (decl.functionDecl() != null) {
                    FunctionDeclNode functionNode = (FunctionDeclNode) astBuilder.visitFunctionDecl(decl.functionDecl(), isExposed);
                    visit(functionNode);
                } else if (decl.globalVarDecl() != null) {
                    VarDeclStmt varDeclNode = (VarDeclStmt) astBuilder.visitGlobalVarDecl(decl.globalVarDecl());
                    visit(varDeclNode);
                }
            }
        }
        currentFile = null;
        return;
    }

    @Override
    public void visit(ProgramNode node) {
        return;        
    }

    @Override
    public void visit(PackageDeclNode node) {
        currentPackage = node.getPackageName();
    }

    @Override
    public void visit(ImportDeclNode node) {
        return;
    }

    @Override
    public void visit(StructDeclNode node) {
        StructSignature signature = new StructSignature(node.getName(), currentPackage, node.isExposed());
        globalRegistry.registerStruct(signature, currentFile);
    }

    @Override
    public void visit(UnionDeclNode node) {
        UnionSignature signature = new UnionSignature(node.getName(), currentPackage, node.isExposed());
        globalRegistry.registerUnion(signature, currentFile);
    }

    @Override
    public void visit(FunctionDeclNode node) {
        List<Parameter> parameters = node.getParameters();
        List<ReturnVar> returnVars = node.getReturnVars();
        String simpleReturnType = node.getSimpleReturnType();

        // Map parameters to Types[] and String[]
        Types[] parameterTypes = parameters.stream()
            .map(p -> Types.fromString(p.getType()))
            .toArray(Types[]::new);
        
        String[] parameterNames = parameters.stream()
            .map(Parameter::getName)
            .toArray(String[]::new);
        
        // Map return types
        Types[] returnTypes;
        if (simpleReturnType != null && !simpleReturnType.isEmpty()) {
            returnTypes = new Types[] { Types.fromString(simpleReturnType) };
        } else {
            returnTypes = returnVars.stream()
                .map(rv -> Types.fromString(rv.getType()))
                .toArray(Types[]::new);
        }
        
        FunctionSignature signature = new FunctionSignature(
            currentPackage, 
            node.getName(), 
            returnTypes, 
            parameterTypes, 
            parameterNames, 
            node.isExposed()
        );
        globalRegistry.registerFunction(signature, currentFile);
    }

    @Override
    public void visit(BlockNode node) {
        
        return;
    }

    @Override
    public void visit(AssignStmt node) {
        
        return;
    }

    @Override
    public void visit(EmptyStmt node) {
        
        return;
    }

    @Override
    public void visit(ExprStmt node) {
        
        return;
    }

    @Override
    public void visit(IfStmt node) {
        
        return;
    }

    @Override
    public void visit(ReturnStmt node) {
        
        return;
    }

    @Override
    public void visit(SwitchStmt node) {
        
        return;
    }

    @Override
    public void visit(TupleAssignStmt node) {
        
        return;
    }

    @Override
    public void visit(VarDeclStmt node) {
        VariableSignature signature = new VariableSignature(currentPackage, node.getName(), Types.fromString(node.getType()), node.isVar(), false);
        globalRegistry.registerVariable(signature, currentFile);
        return;
    }

    @Override
    public void visit(WhileStmt node) {
        
        return;
    }

    @Override
    public void visit(BinaryExpr node) {
        
        return;
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        
        return;
    }

    @Override
    public void visit(CallExpr node) {
        
        return;
    }

    @Override
    public void visit(DecLiteralExpr node) {
        
        return;
    }

    @Override
    public void visit(IdentifierExpr node) {
        
        return;
    }

    @Override
    public void visit(IndexAccessExpr node) {
        
        return;
    }

    @Override
    public void visit(IntLiteralExpr node) {
        
        return;
    }

    @Override
    public void visit(MemberAccessExpr node) {
        
        return;
    }

    @Override
    public void visit(StringLiteralExpr node) {
        
        return;
    }

    @Override
    public void visit(StructLiteralExpr node) {
        
        return;
    }

    @Override
    public void visit(UnaryExpr node) {
        
        return;
    }
    
}
