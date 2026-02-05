// Generated from /home/gszabo/Projects/Else/clean/core/src/main/antlr4/org/clnlang/parser/cln.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class clnParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PACKAGE=1, IMPORT=2, EXPOSE=3, STRUCT=4, UNION=5, VAR=6, IF=7, ELSE=8, 
		WHILE=9, SWITCH=10, CASE=11, DEFAULT=12, RETURN=13, INT_T=14, BOOL_T=15, 
		STRING_T=16, DEC_T=17, BOOL_LIT=18, INT_LIT=19, DEC_LIT=20, STRING_LIT=21, 
		LPAREN=22, RPAREN=23, LBRACE=24, RBRACE=25, LBRACK=26, RBRACK=27, SEMI=28, 
		COMMA=29, COLON=30, DOT=31, ASSIGN=32, PLUS=33, MINUS=34, STAR=35, SLASH=36, 
		INC=37, DEC=38, NOT=39, AND=40, OR=41, EQ=42, NEQ=43, LT=44, LTE=45, GT=46, 
		GTE=47, ID=48, LINE_COMMENT=49, BLOCK_COMMENT=50, WS=51;
	public static final int
		RULE_program = 0, RULE_topLevelDecl = 1, RULE_packageDecl = 2, RULE_importDecl = 3, 
		RULE_decl = 4, RULE_globalVarDecl = 5, RULE_type = 6, RULE_baseType = 7, 
		RULE_primitiveType = 8, RULE_structDecl = 9, RULE_structFieldDecl = 10, 
		RULE_unionDecl = 11, RULE_unionMember = 12, RULE_functionDecl = 13, RULE_returnType = 14, 
		RULE_namedReturnSig = 15, RULE_returnVar = 16, RULE_paramList = 17, RULE_param = 18, 
		RULE_block = 19, RULE_stmt = 20, RULE_varDeclStmt = 21, RULE_varBinding = 22, 
		RULE_assignStmt = 23, RULE_tupleAssignStmt = 24, RULE_tupleBind = 25, 
		RULE_lvalue = 26, RULE_lvalueSuffix = 27, RULE_ifStmt = 28, RULE_whileStmt = 29, 
		RULE_switchStmt = 30, RULE_caseClause = 31, RULE_returnStmt = 32, RULE_exprStmt = 33, 
		RULE_expr = 34, RULE_orExpr = 35, RULE_andExpr = 36, RULE_equalityExpr = 37, 
		RULE_relExpr = 38, RULE_addExpr = 39, RULE_mulExpr = 40, RULE_unaryExpr = 41, 
		RULE_postfixExpr = 42, RULE_postfixOp = 43, RULE_argList = 44, RULE_primaryExpr = 45, 
		RULE_arrayLiteral = 46, RULE_structLiteral = 47, RULE_fieldInitList = 48, 
		RULE_fieldInit = 49, RULE_exprList = 50, RULE_qualifiedName = 51;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "topLevelDecl", "packageDecl", "importDecl", "decl", "globalVarDecl", 
			"type", "baseType", "primitiveType", "structDecl", "structFieldDecl", 
			"unionDecl", "unionMember", "functionDecl", "returnType", "namedReturnSig", 
			"returnVar", "paramList", "param", "block", "stmt", "varDeclStmt", "varBinding", 
			"assignStmt", "tupleAssignStmt", "tupleBind", "lvalue", "lvalueSuffix", 
			"ifStmt", "whileStmt", "switchStmt", "caseClause", "returnStmt", "exprStmt", 
			"expr", "orExpr", "andExpr", "equalityExpr", "relExpr", "addExpr", "mulExpr", 
			"unaryExpr", "postfixExpr", "postfixOp", "argList", "primaryExpr", "arrayLiteral", 
			"structLiteral", "fieldInitList", "fieldInit", "exprList", "qualifiedName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'package'", "'import'", "'expose'", "'struct'", "'union'", "'var'", 
			"'if'", "'else'", "'while'", "'switch'", "'case'", "'default'", "'return'", 
			"'int'", "'bool'", "'string'", "'dec'", null, null, null, null, "'('", 
			"')'", "'{'", "'}'", "'['", "']'", "';'", "','", "':'", "'.'", "'='", 
			"'+'", "'-'", "'*'", "'/'", "'++'", "'--'", "'!'", "'&&'", "'||'", "'=='", 
			"'!='", "'<'", "'<='", "'>'", "'>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PACKAGE", "IMPORT", "EXPOSE", "STRUCT", "UNION", "VAR", "IF", 
			"ELSE", "WHILE", "SWITCH", "CASE", "DEFAULT", "RETURN", "INT_T", "BOOL_T", 
			"STRING_T", "DEC_T", "BOOL_LIT", "INT_LIT", "DEC_LIT", "STRING_LIT", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", 
			"COLON", "DOT", "ASSIGN", "PLUS", "MINUS", "STAR", "SLASH", "INC", "DEC", 
			"NOT", "AND", "OR", "EQ", "NEQ", "LT", "LTE", "GT", "GTE", "ID", "LINE_COMMENT", 
			"BLOCK_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "cln.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public clnParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(clnParser.EOF, 0); }
		public List<TopLevelDeclContext> topLevelDecl() {
			return getRuleContexts(TopLevelDeclContext.class);
		}
		public TopLevelDeclContext topLevelDecl(int i) {
			return getRuleContext(TopLevelDeclContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 281474981150846L) != 0)) {
				{
				{
				setState(104);
				topLevelDecl();
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TopLevelDeclContext extends ParserRuleContext {
		public PackageDeclContext packageDecl() {
			return getRuleContext(PackageDeclContext.class,0);
		}
		public ImportDeclContext importDecl() {
			return getRuleContext(ImportDeclContext.class,0);
		}
		public DeclContext decl() {
			return getRuleContext(DeclContext.class,0);
		}
		public TopLevelDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_topLevelDecl; }
	}

	public final TopLevelDeclContext topLevelDecl() throws RecognitionException {
		TopLevelDeclContext _localctx = new TopLevelDeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_topLevelDecl);
		try {
			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PACKAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				packageDecl();
				}
				break;
			case IMPORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				importDecl();
				}
				break;
			case EXPOSE:
			case STRUCT:
			case UNION:
			case VAR:
			case INT_T:
			case BOOL_T:
			case STRING_T:
			case DEC_T:
			case LPAREN:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				decl();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageDeclContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(clnParser.PACKAGE, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public PackageDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageDecl; }
	}

	public final PackageDeclContext packageDecl() throws RecognitionException {
		PackageDeclContext _localctx = new PackageDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_packageDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(PACKAGE);
			setState(118);
			qualifiedName();
			setState(119);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDeclContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(clnParser.IMPORT, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public TerminalNode DOT() { return getToken(clnParser.DOT, 0); }
		public TerminalNode STAR() { return getToken(clnParser.STAR, 0); }
		public ImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDecl; }
	}

	public final ImportDeclContext importDecl() throws RecognitionException {
		ImportDeclContext _localctx = new ImportDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(IMPORT);
			setState(122);
			qualifiedName();
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(123);
				match(DOT);
				setState(124);
				match(STAR);
				}
			}

			setState(127);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclContext extends ParserRuleContext {
		public StructDeclContext structDecl() {
			return getRuleContext(StructDeclContext.class,0);
		}
		public UnionDeclContext unionDecl() {
			return getRuleContext(UnionDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public GlobalVarDeclContext globalVarDecl() {
			return getRuleContext(GlobalVarDeclContext.class,0);
		}
		public TerminalNode EXPOSE() { return getToken(clnParser.EXPOSE, 0); }
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXPOSE) {
				{
				setState(129);
				match(EXPOSE);
				}
			}

			setState(136);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(132);
				structDecl();
				}
				break;
			case 2:
				{
				setState(133);
				unionDecl();
				}
				break;
			case 3:
				{
				setState(134);
				functionDecl();
				}
				break;
			case 4:
				{
				setState(135);
				globalVarDecl();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalVarDeclContext extends ParserRuleContext {
		public VarBindingContext varBinding() {
			return getRuleContext(VarBindingContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public GlobalVarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalVarDecl; }
	}

	public final GlobalVarDeclContext globalVarDecl() throws RecognitionException {
		GlobalVarDeclContext _localctx = new GlobalVarDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_globalVarDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			varBinding();
			setState(139);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public BaseTypeContext baseType() {
			return getRuleContext(BaseTypeContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(clnParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(clnParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(clnParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(clnParser.RBRACK, i);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			baseType();
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(142);
				match(LBRACK);
				setState(143);
				match(RBRACK);
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BaseTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public BaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseType; }
	}

	public final BaseTypeContext baseType() throws RecognitionException {
		BaseTypeContext _localctx = new BaseTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_baseType);
		try {
			setState(151);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_T:
			case BOOL_T:
			case STRING_T:
			case DEC_T:
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
				primitiveType();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(150);
				qualifiedName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode INT_T() { return getToken(clnParser.INT_T, 0); }
		public TerminalNode BOOL_T() { return getToken(clnParser.BOOL_T, 0); }
		public TerminalNode STRING_T() { return getToken(clnParser.STRING_T, 0); }
		public TerminalNode DEC_T() { return getToken(clnParser.DEC_T, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 245760L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructDeclContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(clnParser.STRUCT, 0); }
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LBRACE() { return getToken(clnParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(clnParser.RBRACE, 0); }
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public List<StructFieldDeclContext> structFieldDecl() {
			return getRuleContexts(StructFieldDeclContext.class);
		}
		public StructFieldDeclContext structFieldDecl(int i) {
			return getRuleContext(StructFieldDeclContext.class,i);
		}
		public StructDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structDecl; }
	}

	public final StructDeclContext structDecl() throws RecognitionException {
		StructDeclContext _localctx = new StructDeclContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_structDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(STRUCT);
			setState(156);
			match(ID);
			setState(157);
			match(LBRACE);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 281474976956480L) != 0)) {
				{
				{
				setState(158);
				structFieldDecl();
				}
				}
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(164);
			match(RBRACE);
			setState(165);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructFieldDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public TerminalNode VAR() { return getToken(clnParser.VAR, 0); }
		public StructFieldDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structFieldDecl; }
	}

	public final StructFieldDeclContext structFieldDecl() throws RecognitionException {
		StructFieldDeclContext _localctx = new StructFieldDeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_structFieldDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(167);
				match(VAR);
				}
			}

			setState(170);
			type();
			setState(171);
			match(ID);
			setState(172);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnionDeclContext extends ParserRuleContext {
		public TerminalNode UNION() { return getToken(clnParser.UNION, 0); }
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LBRACE() { return getToken(clnParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(clnParser.RBRACE, 0); }
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public List<UnionMemberContext> unionMember() {
			return getRuleContexts(UnionMemberContext.class);
		}
		public UnionMemberContext unionMember(int i) {
			return getRuleContext(UnionMemberContext.class,i);
		}
		public UnionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionDecl; }
	}

	public final UnionDeclContext unionDecl() throws RecognitionException {
		UnionDeclContext _localctx = new UnionDeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(UNION);
			setState(175);
			match(ID);
			setState(176);
			match(LBRACE);
			setState(178); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(177);
				unionMember();
				}
				}
				setState(180); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(182);
			match(RBRACE);
			setState(183);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnionMemberContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public UnionMemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unionMember; }
	}

	public final UnionMemberContext unionMember() throws RecognitionException {
		UnionMemberContext _localctx = new UnionMemberContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_unionMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			qualifiedName();
			setState(186);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(188);
				returnType();
				}
				break;
			}
			setState(191);
			match(ID);
			setState(192);
			match(LPAREN);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 281474976956416L) != 0)) {
				{
				setState(193);
				paramList();
				}
			}

			setState(196);
			match(RPAREN);
			setState(197);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public NamedReturnSigContext namedReturnSig() {
			return getRuleContext(NamedReturnSigContext.class,0);
		}
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_returnType);
		try {
			setState(201);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_T:
			case BOOL_T:
			case STRING_T:
			case DEC_T:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(199);
				type();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(200);
				namedReturnSig();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NamedReturnSigContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public List<ReturnVarContext> returnVar() {
			return getRuleContexts(ReturnVarContext.class);
		}
		public ReturnVarContext returnVar(int i) {
			return getRuleContext(ReturnVarContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public NamedReturnSigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedReturnSig; }
	}

	public final NamedReturnSigContext namedReturnSig() throws RecognitionException {
		NamedReturnSigContext _localctx = new NamedReturnSigContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_namedReturnSig);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(LPAREN);
			setState(204);
			returnVar();
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(205);
				match(COMMA);
				setState(206);
				returnVar();
				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(212);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnVarContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(clnParser.VAR, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(clnParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnVar; }
	}

	public final ReturnVarContext returnVar() throws RecognitionException {
		ReturnVarContext _localctx = new ReturnVarContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_returnVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(VAR);
			setState(215);
			type();
			setState(216);
			match(ID);
			setState(217);
			match(ASSIGN);
			setState(218);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			param();
			setState(225);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(221);
				match(COMMA);
				setState(222);
				param();
				}
				}
				setState(227);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			type();
			setState(229);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(clnParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(clnParser.RBRACE, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(LBRACE);
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454589957824L) != 0)) {
				{
				{
				setState(232);
				stmt();
				}
				}
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(238);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public VarDeclStmtContext varDeclStmt() {
			return getRuleContext(VarDeclStmtContext.class,0);
		}
		public AssignStmtContext assignStmt() {
			return getRuleContext(AssignStmtContext.class,0);
		}
		public TupleAssignStmtContext tupleAssignStmt() {
			return getRuleContext(TupleAssignStmtContext.class,0);
		}
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public WhileStmtContext whileStmt() {
			return getRuleContext(WhileStmtContext.class,0);
		}
		public SwitchStmtContext switchStmt() {
			return getRuleContext(SwitchStmtContext.class,0);
		}
		public ReturnStmtContext returnStmt() {
			return getRuleContext(ReturnStmtContext.class,0);
		}
		public ExprStmtContext exprStmt() {
			return getRuleContext(ExprStmtContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_stmt);
		try {
			setState(250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(241);
				varDeclStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(242);
				assignStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(243);
				tupleAssignStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(244);
				ifStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(245);
				whileStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(246);
				switchStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(247);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(248);
				exprStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(249);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarDeclStmtContext extends ParserRuleContext {
		public VarBindingContext varBinding() {
			return getRuleContext(VarBindingContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public VarDeclStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclStmt; }
	}

	public final VarDeclStmtContext varDeclStmt() throws RecognitionException {
		VarDeclStmtContext _localctx = new VarDeclStmtContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_varDeclStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			varBinding();
			setState(253);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarBindingContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(clnParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode VAR() { return getToken(clnParser.VAR, 0); }
		public VarBindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varBinding; }
	}

	public final VarBindingContext varBinding() throws RecognitionException {
		VarBindingContext _localctx = new VarBindingContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_varBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(255);
				match(VAR);
				}
			}

			setState(258);
			type();
			setState(259);
			match(ID);
			setState(260);
			match(ASSIGN);
			setState(261);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignStmtContext extends ParserRuleContext {
		public LvalueContext lvalue() {
			return getRuleContext(LvalueContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(clnParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public AssignStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignStmt; }
	}

	public final AssignStmtContext assignStmt() throws RecognitionException {
		AssignStmtContext _localctx = new AssignStmtContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			lvalue();
			setState(264);
			match(ASSIGN);
			setState(265);
			expr();
			setState(266);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleAssignStmtContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public List<TupleBindContext> tupleBind() {
			return getRuleContexts(TupleBindContext.class);
		}
		public TupleBindContext tupleBind(int i) {
			return getRuleContext(TupleBindContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public TerminalNode ASSIGN() { return getToken(clnParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public TupleAssignStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleAssignStmt; }
	}

	public final TupleAssignStmtContext tupleAssignStmt() throws RecognitionException {
		TupleAssignStmtContext _localctx = new TupleAssignStmtContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_tupleAssignStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(LPAREN);
			setState(269);
			tupleBind();
			setState(274);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(270);
				match(COMMA);
				setState(271);
				tupleBind();
				}
				}
				setState(276);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(277);
			match(RPAREN);
			setState(278);
			match(ASSIGN);
			setState(279);
			expr();
			setState(280);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TupleBindContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode VAR() { return getToken(clnParser.VAR, 0); }
		public TupleBindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tupleBind; }
	}

	public final TupleBindContext tupleBind() throws RecognitionException {
		TupleBindContext _localctx = new TupleBindContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_tupleBind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(282);
				match(VAR);
				}
			}

			setState(285);
			type();
			setState(286);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LvalueContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public List<LvalueSuffixContext> lvalueSuffix() {
			return getRuleContexts(LvalueSuffixContext.class);
		}
		public LvalueSuffixContext lvalueSuffix(int i) {
			return getRuleContext(LvalueSuffixContext.class,i);
		}
		public LvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lvalue; }
	}

	public final LvalueContext lvalue() throws RecognitionException {
		LvalueContext _localctx = new LvalueContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_lvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(ID);
			setState(292);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK || _la==DOT) {
				{
				{
				setState(289);
				lvalueSuffix();
				}
				}
				setState(294);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LvalueSuffixContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(clnParser.DOT, 0); }
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LBRACK() { return getToken(clnParser.LBRACK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(clnParser.RBRACK, 0); }
		public LvalueSuffixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lvalueSuffix; }
	}

	public final LvalueSuffixContext lvalueSuffix() throws RecognitionException {
		LvalueSuffixContext _localctx = new LvalueSuffixContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_lvalueSuffix);
		try {
			setState(301);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(295);
				match(DOT);
				setState(296);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 2);
				{
				setState(297);
				match(LBRACK);
				setState(298);
				expr();
				setState(299);
				match(RBRACK);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStmtContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(clnParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(clnParser.ELSE, 0); }
		public IfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStmt; }
	}

	public final IfStmtContext ifStmt() throws RecognitionException {
		IfStmtContext _localctx = new IfStmtContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_ifStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(IF);
			setState(304);
			match(LPAREN);
			setState(305);
			expr();
			setState(306);
			match(RPAREN);
			setState(307);
			block();
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(308);
				match(ELSE);
				setState(309);
				block();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(clnParser.WHILE, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStmt; }
	}

	public final WhileStmtContext whileStmt() throws RecognitionException {
		WhileStmtContext _localctx = new WhileStmtContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_whileStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(WHILE);
			setState(313);
			match(LPAREN);
			setState(314);
			expr();
			setState(315);
			match(RPAREN);
			setState(316);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStmtContext extends ParserRuleContext {
		public TerminalNode SWITCH() { return getToken(clnParser.SWITCH, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(clnParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(clnParser.RBRACE, 0); }
		public List<CaseClauseContext> caseClause() {
			return getRuleContexts(CaseClauseContext.class);
		}
		public CaseClauseContext caseClause(int i) {
			return getRuleContext(CaseClauseContext.class,i);
		}
		public SwitchStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStmt; }
	}

	public final SwitchStmtContext switchStmt() throws RecognitionException {
		SwitchStmtContext _localctx = new SwitchStmtContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_switchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			match(SWITCH);
			setState(319);
			match(LPAREN);
			setState(320);
			expr();
			setState(321);
			match(RPAREN);
			setState(322);
			match(LBRACE);
			setState(326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CASE || _la==DEFAULT) {
				{
				{
				setState(323);
				caseClause();
				}
				}
				setState(328);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(329);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaseClauseContext extends ParserRuleContext {
		public TerminalNode CASE() { return getToken(clnParser.CASE, 0); }
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode COLON() { return getToken(clnParser.COLON, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public TerminalNode DEFAULT() { return getToken(clnParser.DEFAULT, 0); }
		public CaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_caseClause; }
	}

	public final CaseClauseContext caseClause() throws RecognitionException {
		CaseClauseContext _localctx = new CaseClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_caseClause);
		int _la;
		try {
			setState(349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				match(CASE);
				setState(332);
				qualifiedName();
				setState(333);
				match(ID);
				setState(334);
				match(COLON);
				setState(338);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454589957824L) != 0)) {
					{
					{
					setState(335);
					stmt();
					}
					}
					setState(340);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(341);
				match(DEFAULT);
				setState(342);
				match(COLON);
				setState(346);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454589957824L) != 0)) {
					{
					{
					setState(343);
					stmt();
					}
					}
					setState(348);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStmtContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(clnParser.RETURN, 0); }
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public ReturnStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStmt; }
	}

	public final ReturnStmtContext returnStmt() throws RecognitionException {
		ReturnStmtContext _localctx = new ReturnStmtContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_returnStmt);
		int _la;
		try {
			setState(364);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(351);
				match(RETURN);
				setState(352);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(353);
				match(RETURN);
				setState(354);
				expr();
				setState(355);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(357);
				match(RETURN);
				setState(358);
				match(LPAREN);
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454304489472L) != 0)) {
					{
					setState(359);
					exprList();
					}
				}

				setState(362);
				match(RPAREN);
				setState(363);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprStmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(clnParser.SEMI, 0); }
		public ExprStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprStmt; }
	}

	public final ExprStmtContext exprStmt() throws RecognitionException {
		ExprStmtContext _localctx = new ExprStmtContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_exprStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			expr();
			setState(367);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public OrExprContext orExpr() {
			return getRuleContext(OrExprContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			orExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrExprContext extends ParserRuleContext {
		public List<AndExprContext> andExpr() {
			return getRuleContexts(AndExprContext.class);
		}
		public AndExprContext andExpr(int i) {
			return getRuleContext(AndExprContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(clnParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(clnParser.OR, i);
		}
		public OrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orExpr; }
	}

	public final OrExprContext orExpr() throws RecognitionException {
		OrExprContext _localctx = new OrExprContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_orExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			andExpr();
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(372);
				match(OR);
				setState(373);
				andExpr();
				}
				}
				setState(378);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AndExprContext extends ParserRuleContext {
		public List<EqualityExprContext> equalityExpr() {
			return getRuleContexts(EqualityExprContext.class);
		}
		public EqualityExprContext equalityExpr(int i) {
			return getRuleContext(EqualityExprContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(clnParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(clnParser.AND, i);
		}
		public AndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpr; }
	}

	public final AndExprContext andExpr() throws RecognitionException {
		AndExprContext _localctx = new AndExprContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_andExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			equalityExpr();
			setState(384);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(380);
				match(AND);
				setState(381);
				equalityExpr();
				}
				}
				setState(386);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExprContext extends ParserRuleContext {
		public List<RelExprContext> relExpr() {
			return getRuleContexts(RelExprContext.class);
		}
		public RelExprContext relExpr(int i) {
			return getRuleContext(RelExprContext.class,i);
		}
		public List<TerminalNode> EQ() { return getTokens(clnParser.EQ); }
		public TerminalNode EQ(int i) {
			return getToken(clnParser.EQ, i);
		}
		public List<TerminalNode> NEQ() { return getTokens(clnParser.NEQ); }
		public TerminalNode NEQ(int i) {
			return getToken(clnParser.NEQ, i);
		}
		public EqualityExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpr; }
	}

	public final EqualityExprContext equalityExpr() throws RecognitionException {
		EqualityExprContext _localctx = new EqualityExprContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_equalityExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			relExpr();
			setState(392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EQ || _la==NEQ) {
				{
				{
				setState(388);
				_la = _input.LA(1);
				if ( !(_la==EQ || _la==NEQ) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(389);
				relExpr();
				}
				}
				setState(394);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelExprContext extends ParserRuleContext {
		public List<AddExprContext> addExpr() {
			return getRuleContexts(AddExprContext.class);
		}
		public AddExprContext addExpr(int i) {
			return getRuleContext(AddExprContext.class,i);
		}
		public List<TerminalNode> LT() { return getTokens(clnParser.LT); }
		public TerminalNode LT(int i) {
			return getToken(clnParser.LT, i);
		}
		public List<TerminalNode> LTE() { return getTokens(clnParser.LTE); }
		public TerminalNode LTE(int i) {
			return getToken(clnParser.LTE, i);
		}
		public List<TerminalNode> GT() { return getTokens(clnParser.GT); }
		public TerminalNode GT(int i) {
			return getToken(clnParser.GT, i);
		}
		public List<TerminalNode> GTE() { return getTokens(clnParser.GTE); }
		public TerminalNode GTE(int i) {
			return getToken(clnParser.GTE, i);
		}
		public RelExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relExpr; }
	}

	public final RelExprContext relExpr() throws RecognitionException {
		RelExprContext _localctx = new RelExprContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_relExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			addExpr();
			setState(400);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 263882790666240L) != 0)) {
				{
				{
				setState(396);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 263882790666240L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(397);
				addExpr();
				}
				}
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AddExprContext extends ParserRuleContext {
		public List<MulExprContext> mulExpr() {
			return getRuleContexts(MulExprContext.class);
		}
		public MulExprContext mulExpr(int i) {
			return getRuleContext(MulExprContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(clnParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(clnParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(clnParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(clnParser.MINUS, i);
		}
		public AddExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addExpr; }
	}

	public final AddExprContext addExpr() throws RecognitionException {
		AddExprContext _localctx = new AddExprContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_addExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			mulExpr();
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(404);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(405);
				mulExpr();
				}
				}
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MulExprContext extends ParserRuleContext {
		public List<UnaryExprContext> unaryExpr() {
			return getRuleContexts(UnaryExprContext.class);
		}
		public UnaryExprContext unaryExpr(int i) {
			return getRuleContext(UnaryExprContext.class,i);
		}
		public List<TerminalNode> STAR() { return getTokens(clnParser.STAR); }
		public TerminalNode STAR(int i) {
			return getToken(clnParser.STAR, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(clnParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(clnParser.SLASH, i);
		}
		public MulExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mulExpr; }
	}

	public final MulExprContext mulExpr() throws RecognitionException {
		MulExprContext _localctx = new MulExprContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_mulExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			unaryExpr();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STAR || _la==SLASH) {
				{
				{
				setState(412);
				_la = _input.LA(1);
				if ( !(_la==STAR || _la==SLASH) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(413);
				unaryExpr();
				}
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExprContext extends ParserRuleContext {
		public UnaryExprContext unaryExpr() {
			return getRuleContext(UnaryExprContext.class,0);
		}
		public TerminalNode NOT() { return getToken(clnParser.NOT, 0); }
		public TerminalNode MINUS() { return getToken(clnParser.MINUS, 0); }
		public TerminalNode INC() { return getToken(clnParser.INC, 0); }
		public TerminalNode DEC() { return getToken(clnParser.DEC, 0); }
		public PostfixExprContext postfixExpr() {
			return getRuleContext(PostfixExprContext.class,0);
		}
		public UnaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpr; }
	}

	public final UnaryExprContext unaryExpr() throws RecognitionException {
		UnaryExprContext _localctx = new UnaryExprContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_unaryExpr);
		int _la;
		try {
			setState(422);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MINUS:
			case INC:
			case DEC:
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(419);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 979252543488L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(420);
				unaryExpr();
				}
				break;
			case BOOL_LIT:
			case INT_LIT:
			case DEC_LIT:
			case STRING_LIT:
			case LPAREN:
			case LBRACK:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(421);
				postfixExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixExprContext extends ParserRuleContext {
		public PrimaryExprContext primaryExpr() {
			return getRuleContext(PrimaryExprContext.class,0);
		}
		public List<PostfixOpContext> postfixOp() {
			return getRuleContexts(PostfixOpContext.class);
		}
		public PostfixOpContext postfixOp(int i) {
			return getRuleContext(PostfixOpContext.class,i);
		}
		public PostfixExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpr; }
	}

	public final PostfixExprContext postfixExpr() throws RecognitionException {
		PostfixExprContext _localctx = new PostfixExprContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_postfixExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			primaryExpr();
			setState(428);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 414535647232L) != 0)) {
				{
				{
				setState(425);
				postfixOp();
				}
				}
				setState(430);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PostfixOpContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public TerminalNode DOT() { return getToken(clnParser.DOT, 0); }
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LBRACK() { return getToken(clnParser.LBRACK, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(clnParser.RBRACK, 0); }
		public TerminalNode INC() { return getToken(clnParser.INC, 0); }
		public TerminalNode DEC() { return getToken(clnParser.DEC, 0); }
		public PostfixOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixOp; }
	}

	public final PostfixOpContext postfixOp() throws RecognitionException {
		PostfixOpContext _localctx = new PostfixOpContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_postfixOp);
		int _la;
		try {
			setState(444);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				match(LPAREN);
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454304489472L) != 0)) {
					{
					setState(432);
					argList();
					}
				}

				setState(435);
				match(RPAREN);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(436);
				match(DOT);
				setState(437);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 3);
				{
				setState(438);
				match(LBRACK);
				setState(439);
				expr();
				setState(440);
				match(RBRACK);
				}
				break;
			case INC:
				enterOuterAlt(_localctx, 4);
				{
				setState(442);
				match(INC);
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 5);
				{
				setState(443);
				match(DEC);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			expr();
			setState(451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(447);
				match(COMMA);
				setState(448);
				expr();
				}
				}
				setState(453);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExprContext extends ParserRuleContext {
		public TerminalNode INT_LIT() { return getToken(clnParser.INT_LIT, 0); }
		public TerminalNode DEC_LIT() { return getToken(clnParser.DEC_LIT, 0); }
		public TerminalNode BOOL_LIT() { return getToken(clnParser.BOOL_LIT, 0); }
		public TerminalNode STRING_LIT() { return getToken(clnParser.STRING_LIT, 0); }
		public ArrayLiteralContext arrayLiteral() {
			return getRuleContext(ArrayLiteralContext.class,0);
		}
		public StructLiteralContext structLiteral() {
			return getRuleContext(StructLiteralContext.class,0);
		}
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public PrimaryExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpr; }
	}

	public final PrimaryExprContext primaryExpr() throws RecognitionException {
		PrimaryExprContext _localctx = new PrimaryExprContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_primaryExpr);
		try {
			setState(465);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(454);
				match(INT_LIT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
				match(DEC_LIT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(456);
				match(BOOL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(457);
				match(STRING_LIT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(458);
				arrayLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(459);
				structLiteral();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(460);
				match(ID);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(461);
				match(LPAREN);
				setState(462);
				expr();
				setState(463);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayLiteralContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(clnParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(clnParser.RBRACK, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public ArrayLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayLiteral; }
	}

	public final ArrayLiteralContext arrayLiteral() throws RecognitionException {
		ArrayLiteralContext _localctx = new ArrayLiteralContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_arrayLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			match(LBRACK);
			setState(469);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282454304489472L) != 0)) {
				{
				setState(468);
				exprList();
				}
			}

			setState(471);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StructLiteralContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public FieldInitListContext fieldInitList() {
			return getRuleContext(FieldInitListContext.class,0);
		}
		public StructLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structLiteral; }
	}

	public final StructLiteralContext structLiteral() throws RecognitionException {
		StructLiteralContext _localctx = new StructLiteralContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_structLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(473);
			qualifiedName();
			setState(474);
			match(LPAREN);
			setState(476);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(475);
				fieldInitList();
				}
			}

			setState(478);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldInitListContext extends ParserRuleContext {
		public List<FieldInitContext> fieldInit() {
			return getRuleContexts(FieldInitContext.class);
		}
		public FieldInitContext fieldInit(int i) {
			return getRuleContext(FieldInitContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public FieldInitListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldInitList; }
	}

	public final FieldInitListContext fieldInitList() throws RecognitionException {
		FieldInitListContext _localctx = new FieldInitListContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_fieldInitList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(480);
			fieldInit();
			setState(485);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(481);
				match(COMMA);
				setState(482);
				fieldInit();
				}
				}
				setState(487);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FieldInitContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public TerminalNode COLON() { return getToken(clnParser.COLON, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FieldInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldInit; }
	}

	public final FieldInitContext fieldInit() throws RecognitionException {
		FieldInitContext _localctx = new FieldInitContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_fieldInit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(488);
			match(ID);
			setState(489);
			match(COLON);
			setState(490);
			expr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(clnParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(clnParser.COMMA, i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(492);
			expr();
			setState(497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(493);
				match(COMMA);
				setState(494);
				expr();
				}
				}
				setState(499);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(clnParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(clnParser.ID, i);
		}
		public List<TerminalNode> DOT() { return getTokens(clnParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(clnParser.DOT, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(500);
			match(ID);
			setState(505);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(501);
					match(DOT);
					setState(502);
					match(ID);
					}
					} 
				}
				setState(507);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,45,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u00013\u01fd\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
		"2\u00072\u00023\u00073\u0001\u0000\u0005\u0000j\b\u0000\n\u0000\f\u0000"+
		"m\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001t\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003~\b\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u0004\u0083\b\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u0089\b\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006"+
		"\u0091\b\u0006\n\u0006\f\u0006\u0094\t\u0006\u0001\u0007\u0001\u0007\u0003"+
		"\u0007\u0098\b\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0005"+
		"\t\u00a0\b\t\n\t\f\t\u00a3\t\t\u0001\t\u0001\t\u0001\t\u0001\n\u0003\n"+
		"\u00a9\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0004\u000b\u00b3\b\u000b\u000b\u000b\f\u000b\u00b4"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0003"+
		"\r\u00be\b\r\u0001\r\u0001\r\u0001\r\u0003\r\u00c3\b\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0003\u000e\u00ca\b\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00d0\b\u000f\n\u000f"+
		"\f\u000f\u00d3\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0005\u0011\u00e0\b\u0011\n\u0011\f\u0011\u00e3\t\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0005\u0013\u00ea"+
		"\b\u0013\n\u0013\f\u0013\u00ed\t\u0013\u0001\u0013\u0001\u0013\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u00fb\b\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0016\u0003\u0016\u0101\b\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0005\u0018\u0111\b\u0018\n\u0018\f\u0018\u0114\t\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0003"+
		"\u0019\u011c\b\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001"+
		"\u001a\u0005\u001a\u0123\b\u001a\n\u001a\f\u001a\u0126\t\u001a\u0001\u001b"+
		"\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b"+
		"\u012e\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0003\u001c\u0137\b\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u0145\b\u001e"+
		"\n\u001e\f\u001e\u0148\t\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u0151\b\u001f\n"+
		"\u001f\f\u001f\u0154\t\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0005"+
		"\u001f\u0159\b\u001f\n\u001f\f\u001f\u015c\t\u001f\u0003\u001f\u015e\b"+
		"\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 "+
		"\u0003 \u0169\b \u0001 \u0001 \u0003 \u016d\b \u0001!\u0001!\u0001!\u0001"+
		"\"\u0001\"\u0001#\u0001#\u0001#\u0005#\u0177\b#\n#\f#\u017a\t#\u0001$"+
		"\u0001$\u0001$\u0005$\u017f\b$\n$\f$\u0182\t$\u0001%\u0001%\u0001%\u0005"+
		"%\u0187\b%\n%\f%\u018a\t%\u0001&\u0001&\u0001&\u0005&\u018f\b&\n&\f&\u0192"+
		"\t&\u0001\'\u0001\'\u0001\'\u0005\'\u0197\b\'\n\'\f\'\u019a\t\'\u0001"+
		"(\u0001(\u0001(\u0005(\u019f\b(\n(\f(\u01a2\t(\u0001)\u0001)\u0001)\u0003"+
		")\u01a7\b)\u0001*\u0001*\u0005*\u01ab\b*\n*\f*\u01ae\t*\u0001+\u0001+"+
		"\u0003+\u01b2\b+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0003+\u01bd\b+\u0001,\u0001,\u0001,\u0005,\u01c2\b,\n,\f,\u01c5"+
		"\t,\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001"+
		"-\u0001-\u0003-\u01d2\b-\u0001.\u0001.\u0003.\u01d6\b.\u0001.\u0001.\u0001"+
		"/\u0001/\u0001/\u0003/\u01dd\b/\u0001/\u0001/\u00010\u00010\u00010\u0005"+
		"0\u01e4\b0\n0\f0\u01e7\t0\u00011\u00011\u00011\u00011\u00012\u00012\u0001"+
		"2\u00052\u01f0\b2\n2\f2\u01f3\t2\u00013\u00013\u00013\u00053\u01f8\b3"+
		"\n3\f3\u01fb\t3\u00013\u0000\u00004\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdf\u0000\u0006\u0001\u0000\u000e\u0011\u0001\u0000*+\u0001"+
		"\u0000,/\u0001\u0000!\"\u0001\u0000#$\u0002\u0000\"\"%\'\u020b\u0000k"+
		"\u0001\u0000\u0000\u0000\u0002s\u0001\u0000\u0000\u0000\u0004u\u0001\u0000"+
		"\u0000\u0000\u0006y\u0001\u0000\u0000\u0000\b\u0082\u0001\u0000\u0000"+
		"\u0000\n\u008a\u0001\u0000\u0000\u0000\f\u008d\u0001\u0000\u0000\u0000"+
		"\u000e\u0097\u0001\u0000\u0000\u0000\u0010\u0099\u0001\u0000\u0000\u0000"+
		"\u0012\u009b\u0001\u0000\u0000\u0000\u0014\u00a8\u0001\u0000\u0000\u0000"+
		"\u0016\u00ae\u0001\u0000\u0000\u0000\u0018\u00b9\u0001\u0000\u0000\u0000"+
		"\u001a\u00bd\u0001\u0000\u0000\u0000\u001c\u00c9\u0001\u0000\u0000\u0000"+
		"\u001e\u00cb\u0001\u0000\u0000\u0000 \u00d6\u0001\u0000\u0000\u0000\""+
		"\u00dc\u0001\u0000\u0000\u0000$\u00e4\u0001\u0000\u0000\u0000&\u00e7\u0001"+
		"\u0000\u0000\u0000(\u00fa\u0001\u0000\u0000\u0000*\u00fc\u0001\u0000\u0000"+
		"\u0000,\u0100\u0001\u0000\u0000\u0000.\u0107\u0001\u0000\u0000\u00000"+
		"\u010c\u0001\u0000\u0000\u00002\u011b\u0001\u0000\u0000\u00004\u0120\u0001"+
		"\u0000\u0000\u00006\u012d\u0001\u0000\u0000\u00008\u012f\u0001\u0000\u0000"+
		"\u0000:\u0138\u0001\u0000\u0000\u0000<\u013e\u0001\u0000\u0000\u0000>"+
		"\u015d\u0001\u0000\u0000\u0000@\u016c\u0001\u0000\u0000\u0000B\u016e\u0001"+
		"\u0000\u0000\u0000D\u0171\u0001\u0000\u0000\u0000F\u0173\u0001\u0000\u0000"+
		"\u0000H\u017b\u0001\u0000\u0000\u0000J\u0183\u0001\u0000\u0000\u0000L"+
		"\u018b\u0001\u0000\u0000\u0000N\u0193\u0001\u0000\u0000\u0000P\u019b\u0001"+
		"\u0000\u0000\u0000R\u01a6\u0001\u0000\u0000\u0000T\u01a8\u0001\u0000\u0000"+
		"\u0000V\u01bc\u0001\u0000\u0000\u0000X\u01be\u0001\u0000\u0000\u0000Z"+
		"\u01d1\u0001\u0000\u0000\u0000\\\u01d3\u0001\u0000\u0000\u0000^\u01d9"+
		"\u0001\u0000\u0000\u0000`\u01e0\u0001\u0000\u0000\u0000b\u01e8\u0001\u0000"+
		"\u0000\u0000d\u01ec\u0001\u0000\u0000\u0000f\u01f4\u0001\u0000\u0000\u0000"+
		"hj\u0003\u0002\u0001\u0000ih\u0001\u0000\u0000\u0000jm\u0001\u0000\u0000"+
		"\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000ln\u0001\u0000"+
		"\u0000\u0000mk\u0001\u0000\u0000\u0000no\u0005\u0000\u0000\u0001o\u0001"+
		"\u0001\u0000\u0000\u0000pt\u0003\u0004\u0002\u0000qt\u0003\u0006\u0003"+
		"\u0000rt\u0003\b\u0004\u0000sp\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000"+
		"\u0000sr\u0001\u0000\u0000\u0000t\u0003\u0001\u0000\u0000\u0000uv\u0005"+
		"\u0001\u0000\u0000vw\u0003f3\u0000wx\u0005\u001c\u0000\u0000x\u0005\u0001"+
		"\u0000\u0000\u0000yz\u0005\u0002\u0000\u0000z}\u0003f3\u0000{|\u0005\u001f"+
		"\u0000\u0000|~\u0005#\u0000\u0000}{\u0001\u0000\u0000\u0000}~\u0001\u0000"+
		"\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0080\u0005\u001c\u0000"+
		"\u0000\u0080\u0007\u0001\u0000\u0000\u0000\u0081\u0083\u0005\u0003\u0000"+
		"\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000"+
		"\u0000\u0083\u0088\u0001\u0000\u0000\u0000\u0084\u0089\u0003\u0012\t\u0000"+
		"\u0085\u0089\u0003\u0016\u000b\u0000\u0086\u0089\u0003\u001a\r\u0000\u0087"+
		"\u0089\u0003\n\u0005\u0000\u0088\u0084\u0001\u0000\u0000\u0000\u0088\u0085"+
		"\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0087"+
		"\u0001\u0000\u0000\u0000\u0089\t\u0001\u0000\u0000\u0000\u008a\u008b\u0003"+
		",\u0016\u0000\u008b\u008c\u0005\u001c\u0000\u0000\u008c\u000b\u0001\u0000"+
		"\u0000\u0000\u008d\u0092\u0003\u000e\u0007\u0000\u008e\u008f\u0005\u001a"+
		"\u0000\u0000\u008f\u0091\u0005\u001b\u0000\u0000\u0090\u008e\u0001\u0000"+
		"\u0000\u0000\u0091\u0094\u0001\u0000\u0000\u0000\u0092\u0090\u0001\u0000"+
		"\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\r\u0001\u0000\u0000"+
		"\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0095\u0098\u0003\u0010\b\u0000"+
		"\u0096\u0098\u0003f3\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0097\u0096"+
		"\u0001\u0000\u0000\u0000\u0098\u000f\u0001\u0000\u0000\u0000\u0099\u009a"+
		"\u0007\u0000\u0000\u0000\u009a\u0011\u0001\u0000\u0000\u0000\u009b\u009c"+
		"\u0005\u0004\u0000\u0000\u009c\u009d\u00050\u0000\u0000\u009d\u00a1\u0005"+
		"\u0018\u0000\u0000\u009e\u00a0\u0003\u0014\n\u0000\u009f\u009e\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1\u009f\u0001\u0000"+
		"\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a4\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005\u0019"+
		"\u0000\u0000\u00a5\u00a6\u0005\u001c\u0000\u0000\u00a6\u0013\u0001\u0000"+
		"\u0000\u0000\u00a7\u00a9\u0005\u0006\u0000\u0000\u00a8\u00a7\u0001\u0000"+
		"\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000"+
		"\u0000\u0000\u00aa\u00ab\u0003\f\u0006\u0000\u00ab\u00ac\u00050\u0000"+
		"\u0000\u00ac\u00ad\u0005\u001c\u0000\u0000\u00ad\u0015\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0005\u0005\u0000\u0000\u00af\u00b0\u00050\u0000\u0000"+
		"\u00b0\u00b2\u0005\u0018\u0000\u0000\u00b1\u00b3\u0003\u0018\f\u0000\u00b2"+
		"\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4"+
		"\u00b2\u0001\u0000\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b6\u00b7\u0005\u0019\u0000\u0000\u00b7"+
		"\u00b8\u0005\u001c\u0000\u0000\u00b8\u0017\u0001\u0000\u0000\u0000\u00b9"+
		"\u00ba\u0003f3\u0000\u00ba\u00bb\u0005\u001c\u0000\u0000\u00bb\u0019\u0001"+
		"\u0000\u0000\u0000\u00bc\u00be\u0003\u001c\u000e\u0000\u00bd\u00bc\u0001"+
		"\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf\u0001"+
		"\u0000\u0000\u0000\u00bf\u00c0\u00050\u0000\u0000\u00c0\u00c2\u0005\u0016"+
		"\u0000\u0000\u00c1\u00c3\u0003\"\u0011\u0000\u00c2\u00c1\u0001\u0000\u0000"+
		"\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000"+
		"\u0000\u00c4\u00c5\u0005\u0017\u0000\u0000\u00c5\u00c6\u0003&\u0013\u0000"+
		"\u00c6\u001b\u0001\u0000\u0000\u0000\u00c7\u00ca\u0003\f\u0006\u0000\u00c8"+
		"\u00ca\u0003\u001e\u000f\u0000\u00c9\u00c7\u0001\u0000\u0000\u0000\u00c9"+
		"\u00c8\u0001\u0000\u0000\u0000\u00ca\u001d\u0001\u0000\u0000\u0000\u00cb"+
		"\u00cc\u0005\u0016\u0000\u0000\u00cc\u00d1\u0003 \u0010\u0000\u00cd\u00ce"+
		"\u0005\u001d\u0000\u0000\u00ce\u00d0\u0003 \u0010\u0000\u00cf\u00cd\u0001"+
		"\u0000\u0000\u0000\u00d0\u00d3\u0001\u0000\u0000\u0000\u00d1\u00cf\u0001"+
		"\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00d4\u0001"+
		"\u0000\u0000\u0000\u00d3\u00d1\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005"+
		"\u0017\u0000\u0000\u00d5\u001f\u0001\u0000\u0000\u0000\u00d6\u00d7\u0005"+
		"\u0006\u0000\u0000\u00d7\u00d8\u0003\f\u0006\u0000\u00d8\u00d9\u00050"+
		"\u0000\u0000\u00d9\u00da\u0005 \u0000\u0000\u00da\u00db\u0003D\"\u0000"+
		"\u00db!\u0001\u0000\u0000\u0000\u00dc\u00e1\u0003$\u0012\u0000\u00dd\u00de"+
		"\u0005\u001d\u0000\u0000\u00de\u00e0\u0003$\u0012\u0000\u00df\u00dd\u0001"+
		"\u0000\u0000\u0000\u00e0\u00e3\u0001\u0000\u0000\u0000\u00e1\u00df\u0001"+
		"\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000\u0000\u00e2#\u0001\u0000"+
		"\u0000\u0000\u00e3\u00e1\u0001\u0000\u0000\u0000\u00e4\u00e5\u0003\f\u0006"+
		"\u0000\u00e5\u00e6\u00050\u0000\u0000\u00e6%\u0001\u0000\u0000\u0000\u00e7"+
		"\u00eb\u0005\u0018\u0000\u0000\u00e8\u00ea\u0003(\u0014\u0000\u00e9\u00e8"+
		"\u0001\u0000\u0000\u0000\u00ea\u00ed\u0001\u0000\u0000\u0000\u00eb\u00e9"+
		"\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ee"+
		"\u0001\u0000\u0000\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ee\u00ef"+
		"\u0005\u0019\u0000\u0000\u00ef\'\u0001\u0000\u0000\u0000\u00f0\u00fb\u0003"+
		"&\u0013\u0000\u00f1\u00fb\u0003*\u0015\u0000\u00f2\u00fb\u0003.\u0017"+
		"\u0000\u00f3\u00fb\u00030\u0018\u0000\u00f4\u00fb\u00038\u001c\u0000\u00f5"+
		"\u00fb\u0003:\u001d\u0000\u00f6\u00fb\u0003<\u001e\u0000\u00f7\u00fb\u0003"+
		"@ \u0000\u00f8\u00fb\u0003B!\u0000\u00f9\u00fb\u0005\u001c\u0000\u0000"+
		"\u00fa\u00f0\u0001\u0000\u0000\u0000\u00fa\u00f1\u0001\u0000\u0000\u0000"+
		"\u00fa\u00f2\u0001\u0000\u0000\u0000\u00fa\u00f3\u0001\u0000\u0000\u0000"+
		"\u00fa\u00f4\u0001\u0000\u0000\u0000\u00fa\u00f5\u0001\u0000\u0000\u0000"+
		"\u00fa\u00f6\u0001\u0000\u0000\u0000\u00fa\u00f7\u0001\u0000\u0000\u0000"+
		"\u00fa\u00f8\u0001\u0000\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000"+
		"\u00fb)\u0001\u0000\u0000\u0000\u00fc\u00fd\u0003,\u0016\u0000\u00fd\u00fe"+
		"\u0005\u001c\u0000\u0000\u00fe+\u0001\u0000\u0000\u0000\u00ff\u0101\u0005"+
		"\u0006\u0000\u0000\u0100\u00ff\u0001\u0000\u0000\u0000\u0100\u0101\u0001"+
		"\u0000\u0000\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0103\u0003"+
		"\f\u0006\u0000\u0103\u0104\u00050\u0000\u0000\u0104\u0105\u0005 \u0000"+
		"\u0000\u0105\u0106\u0003D\"\u0000\u0106-\u0001\u0000\u0000\u0000\u0107"+
		"\u0108\u00034\u001a\u0000\u0108\u0109\u0005 \u0000\u0000\u0109\u010a\u0003"+
		"D\"\u0000\u010a\u010b\u0005\u001c\u0000\u0000\u010b/\u0001\u0000\u0000"+
		"\u0000\u010c\u010d\u0005\u0016\u0000\u0000\u010d\u0112\u00032\u0019\u0000"+
		"\u010e\u010f\u0005\u001d\u0000\u0000\u010f\u0111\u00032\u0019\u0000\u0110"+
		"\u010e\u0001\u0000\u0000\u0000\u0111\u0114\u0001\u0000\u0000\u0000\u0112"+
		"\u0110\u0001\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113"+
		"\u0115\u0001\u0000\u0000\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0115"+
		"\u0116\u0005\u0017\u0000\u0000\u0116\u0117\u0005 \u0000\u0000\u0117\u0118"+
		"\u0003D\"\u0000\u0118\u0119\u0005\u001c\u0000\u0000\u01191\u0001\u0000"+
		"\u0000\u0000\u011a\u011c\u0005\u0006\u0000\u0000\u011b\u011a\u0001\u0000"+
		"\u0000\u0000\u011b\u011c\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000"+
		"\u0000\u0000\u011d\u011e\u0003\f\u0006\u0000\u011e\u011f\u00050\u0000"+
		"\u0000\u011f3\u0001\u0000\u0000\u0000\u0120\u0124\u00050\u0000\u0000\u0121"+
		"\u0123\u00036\u001b\u0000\u0122\u0121\u0001\u0000\u0000\u0000\u0123\u0126"+
		"\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0124\u0125"+
		"\u0001\u0000\u0000\u0000\u01255\u0001\u0000\u0000\u0000\u0126\u0124\u0001"+
		"\u0000\u0000\u0000\u0127\u0128\u0005\u001f\u0000\u0000\u0128\u012e\u0005"+
		"0\u0000\u0000\u0129\u012a\u0005\u001a\u0000\u0000\u012a\u012b\u0003D\""+
		"\u0000\u012b\u012c\u0005\u001b\u0000\u0000\u012c\u012e\u0001\u0000\u0000"+
		"\u0000\u012d\u0127\u0001\u0000\u0000\u0000\u012d\u0129\u0001\u0000\u0000"+
		"\u0000\u012e7\u0001\u0000\u0000\u0000\u012f\u0130\u0005\u0007\u0000\u0000"+
		"\u0130\u0131\u0005\u0016\u0000\u0000\u0131\u0132\u0003D\"\u0000\u0132"+
		"\u0133\u0005\u0017\u0000\u0000\u0133\u0136\u0003&\u0013\u0000\u0134\u0135"+
		"\u0005\b\u0000\u0000\u0135\u0137\u0003&\u0013\u0000\u0136\u0134\u0001"+
		"\u0000\u0000\u0000\u0136\u0137\u0001\u0000\u0000\u0000\u01379\u0001\u0000"+
		"\u0000\u0000\u0138\u0139\u0005\t\u0000\u0000\u0139\u013a\u0005\u0016\u0000"+
		"\u0000\u013a\u013b\u0003D\"\u0000\u013b\u013c\u0005\u0017\u0000\u0000"+
		"\u013c\u013d\u0003&\u0013\u0000\u013d;\u0001\u0000\u0000\u0000\u013e\u013f"+
		"\u0005\n\u0000\u0000\u013f\u0140\u0005\u0016\u0000\u0000\u0140\u0141\u0003"+
		"D\"\u0000\u0141\u0142\u0005\u0017\u0000\u0000\u0142\u0146\u0005\u0018"+
		"\u0000\u0000\u0143\u0145\u0003>\u001f\u0000\u0144\u0143\u0001\u0000\u0000"+
		"\u0000\u0145\u0148\u0001\u0000\u0000\u0000\u0146\u0144\u0001\u0000\u0000"+
		"\u0000\u0146\u0147\u0001\u0000\u0000\u0000\u0147\u0149\u0001\u0000\u0000"+
		"\u0000\u0148\u0146\u0001\u0000\u0000\u0000\u0149\u014a\u0005\u0019\u0000"+
		"\u0000\u014a=\u0001\u0000\u0000\u0000\u014b\u014c\u0005\u000b\u0000\u0000"+
		"\u014c\u014d\u0003f3\u0000\u014d\u014e\u00050\u0000\u0000\u014e\u0152"+
		"\u0005\u001e\u0000\u0000\u014f\u0151\u0003(\u0014\u0000\u0150\u014f\u0001"+
		"\u0000\u0000\u0000\u0151\u0154\u0001\u0000\u0000\u0000\u0152\u0150\u0001"+
		"\u0000\u0000\u0000\u0152\u0153\u0001\u0000\u0000\u0000\u0153\u015e\u0001"+
		"\u0000\u0000\u0000\u0154\u0152\u0001\u0000\u0000\u0000\u0155\u0156\u0005"+
		"\f\u0000\u0000\u0156\u015a\u0005\u001e\u0000\u0000\u0157\u0159\u0003("+
		"\u0014\u0000\u0158\u0157\u0001\u0000\u0000\u0000\u0159\u015c\u0001\u0000"+
		"\u0000\u0000\u015a\u0158\u0001\u0000\u0000\u0000\u015a\u015b\u0001\u0000"+
		"\u0000\u0000\u015b\u015e\u0001\u0000\u0000\u0000\u015c\u015a\u0001\u0000"+
		"\u0000\u0000\u015d\u014b\u0001\u0000\u0000\u0000\u015d\u0155\u0001\u0000"+
		"\u0000\u0000\u015e?\u0001\u0000\u0000\u0000\u015f\u0160\u0005\r\u0000"+
		"\u0000\u0160\u016d\u0005\u001c\u0000\u0000\u0161\u0162\u0005\r\u0000\u0000"+
		"\u0162\u0163\u0003D\"\u0000\u0163\u0164\u0005\u001c\u0000\u0000\u0164"+
		"\u016d\u0001\u0000\u0000\u0000\u0165\u0166\u0005\r\u0000\u0000\u0166\u0168"+
		"\u0005\u0016\u0000\u0000\u0167\u0169\u0003d2\u0000\u0168\u0167\u0001\u0000"+
		"\u0000\u0000\u0168\u0169\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000"+
		"\u0000\u0000\u016a\u016b\u0005\u0017\u0000\u0000\u016b\u016d\u0005\u001c"+
		"\u0000\u0000\u016c\u015f\u0001\u0000\u0000\u0000\u016c\u0161\u0001\u0000"+
		"\u0000\u0000\u016c\u0165\u0001\u0000\u0000\u0000\u016dA\u0001\u0000\u0000"+
		"\u0000\u016e\u016f\u0003D\"\u0000\u016f\u0170\u0005\u001c\u0000\u0000"+
		"\u0170C\u0001\u0000\u0000\u0000\u0171\u0172\u0003F#\u0000\u0172E\u0001"+
		"\u0000\u0000\u0000\u0173\u0178\u0003H$\u0000\u0174\u0175\u0005)\u0000"+
		"\u0000\u0175\u0177\u0003H$\u0000\u0176\u0174\u0001\u0000\u0000\u0000\u0177"+
		"\u017a\u0001\u0000\u0000\u0000\u0178\u0176\u0001\u0000\u0000\u0000\u0178"+
		"\u0179\u0001\u0000\u0000\u0000\u0179G\u0001\u0000\u0000\u0000\u017a\u0178"+
		"\u0001\u0000\u0000\u0000\u017b\u0180\u0003J%\u0000\u017c\u017d\u0005("+
		"\u0000\u0000\u017d\u017f\u0003J%\u0000\u017e\u017c\u0001\u0000\u0000\u0000"+
		"\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000"+
		"\u0180\u0181\u0001\u0000\u0000\u0000\u0181I\u0001\u0000\u0000\u0000\u0182"+
		"\u0180\u0001\u0000\u0000\u0000\u0183\u0188\u0003L&\u0000\u0184\u0185\u0007"+
		"\u0001\u0000\u0000\u0185\u0187\u0003L&\u0000\u0186\u0184\u0001\u0000\u0000"+
		"\u0000\u0187\u018a\u0001\u0000\u0000\u0000\u0188\u0186\u0001\u0000\u0000"+
		"\u0000\u0188\u0189\u0001\u0000\u0000\u0000\u0189K\u0001\u0000\u0000\u0000"+
		"\u018a\u0188\u0001\u0000\u0000\u0000\u018b\u0190\u0003N\'\u0000\u018c"+
		"\u018d\u0007\u0002\u0000\u0000\u018d\u018f\u0003N\'\u0000\u018e\u018c"+
		"\u0001\u0000\u0000\u0000\u018f\u0192\u0001\u0000\u0000\u0000\u0190\u018e"+
		"\u0001\u0000\u0000\u0000\u0190\u0191\u0001\u0000\u0000\u0000\u0191M\u0001"+
		"\u0000\u0000\u0000\u0192\u0190\u0001\u0000\u0000\u0000\u0193\u0198\u0003"+
		"P(\u0000\u0194\u0195\u0007\u0003\u0000\u0000\u0195\u0197\u0003P(\u0000"+
		"\u0196\u0194\u0001\u0000\u0000\u0000\u0197\u019a\u0001\u0000\u0000\u0000"+
		"\u0198\u0196\u0001\u0000\u0000\u0000\u0198\u0199\u0001\u0000\u0000\u0000"+
		"\u0199O\u0001\u0000\u0000\u0000\u019a\u0198\u0001\u0000\u0000\u0000\u019b"+
		"\u01a0\u0003R)\u0000\u019c\u019d\u0007\u0004\u0000\u0000\u019d\u019f\u0003"+
		"R)\u0000\u019e\u019c\u0001\u0000\u0000\u0000\u019f\u01a2\u0001\u0000\u0000"+
		"\u0000\u01a0\u019e\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1Q\u0001\u0000\u0000\u0000\u01a2\u01a0\u0001\u0000\u0000\u0000"+
		"\u01a3\u01a4\u0007\u0005\u0000\u0000\u01a4\u01a7\u0003R)\u0000\u01a5\u01a7"+
		"\u0003T*\u0000\u01a6\u01a3\u0001\u0000\u0000\u0000\u01a6\u01a5\u0001\u0000"+
		"\u0000\u0000\u01a7S\u0001\u0000\u0000\u0000\u01a8\u01ac\u0003Z-\u0000"+
		"\u01a9\u01ab\u0003V+\u0000\u01aa\u01a9\u0001\u0000\u0000\u0000\u01ab\u01ae"+
		"\u0001\u0000\u0000\u0000\u01ac\u01aa\u0001\u0000\u0000\u0000\u01ac\u01ad"+
		"\u0001\u0000\u0000\u0000\u01adU\u0001\u0000\u0000\u0000\u01ae\u01ac\u0001"+
		"\u0000\u0000\u0000\u01af\u01b1\u0005\u0016\u0000\u0000\u01b0\u01b2\u0003"+
		"X,\u0000\u01b1\u01b0\u0001\u0000\u0000\u0000\u01b1\u01b2\u0001\u0000\u0000"+
		"\u0000\u01b2\u01b3\u0001\u0000\u0000\u0000\u01b3\u01bd\u0005\u0017\u0000"+
		"\u0000\u01b4\u01b5\u0005\u001f\u0000\u0000\u01b5\u01bd\u00050\u0000\u0000"+
		"\u01b6\u01b7\u0005\u001a\u0000\u0000\u01b7\u01b8\u0003D\"\u0000\u01b8"+
		"\u01b9\u0005\u001b\u0000\u0000\u01b9\u01bd\u0001\u0000\u0000\u0000\u01ba"+
		"\u01bd\u0005%\u0000\u0000\u01bb\u01bd\u0005&\u0000\u0000\u01bc\u01af\u0001"+
		"\u0000\u0000\u0000\u01bc\u01b4\u0001\u0000\u0000\u0000\u01bc\u01b6\u0001"+
		"\u0000\u0000\u0000\u01bc\u01ba\u0001\u0000\u0000\u0000\u01bc\u01bb\u0001"+
		"\u0000\u0000\u0000\u01bdW\u0001\u0000\u0000\u0000\u01be\u01c3\u0003D\""+
		"\u0000\u01bf\u01c0\u0005\u001d\u0000\u0000\u01c0\u01c2\u0003D\"\u0000"+
		"\u01c1\u01bf\u0001\u0000\u0000\u0000\u01c2\u01c5\u0001\u0000\u0000\u0000"+
		"\u01c3\u01c1\u0001\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000"+
		"\u01c4Y\u0001\u0000\u0000\u0000\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c6"+
		"\u01d2\u0005\u0013\u0000\u0000\u01c7\u01d2\u0005\u0014\u0000\u0000\u01c8"+
		"\u01d2\u0005\u0012\u0000\u0000\u01c9\u01d2\u0005\u0015\u0000\u0000\u01ca"+
		"\u01d2\u0003\\.\u0000\u01cb\u01d2\u0003^/\u0000\u01cc\u01d2\u00050\u0000"+
		"\u0000\u01cd\u01ce\u0005\u0016\u0000\u0000\u01ce\u01cf\u0003D\"\u0000"+
		"\u01cf\u01d0\u0005\u0017\u0000\u0000\u01d0\u01d2\u0001\u0000\u0000\u0000"+
		"\u01d1\u01c6\u0001\u0000\u0000\u0000\u01d1\u01c7\u0001\u0000\u0000\u0000"+
		"\u01d1\u01c8\u0001\u0000\u0000\u0000\u01d1\u01c9\u0001\u0000\u0000\u0000"+
		"\u01d1\u01ca\u0001\u0000\u0000\u0000\u01d1\u01cb\u0001\u0000\u0000\u0000"+
		"\u01d1\u01cc\u0001\u0000\u0000\u0000\u01d1\u01cd\u0001\u0000\u0000\u0000"+
		"\u01d2[\u0001\u0000\u0000\u0000\u01d3\u01d5\u0005\u001a\u0000\u0000\u01d4"+
		"\u01d6\u0003d2\u0000\u01d5\u01d4\u0001\u0000\u0000\u0000\u01d5\u01d6\u0001"+
		"\u0000\u0000\u0000\u01d6\u01d7\u0001\u0000\u0000\u0000\u01d7\u01d8\u0005"+
		"\u001b\u0000\u0000\u01d8]\u0001\u0000\u0000\u0000\u01d9\u01da\u0003f3"+
		"\u0000\u01da\u01dc\u0005\u0016\u0000\u0000\u01db\u01dd\u0003`0\u0000\u01dc"+
		"\u01db\u0001\u0000\u0000\u0000\u01dc\u01dd\u0001\u0000\u0000\u0000\u01dd"+
		"\u01de\u0001\u0000\u0000\u0000\u01de\u01df\u0005\u0017\u0000\u0000\u01df"+
		"_\u0001\u0000\u0000\u0000\u01e0\u01e5\u0003b1\u0000\u01e1\u01e2\u0005"+
		"\u001d\u0000\u0000\u01e2\u01e4\u0003b1\u0000\u01e3\u01e1\u0001\u0000\u0000"+
		"\u0000\u01e4\u01e7\u0001\u0000\u0000\u0000\u01e5\u01e3\u0001\u0000\u0000"+
		"\u0000\u01e5\u01e6\u0001\u0000\u0000\u0000\u01e6a\u0001\u0000\u0000\u0000"+
		"\u01e7\u01e5\u0001\u0000\u0000\u0000\u01e8\u01e9\u00050\u0000\u0000\u01e9"+
		"\u01ea\u0005\u001e\u0000\u0000\u01ea\u01eb\u0003D\"\u0000\u01ebc\u0001"+
		"\u0000\u0000\u0000\u01ec\u01f1\u0003D\"\u0000\u01ed\u01ee\u0005\u001d"+
		"\u0000\u0000\u01ee\u01f0\u0003D\"\u0000\u01ef\u01ed\u0001\u0000\u0000"+
		"\u0000\u01f0\u01f3\u0001\u0000\u0000\u0000\u01f1\u01ef\u0001\u0000\u0000"+
		"\u0000\u01f1\u01f2\u0001\u0000\u0000\u0000\u01f2e\u0001\u0000\u0000\u0000"+
		"\u01f3\u01f1\u0001\u0000\u0000\u0000\u01f4\u01f9\u00050\u0000\u0000\u01f5"+
		"\u01f6\u0005\u001f\u0000\u0000\u01f6\u01f8\u00050\u0000\u0000\u01f7\u01f5"+
		"\u0001\u0000\u0000\u0000\u01f8\u01fb\u0001\u0000\u0000\u0000\u01f9\u01f7"+
		"\u0001\u0000\u0000\u0000\u01f9\u01fa\u0001\u0000\u0000\u0000\u01fag\u0001"+
		"\u0000\u0000\u0000\u01fb\u01f9\u0001\u0000\u0000\u0000.ks}\u0082\u0088"+
		"\u0092\u0097\u00a1\u00a8\u00b4\u00bd\u00c2\u00c9\u00d1\u00e1\u00eb\u00fa"+
		"\u0100\u0112\u011b\u0124\u012d\u0136\u0146\u0152\u015a\u015d\u0168\u016c"+
		"\u0178\u0180\u0188\u0190\u0198\u01a0\u01a6\u01ac\u01b1\u01bc\u01c3\u01d1"+
		"\u01d5\u01dc\u01e5\u01f1\u01f9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}