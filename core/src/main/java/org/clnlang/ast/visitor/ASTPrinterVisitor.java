package org.clnlang.ast.visitor;

import org.clnlang.ast.ASTNode;
import org.clnlang.ast.declaration.FunctionDeclNode;
import org.clnlang.ast.declaration.ImportDeclNode;
import org.clnlang.ast.declaration.PackageDeclNode;
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.declaration.StructDeclNode;
import org.clnlang.ast.declaration.UnionDeclNode;

/**
 * Example visitor that prints information about the AST nodes.
 * This demonstrates how to implement a custom AST visitor.
 */
public class ASTPrinterVisitor implements ASTVisitor {

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
        System.out.print(")");
        if (node.getBlock() != null) {
            System.out.print(" " + node.getBlock());
        }
        System.out.println();
    }

    private void print(String message) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("  ");
        }
        System.out.print(message);
    }

    private void println(String message) {
        print(message);
    }
}
