# Clean Language Parser - Visitor Pattern Implementation

This document describes the visitor pattern implementation for parsing Clean language files and building an Abstract Syntax Tree (AST).

## Architecture

The parser implementation consists of three main layers:

### 1. ANTLR4 Generated Classes (Parse Tree)
- **clnLexer** - Tokenizes input
- **clnParser** - Creates a parse tree from tokens
- Auto-generated from [cln.g4](src/main/antlr4/org/clnlang/parser/cln.g4)

### 2. AST Layer ([ast/](src/main/java/org/clnlang/ast/))
Object-oriented representation of the program structure:
- **ASTNode** - Base class for all AST nodes
- **ProgramNode** - Root node containing package, imports, and declarations
- **PackageDeclNode** - Package declaration
- **ImportDeclNode** - Import statement
- **StructDeclNode** - Struct definition with fields
- **UnionDeclNode** - Union definition with member types
- **FunctionDeclNode** - Function with parameters, return values, and body block
- **BlockNode** - Statement block representing function body

### 3. Visitor Pattern Implementation

#### ANTLR Visitor: [ClnASTBuilder](src/main/java/org/clnlang/parser/ClnASTBuilder.java)
Converts ANTLR parse tree to AST:
```java
ClnASTBuilder astBuilder = new ClnASTBuilder();
ProgramNode ast = (ProgramNode) astBuilder.visit(parseTree);
```

**Key Methods:**
- `visitProgram()` - Entry point, builds ProgramNode
- `visitPackageDecl()` - Extracts package name
- `visitImportDecl()` - Processes imports (including wildcards)
- `visitStructDecl()` - Builds struct with fields and types
- `visitUnionDecl()` - Builds union with member types
- `visitFunctionDecl()` - Captures function signature (params + return vars + block)
- `visitBlock()` - Processes function body (currently counts statements)

#### AST Visitor: [ASTVisitor](src/main/java/org/clnlang/ast/ASTVisitor.java) Interface
Defines operations on AST nodes:
```java
public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(PackageDeclNode node);
    void visit(UnionDeclNode node);
    void visit(ImportDeclNode node);
    void visit(StructDeclNode node);
    void visit(FunctionDeclNode node);
}
```

## Usage Examples

### Basic File Parsing

```java
// 1. Create lexer and parser
CharStream input = CharStreams.fromFileName("program.cln");
clnLexer lexer = new clnLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
clnParser parser = new clnParser(tokens);

// 2. Generate parse tree
ParseTree tree = parser.program();

// 3. Build AST using visitor
ClnASTBuilder astBuilder = new ClnASTBuilder();
ProgramNode ast = (ProgramNode) astBuilder.visit(tree);

// 4. Work with the AST
System.out.println(ast); // Uses toString()
```

### Creating a Custom Visitor

Example: [ASTPrinterVisitor](src/main/java/org/clnlang/ast/ASTPrinterVisitor.java)

```java
public class MyCustomVisitor implements ASTVisitor {
    @Override
    public void visit(FunctionDeclNode node) {
        System.out.println("Found function: " + node.getName());
        // Process parameters
        for (var param : node.getParameters()) {
            System.out.println("  Param: " + param.getType() + " " + param.getName());
        // Access function block
        if (node.getBlock() != null) {
            System.out.println("  Block with " + node.getBlock().getStatements().size() + " statements");
        }
    }
    
    @Override
    public void visit(StructDeclNode node) {
        System.out.println("Found struct: " + node.getName());
        // Process fields
        for (var field : node.getFields()) {
            System.out.println("  Field: " + field.getType() + " " + field.getName());
        }
    }
    
    @Override
    public void visit(UnionDeclNode node) {
        System.out.println("Found union: " + node.getName());
        // Process members
        for (String member : node.getMembers()) {
            System.out.println("  Member: " + member
            System.out.println("  Field: " + field.getType() + " " + field.getName());
        }
    }
    
    // Implement other visit methods...
}

// Use your visitor
MyCustomVisitor visitor = new MyCustomVisitor();
ast.accept(visitor);
```

### Traversing the AST

```java
ProgramNode program = (ProgramNode) astBuilder.visit(tree);

// Access package
if (program.getPackageDecl() != null) {
    String pkgName = program.getPackageDecl().getPackageName();
}

// P    BlockNode block = func.getBlock();
    } else if (decl instanceof StructDeclNode) {
        StructDeclNode struct = (StructDeclNode) decl;
        String name = struct.getName();
        List<FieldDecl> fields = struct.getFields();
    } else if (decl instanceof UnionDeclNode) {
        UnionDeclNode union = (UnionDeclNode) decl;
        String name = union.getName();
        List<String> members = union.getMember
}

// Process declarations
for (ASTNode decl : program.getDeclarations()) {
    if (decl instanceof FunctionDeclNode) {
        FunctionDeclNode func = (FunctionDeclNode) decl;
        String name = func.getName();
        List<Parameter> params = func.getParameters();
        List<ReturnVar> returns = func.getReturnVars();
    } else if (decl instanceof StructDeclNode) {
        StructDeclNode struct = (StructDeclNode) decl;
        String name = struct.getName();
        List<FieldDecl> fields = struct.getFields();
    }
}Union definitions with member types  
✅ Function declarations with:
  - Named return values
  - Typed parameters
  - Function body blocks
  - exposure modifier (`expose` keyword)
✅ Type parsing (primitives + qualified names + arrays)  
✅ Block representation (function bodie
```bash
# Compile
mvn clean compile

# Run on a Clean source file
mvn exec:java -Dexec.mainClass="org.clnlang.Main" -Dexec.args="path/to/program.cln"
```

## Current Features

✅ Package declarations  
✅ Import statements (with wildcard support)  
✅ Struct definitions with fields  
✅ Function declarations with:
  - Named return values
  - Typed parameters
  - exposure modifier (`expose` keyword)
✅ Type parsing (primitives + qualified names + arrays)  

## Extending the Implementation

To add support for additional language features:

1. **Add AST Node Class** in `ast/` package
2. **Add visit method** to `ASTVisitor` interface
3. **Implement in `ClnASTBuilder`** to convert parse tree to AST
4. **Update visitors** like `ASTPrinterVisitor` to handle new nodes

### Example: Adding Union Support

```java
// 1. Create UnionDeclNode.java
public class UnionDeclNode extends ASTNode {
    private String name;
    private List<String> members;
    // ... implementation
}

// 2. Add to ASTVisitor interface
void visit(UnionDeclNode node);

// 3. Add to ClnASTBuilder
@Override
public ASTNode visitUnionDecl(clnParser.UnionDeclContext ctx) {
    String name = ctx.ID().getText();
    UnionDeclNode union = new UnionDeclNode(name);
    for (clnParser.UnionMemberContext member : ctx.unionMember()) {
        union.addMember(getQualifiedName(member.qualifiedName()));
    }
    return union;
}

// 4. Implement in your visitors
@Override
public void visit(UnionDeclNode node) {
    System.out.println("Union: " + node.getName());
}
```

## Next Steps

The AST can be used for:
- **Semantic Analysis** - Type checking, scope resolution
- **Symbol Table** - Track variables, functions, types
- **Code Generation** - Generate bytecode, IR, or target language
- **Optimization** - AST transformations
- **IDE Features** - Auto-complete, refactoring, navigation
