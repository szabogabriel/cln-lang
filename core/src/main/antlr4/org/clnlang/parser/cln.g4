grammar cln;

// =========================
// Parser rules
// =========================

program
  : topLevelDecl* EOF
  ;

topLevelDecl
  : packageDecl
  | importDecl
  | decl
  ;

packageDecl
  : PACKAGE qualifiedName SEMI
  ;

importDecl
  : IMPORT qualifiedName (DOT STAR)? SEMI
  ;

decl
  : EXPOSE? (structDecl | unionDecl | functionDecl | globalVarDecl)
  ;

globalVarDecl
  : varBinding SEMI
  ;

// ---- Types ----

type
  : baseType (LBRACK RBRACK)*
  ;

baseType
  : primitiveType
  | qualifiedName
  ;

primitiveType
  : INT_T
  | BOOL_T
  | STRING_T
  ;

// ---- Structs and Unions ----

structDecl
  : STRUCT ID LBRACE structFieldDecl* RBRACE SEMI
  ;

structFieldDecl
  : VAR? type ID SEMI
  ;

unionDecl
  : UNION ID LBRACE unionMember+ RBRACE SEMI
  ;

unionMember
  : qualifiedName SEMI
  ;

// ---- Functions ----

functionDecl
  : namedReturnSig ID LPAREN paramList? RPAREN block
  ;

namedReturnSig
  : LPAREN returnVar (COMMA returnVar)* RPAREN
  ;

returnVar
  : VAR type ID ASSIGN expr
  ;

paramList
  : param (COMMA param)*
  ;

param
  : type ID
  ;

// ---- Statements ----

block
  : LBRACE stmt* RBRACE
  ;

stmt
  : block
  | varDeclStmt
  | assignStmt
  | tupleAssignStmt
  | ifStmt
  | whileStmt
  | switchStmt
  | returnStmt
  | exprStmt
  | SEMI
  ;

varDeclStmt
  : varBinding SEMI
  ;

varBinding
  : VAR? type ID ASSIGN expr
  ;

assignStmt
  : lvalue ASSIGN expr SEMI
  ;

tupleAssignStmt
  : LPAREN tupleBind (COMMA tupleBind)* RPAREN ASSIGN expr SEMI
  ;

tupleBind
  : VAR? type ID
  ;

lvalue
  : ID lvalueSuffix*
  ;

lvalueSuffix
  : DOT ID
  | LBRACK expr RBRACK
  ;

ifStmt
  : IF LPAREN expr RPAREN block (ELSE block)?
  ;

whileStmt
  : WHILE LPAREN expr RPAREN block
  ;

switchStmt
  : SWITCH LPAREN expr RPAREN LBRACE caseClause* RBRACE
  ;

caseClause
  : CASE qualifiedName ID COLON stmt*
  | DEFAULT COLON stmt*
  ;

returnStmt
  : RETURN SEMI
  | RETURN expr SEMI
  | RETURN LPAREN exprList? RPAREN SEMI
  ;

exprStmt
  : expr SEMI
  ;

// ---- Expressions (precedence ladder) ----

expr
  : orExpr
  ;

orExpr
  : andExpr (OR andExpr)*
  ;

andExpr
  : equalityExpr (AND equalityExpr)*
  ;

equalityExpr
  : relExpr ((EQ | NEQ) relExpr)*
  ;

relExpr
  : addExpr ((LT | LTE | GT | GTE) addExpr)*
  ;

addExpr
  : mulExpr ((PLUS | MINUS) mulExpr)*
  ;

mulExpr
  : unaryExpr ((STAR | SLASH) unaryExpr)*
  ;

unaryExpr
  : (NOT | MINUS) unaryExpr
  | postfixExpr
  ;

postfixExpr
  : primaryExpr postfixOp*
  ;

postfixOp
  : LPAREN argList? RPAREN
  | DOT ID
  | LBRACK expr RBRACK
  ;

argList
  : expr (COMMA expr)*
  ;

primaryExpr
  : INT_LIT
  | BOOL_LIT
  | STRING_LIT
  | structLiteral
  | ID
  | LPAREN expr RPAREN
  ;

structLiteral
  : qualifiedName LPAREN fieldInitList? RPAREN
  ;

fieldInitList
  : fieldInit (COMMA fieldInit)*
  ;

fieldInit
  : ID COLON expr
  ;

exprList
  : expr (COMMA expr)*
  ;

qualifiedName
  : ID (DOT ID)*
  ;

// =========================
// Lexer rules
// =========================

// Keywords
PACKAGE : 'package';
IMPORT  : 'import';
EXPOSE  : 'expose';

STRUCT  : 'struct';
UNION   : 'union';

VAR     : 'var';

IF      : 'if';
ELSE    : 'else';
WHILE   : 'while';
SWITCH  : 'switch';
CASE    : 'case';
DEFAULT : 'default';
RETURN  : 'return';

// Primitive type keywords
INT_T    : 'int';
BOOL_T   : 'bool';
STRING_T : 'string';

// Literals
BOOL_LIT   : 'true' | 'false';
INT_LIT    : [0-9]+;
STRING_LIT : '"' ( '\\' . | ~["\\\r\n] )* '"';

// Operators / punctuation
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';

SEMI   : ';';
COMMA  : ',';
COLON  : ':';
DOT    : '.';

ASSIGN : '=';

PLUS   : '+';
MINUS  : '-';
STAR   : '*';
SLASH  : '/';

NOT    : '!';
AND    : '&&';
OR     : '||';

EQ     : '==';
NEQ    : '!=';
LT     : '<';
LTE    : '<=';
GT     : '>';
GTE    : '>=';

// Identifiers
ID : [A-Za-z_] [A-Za-z0-9_]* ;

// Comments + whitespace
LINE_COMMENT  : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;
WS            : [ \t\r\n]+ -> skip ;

