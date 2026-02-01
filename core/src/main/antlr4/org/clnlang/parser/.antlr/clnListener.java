// Generated from /home/gszabo/Projects/Else/clean/core/src/main/antlr4/org/clnlang/parser/cln.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link clnParser}.
 */
public interface clnListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link clnParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(clnParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(clnParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#topLevelDecl}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelDecl(clnParser.TopLevelDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#topLevelDecl}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelDecl(clnParser.TopLevelDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#packageDecl}.
	 * @param ctx the parse tree
	 */
	void enterPackageDecl(clnParser.PackageDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#packageDecl}.
	 * @param ctx the parse tree
	 */
	void exitPackageDecl(clnParser.PackageDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void enterImportDecl(clnParser.ImportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void exitImportDecl(clnParser.ImportDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(clnParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(clnParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#globalVarDecl}.
	 * @param ctx the parse tree
	 */
	void enterGlobalVarDecl(clnParser.GlobalVarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#globalVarDecl}.
	 * @param ctx the parse tree
	 */
	void exitGlobalVarDecl(clnParser.GlobalVarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(clnParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(clnParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(clnParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(clnParser.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(clnParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(clnParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#structDecl}.
	 * @param ctx the parse tree
	 */
	void enterStructDecl(clnParser.StructDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#structDecl}.
	 * @param ctx the parse tree
	 */
	void exitStructDecl(clnParser.StructDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#structFieldDecl}.
	 * @param ctx the parse tree
	 */
	void enterStructFieldDecl(clnParser.StructFieldDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#structFieldDecl}.
	 * @param ctx the parse tree
	 */
	void exitStructFieldDecl(clnParser.StructFieldDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#unionDecl}.
	 * @param ctx the parse tree
	 */
	void enterUnionDecl(clnParser.UnionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#unionDecl}.
	 * @param ctx the parse tree
	 */
	void exitUnionDecl(clnParser.UnionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void enterUnionMember(clnParser.UnionMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#unionMember}.
	 * @param ctx the parse tree
	 */
	void exitUnionMember(clnParser.UnionMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(clnParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(clnParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#namedReturnSig}.
	 * @param ctx the parse tree
	 */
	void enterNamedReturnSig(clnParser.NamedReturnSigContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#namedReturnSig}.
	 * @param ctx the parse tree
	 */
	void exitNamedReturnSig(clnParser.NamedReturnSigContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#returnVar}.
	 * @param ctx the parse tree
	 */
	void enterReturnVar(clnParser.ReturnVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#returnVar}.
	 * @param ctx the parse tree
	 */
	void exitReturnVar(clnParser.ReturnVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(clnParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(clnParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(clnParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(clnParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(clnParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(clnParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(clnParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(clnParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#varDeclStmt}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclStmt(clnParser.VarDeclStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#varDeclStmt}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclStmt(clnParser.VarDeclStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#varBinding}.
	 * @param ctx the parse tree
	 */
	void enterVarBinding(clnParser.VarBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#varBinding}.
	 * @param ctx the parse tree
	 */
	void exitVarBinding(clnParser.VarBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmt(clnParser.AssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#assignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmt(clnParser.AssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#tupleAssignStmt}.
	 * @param ctx the parse tree
	 */
	void enterTupleAssignStmt(clnParser.TupleAssignStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#tupleAssignStmt}.
	 * @param ctx the parse tree
	 */
	void exitTupleAssignStmt(clnParser.TupleAssignStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#tupleBind}.
	 * @param ctx the parse tree
	 */
	void enterTupleBind(clnParser.TupleBindContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#tupleBind}.
	 * @param ctx the parse tree
	 */
	void exitTupleBind(clnParser.TupleBindContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterLvalue(clnParser.LvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitLvalue(clnParser.LvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#lvalueSuffix}.
	 * @param ctx the parse tree
	 */
	void enterLvalueSuffix(clnParser.LvalueSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#lvalueSuffix}.
	 * @param ctx the parse tree
	 */
	void exitLvalueSuffix(clnParser.LvalueSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(clnParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(clnParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(clnParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(clnParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#switchStmt}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStmt(clnParser.SwitchStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#switchStmt}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStmt(clnParser.SwitchStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#caseClause}.
	 * @param ctx the parse tree
	 */
	void enterCaseClause(clnParser.CaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#caseClause}.
	 * @param ctx the parse tree
	 */
	void exitCaseClause(clnParser.CaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(clnParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(clnParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(clnParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(clnParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(clnParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(clnParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(clnParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(clnParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(clnParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(clnParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(clnParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(clnParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelExpr(clnParser.RelExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#relExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelExpr(clnParser.RelExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(clnParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(clnParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(clnParser.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(clnParser.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(clnParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#unaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(clnParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpr(clnParser.PostfixExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#postfixExpr}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpr(clnParser.PostfixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#postfixOp}.
	 * @param ctx the parse tree
	 */
	void enterPostfixOp(clnParser.PostfixOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#postfixOp}.
	 * @param ctx the parse tree
	 */
	void exitPostfixOp(clnParser.PostfixOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(clnParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(clnParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(clnParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(clnParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#structLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStructLiteral(clnParser.StructLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#structLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStructLiteral(clnParser.StructLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#fieldInitList}.
	 * @param ctx the parse tree
	 */
	void enterFieldInitList(clnParser.FieldInitListContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#fieldInitList}.
	 * @param ctx the parse tree
	 */
	void exitFieldInitList(clnParser.FieldInitListContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#fieldInit}.
	 * @param ctx the parse tree
	 */
	void enterFieldInit(clnParser.FieldInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#fieldInit}.
	 * @param ctx the parse tree
	 */
	void exitFieldInit(clnParser.FieldInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(clnParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(clnParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link clnParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(clnParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link clnParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(clnParser.QualifiedNameContext ctx);
}