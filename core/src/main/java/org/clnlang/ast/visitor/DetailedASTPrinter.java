package org.clnlang.ast.visitor;

import org.clnlang.ast.ASTNode;
import org.clnlang.ast.BlockNode;
import org.clnlang.ast.declaration.*;
import org.clnlang.ast.expression.*;
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

    @Override
    public void visit(PackageDeclNode node) {
        println("Package: " + node.getPackageName());
    }

    @Override
    public void visit(ImportDeclNode node) {
        println("Import: " + node.getImportPath() + (node.isWildcard() ? ".*" : ""));
    }

    @Override
    public void visit(StructDeclNode node) {
        println((node.isExposed() ? "expose " : "") + "Struct: " + node.getName());
        indentLevel++;
        for (StructDeclNode.FieldDecl field : node.getFields()) {
            println("Field: " + field.getType() + " " + field.getName());
        }
        indentLevel--;
    }

    @Override
    public void visit(UnionDeclNode node) {
        println((node.isExposed() ? "expose " : "") + "Union: " + node.getName());
        indentLevel++;
        for (String member : node.getMembers()) {
            println("Member: " + member);
        }
        indentLevel--;
    }

    @Override
    public void visit(FunctionDeclNode node) {
        print((node.isExposed() ? "expose " : "") + "Function: (");
        for (int i = 0; i < node.getReturnVars().size(); i++) {
            if (i > 0) System.out.print(", ");
            FunctionDeclNode.ReturnVar ret = node.getReturnVars().get(i);
            System.out.print(ret.getType() + " " + ret.getName());
        }
        System.out.print(") " + node.getName() + "(");
        for (int i = 0; i < node.getParameters().size(); i++) {
            if (i > 0) System.out.print(", ");
            FunctionDeclNode.Parameter param = node.getParameters().get(i);
            System.out.print(param.getType() + " " + param.getName());
        }
        System.out.println(")");
        if (node.getBlock() != null) {
            indentLevel++;
            node.getBlock().accept(this);
            indentLevel--;
        }
    }

    @Override
    public void visit(BlockNode node) {
        println("Block {");
        indentLevel++;
        for (Stmt stmt : node.getStatements()) {
            stmt.accept(this);
        }
        indentLevel--;
        println("}");
    }

    // Statement visitors
    @Override
    public void visit(VarDeclStmt node) {
        println("VarDecl: " + (node.isVar() ? "var " : "") + node.getType() + " " + node.getName() 
                + (node.getInitializer() != null ? " = " + node.getInitializer() : ""));
    }

    @Override
    public void visit(AssignStmt node) {
        println("Assign: " + node.getLvalue() + " = " + node.getValue());
    }

    @Override
    public void visit(IfStmt node) {
        println("If: " + node.getCondition());
        indentLevel++;
        println("Then:");
        if (node.getThenBlock() != null) {
            node.getThenBlock().accept(this);
        }
        if (node.getElseBlock() != null) {
            println("Else:");
            node.getElseBlock().accept(this);
        }
        indentLevel--;
    }

    @Override
    public void visit(WhileStmt node) {
        println("While: " + node.getCondition());
        indentLevel++;
        if (node.getBody() != null) {
            node.getBody().accept(this);
        }
        indentLevel--;
    }

    @Override
    public void visit(SwitchStmt node) {
        println("Switch: " + node.getExpression());
        indentLevel++;
        for (SwitchStmt.CaseClause caseClause : node.getCases()) {
            if (caseClause.isDefault()) {
                println("Default:");
            } else {
                println("Case: " + caseClause.getQualifiedName() + " " + caseClause.getVarName());
            }
            indentLevel++;
            for (Stmt stmt : caseClause.getStatements()) {
                stmt.accept(this);
            }
            indentLevel--;
        }
        indentLevel--;
    }

    @Override
    public void visit(ReturnStmt node) {
        print("Return");
        if (!node.getReturnValues().isEmpty()) {
            System.out.print(": ");
            for (int i = 0; i < node.getReturnValues().size(); i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(node.getReturnValues().get(i));
            }
        }
        System.out.println();
    }

    @Override
    public void visit(ExprStmt node) {
        println("ExprStmt: " + node.getExpression());
    }

    @Override
    public void visit(TupleAssignStmt node) {
        println("TupleAssign: " + node.getBindings() + " = " + node.getValue());
    }

    @Override
    public void visit(EmptyStmt node) {
        println("EmptyStmt;");
    }

    // Expression visitors
    @Override
    public void visit(BinaryExpr node) {
        println("BinaryExpr: " + node.getLeft() + " " + node.getOperator() + " " + node.getRight());
    }

    @Override
    public void visit(BoolLiteralExpr node) {
        println("BoolLiteral: " + node.getValue());
    }

    @Override
    public void visit(CallExpr node) {
        print("Call: ");
        System.out.print(node.getFunction() + "(");
        for (int i = 0; i < node.getArguments().size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(node.getArguments().get(i));
        }
        System.out.println(")");
    }

    @Override
    public void visit(IdentifierExpr node) {
        println("Identifier: " + node.getName());
    }

    @Override
    public void visit(IndexAccessExpr node) {
        println("IndexAccess: " + node.getArray() + "[" + node.getIndex() + "]");
    }

    @Override
    public void visit(IntLiteralExpr node) {
        println("IntLiteral: " + node.getValue());
    }

    @Override
    public void visit(MemberAccessExpr node) {
        println("MemberAccess: " + node.getObject() + "." + node.getMember());
    }

    @Override
    public void visit(StringLiteralExpr node) {
        println("StringLiteral: \"" + node.getValue() + "\"");
    }

    @Override
    public void visit(StructLiteralExpr node) {
        print("StructLiteral: " + node.getTypeName() + " { ");
        for (int i = 0; i < node.getFields().size(); i++) {
            if (i > 0) System.out.print(", ");
            StructLiteralExpr.FieldInit field = node.getFields().get(i);
            System.out.print(field.getFieldName() + ": " + field.getValue());
        }
        System.out.println(" }");
    }

    @Override
    public void visit(UnaryExpr node) {
        println("UnaryExpr: " + node.getOperator() + node.getOperand());
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

    @Override
    public void visit(DecLiteralExpr node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}
