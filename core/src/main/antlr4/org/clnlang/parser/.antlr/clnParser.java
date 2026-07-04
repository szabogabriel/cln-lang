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
		STRING_T=16, DEC_T=17, ANY_STRUCT=18, BOOL_LIT=19, INT_LIT=20, DEC_LIT=21, 
		STRING_LIT=22, LPAREN=23, RPAREN=24, LBRACE=25, RBRACE=26, LBRACK=27, 
		RBRACK=28, SEMI=29, COMMA=30, COLON=31, DOT=32, ASSIGN=33, PLUS=34, MINUS=35, 
		STAR=36, SLASH=37, INC=38, DEC=39, NOT=40, AND=41, OR=42, EQ=43, NEQ=44, 
		LT=45, LTE=46, GT=47, GTE=48, ID=49, LINE_COMMENT=50, BLOCK_COMMENT=51, 
		WS=52;
	public static final int
		RULE_program = 0, RULE_topLevelDecl = 1, RULE_packageDecl = 2, RULE_importDecl = 3, 
		RULE_decl = 4, RULE_globalVarDecl = 5, RULE_type = 6, RULE_baseType = 7, 
		RULE_primitiveType = 8, RULE_decimalType = 9, RULE_structDecl = 10, RULE_structFieldDecl = 11, 
		RULE_unionDecl = 12, RULE_unionMember = 13, RULE_functionDecl = 14, RULE_returnType = 15, 
		RULE_namedReturnSig = 16, RULE_returnVar = 17, RULE_paramList = 18, RULE_param = 19, 
		RULE_block = 20, RULE_stmt = 21, RULE_varDeclStmt = 22, RULE_varBinding = 23, 
		RULE_assignStmt = 24, RULE_tupleAssignStmt = 25, RULE_tupleBind = 26, 
		RULE_lvalue = 27, RULE_lvalueSuffix = 28, RULE_ifStmt = 29, RULE_whileStmt = 30, 
		RULE_switchStmt = 31, RULE_caseClause = 32, RULE_returnStmt = 33, RULE_exprStmt = 34, 
		RULE_expr = 35, RULE_orExpr = 36, RULE_andExpr = 37, RULE_equalityExpr = 38, 
		RULE_relExpr = 39, RULE_addExpr = 40, RULE_mulExpr = 41, RULE_unaryExpr = 42, 
		RULE_postfixExpr = 43, RULE_postfixOp = 44, RULE_argList = 45, RULE_primaryExpr = 46, 
		RULE_arrayLiteral = 47, RULE_structLiteral = 48, RULE_fieldInitList = 49, 
		RULE_fieldInit = 50, RULE_exprList = 51, RULE_qualifiedName = 52;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "topLevelDecl", "packageDecl", "importDecl", "decl", "globalVarDecl", 
			"type", "baseType", "primitiveType", "decimalType", "structDecl", "structFieldDecl", 
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
			"'int'", "'bool'", "'string'", "'dec'", "'AnyStruct'", null, null, null, 
			null, "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", "','", "':'", 
			"'.'", "'='", "'+'", "'-'", "'*'", "'/'", "'++'", "'--'", "'!'", "'&&'", 
			"'||'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PACKAGE", "IMPORT", "EXPOSE", "STRUCT", "UNION", "VAR", "IF", 
			"ELSE", "WHILE", "SWITCH", "CASE", "DEFAULT", "RETURN", "INT_T", "BOOL_T", 
			"STRING_T", "DEC_T", "ANY_STRUCT", "BOOL_LIT", "INT_LIT", "DEC_LIT", 
			"STRING_LIT", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", 
			"SEMI", "COMMA", "COLON", "DOT", "ASSIGN", "PLUS", "MINUS", "STAR", "SLASH", 
			"INC", "DEC", "NOT", "AND", "OR", "EQ", "NEQ", "LT", "LTE", "GT", "GTE", 
			"ID", "LINE_COMMENT", "BLOCK_COMMENT", "WS"
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
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 562949962317950L) != 0)) {
				{
				{
				setState(106);
				topLevelDecl();
				}
				}
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(112);
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
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PACKAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				packageDecl();
				}
				break;
			case IMPORT:
				enterOuterAlt(_localctx, 2);
				{
				setState(115);
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
			case ANY_STRUCT:
			case LPAREN:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(116);
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
			setState(119);
			match(PACKAGE);
			setState(120);
			qualifiedName();
			setState(121);
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
			setState(123);
			match(IMPORT);
			setState(124);
			qualifiedName();
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOT) {
				{
				setState(125);
				match(DOT);
				setState(126);
				match(STAR);
				}
			}

			setState(129);
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
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXPOSE) {
				{
				setState(131);
				match(EXPOSE);
				}
			}

			setState(138);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(134);
				structDecl();
				}
				break;
			case 2:
				{
				setState(135);
				unionDecl();
				}
				break;
			case 3:
				{
				setState(136);
				functionDecl();
				}
				break;
			case 4:
				{
				setState(137);
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
			setState(140);
			varBinding();
			setState(141);
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
			setState(143);
			baseType();
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(144);
				match(LBRACK);
				setState(145);
				match(RBRACK);
				}
				}
				setState(150);
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
		public TerminalNode ANY_STRUCT() { return getToken(clnParser.ANY_STRUCT, 0); }
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
			setState(154);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_T:
			case BOOL_T:
			case STRING_T:
			case DEC_T:
				enterOuterAlt(_localctx, 1);
				{
				setState(151);
				primitiveType();
				}
				break;
			case ANY_STRUCT:
				enterOuterAlt(_localctx, 2);
				{
				setState(152);
				match(ANY_STRUCT);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(153);
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
		public DecimalTypeContext decimalType() {
			return getRuleContext(DecimalTypeContext.class,0);
		}
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_primitiveType);
		try {
			setState(160);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_T:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				match(INT_T);
				}
				break;
			case BOOL_T:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				match(BOOL_T);
				}
				break;
			case STRING_T:
				enterOuterAlt(_localctx, 3);
				{
				setState(158);
				match(STRING_T);
				}
				break;
			case DEC_T:
				enterOuterAlt(_localctx, 4);
				{
				setState(159);
				decimalType();
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
	public static class DecimalTypeContext extends ParserRuleContext {
		public TerminalNode DEC_T() { return getToken(clnParser.DEC_T, 0); }
		public TerminalNode LPAREN() { return getToken(clnParser.LPAREN, 0); }
		public TerminalNode INT_LIT() { return getToken(clnParser.INT_LIT, 0); }
		public TerminalNode RPAREN() { return getToken(clnParser.RPAREN, 0); }
		public TerminalNode COMMA() { return getToken(clnParser.COMMA, 0); }
		public TerminalNode ID() { return getToken(clnParser.ID, 0); }
		public DecimalTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalType; }
	}

	public final DecimalTypeContext decimalType() throws RecognitionException {
		DecimalTypeContext _localctx = new DecimalTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_decimalType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(DEC_T);
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(163);
				match(LPAREN);
				setState(164);
				match(INT_LIT);
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(165);
					match(COMMA);
					setState(166);
					match(ID);
					}
				}

				setState(169);
				match(RPAREN);
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
		enterRule(_localctx, 20, RULE_structDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(STRUCT);
			setState(173);
			match(ID);
			setState(174);
			match(LBRACE);
			setState(178);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 562949953929280L) != 0)) {
				{
				{
				setState(175);
				structFieldDecl();
				}
				}
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(181);
			match(RBRACE);
			setState(182);
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
		enterRule(_localctx, 22, RULE_structFieldDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(184);
				match(VAR);
				}
			}

			setState(187);
			type();
			setState(188);
			match(ID);
			setState(189);
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
		enterRule(_localctx, 24, RULE_unionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(UNION);
			setState(192);
			match(ID);
			setState(193);
			match(LBRACE);
			setState(195); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(194);
				unionMember();
				}
				}
				setState(197); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(199);
			match(RBRACE);
			setState(200);
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
		enterRule(_localctx, 26, RULE_unionMember);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			qualifiedName();
			setState(203);
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
		enterRule(_localctx, 28, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(205);
				returnType();
				}
				break;
			}
			setState(208);
			match(ID);
			setState(209);
			match(LPAREN);
			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 562949953929216L) != 0)) {
				{
				setState(210);
				paramList();
				}
			}

			setState(213);
			match(RPAREN);
			setState(214);
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
		enterRule(_localctx, 30, RULE_returnType);
		try {
			setState(218);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT_T:
			case BOOL_T:
			case STRING_T:
			case DEC_T:
			case ANY_STRUCT:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(216);
				type();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(217);
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
		enterRule(_localctx, 32, RULE_namedReturnSig);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(LPAREN);
			setState(221);
			returnVar();
			setState(226);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(222);
				match(COMMA);
				setState(223);
				returnVar();
				}
				}
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(229);
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
		enterRule(_localctx, 34, RULE_returnVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(VAR);
			setState(232);
			type();
			setState(233);
			match(ID);
			setState(234);
			match(ASSIGN);
			setState(235);
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
		enterRule(_localctx, 36, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			param();
			setState(242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(238);
				match(COMMA);
				setState(239);
				param();
				}
				}
				setState(244);
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
		enterRule(_localctx, 38, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			type();
			setState(246);
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
		enterRule(_localctx, 40, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(LBRACE);
			setState(252);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564909179922112L) != 0)) {
				{
				{
				setState(249);
				stmt();
				}
				}
				setState(254);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(255);
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
		enterRule(_localctx, 42, RULE_stmt);
		try {
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(258);
				varDeclStmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(259);
				assignStmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(260);
				tupleAssignStmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(261);
				ifStmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(262);
				whileStmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(263);
				switchStmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(264);
				returnStmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(265);
				exprStmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(266);
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
		enterRule(_localctx, 44, RULE_varDeclStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			varBinding();
			setState(270);
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
		enterRule(_localctx, 46, RULE_varBinding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(272);
				match(VAR);
				}
			}

			setState(275);
			type();
			setState(276);
			match(ID);
			setState(277);
			match(ASSIGN);
			setState(278);
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
		enterRule(_localctx, 48, RULE_assignStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			lvalue();
			setState(281);
			match(ASSIGN);
			setState(282);
			expr();
			setState(283);
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
		enterRule(_localctx, 50, RULE_tupleAssignStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(LPAREN);
			setState(286);
			tupleBind();
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(287);
				match(COMMA);
				setState(288);
				tupleBind();
				}
				}
				setState(293);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(294);
			match(RPAREN);
			setState(295);
			match(ASSIGN);
			setState(296);
			expr();
			setState(297);
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
		enterRule(_localctx, 52, RULE_tupleBind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==VAR) {
				{
				setState(299);
				match(VAR);
				}
			}

			setState(302);
			type();
			setState(303);
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
		enterRule(_localctx, 54, RULE_lvalue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(ID);
			setState(309);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK || _la==DOT) {
				{
				{
				setState(306);
				lvalueSuffix();
				}
				}
				setState(311);
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
		enterRule(_localctx, 56, RULE_lvalueSuffix);
		try {
			setState(318);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(312);
				match(DOT);
				setState(313);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 2);
				{
				setState(314);
				match(LBRACK);
				setState(315);
				expr();
				setState(316);
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
		enterRule(_localctx, 58, RULE_ifStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(320);
			match(IF);
			setState(321);
			match(LPAREN);
			setState(322);
			expr();
			setState(323);
			match(RPAREN);
			setState(324);
			block();
			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(325);
				match(ELSE);
				setState(326);
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
		enterRule(_localctx, 60, RULE_whileStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			match(WHILE);
			setState(330);
			match(LPAREN);
			setState(331);
			expr();
			setState(332);
			match(RPAREN);
			setState(333);
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
		enterRule(_localctx, 62, RULE_switchStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(SWITCH);
			setState(336);
			match(LPAREN);
			setState(337);
			expr();
			setState(338);
			match(RPAREN);
			setState(339);
			match(LBRACE);
			setState(343);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CASE || _la==DEFAULT) {
				{
				{
				setState(340);
				caseClause();
				}
				}
				setState(345);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(346);
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
		enterRule(_localctx, 64, RULE_caseClause);
		int _la;
		try {
			setState(366);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CASE:
				enterOuterAlt(_localctx, 1);
				{
				setState(348);
				match(CASE);
				setState(349);
				qualifiedName();
				setState(350);
				match(ID);
				setState(351);
				match(COLON);
				setState(355);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564909179922112L) != 0)) {
					{
					{
					setState(352);
					stmt();
					}
					}
					setState(357);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case DEFAULT:
				enterOuterAlt(_localctx, 2);
				{
				setState(358);
				match(DEFAULT);
				setState(359);
				match(COLON);
				setState(363);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564909179922112L) != 0)) {
					{
					{
					setState(360);
					stmt();
					}
					}
					setState(365);
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
		enterRule(_localctx, 66, RULE_returnStmt);
		int _la;
		try {
			setState(381);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(368);
				match(RETURN);
				setState(369);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(370);
				match(RETURN);
				setState(371);
				expr();
				setState(372);
				match(SEMI);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(374);
				match(RETURN);
				setState(375);
				match(LPAREN);
				setState(377);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564908608978944L) != 0)) {
					{
					setState(376);
					exprList();
					}
				}

				setState(379);
				match(RPAREN);
				setState(380);
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
		enterRule(_localctx, 68, RULE_exprStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			expr();
			setState(384);
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
		enterRule(_localctx, 70, RULE_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
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
		enterRule(_localctx, 72, RULE_orExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			andExpr();
			setState(393);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR) {
				{
				{
				setState(389);
				match(OR);
				setState(390);
				andExpr();
				}
				}
				setState(395);
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
		enterRule(_localctx, 74, RULE_andExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			equalityExpr();
			setState(401);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(397);
				match(AND);
				setState(398);
				equalityExpr();
				}
				}
				setState(403);
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
		enterRule(_localctx, 76, RULE_equalityExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			relExpr();
			setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EQ || _la==NEQ) {
				{
				{
				setState(405);
				_la = _input.LA(1);
				if ( !(_la==EQ || _la==NEQ) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(406);
				relExpr();
				}
				}
				setState(411);
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
		enterRule(_localctx, 78, RULE_relExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412);
			addExpr();
			setState(417);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 527765581332480L) != 0)) {
				{
				{
				setState(413);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 527765581332480L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(414);
				addExpr();
				}
				}
				setState(419);
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
		enterRule(_localctx, 80, RULE_addExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			mulExpr();
			setState(425);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(421);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(422);
				mulExpr();
				}
				}
				setState(427);
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
		enterRule(_localctx, 82, RULE_mulExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			unaryExpr();
			setState(433);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STAR || _la==SLASH) {
				{
				{
				setState(429);
				_la = _input.LA(1);
				if ( !(_la==STAR || _la==SLASH) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(430);
				unaryExpr();
				}
				}
				setState(435);
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
		enterRule(_localctx, 84, RULE_unaryExpr);
		int _la;
		try {
			setState(439);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MINUS:
			case INC:
			case DEC:
			case NOT:
				enterOuterAlt(_localctx, 1);
				{
				setState(436);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1958505086976L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(437);
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
				setState(438);
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
		enterRule(_localctx, 86, RULE_postfixExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			primaryExpr();
			setState(445);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 829071294464L) != 0)) {
				{
				{
				setState(442);
				postfixOp();
				}
				}
				setState(447);
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
		enterRule(_localctx, 88, RULE_postfixOp);
		int _la;
		try {
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(448);
				match(LPAREN);
				setState(450);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564908608978944L) != 0)) {
					{
					setState(449);
					argList();
					}
				}

				setState(452);
				match(RPAREN);
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(453);
				match(DOT);
				setState(454);
				match(ID);
				}
				break;
			case LBRACK:
				enterOuterAlt(_localctx, 3);
				{
				setState(455);
				match(LBRACK);
				setState(456);
				expr();
				setState(457);
				match(RBRACK);
				}
				break;
			case INC:
				enterOuterAlt(_localctx, 4);
				{
				setState(459);
				match(INC);
				}
				break;
			case DEC:
				enterOuterAlt(_localctx, 5);
				{
				setState(460);
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
		enterRule(_localctx, 90, RULE_argList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			expr();
			setState(468);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(464);
				match(COMMA);
				setState(465);
				expr();
				}
				}
				setState(470);
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
		enterRule(_localctx, 92, RULE_primaryExpr);
		try {
			setState(482);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(471);
				match(INT_LIT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(472);
				match(DEC_LIT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(473);
				match(BOOL_LIT);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(474);
				match(STRING_LIT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(475);
				arrayLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(476);
				structLiteral();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(477);
				match(ID);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(478);
				match(LPAREN);
				setState(479);
				expr();
				setState(480);
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
		enterRule(_localctx, 94, RULE_arrayLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484);
			match(LBRACK);
			setState(486);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 564908608978944L) != 0)) {
				{
				setState(485);
				exprList();
				}
			}

			setState(488);
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
		enterRule(_localctx, 96, RULE_structLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			qualifiedName();
			setState(491);
			match(LPAREN);
			setState(493);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(492);
				fieldInitList();
				}
			}

			setState(495);
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
		enterRule(_localctx, 98, RULE_fieldInitList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			fieldInit();
			setState(502);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(498);
				match(COMMA);
				setState(499);
				fieldInit();
				}
				}
				setState(504);
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
		enterRule(_localctx, 100, RULE_fieldInit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			match(ID);
			setState(506);
			match(COLON);
			setState(507);
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
		enterRule(_localctx, 102, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			expr();
			setState(514);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(510);
				match(COMMA);
				setState(511);
				expr();
				}
				}
				setState(516);
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
		enterRule(_localctx, 104, RULE_qualifiedName);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(517);
			match(ID);
			setState(522);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(518);
					match(DOT);
					setState(519);
					match(ID);
					}
					} 
				}
				setState(524);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
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
		"\u0004\u00014\u020e\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
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
		"2\u00072\u00023\u00073\u00024\u00074\u0001\u0000\u0005\u0000l\b\u0000"+
		"\n\u0000\f\u0000o\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u0001v\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u0080\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u0004\u0085\b"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u008b"+
		"\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0005\u0006\u0093\b\u0006\n\u0006\f\u0006\u0096\t\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0003\u0007\u009b\b\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\b\u00a1\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003"+
		"\t\u00a8\b\t\u0001\t\u0003\t\u00ab\b\t\u0001\n\u0001\n\u0001\n\u0001\n"+
		"\u0005\n\u00b1\b\n\n\n\f\n\u00b4\t\n\u0001\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0003\u000b\u00ba\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0004\f\u00c4\b\f\u000b\f\f\f\u00c5\u0001"+
		"\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\u000e\u0003\u000e\u00cf"+
		"\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00d4\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0003\u000f"+
		"\u00db\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u00e1\b\u0010\n\u0010\f\u0010\u00e4\t\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00f1\b\u0012\n\u0012\f\u0012"+
		"\u00f4\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0005\u0014\u00fb\b\u0014\n\u0014\f\u0014\u00fe\t\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u010c"+
		"\b\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0003\u0017\u0112"+
		"\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u0122\b\u0019\n\u0019\f\u0019"+
		"\u0125\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u001a\u0003\u001a\u012d\b\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001b\u0001\u001b\u0005\u001b\u0134\b\u001b\n\u001b\f\u001b\u0137"+
		"\t\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0003\u001c\u013f\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u0148\b\u001d\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0005"+
		"\u001f\u0156\b\u001f\n\u001f\f\u001f\u0159\t\u001f\u0001\u001f\u0001\u001f"+
		"\u0001 \u0001 \u0001 \u0001 \u0001 \u0005 \u0162\b \n \f \u0165\t \u0001"+
		" \u0001 \u0001 \u0005 \u016a\b \n \f \u016d\t \u0003 \u016f\b \u0001!"+
		"\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u017a"+
		"\b!\u0001!\u0001!\u0003!\u017e\b!\u0001\"\u0001\"\u0001\"\u0001#\u0001"+
		"#\u0001$\u0001$\u0001$\u0005$\u0188\b$\n$\f$\u018b\t$\u0001%\u0001%\u0001"+
		"%\u0005%\u0190\b%\n%\f%\u0193\t%\u0001&\u0001&\u0001&\u0005&\u0198\b&"+
		"\n&\f&\u019b\t&\u0001\'\u0001\'\u0001\'\u0005\'\u01a0\b\'\n\'\f\'\u01a3"+
		"\t\'\u0001(\u0001(\u0001(\u0005(\u01a8\b(\n(\f(\u01ab\t(\u0001)\u0001"+
		")\u0001)\u0005)\u01b0\b)\n)\f)\u01b3\t)\u0001*\u0001*\u0001*\u0003*\u01b8"+
		"\b*\u0001+\u0001+\u0005+\u01bc\b+\n+\f+\u01bf\t+\u0001,\u0001,\u0003,"+
		"\u01c3\b,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001,\u0001"+
		",\u0003,\u01ce\b,\u0001-\u0001-\u0001-\u0005-\u01d3\b-\n-\f-\u01d6\t-"+
		"\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
		".\u0001.\u0003.\u01e3\b.\u0001/\u0001/\u0003/\u01e7\b/\u0001/\u0001/\u0001"+
		"0\u00010\u00010\u00030\u01ee\b0\u00010\u00010\u00011\u00011\u00011\u0005"+
		"1\u01f5\b1\n1\f1\u01f8\t1\u00012\u00012\u00012\u00012\u00013\u00013\u0001"+
		"3\u00053\u0201\b3\n3\f3\u0204\t3\u00014\u00014\u00014\u00054\u0209\b4"+
		"\n4\f4\u020c\t4\u00014\u0000\u00005\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDF"+
		"HJLNPRTVXZ\\^`bdfh\u0000\u0005\u0001\u0000+,\u0001\u0000-0\u0001\u0000"+
		"\"#\u0001\u0000$%\u0002\u0000##&(\u0221\u0000m\u0001\u0000\u0000\u0000"+
		"\u0002u\u0001\u0000\u0000\u0000\u0004w\u0001\u0000\u0000\u0000\u0006{"+
		"\u0001\u0000\u0000\u0000\b\u0084\u0001\u0000\u0000\u0000\n\u008c\u0001"+
		"\u0000\u0000\u0000\f\u008f\u0001\u0000\u0000\u0000\u000e\u009a\u0001\u0000"+
		"\u0000\u0000\u0010\u00a0\u0001\u0000\u0000\u0000\u0012\u00a2\u0001\u0000"+
		"\u0000\u0000\u0014\u00ac\u0001\u0000\u0000\u0000\u0016\u00b9\u0001\u0000"+
		"\u0000\u0000\u0018\u00bf\u0001\u0000\u0000\u0000\u001a\u00ca\u0001\u0000"+
		"\u0000\u0000\u001c\u00ce\u0001\u0000\u0000\u0000\u001e\u00da\u0001\u0000"+
		"\u0000\u0000 \u00dc\u0001\u0000\u0000\u0000\"\u00e7\u0001\u0000\u0000"+
		"\u0000$\u00ed\u0001\u0000\u0000\u0000&\u00f5\u0001\u0000\u0000\u0000("+
		"\u00f8\u0001\u0000\u0000\u0000*\u010b\u0001\u0000\u0000\u0000,\u010d\u0001"+
		"\u0000\u0000\u0000.\u0111\u0001\u0000\u0000\u00000\u0118\u0001\u0000\u0000"+
		"\u00002\u011d\u0001\u0000\u0000\u00004\u012c\u0001\u0000\u0000\u00006"+
		"\u0131\u0001\u0000\u0000\u00008\u013e\u0001\u0000\u0000\u0000:\u0140\u0001"+
		"\u0000\u0000\u0000<\u0149\u0001\u0000\u0000\u0000>\u014f\u0001\u0000\u0000"+
		"\u0000@\u016e\u0001\u0000\u0000\u0000B\u017d\u0001\u0000\u0000\u0000D"+
		"\u017f\u0001\u0000\u0000\u0000F\u0182\u0001\u0000\u0000\u0000H\u0184\u0001"+
		"\u0000\u0000\u0000J\u018c\u0001\u0000\u0000\u0000L\u0194\u0001\u0000\u0000"+
		"\u0000N\u019c\u0001\u0000\u0000\u0000P\u01a4\u0001\u0000\u0000\u0000R"+
		"\u01ac\u0001\u0000\u0000\u0000T\u01b7\u0001\u0000\u0000\u0000V\u01b9\u0001"+
		"\u0000\u0000\u0000X\u01cd\u0001\u0000\u0000\u0000Z\u01cf\u0001\u0000\u0000"+
		"\u0000\\\u01e2\u0001\u0000\u0000\u0000^\u01e4\u0001\u0000\u0000\u0000"+
		"`\u01ea\u0001\u0000\u0000\u0000b\u01f1\u0001\u0000\u0000\u0000d\u01f9"+
		"\u0001\u0000\u0000\u0000f\u01fd\u0001\u0000\u0000\u0000h\u0205\u0001\u0000"+
		"\u0000\u0000jl\u0003\u0002\u0001\u0000kj\u0001\u0000\u0000\u0000lo\u0001"+
		"\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000"+
		"np\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000pq\u0005\u0000\u0000"+
		"\u0001q\u0001\u0001\u0000\u0000\u0000rv\u0003\u0004\u0002\u0000sv\u0003"+
		"\u0006\u0003\u0000tv\u0003\b\u0004\u0000ur\u0001\u0000\u0000\u0000us\u0001"+
		"\u0000\u0000\u0000ut\u0001\u0000\u0000\u0000v\u0003\u0001\u0000\u0000"+
		"\u0000wx\u0005\u0001\u0000\u0000xy\u0003h4\u0000yz\u0005\u001d\u0000\u0000"+
		"z\u0005\u0001\u0000\u0000\u0000{|\u0005\u0002\u0000\u0000|\u007f\u0003"+
		"h4\u0000}~\u0005 \u0000\u0000~\u0080\u0005$\u0000\u0000\u007f}\u0001\u0000"+
		"\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000"+
		"\u0000\u0000\u0081\u0082\u0005\u001d\u0000\u0000\u0082\u0007\u0001\u0000"+
		"\u0000\u0000\u0083\u0085\u0005\u0003\u0000\u0000\u0084\u0083\u0001\u0000"+
		"\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u008a\u0001\u0000"+
		"\u0000\u0000\u0086\u008b\u0003\u0014\n\u0000\u0087\u008b\u0003\u0018\f"+
		"\u0000\u0088\u008b\u0003\u001c\u000e\u0000\u0089\u008b\u0003\n\u0005\u0000"+
		"\u008a\u0086\u0001\u0000\u0000\u0000\u008a\u0087\u0001\u0000\u0000\u0000"+
		"\u008a\u0088\u0001\u0000\u0000\u0000\u008a\u0089\u0001\u0000\u0000\u0000"+
		"\u008b\t\u0001\u0000\u0000\u0000\u008c\u008d\u0003.\u0017\u0000\u008d"+
		"\u008e\u0005\u001d\u0000\u0000\u008e\u000b\u0001\u0000\u0000\u0000\u008f"+
		"\u0094\u0003\u000e\u0007\u0000\u0090\u0091\u0005\u001b\u0000\u0000\u0091"+
		"\u0093\u0005\u001c\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0093"+
		"\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094"+
		"\u0095\u0001\u0000\u0000\u0000\u0095\r\u0001\u0000\u0000\u0000\u0096\u0094"+
		"\u0001\u0000\u0000\u0000\u0097\u009b\u0003\u0010\b\u0000\u0098\u009b\u0005"+
		"\u0012\u0000\u0000\u0099\u009b\u0003h4\u0000\u009a\u0097\u0001\u0000\u0000"+
		"\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009a\u0099\u0001\u0000\u0000"+
		"\u0000\u009b\u000f\u0001\u0000\u0000\u0000\u009c\u00a1\u0005\u000e\u0000"+
		"\u0000\u009d\u00a1\u0005\u000f\u0000\u0000\u009e\u00a1\u0005\u0010\u0000"+
		"\u0000\u009f\u00a1\u0003\u0012\t\u0000\u00a0\u009c\u0001\u0000\u0000\u0000"+
		"\u00a0\u009d\u0001\u0000\u0000\u0000\u00a0\u009e\u0001\u0000\u0000\u0000"+
		"\u00a0\u009f\u0001\u0000\u0000\u0000\u00a1\u0011\u0001\u0000\u0000\u0000"+
		"\u00a2\u00aa\u0005\u0011\u0000\u0000\u00a3\u00a4\u0005\u0017\u0000\u0000"+
		"\u00a4\u00a7\u0005\u0014\u0000\u0000\u00a5\u00a6\u0005\u001e\u0000\u0000"+
		"\u00a6\u00a8\u00051\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a8\u0001\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9"+
		"\u00ab\u0005\u0018\u0000\u0000\u00aa\u00a3\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ab\u0001\u0000\u0000\u0000\u00ab\u0013\u0001\u0000\u0000\u0000\u00ac"+
		"\u00ad\u0005\u0004\u0000\u0000\u00ad\u00ae\u00051\u0000\u0000\u00ae\u00b2"+
		"\u0005\u0019\u0000\u0000\u00af\u00b1\u0003\u0016\u000b\u0000\u00b0\u00af"+
		"\u0001\u0000\u0000\u0000\u00b1\u00b4\u0001\u0000\u0000\u0000\u00b2\u00b0"+
		"\u0001\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000\u0000\u0000\u00b3\u00b5"+
		"\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0005\u001a\u0000\u0000\u00b6\u00b7\u0005\u001d\u0000\u0000\u00b7\u0015"+
		"\u0001\u0000\u0000\u0000\u00b8\u00ba\u0005\u0006\u0000\u0000\u00b9\u00b8"+
		"\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bb"+
		"\u0001\u0000\u0000\u0000\u00bb\u00bc\u0003\f\u0006\u0000\u00bc\u00bd\u0005"+
		"1\u0000\u0000\u00bd\u00be\u0005\u001d\u0000\u0000\u00be\u0017\u0001\u0000"+
		"\u0000\u0000\u00bf\u00c0\u0005\u0005\u0000\u0000\u00c0\u00c1\u00051\u0000"+
		"\u0000\u00c1\u00c3\u0005\u0019\u0000\u0000\u00c2\u00c4\u0003\u001a\r\u0000"+
		"\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000"+
		"\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000"+
		"\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c8\u0005\u001a\u0000\u0000"+
		"\u00c8\u00c9\u0005\u001d\u0000\u0000\u00c9\u0019\u0001\u0000\u0000\u0000"+
		"\u00ca\u00cb\u0003h4\u0000\u00cb\u00cc\u0005\u001d\u0000\u0000\u00cc\u001b"+
		"\u0001\u0000\u0000\u0000\u00cd\u00cf\u0003\u001e\u000f\u0000\u00ce\u00cd"+
		"\u0001\u0000\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d0"+
		"\u0001\u0000\u0000\u0000\u00d0\u00d1\u00051\u0000\u0000\u00d1\u00d3\u0005"+
		"\u0017\u0000\u0000\u00d2\u00d4\u0003$\u0012\u0000\u00d3\u00d2\u0001\u0000"+
		"\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000\u0000\u00d4\u00d5\u0001\u0000"+
		"\u0000\u0000\u00d5\u00d6\u0005\u0018\u0000\u0000\u00d6\u00d7\u0003(\u0014"+
		"\u0000\u00d7\u001d\u0001\u0000\u0000\u0000\u00d8\u00db\u0003\f\u0006\u0000"+
		"\u00d9\u00db\u0003 \u0010\u0000\u00da\u00d8\u0001\u0000\u0000\u0000\u00da"+
		"\u00d9\u0001\u0000\u0000\u0000\u00db\u001f\u0001\u0000\u0000\u0000\u00dc"+
		"\u00dd\u0005\u0017\u0000\u0000\u00dd\u00e2\u0003\"\u0011\u0000\u00de\u00df"+
		"\u0005\u001e\u0000\u0000\u00df\u00e1\u0003\"\u0011\u0000\u00e0\u00de\u0001"+
		"\u0000\u0000\u0000\u00e1\u00e4\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e5\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e5\u00e6\u0005"+
		"\u0018\u0000\u0000\u00e6!\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005\u0006"+
		"\u0000\u0000\u00e8\u00e9\u0003\f\u0006\u0000\u00e9\u00ea\u00051\u0000"+
		"\u0000\u00ea\u00eb\u0005!\u0000\u0000\u00eb\u00ec\u0003F#\u0000\u00ec"+
		"#\u0001\u0000\u0000\u0000\u00ed\u00f2\u0003&\u0013\u0000\u00ee\u00ef\u0005"+
		"\u001e\u0000\u0000\u00ef\u00f1\u0003&\u0013\u0000\u00f0\u00ee\u0001\u0000"+
		"\u0000\u0000\u00f1\u00f4\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000"+
		"\u0000\u0000\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3%\u0001\u0000\u0000"+
		"\u0000\u00f4\u00f2\u0001\u0000\u0000\u0000\u00f5\u00f6\u0003\f\u0006\u0000"+
		"\u00f6\u00f7\u00051\u0000\u0000\u00f7\'\u0001\u0000\u0000\u0000\u00f8"+
		"\u00fc\u0005\u0019\u0000\u0000\u00f9\u00fb\u0003*\u0015\u0000\u00fa\u00f9"+
		"\u0001\u0000\u0000\u0000\u00fb\u00fe\u0001\u0000\u0000\u0000\u00fc\u00fa"+
		"\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000\u0000\u00fd\u00ff"+
		"\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000\u0000\u0000\u00ff\u0100"+
		"\u0005\u001a\u0000\u0000\u0100)\u0001\u0000\u0000\u0000\u0101\u010c\u0003"+
		"(\u0014\u0000\u0102\u010c\u0003,\u0016\u0000\u0103\u010c\u00030\u0018"+
		"\u0000\u0104\u010c\u00032\u0019\u0000\u0105\u010c\u0003:\u001d\u0000\u0106"+
		"\u010c\u0003<\u001e\u0000\u0107\u010c\u0003>\u001f\u0000\u0108\u010c\u0003"+
		"B!\u0000\u0109\u010c\u0003D\"\u0000\u010a\u010c\u0005\u001d\u0000\u0000"+
		"\u010b\u0101\u0001\u0000\u0000\u0000\u010b\u0102\u0001\u0000\u0000\u0000"+
		"\u010b\u0103\u0001\u0000\u0000\u0000\u010b\u0104\u0001\u0000\u0000\u0000"+
		"\u010b\u0105\u0001\u0000\u0000\u0000\u010b\u0106\u0001\u0000\u0000\u0000"+
		"\u010b\u0107\u0001\u0000\u0000\u0000\u010b\u0108\u0001\u0000\u0000\u0000"+
		"\u010b\u0109\u0001\u0000\u0000\u0000\u010b\u010a\u0001\u0000\u0000\u0000"+
		"\u010c+\u0001\u0000\u0000\u0000\u010d\u010e\u0003.\u0017\u0000\u010e\u010f"+
		"\u0005\u001d\u0000\u0000\u010f-\u0001\u0000\u0000\u0000\u0110\u0112\u0005"+
		"\u0006\u0000\u0000\u0111\u0110\u0001\u0000\u0000\u0000\u0111\u0112\u0001"+
		"\u0000\u0000\u0000\u0112\u0113\u0001\u0000\u0000\u0000\u0113\u0114\u0003"+
		"\f\u0006\u0000\u0114\u0115\u00051\u0000\u0000\u0115\u0116\u0005!\u0000"+
		"\u0000\u0116\u0117\u0003F#\u0000\u0117/\u0001\u0000\u0000\u0000\u0118"+
		"\u0119\u00036\u001b\u0000\u0119\u011a\u0005!\u0000\u0000\u011a\u011b\u0003"+
		"F#\u0000\u011b\u011c\u0005\u001d\u0000\u0000\u011c1\u0001\u0000\u0000"+
		"\u0000\u011d\u011e\u0005\u0017\u0000\u0000\u011e\u0123\u00034\u001a\u0000"+
		"\u011f\u0120\u0005\u001e\u0000\u0000\u0120\u0122\u00034\u001a\u0000\u0121"+
		"\u011f\u0001\u0000\u0000\u0000\u0122\u0125\u0001\u0000\u0000\u0000\u0123"+
		"\u0121\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124"+
		"\u0126\u0001\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0126"+
		"\u0127\u0005\u0018\u0000\u0000\u0127\u0128\u0005!\u0000\u0000\u0128\u0129"+
		"\u0003F#\u0000\u0129\u012a\u0005\u001d\u0000\u0000\u012a3\u0001\u0000"+
		"\u0000\u0000\u012b\u012d\u0005\u0006\u0000\u0000\u012c\u012b\u0001\u0000"+
		"\u0000\u0000\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u012e\u0001\u0000"+
		"\u0000\u0000\u012e\u012f\u0003\f\u0006\u0000\u012f\u0130\u00051\u0000"+
		"\u0000\u01305\u0001\u0000\u0000\u0000\u0131\u0135\u00051\u0000\u0000\u0132"+
		"\u0134\u00038\u001c\u0000\u0133\u0132\u0001\u0000\u0000\u0000\u0134\u0137"+
		"\u0001\u0000\u0000\u0000\u0135\u0133\u0001\u0000\u0000\u0000\u0135\u0136"+
		"\u0001\u0000\u0000\u0000\u01367\u0001\u0000\u0000\u0000\u0137\u0135\u0001"+
		"\u0000\u0000\u0000\u0138\u0139\u0005 \u0000\u0000\u0139\u013f\u00051\u0000"+
		"\u0000\u013a\u013b\u0005\u001b\u0000\u0000\u013b\u013c\u0003F#\u0000\u013c"+
		"\u013d\u0005\u001c\u0000\u0000\u013d\u013f\u0001\u0000\u0000\u0000\u013e"+
		"\u0138\u0001\u0000\u0000\u0000\u013e\u013a\u0001\u0000\u0000\u0000\u013f"+
		"9\u0001\u0000\u0000\u0000\u0140\u0141\u0005\u0007\u0000\u0000\u0141\u0142"+
		"\u0005\u0017\u0000\u0000\u0142\u0143\u0003F#\u0000\u0143\u0144\u0005\u0018"+
		"\u0000\u0000\u0144\u0147\u0003(\u0014\u0000\u0145\u0146\u0005\b\u0000"+
		"\u0000\u0146\u0148\u0003(\u0014\u0000\u0147\u0145\u0001\u0000\u0000\u0000"+
		"\u0147\u0148\u0001\u0000\u0000\u0000\u0148;\u0001\u0000\u0000\u0000\u0149"+
		"\u014a\u0005\t\u0000\u0000\u014a\u014b\u0005\u0017\u0000\u0000\u014b\u014c"+
		"\u0003F#\u0000\u014c\u014d\u0005\u0018\u0000\u0000\u014d\u014e\u0003("+
		"\u0014\u0000\u014e=\u0001\u0000\u0000\u0000\u014f\u0150\u0005\n\u0000"+
		"\u0000\u0150\u0151\u0005\u0017\u0000\u0000\u0151\u0152\u0003F#\u0000\u0152"+
		"\u0153\u0005\u0018\u0000\u0000\u0153\u0157\u0005\u0019\u0000\u0000\u0154"+
		"\u0156\u0003@ \u0000\u0155\u0154\u0001\u0000\u0000\u0000\u0156\u0159\u0001"+
		"\u0000\u0000\u0000\u0157\u0155\u0001\u0000\u0000\u0000\u0157\u0158\u0001"+
		"\u0000\u0000\u0000\u0158\u015a\u0001\u0000\u0000\u0000\u0159\u0157\u0001"+
		"\u0000\u0000\u0000\u015a\u015b\u0005\u001a\u0000\u0000\u015b?\u0001\u0000"+
		"\u0000\u0000\u015c\u015d\u0005\u000b\u0000\u0000\u015d\u015e\u0003h4\u0000"+
		"\u015e\u015f\u00051\u0000\u0000\u015f\u0163\u0005\u001f\u0000\u0000\u0160"+
		"\u0162\u0003*\u0015\u0000\u0161\u0160\u0001\u0000\u0000\u0000\u0162\u0165"+
		"\u0001\u0000\u0000\u0000\u0163\u0161\u0001\u0000\u0000\u0000\u0163\u0164"+
		"\u0001\u0000\u0000\u0000\u0164\u016f\u0001\u0000\u0000\u0000\u0165\u0163"+
		"\u0001\u0000\u0000\u0000\u0166\u0167\u0005\f\u0000\u0000\u0167\u016b\u0005"+
		"\u001f\u0000\u0000\u0168\u016a\u0003*\u0015\u0000\u0169\u0168\u0001\u0000"+
		"\u0000\u0000\u016a\u016d\u0001\u0000\u0000\u0000\u016b\u0169\u0001\u0000"+
		"\u0000\u0000\u016b\u016c\u0001\u0000\u0000\u0000\u016c\u016f\u0001\u0000"+
		"\u0000\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016e\u015c\u0001\u0000"+
		"\u0000\u0000\u016e\u0166\u0001\u0000\u0000\u0000\u016fA\u0001\u0000\u0000"+
		"\u0000\u0170\u0171\u0005\r\u0000\u0000\u0171\u017e\u0005\u001d\u0000\u0000"+
		"\u0172\u0173\u0005\r\u0000\u0000\u0173\u0174\u0003F#\u0000\u0174\u0175"+
		"\u0005\u001d\u0000\u0000\u0175\u017e\u0001\u0000\u0000\u0000\u0176\u0177"+
		"\u0005\r\u0000\u0000\u0177\u0179\u0005\u0017\u0000\u0000\u0178\u017a\u0003"+
		"f3\u0000\u0179\u0178\u0001\u0000\u0000\u0000\u0179\u017a\u0001\u0000\u0000"+
		"\u0000\u017a\u017b\u0001\u0000\u0000\u0000\u017b\u017c\u0005\u0018\u0000"+
		"\u0000\u017c\u017e\u0005\u001d\u0000\u0000\u017d\u0170\u0001\u0000\u0000"+
		"\u0000\u017d\u0172\u0001\u0000\u0000\u0000\u017d\u0176\u0001\u0000\u0000"+
		"\u0000\u017eC\u0001\u0000\u0000\u0000\u017f\u0180\u0003F#\u0000\u0180"+
		"\u0181\u0005\u001d\u0000\u0000\u0181E\u0001\u0000\u0000\u0000\u0182\u0183"+
		"\u0003H$\u0000\u0183G\u0001\u0000\u0000\u0000\u0184\u0189\u0003J%\u0000"+
		"\u0185\u0186\u0005*\u0000\u0000\u0186\u0188\u0003J%\u0000\u0187\u0185"+
		"\u0001\u0000\u0000\u0000\u0188\u018b\u0001\u0000\u0000\u0000\u0189\u0187"+
		"\u0001\u0000\u0000\u0000\u0189\u018a\u0001\u0000\u0000\u0000\u018aI\u0001"+
		"\u0000\u0000\u0000\u018b\u0189\u0001\u0000\u0000\u0000\u018c\u0191\u0003"+
		"L&\u0000\u018d\u018e\u0005)\u0000\u0000\u018e\u0190\u0003L&\u0000\u018f"+
		"\u018d\u0001\u0000\u0000\u0000\u0190\u0193\u0001\u0000\u0000\u0000\u0191"+
		"\u018f\u0001\u0000\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000\u0192"+
		"K\u0001\u0000\u0000\u0000\u0193\u0191\u0001\u0000\u0000\u0000\u0194\u0199"+
		"\u0003N\'\u0000\u0195\u0196\u0007\u0000\u0000\u0000\u0196\u0198\u0003"+
		"N\'\u0000\u0197\u0195\u0001\u0000\u0000\u0000\u0198\u019b\u0001\u0000"+
		"\u0000\u0000\u0199\u0197\u0001\u0000\u0000\u0000\u0199\u019a\u0001\u0000"+
		"\u0000\u0000\u019aM\u0001\u0000\u0000\u0000\u019b\u0199\u0001\u0000\u0000"+
		"\u0000\u019c\u01a1\u0003P(\u0000\u019d\u019e\u0007\u0001\u0000\u0000\u019e"+
		"\u01a0\u0003P(\u0000\u019f\u019d\u0001\u0000\u0000\u0000\u01a0\u01a3\u0001"+
		"\u0000\u0000\u0000\u01a1\u019f\u0001\u0000\u0000\u0000\u01a1\u01a2\u0001"+
		"\u0000\u0000\u0000\u01a2O\u0001\u0000\u0000\u0000\u01a3\u01a1\u0001\u0000"+
		"\u0000\u0000\u01a4\u01a9\u0003R)\u0000\u01a5\u01a6\u0007\u0002\u0000\u0000"+
		"\u01a6\u01a8\u0003R)\u0000\u01a7\u01a5\u0001\u0000\u0000\u0000\u01a8\u01ab"+
		"\u0001\u0000\u0000\u0000\u01a9\u01a7\u0001\u0000\u0000\u0000\u01a9\u01aa"+
		"\u0001\u0000\u0000\u0000\u01aaQ\u0001\u0000\u0000\u0000\u01ab\u01a9\u0001"+
		"\u0000\u0000\u0000\u01ac\u01b1\u0003T*\u0000\u01ad\u01ae\u0007\u0003\u0000"+
		"\u0000\u01ae\u01b0\u0003T*\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01b0"+
		"\u01b3\u0001\u0000\u0000\u0000\u01b1\u01af\u0001\u0000\u0000\u0000\u01b1"+
		"\u01b2\u0001\u0000\u0000\u0000\u01b2S\u0001\u0000\u0000\u0000\u01b3\u01b1"+
		"\u0001\u0000\u0000\u0000\u01b4\u01b5\u0007\u0004\u0000\u0000\u01b5\u01b8"+
		"\u0003T*\u0000\u01b6\u01b8\u0003V+\u0000\u01b7\u01b4\u0001\u0000\u0000"+
		"\u0000\u01b7\u01b6\u0001\u0000\u0000\u0000\u01b8U\u0001\u0000\u0000\u0000"+
		"\u01b9\u01bd\u0003\\.\u0000\u01ba\u01bc\u0003X,\u0000\u01bb\u01ba\u0001"+
		"\u0000\u0000\u0000\u01bc\u01bf\u0001\u0000\u0000\u0000\u01bd\u01bb\u0001"+
		"\u0000\u0000\u0000\u01bd\u01be\u0001\u0000\u0000\u0000\u01beW\u0001\u0000"+
		"\u0000\u0000\u01bf\u01bd\u0001\u0000\u0000\u0000\u01c0\u01c2\u0005\u0017"+
		"\u0000\u0000\u01c1\u01c3\u0003Z-\u0000\u01c2\u01c1\u0001\u0000\u0000\u0000"+
		"\u01c2\u01c3\u0001\u0000\u0000\u0000\u01c3\u01c4\u0001\u0000\u0000\u0000"+
		"\u01c4\u01ce\u0005\u0018\u0000\u0000\u01c5\u01c6\u0005 \u0000\u0000\u01c6"+
		"\u01ce\u00051\u0000\u0000\u01c7\u01c8\u0005\u001b\u0000\u0000\u01c8\u01c9"+
		"\u0003F#\u0000\u01c9\u01ca\u0005\u001c\u0000\u0000\u01ca\u01ce\u0001\u0000"+
		"\u0000\u0000\u01cb\u01ce\u0005&\u0000\u0000\u01cc\u01ce\u0005\'\u0000"+
		"\u0000\u01cd\u01c0\u0001\u0000\u0000\u0000\u01cd\u01c5\u0001\u0000\u0000"+
		"\u0000\u01cd\u01c7\u0001\u0000\u0000\u0000\u01cd\u01cb\u0001\u0000\u0000"+
		"\u0000\u01cd\u01cc\u0001\u0000\u0000\u0000\u01ceY\u0001\u0000\u0000\u0000"+
		"\u01cf\u01d4\u0003F#\u0000\u01d0\u01d1\u0005\u001e\u0000\u0000\u01d1\u01d3"+
		"\u0003F#\u0000\u01d2\u01d0\u0001\u0000\u0000\u0000\u01d3\u01d6\u0001\u0000"+
		"\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000\u0000\u01d4\u01d5\u0001\u0000"+
		"\u0000\u0000\u01d5[\u0001\u0000\u0000\u0000\u01d6\u01d4\u0001\u0000\u0000"+
		"\u0000\u01d7\u01e3\u0005\u0014\u0000\u0000\u01d8\u01e3\u0005\u0015\u0000"+
		"\u0000\u01d9\u01e3\u0005\u0013\u0000\u0000\u01da\u01e3\u0005\u0016\u0000"+
		"\u0000\u01db\u01e3\u0003^/\u0000\u01dc\u01e3\u0003`0\u0000\u01dd\u01e3"+
		"\u00051\u0000\u0000\u01de\u01df\u0005\u0017\u0000\u0000\u01df\u01e0\u0003"+
		"F#\u0000\u01e0\u01e1\u0005\u0018\u0000\u0000\u01e1\u01e3\u0001\u0000\u0000"+
		"\u0000\u01e2\u01d7\u0001\u0000\u0000\u0000\u01e2\u01d8\u0001\u0000\u0000"+
		"\u0000\u01e2\u01d9\u0001\u0000\u0000\u0000\u01e2\u01da\u0001\u0000\u0000"+
		"\u0000\u01e2\u01db\u0001\u0000\u0000\u0000\u01e2\u01dc\u0001\u0000\u0000"+
		"\u0000\u01e2\u01dd\u0001\u0000\u0000\u0000\u01e2\u01de\u0001\u0000\u0000"+
		"\u0000\u01e3]\u0001\u0000\u0000\u0000\u01e4\u01e6\u0005\u001b\u0000\u0000"+
		"\u01e5\u01e7\u0003f3\u0000\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e6\u01e7"+
		"\u0001\u0000\u0000\u0000\u01e7\u01e8\u0001\u0000\u0000\u0000\u01e8\u01e9"+
		"\u0005\u001c\u0000\u0000\u01e9_\u0001\u0000\u0000\u0000\u01ea\u01eb\u0003"+
		"h4\u0000\u01eb\u01ed\u0005\u0017\u0000\u0000\u01ec\u01ee\u0003b1\u0000"+
		"\u01ed\u01ec\u0001\u0000\u0000\u0000\u01ed\u01ee\u0001\u0000\u0000\u0000"+
		"\u01ee\u01ef\u0001\u0000\u0000\u0000\u01ef\u01f0\u0005\u0018\u0000\u0000"+
		"\u01f0a\u0001\u0000\u0000\u0000\u01f1\u01f6\u0003d2\u0000\u01f2\u01f3"+
		"\u0005\u001e\u0000\u0000\u01f3\u01f5\u0003d2\u0000\u01f4\u01f2\u0001\u0000"+
		"\u0000\u0000\u01f5\u01f8\u0001\u0000\u0000\u0000\u01f6\u01f4\u0001\u0000"+
		"\u0000\u0000\u01f6\u01f7\u0001\u0000\u0000\u0000\u01f7c\u0001\u0000\u0000"+
		"\u0000\u01f8\u01f6\u0001\u0000\u0000\u0000\u01f9\u01fa\u00051\u0000\u0000"+
		"\u01fa\u01fb\u0005\u001f\u0000\u0000\u01fb\u01fc\u0003F#\u0000\u01fce"+
		"\u0001\u0000\u0000\u0000\u01fd\u0202\u0003F#\u0000\u01fe\u01ff\u0005\u001e"+
		"\u0000\u0000\u01ff\u0201\u0003F#\u0000\u0200\u01fe\u0001\u0000\u0000\u0000"+
		"\u0201\u0204\u0001\u0000\u0000\u0000\u0202\u0200\u0001\u0000\u0000\u0000"+
		"\u0202\u0203\u0001\u0000\u0000\u0000\u0203g\u0001\u0000\u0000\u0000\u0204"+
		"\u0202\u0001\u0000\u0000\u0000\u0205\u020a\u00051\u0000\u0000\u0206\u0207"+
		"\u0005 \u0000\u0000\u0207\u0209\u00051\u0000\u0000\u0208\u0206\u0001\u0000"+
		"\u0000\u0000\u0209\u020c\u0001\u0000\u0000\u0000\u020a\u0208\u0001\u0000"+
		"\u0000\u0000\u020a\u020b\u0001\u0000\u0000\u0000\u020bi\u0001\u0000\u0000"+
		"\u0000\u020c\u020a\u0001\u0000\u0000\u00001mu\u007f\u0084\u008a\u0094"+
		"\u009a\u00a0\u00a7\u00aa\u00b2\u00b9\u00c5\u00ce\u00d3\u00da\u00e2\u00f2"+
		"\u00fc\u010b\u0111\u0123\u012c\u0135\u013e\u0147\u0157\u0163\u016b\u016e"+
		"\u0179\u017d\u0189\u0191\u0199\u01a1\u01a9\u01b1\u01b7\u01bd\u01c2\u01cd"+
		"\u01d4\u01e2\u01e6\u01ed\u01f6\u0202\u020a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}