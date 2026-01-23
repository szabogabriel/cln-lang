# Package Organization

The AST (Abstract Syntax Tree) implementation is organized into logical subpackages following a clean architecture:

## Package Structure

```
org.clnlang.ast/
├── ASTNode.java                    # Base class for all AST nodes
├── BlockNode.java                  # Statement block container
│
├── declaration/                    # Top-level declarations
│   ├── ProgramNode.java           # Root AST node
│   ├── PackageDeclNode.java       # Package declaration
│   ├── ImportDeclNode.java        # Import statement
│   ├── StructDeclNode.java        # Struct declaration
│   ├── UnionDeclNode.java         # Union declaration
│   └── FunctionDeclNode.java      # Function declaration
│
├── statement/                      # Statement nodes
│   ├── Stmt.java                  # Base statement class
│   ├── VarDeclStmt.java          # Variable declaration
│   ├── AssignStmt.java           # Assignment statement
│   ├── TupleAssignStmt.java      # Tuple destructuring assignment
│   ├── IfStmt.java               # If-else conditional
│   ├── WhileStmt.java            # While loop
│   ├── SwitchStmt.java           # Switch statement
│   ├── ReturnStmt.java           # Return statement
│   ├── ExprStmt.java             # Expression as statement
│   └── EmptyStmt.java            # Empty statement
│
├── expression/                     # Expression nodes
│   ├── Expr.java                  # Base expression class
│   ├── BinaryExpr.java           # Binary operations (+, -, *, /, etc.)
│   ├── UnaryExpr.java            # Unary operations (!, -, etc.)
│   ├── IdentifierExpr.java       # Variable/function reference
│   ├── IntLiteralExpr.java       # Integer literal
│   ├── BoolLiteralExpr.java      # Boolean literal
│   ├── StringLiteralExpr.java    # String literal
│   ├── CallExpr.java             # Function call
│   ├── MemberAccessExpr.java     # Member access (object.field)
│   ├── IndexAccessExpr.java      # Array/index access
│   └── StructLiteralExpr.java    # Struct literal constructor
│
└── visitor/                        # Visitor pattern implementations
    ├── ASTVisitor.java            # Base visitor interface
    ├── ASTPrinterVisitor.java     # Basic AST printer
    └── DetailedASTPrinter.java    # Detailed AST printer
```

## Design Principles

### 1. Separation of Concerns
- **declarations**: Top-level program structure (packages, imports, types, functions)
- **statements**: Executable statements within blocks
- **expressions**: Value-producing expressions
- **visitor**: AST traversal and processing implementations

### 2. Type Hierarchy
All nodes extend `ASTNode` and implement the visitor pattern:
- `ASTNode` (base)
  - Declaration nodes (in `declaration` package)
  - `Stmt` → Statement implementations (in `statement` package)
  - `Expr` → Expression implementations (in `expression` package)

### 3. Visitor Pattern
The visitor pattern allows easy addition of new AST operations without modifying node classes:
- Implement `ASTVisitor` interface
- Add `visit()` methods for each node type
- Call `node.accept(visitor)` to traverse

## Usage Example

```java
import org.clnlang.ast.declaration.ProgramNode;
import org.clnlang.ast.visitor.DetailedASTPrinter;

// Build AST
ProgramNode program = builder.visitProgram(ctx);

// Process with visitor
DetailedASTPrinter printer = new DetailedASTPrinter();
program.accept(printer);
```

## Benefits

1. **Clear Organization**: Related classes grouped in logical packages
2. **Maintainability**: Easy to locate and modify specific node types
3. **Extensibility**: New visitors can be added without changing existing code
4. **Type Safety**: Strong typing with `Stmt` and `Expr` base classes
5. **Scalability**: Package structure scales well as language features grow
