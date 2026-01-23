package org.clnlang.ast.visitor;

import org.clnlang.ast.visitor.ASTVisitor;

import org.clnlang.ast.ASTNode;
import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.*;
import org.clnlang.ast.statement.*;

/**
 * Detailed AST printer that shows the full structure including statements and
 * expressions.
 */
public class DetailedASTPrinter implements ASTVisitor {

    private int indentLevel = 0;

    @Override
    public void visit(ProgramNode node) {
        println("Program:");
        indentLevel++;

        if (node.getPackageDecl() != null) {
            node.getPackageDecl().accept(this);
        }
        for (ImportDeclNode imp : node.getImports()) {
            imp.accept(this);
        }
        for (ASTNode decl : node.getDeclarations()) {
            decl.accept(this);
        }
        indentLevel--;
    }

    public void visit(PackageDeclNode node) {
        println("Package: " + node.getPackageName());
    }

    public void visit(ImportDeclNode node) {
        println("Import: " + node.getImportPath() + (node.isWildcard() ? ".*" : ""));
    }

    public void visit(StructDeclNode node) {
        println((node.isExposed() ? "expose " : "") + "Struct: " + node.getName());
        for (StructDeclNode.FieldDecl field : node.getFields()) {
            println("Field: " + field.getType() + " " + field.getName());
        }
    }

    public void visit(UnionDeclNode node) {
        println((node.isExposed() ? "expose " : "") + "Union: " + node.getName());
        for (String member : node.getMembers()) {
            println("Member: " + member);
        }
    }

    public void visit(FunctionDeclNode node) {
        print((node.isExposed() ? "expose " : "") + "Function: (");
        for (int i = 0; i < node.getReturnVars().size(); i++) {
            if (i > 0) System.out.print(", ");
            FunctionDeclNode.ReturnVar ret = node.getReturnVars().get(i);
            System.out.print(ret.getType() + " " + ret.getName());
        }
        System.out.print(") " + node.getName() + "(");
        for (int i = 0; i < node.getParameters().size(); i++) {
            FunctionDeclNode.Parameter param = node.getParameters().get(i);
            System.out.print(param.getType() + " " + param.getName());
        }
        System.out.println(")");
        if (node.getBlock() != null) {
            indentLevel++;
            printBlock(node.getBlock());
            indentLevel--;
        }
    }

    private void printBlock(BlockNode block) {
        println("Block:");
        for (Stmt stmt : block.getStatements()) {
            printStmt(stmt);
        }
    }

    private void printStmt(Stmt stmt) {
        if (stmt instanceof VarDeclStmt) {
            VarDeclStmt varDecl = (VarDeclStmt) stmt;
            println("VarDecl: " + stmt);
        } else if (stmt instanceof AssignStmt) {
            println("Assign: " + stmt);
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) stmt;
            println("If: " + ifStmt.getCondition());
            println("Then:");
            printBlock(ifStmt.getThenBlock());
            if (ifStmt.getElseBlock() != null) {
                println("Else:");
                indentLevel++;
                printBlock(ifStmt.getElseBlock());
                indentLevel--;
            }
        } else if (stmt instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) stmt;
            println("While: " + whileStmt.getCondition());
            printBlock(whileStmt.getBody());
        } else if (stmt instanceof SwitchStmt) {
            SwitchStmt switchStmt = (SwitchStmt) stmt;
            println("Switch: " + switchStmt.getExpression());
            for (SwitchStmt.CaseClause caseClause : switchStmt.getCases()) {
                println(caseClause.toString());
            }
        } else if (stmt instanceof ReturnStmt) {
            println("Return: " + stmt);
        } else if (stmt instanceof ExprStmt) {
            println("ExprStmt: " + stmt);
        } else if (stmt instanceof TupleAssignStmt) {
            println("TupleAssign: " + stmt);
        } else if (stmt instanceof EmptyStmt) {
            println("EmptyStmt");
        } else {
            println("Unknown statement: " + stmt);
        }
    }

    private void print(String message) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  ");
        }
        System.out.print(message);
    }

    private void println(String message) {
        print(message);
        System.out.println();
    }
}