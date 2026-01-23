# Statement and Expression Implementation

This document describes the complete statement and expression hierarchy implemented for the Clean language parser.

## Package Structure

```
org.clnlang.ast/
├── statement/          # Statement classes
│   ├── Stmt.java       # Base statement class
│   ├── VarDeclStmt.java
│   ├── AssignStmt.java
│   ├── TupleAssignStmt.java
│   ├── IfStmt.java
│   ├── WhileStmt.java
│   ├── SwitchStmt.java
│   ├── ReturnStmt.java
│   ├── ExprStmt.java
│   └── EmptyStmt.java
└── expression/         # Expression classes
    ├── Expr.java       # Base expression class
    ├── BinaryExpr.java
    ├── UnaryExpr.java
    ├── IdentifierExpr.java
    ├── IntLiteralExpr.java
    ├── BoolLiteralExpr.java
    ├── StringLiteralExpr.java
    ├── CallExpr.java
    ├── MemberAccessExpr.java
    ├── IndexAccessExpr.java
    └── StructLiteralExpr.java
```

## Statement Classes

### Base Class: `Stmt`
All statement nodes extend from `Stmt`, which itself extends `ASTNode`.

### Statement Types

1. **VarDeclStmt** - Variable declaration with initialization
   ```clean
   var int x = 10;
   Point p = Point(x: 5, y: 10);
   ```

2. **AssignStmt** - Assignment to an lvalue
   ```clean
   x = 10;
   p.x = 5;
   arr[0] = value;
   ```

3. **TupleAssignStmt** - Tuple destructuring assignment
   ```clean
   (var int a, var int b) = getTuple();
   ```

4. **IfStmt** - Conditional statement
   ```clean
   if (condition) {
       // then block
   } else {
       // else block
   }
   ```

5. **WhileStmt** - Loop statement
   ```clean
   while (condition) {
       // body
   }
   ```

6. **SwitchStmt** - Pattern matching with case clauses
   ```clean
   switch (shape) {
       case Circle c: // statements
       case Rectangle r: // statements
       default: // statements
   }
   ```

7. **ReturnStmt** - Function return
   ```clean
   return;
   return value;
   return (val1, val2);
   ```

8. **ExprStmt** - Expression as statement
   ```clean
   functionCall();
   ```

9. **EmptyStmt** - Empty statement (just semicolon)
   ```clean
   ;
   ```

## Expression Classes

### Base Class: `Expr`
All expression nodes extend from `Expr`, which itself extends `ASTNode`.

### Expression Types

1. **BinaryExpr** - Binary operations
   - Operators: `||`, `&&`, `==`, `!=`, `<`, `<=`, `>`, `>=`, `+`, `-`, `*`, `/`
   - Example: `(a + b)`, `(x > 10)`, `(flag && enabled)`

2. **UnaryExpr** - Unary operations
   - Operators: `!` (not), `-` (negation)
   - Example: `!flag`, `-value`

3. **IdentifierExpr** - Variable/function reference
   - Example: `x`, `myFunction`, `counter`

4. **IntLiteralExpr** - Integer literal
   - Example: `42`, `0`, `1000`

5. **BoolLiteralExpr** - Boolean literal
   - Example: `true`, `false`

6. **StringLiteralExpr** - String literal
   - Example: `"Hello, World!"`

7. **CallExpr** - Function call
   - Example: `add(a, b)`, `process()`, `obj.method(x, y)`

8. **MemberAccessExpr** - Dot notation for member access
   - Example: `point.x`, `obj.field`

9. **IndexAccessExpr** - Array/index access
   - Example: `arr[i]`, `matrix[0][1]`

10. **StructLiteralExpr** - Struct initialization
    - Example: `Point(x: 10, y: 20)`
    - Contains list of `FieldInit` with field name and value

## BlockNode Update

`BlockNode` now holds `List<Stmt>` instead of `List<ASTNode>`, properly typed for statements.

## ClnASTBuilder Updates

The visitor now builds complete AST structures including:

### Statement Visitors
- `visitStmt()` - Dispatches to appropriate statement visitor
- `visitVarDeclStmt()` - Builds variable declaration
- `visitAssignStmt()` - Builds assignment
- `visitTupleAssignStmt()` - Builds tuple assignment
- `visitIfStmt()` - Builds if-else statement
- `visitWhileStmt()` - Builds while loop
- `visitSwitchStmt()` - Builds switch with case clauses
- `visitReturnStmt()` - Builds return statement
- `visitExprStmt()` - Builds expression statement

### Expression Visitors
- `visitExpr()` - Entry point for expressions
- `visitOrExpr()` - Handles `||` operator (left-associative)
- `visitAndExpr()` - Handles `&&` operator
- `visitEqualityExpr()` - Handles `==`, `!=`
- `visitRelExpr()` - Handles `<`, `<=`, `>`, `>=`
- `visitAddExpr()` - Handles `+`, `-`
- `visitMulExpr()` - Handles `*`, `/`
- `visitUnaryExpr()` - Handles `!`, `-` (unary)
- `visitPostfixExpr()` - Handles calls, member access, indexing
- `visitPrimaryExpr()` - Handles literals, identifiers, parentheses
- `visitStructLiteral()` - Handles struct construction
- `visitLvalue()` - Handles assignable expressions

## Operator Precedence

The parser respects proper operator precedence (from lowest to highest):
1. `||` (logical OR)
2. `&&` (logical AND)
3. `==`, `!=` (equality)
4. `<`, `<=`, `>`, `>=` (relational)
5. `+`, `-` (additive)
6. `*`, `/` (multiplicative)
7. `!`, `-` (unary)
8. Function calls, member access, indexing (postfix)

## Example Usage

```java
// Parse a file
ClnASTBuilder astBuilder = new ClnASTBuilder();
ProgramNode ast = (ProgramNode) astBuilder.visit(parseTree);

// Access function
FunctionDeclNode func = (FunctionDeclNode) ast.getDeclarations().get(0);
BlockNode body = func.getBlock();

// Iterate through statements
for (Stmt stmt : body.getStatements()) {
    if (stmt instanceof IfStmt) {
        IfStmt ifStmt = (IfStmt) stmt;
        Expr condition = ifStmt.getCondition();
        
        if (condition instanceof BinaryExpr) {
            BinaryExpr binExpr = (BinaryExpr) condition;
            String operator = binExpr.getOperator();
            Expr left = binExpr.getLeft();
            Expr right = binExpr.getRight();
        }
    }
}
```

## Detailed AST Printer

The `DetailedASTPrinter` class provides a comprehensive view of the AST including:
- All declarations (packages, imports, structs, unions, functions)
- Function bodies with statement-by-statement breakdown
- Expression trees with proper nesting
- Control flow structures (if/else, while, switch)

## Test Results

Running the parser on test files shows complete statement and expression parsing:

```
Function: (int result) add(int a, int b)
  Block:
    Assign: result = (a + b)
    Return: return

Function: (int answer) main()
  Block:
    VarDecl: var Point p = Point(x: 10, y: 20)
    VarDecl: var int sum = add(p.x, p.y)
    If: (sum > 25)
      Then:
        Block:
          Assign: answer = sum
      Else:
        Block:
          Assign: answer = 0
    Return: return
```

## Next Steps

With complete statement and expression support, you can now:
1. **Type Checking** - Validate expression types and type compatibility
2. **Symbol Tables** - Track variable scopes and declarations
3. **Semantic Analysis** - Check for undefined variables, type mismatches
4. **Code Generation** - Generate bytecode, IR, or target language code
5. **Optimization** - Transform AST for better performance
6. **Interpreter** - Execute the AST directly
