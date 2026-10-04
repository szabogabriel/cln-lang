package org.clnlang.compreg.compiler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.CurrentExecution;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.ArrayIndexOffsets;
import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.commands.Jump;
import org.clnlang.compreg.commands.JumpIfFalse;
import org.clnlang.compreg.commands.Label;
import org.clnlang.compreg.commands.ReturnCommand;
import org.clnlang.compreg.commands.array.ArrayStoreBool;
import org.clnlang.compreg.commands.array.ArrayStoreDec;
import org.clnlang.compreg.commands.array.ArrayStoreInt;
import org.clnlang.compreg.commands.array.ArrayStoreString;
import org.clnlang.compreg.commands.global.load.GlobalLoadBool;
import org.clnlang.compreg.commands.global.load.GlobalLoadDec;
import org.clnlang.compreg.commands.global.load.GlobalLoadInt;
import org.clnlang.compreg.commands.global.load.GlobalLoadString;
import org.clnlang.compreg.commands.global.store.GlobalStoreBool;
import org.clnlang.compreg.commands.global.store.GlobalStoreDec;
import org.clnlang.compreg.commands.global.store.GlobalStoreInt;
import org.clnlang.compreg.commands.global.store.GlobalStoreString;
import org.clnlang.compreg.commands.increment.IncrementDec;
import org.clnlang.compreg.commands.increment.IncrementInt;
import org.clnlang.compreg.commands.move.MoveBool;
import org.clnlang.compreg.commands.move.MoveDec;
import org.clnlang.compreg.commands.move.MoveInt;
import org.clnlang.compreg.commands.move.MoveString;
import org.clnlang.compreg.lib.JavaLibrary;
import org.clnlang.compreg.lib.LibraryRegistry;
import org.clnlang.compreg.lib.LibraryRegistry.Constant;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.compreg.runtime.RegisterBank;
import org.clnlang.compreg.runtime.StructValue;
import org.clnlang.parser.clnBaseVisitor;
import org.clnlang.parser.clnParser;
import org.clnlang.parser.clnParser.DeclContext;
import org.clnlang.parser.clnParser.FunctionDeclContext;
import org.clnlang.parser.clnParser.GlobalVarDeclContext;
import org.clnlang.parser.clnParser.ImportDeclContext;

public class RegisterCompiler extends clnBaseVisitor<Object> {

    private Memory memory;
    private CurrentExecution currentExecution;
    private CompileContext compileContext;
    private final Set<String> definedTypes = new HashSet<>();
    private final Set<String> externalTypes = new HashSet<>();
    private final Map<String, StructLayout> structLayouts = new HashMap<>();
    private final Map<String, List<StructValue.FieldLayout>> externalStructLayouts = new HashMap<>();
    private final Map<String, StructValue> globalStructValues = new HashMap<>();
    private boolean compilingGlobalInitializer;
    private Map<String, LocalVariable> localVariables;
    private String packageName = "";
    private List<ImportInfo> imports = new ArrayList<>();
    private final Map<String, CompiledFunction> functionsByQualifiedName = new HashMap<>();
    private final Map<CompiledFunction, FunctionDeclContext> functionDeclarations = new IdentityHashMap<>();
    private final List<CompiledFunction> externalFunctions = new ArrayList<>();
    private final List<Constant> externalConstants = new ArrayList<>();
    private RegisterExpressionCompiler expressionCompiler;

    private static final class ImportInfo {
        private final String packageName;
        private final String functionName;

        private ImportInfo(String packageName, String functionName) {
            this.packageName = packageName;
            this.functionName = functionName;
        }

        private boolean imports(CompiledFunction function) {
            return packageName.equals(function.getPackageName())
                    && (functionName == null || functionName.equals(function.getName()));
        }
    }

    private static final class LocalVariable {
        private final String type;
        private final int offset;
        private final boolean mutable;
        private final DecimalTypeInfo decimalTypeInfo;
        private final int arrayLength;
        private final int[] arrayDimensions;
        private final StructValue structValue;

        private LocalVariable(String type, int offset, boolean mutable, DecimalTypeInfo decimalTypeInfo) {
            this(type, offset, mutable, decimalTypeInfo, (int[]) null);
        }

        private LocalVariable(String type, int offset, boolean mutable, DecimalTypeInfo decimalTypeInfo,
                int[] arrayDimensions) {
            this(type, offset, mutable, decimalTypeInfo, arrayDimensions, null);
        }

        private LocalVariable(String type, int offset, boolean mutable, DecimalTypeInfo decimalTypeInfo,
                StructValue structValue) {
            this(type, offset, mutable, decimalTypeInfo, null, structValue);
        }

        private LocalVariable(String type, int offset, boolean mutable, DecimalTypeInfo decimalTypeInfo,
                int[] arrayDimensions, StructValue structValue) {
            this.type = type;
            this.offset = offset;
            this.mutable = mutable;
            this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
            this.arrayDimensions = arrayDimensions == null ? null : arrayDimensions.clone();
            this.arrayLength = arrayDimensions == null ? -1 : dimensionsElementCount(arrayDimensions);
            this.structValue = structValue;
        }
    }

    private final class ExpressionContext implements RegisterExpressionCompiler.Context {
        @Override
        public int allocate(String type) {
            return RegisterCompiler.this.allocate(type);
        }

        @Override
        public CompiledValue compileIdentifier(String name, int line) {
            return RegisterCompiler.this.compileIdentifier(name, line);
        }

        @Override
        public CompiledValue compileStructLiteral(clnParser.StructLiteralContext literal, int line) {
            return RegisterCompiler.this.compileStructLiteral(literal, line);
        }

        @Override
        public CompiledValue compileStructMember(String variable, List<String> members, int line) {
            return RegisterCompiler.this.compileStructMember(variable, members, line);
        }

        @Override
        public CompiledValue compileIncrement(String name, boolean increment, boolean prefix, int line) {
            return RegisterCompiler.this.compileIncrement(name, increment, prefix, line);
        }

        @Override
        public CompiledFunction resolveFunction(String name, int line, List<String> argumentTypes) {
            return RegisterCompiler.this.resolveFunction(name, line, argumentTypes);
        }

        @Override
        public boolean hasZeroArgumentFunction(String name) {
            return RegisterCompiler.this.hasZeroArgumentFunction(name);
        }

        @Override
        public List<Call.Register> allocateCallResults(CompiledFunction function) {
            return RegisterCompiler.this.allocateCallResults(function);
        }

        @Override
        public IllegalArgumentException unsupported(int line, String feature) {
            return RegisterCompiler.this.unsupported(line, feature);
        }
    }

    public void addExternalTypes(java.util.Collection<String> typeNames) {
        externalTypes.addAll(typeNames);
    }

    public void addExternalFunctions(java.util.Collection<CompiledFunction> functions) {
        externalFunctions.addAll(functions);
    }

    public void addLibrary(JavaLibrary library) {
        if (library == null) {
            throw new IllegalArgumentException("Library must not be null.");
        }
        LibraryRegistry registry = new LibraryRegistry();
        library.register(registry);
        addExternalFunctions(registry.getFunctions());
        externalConstants.addAll(registry.getConstants());
        for (Map.Entry<String, List<StructValue.FieldLayout>> entry : registry.getStructLayouts().entrySet()) {
            externalStructLayouts.put(entry.getKey(), entry.getValue());
            externalTypes.add(entry.getKey());
        }
    }

    public CurrentExecution compileProgram(clnParser.ProgramContext ctx) {
        currentExecution = new CurrentExecution();
        memory = new Memory();
        compileContext = new CompileContext();
        expressionCompiler = new RegisterExpressionCompiler(new ExpressionContext());
        installLibraryConstants();
        packageName = "";
        imports = new ArrayList<>();
        functionsByQualifiedName.clear();
        functionDeclarations.clear();
        structLayouts.clear();
        globalStructValues.clear();

        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.packageDecl() != null) {
                packageName = topLevel.packageDecl().qualifiedName().getText();
                break;
            }
        }

        currentExecution.setCurrentPackage(packageName);

        definedTypes.clear();
        definedTypes.addAll(externalTypes);
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.decl() != null) {
                DeclContext decl = topLevel.decl();
                if (decl.structDecl() != null) {
                    definedTypes.add(decl.structDecl().ID().getText());
                } else if (decl.unionDecl() != null) {
                    definedTypes.add(decl.unionDecl().ID().getText());
                }
            }
        }

        for (Map.Entry<String, List<StructValue.FieldLayout>> entry : externalStructLayouts.entrySet()) {
            StructLayout layout = new StructLayout();
            for (StructValue.FieldLayout field : entry.getValue()) {
                layout.fields.put(field.getName(), new StructLayout.Field(field.getName(), field.getType(),
                        field.isMutable(), field.getDecimalTypeInfo()));
            }
            structLayouts.put(entry.getKey(), layout);
        }

        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.decl() != null && topLevel.decl().structDecl() != null) {
                registerStructLayout(topLevel.decl().structDecl());
            }
        }

        List<DeclContext> declarations = new ArrayList<>();
        for (clnParser.TopLevelDeclContext topLevel : ctx.topLevelDecl()) {
            if (topLevel.importDecl() != null) {
                imports.add(parseImport(topLevel.importDecl()));
            } else if (topLevel.decl() != null) {
                declarations.add(topLevel.decl());
            }
        }

        for (DeclContext declaration : declarations) {
            if (declaration.functionDecl() != null) {
                CompiledFunction function = createFunctionSignature(declaration.functionDecl(), declaration.EXPOSE() != null);
                functionsByQualifiedName.put(functionKey(function.getPackageName(), function.getName()), function);
                functionDeclarations.put(function, declaration.functionDecl());
            }
        }

        for (DeclContext declaration : declarations) {
            if (declaration.globalVarDecl() != null) {
                currentExecution.addGlobalInitializer(
                        compileGlobalVarDecl(declaration.globalVarDecl(), declaration.EXPOSE() != null));
            }
        }

        currentExecution.setGlobalFrameLayout(memory.currentFrameLayout());
        for (Map.Entry<CompiledFunction, FunctionDeclContext> functionEntry : functionDeclarations.entrySet()) {
            memory.pushOffset();
            try {
                compileFunctionBody(functionEntry.getKey(), functionEntry.getValue());
            } finally {
                memory.popOffset();
            }
            currentExecution.addFunction(functionEntry.getKey());
        }
        return currentExecution;
    }

    private ImportInfo parseImport(ImportDeclContext importDecl) {
        String importName = importDecl.qualifiedName().getText();
        if (importDecl.STAR() != null) {
            return new ImportInfo(importName, null);
        }
        int separator = importName.lastIndexOf('.');
        return separator < 0
                ? new ImportInfo("", importName)
                : new ImportInfo(importName.substring(0, separator), importName.substring(separator + 1));
    }

    private String functionKey(String functionPackage, String functionName) {
        return functionPackage + "::" + functionName;
    }

    public CompileContext.Offsets getGlobalOffsets() {
        return compileContext.getGlobalOffsets();
    }

    private void installLibraryConstants() {
        CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
        for (Constant constant : externalConstants) {
            String name = constant.getName();
            if (offsets.getType(name) != null) {
                throw new IllegalArgumentException("Duplicate library global constant '" + name + "'.");
            }
            int offset = allocate(constant.getType());
            registerGlobalOffset(offsets, name, constant.getType(), offset);
            offsets.registerType(name, constant.getType());
            offsets.registerGlobalFlags(name, false, true);
            offsets.registerDecimalTypeInfo(name, DecimalTypeInfo.DEFAULT);
            Object value = constant.getValue();
            currentExecution.addGlobalInitializer(runtimeMemory -> {
                switch (constant.getType()) {
                    case "int" -> runtimeMemory.setIntAbsolute(offset, (Long) value);
                    case "dec" -> runtimeMemory.setDecAbsolute(offset, (java.math.BigDecimal) value);
                    case "bool" -> runtimeMemory.setBoolAbsolute(offset, (Boolean) value);
                    case "string" -> runtimeMemory.setStrAbsolute(offset, (String) value);
                    default -> throw new IllegalStateException("Unsupported library constant type.");
                }
            });
        }
    }

    private CompiledFunction createFunctionSignature(FunctionDeclContext functionDecl, boolean isExposed) {
        List<CompiledFunction.Slot> parameters = new ArrayList<>();
        if (functionDecl.paramList() != null) {
            for (clnParser.ParamContext parameter : functionDecl.paramList().param()) {
                String type = normalizeType(parameter.type().getText());
                validateRegisterType(type, parameter.type().getStart().getLine());
                parameters.add(new CompiledFunction.Slot(parameter.ID().getText(), type, false,
                        extractDecimalTypeInfo(parameter.type())));
            }
        }

        List<CompiledFunction.Slot> results = new ArrayList<>();
        if (functionDecl.returnType() != null) {
            clnParser.ReturnTypeContext returnType = functionDecl.returnType();
            if (returnType.type() != null) {
                String type = normalizeType(returnType.type().getText());
                validateRegisterType(type, returnType.type().getStart().getLine());
                results.add(new CompiledFunction.Slot(null, type, false, extractDecimalTypeInfo(returnType.type())));
            } else {
                for (clnParser.ReturnVarContext returnVar : returnType.namedReturnSig().returnVar()) {
                    String type = normalizeType(returnVar.type().getText());
                    validateRegisterType(type, returnVar.type().getStart().getLine());
                    results.add(new CompiledFunction.Slot(returnVar.ID().getText(), type, returnVar.VAR() != null,
                            extractDecimalTypeInfo(returnVar.type())));
                }
            }
        }
        return new CompiledFunction(packageName, functionDecl.ID().getText(), isExposed, parameters, results);
    }

    private void compileFunctionBody(CompiledFunction function, FunctionDeclContext functionDecl) {
        Map<String, LocalVariable> previousLocals = localVariables;
        localVariables = new HashMap<>();
        try {
            List<Command> initializers = new ArrayList<>();
            List<CompiledFunction.Slot> parameters = function.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                CompiledFunction.Slot parameter = parameters.get(i);
                int offset = allocate(parameter.getType());
                function.setParameterOffset(i, offset);
                localVariables.put(parameter.getName(), new LocalVariable(parameter.getType(), offset, false,
                        parameter.getDecimalTypeInfo()));
            }

            List<CompiledFunction.Slot> results = function.getReturnValues();
            for (int i = 0; i < results.size(); i++) {
                CompiledFunction.Slot result = results.get(i);
                int offset = allocate(result.getType());
                function.setReturnOffset(i, offset);
                if (result.getName() != null) {
                    localVariables.put(result.getName(), new LocalVariable(result.getType(), offset,
                            result.isMutable(), result.getDecimalTypeInfo()));
                }
            }

            if (functionDecl.returnType() != null && functionDecl.returnType().namedReturnSig() != null) {
                List<clnParser.ReturnVarContext> returnVars = functionDecl.returnType().namedReturnSig().returnVar();
                for (int i = 0; i < returnVars.size(); i++) {
                    clnParser.ReturnVarContext returnVar = returnVars.get(i);
                    CompiledFunction.Slot result = results.get(i);
                    CompiledValue value = compileExpression(returnVar.expr());
                    requireSameType(value.type, result.getType(), returnVar.expr().getStart().getLine(),
                            "initializer for named return '" + result.getName() + "'");
                    initializers.addAll(value.commands);
                    initializers.add(move(result.getType(), value.offset, result.getOffset(),
                            result.getDecimalTypeInfo()));
                }
            }

            List<Command> body = new ArrayList<>();
            for (clnParser.StmtContext statement : functionDecl.block().stmt()) {
                body.addAll(compileFunctionStatement(statement, function));
            }
                function.setImplementation(memory.currentFrameLayout(), CommandSequenceCompiler.compile(initializers),
                    CommandSequenceCompiler.compile(body));
        } finally {
            localVariables = previousLocals;
        }
    }

    private List<Command> compileFunctionStatement(clnParser.StmtContext statement, CompiledFunction function) {
        if (statement.SEMI() != null) {
            return List.of();
        }
        if (statement.block() != null) {
            return List.of(compileScopedBlock(statement.block(), function));
        }
        if (statement.varDeclStmt() != null) {
            return List.of(compileLocalDeclaration(statement.varDeclStmt().varBinding()));
        }
        if (statement.assignStmt() != null) {
            return List.of(compileAssignment(statement.assignStmt()));
        }
        if (statement.tupleAssignStmt() != null) {
            return List.of(compileTupleAssignment(statement.tupleAssignStmt()));
        }
        if (statement.returnStmt() != null) {
            return compileReturn(statement.returnStmt(), function);
        }
        if (statement.ifStmt() != null) {
            return List.of(compileIf(statement.ifStmt(), function));
        }
        if (statement.whileStmt() != null) {
            return List.of(compileWhile(statement.whileStmt(), function));
        }
        if (statement.exprStmt() != null) {
            CompiledValue value = compileExpression(statement.exprStmt().expr());
            return value.commands;
        }
        throw unsupported(statement.getStart().getLine(), "function statement");
    }

    private Command compileScopedBlock(clnParser.BlockContext block, CompiledFunction function) {
        Map<String, LocalVariable> previousLocals = localVariables;
        localVariables = new HashMap<>(previousLocals);
        try {
            List<Command> commands = new ArrayList<>();
            for (clnParser.StmtContext statement : block.stmt()) {
                commands.addAll(compileFunctionStatement(statement, function));
            }
            return CommandSequenceCompiler.compile(commands);
        } finally {
            localVariables = previousLocals;
        }
    }

    private Command compileIf(clnParser.IfStmtContext ifStmt, CompiledFunction function) {
        CompiledValue condition = compileExpression(ifStmt.expr());
        requireSameType(condition.type, "bool", ifStmt.expr().getStart().getLine(), "if condition");

        Label elseLabel = new Label();
        Label endLabel = new Label();
        List<Command> commands = new ArrayList<>(condition.commands);
        commands.add(new JumpIfFalse(condition.offset, elseLabel));
        commands.add(compileScopedBlock(ifStmt.block(0), function));
        if (ifStmt.ELSE() != null) {
            commands.add(new Jump(endLabel));
            commands.add(elseLabel);
            commands.add(compileScopedBlock(ifStmt.block(1), function));
        } else {
            commands.add(elseLabel);
        }
        commands.add(endLabel);
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileWhile(clnParser.WhileStmtContext whileStmt, CompiledFunction function) {
        Label startLabel = new Label();
        Label endLabel = new Label();
        List<Command> commands = new ArrayList<>();
        commands.add(startLabel);

        CompiledValue condition = compileExpression(whileStmt.expr());
        requireSameType(condition.type, "bool", whileStmt.expr().getStart().getLine(), "while condition");
        commands.addAll(condition.commands);
        commands.add(new JumpIfFalse(condition.offset, endLabel));
        commands.add(compileScopedBlock(whileStmt.block(), function));
        commands.add(new Jump(startLabel));
        commands.add(endLabel);
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileLocalDeclaration(clnParser.VarBindingContext binding) {
        String name = binding.ID().getText();
        String type = normalizeType(binding.type().getText());
        if (isStructType(type)) {
            // Struct fields are validated when their declaration is registered.
        } else if (isArrayType(type)) {
            validateArrayType(type, binding.type().getStart().getLine());
        } else {
            validateRegisterType(type, binding.type().getStart().getLine());
        }
        if (localVariables.containsKey(name)) {
            throw new IllegalArgumentException("line " + binding.ID().getSymbol().getLine()
                    + ": Duplicate local variable '" + name + "'.");
        }

        CompiledValue value = compileExpression(binding.expr());
        requireSameType(value.type, type, binding.expr().getStart().getLine(), "initializer for local '" + name + "'");
        DecimalTypeInfo decimalTypeInfo = extractDecimalTypeInfo(binding.type());
        if (isStructType(type)) {
            if (value.structValue == null) {
                throw unsupported(binding.expr().getStart().getLine(), "struct-variable initialization");
            }
            localVariables.put(name, new LocalVariable(type, -1, binding.VAR() != null,
                    decimalTypeInfo, value.structValue));
            return CommandSequenceCompiler.compile(value.commands);
        }
        if (isArrayType(type)) {
            if (!value.arrayLiteral) {
                throw unsupported(binding.expr().getStart().getLine(), "array-variable initialization");
            }
            localVariables.put(name, new LocalVariable(type, value.offset, binding.VAR() != null,
                    decimalTypeInfo, value.arrayDimensions));
            return CommandSequenceCompiler.compile(value.commands);
        }
        int target = allocate(type);
        localVariables.put(name, new LocalVariable(type, target, binding.VAR() != null, decimalTypeInfo));
        List<Command> commands = new ArrayList<>(value.commands);
        commands.add(move(type, value.offset, target, decimalTypeInfo));
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileAssignment(clnParser.AssignStmtContext assignment) {
        clnParser.LvalueContext lvalue = assignment.lvalue();
        if (!lvalue.lvalueSuffix().isEmpty()
                && lvalue.lvalueSuffix().stream().allMatch(suffix -> suffix.DOT() != null)) {
            if (lvalue.lvalueSuffix().size() != 1) {
                throw unsupported(lvalue.getStart().getLine(), "nested struct field assignment");
            }
            return compileStructFieldAssignment(lvalue.ID().getText(),
                    lvalue.lvalueSuffix(0).ID().getText(), assignment.expr(), lvalue.getStart().getLine());
        }
        if (!lvalue.lvalueSuffix().isEmpty()
            && lvalue.lvalueSuffix().stream().allMatch(suffix -> suffix.LBRACK() != null)) {
            List<clnParser.ExprContext> indices = lvalue.lvalueSuffix().stream()
                .map(clnParser.LvalueSuffixContext::expr).toList();
            return compileArrayAssignment(lvalue.ID().getText(), indices, assignment.expr(),
                lvalue.getStart().getLine());
        }
        if (!lvalue.lvalueSuffix().isEmpty()) {
            throw unsupported(lvalue.getStart().getLine(), "member and indexed assignments");
        }
        String name = lvalue.ID().getText();
        LocalVariable local = localVariables.get(name);
        String type;
        int target;
        DecimalTypeInfo decimalTypeInfo;
        boolean global = false;
        if (local != null) {
            if (!local.mutable) {
                throw new IllegalArgumentException("line " + lvalue.getStart().getLine()
                        + ": Cannot assign to immutable variable '" + name + "'.");
            }
            type = local.type;
            target = local.offset;
            decimalTypeInfo = local.decimalTypeInfo;
        } else {
            global = true;
            CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
            type = offsets.getType(name);
            if (type == null) {
                throw new IllegalArgumentException("line " + lvalue.getStart().getLine()
                        + ": Unknown variable '" + name + "'.");
            }
            if (!offsets.isMutable(name)) {
                throw new IllegalArgumentException("line " + lvalue.getStart().getLine()
                        + ": Cannot assign to immutable global '" + name + "'.");
            }
            target = globalOffset(offsets, name, type);
            decimalTypeInfo = offsets.getDecimalTypeInfo(name);
        }

        CompiledValue value = compileExpression(assignment.expr());
        requireSameType(value.type, type, assignment.expr().getStart().getLine(), "assignment to '" + name + "'");
        List<Command> commands = new ArrayList<>(value.commands);
        commands.add(global ? globalStore(type, value.offset, target, decimalTypeInfo)
            : move(type, value.offset, target, decimalTypeInfo));
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileStructFieldAssignment(String variableName, String fieldName,
            clnParser.ExprContext valueContext, int line) {
        StructValue structValue = resolveStructValue(variableName, line);
        StructValue.FieldSlot field = structValue.getField(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("line " + line + ": Struct " + structValue.getTypeName()
                    + " has no field '" + fieldName + "'.");
        }
        if (!field.isMutable()) {
            throw new IllegalArgumentException("line " + line + ": Cannot assign to immutable field '"
                    + fieldName + "'.");
        }

        CompiledValue value = compileExpression(valueContext);
        requireSameType(value.type, field.getType(), valueContext.getStart().getLine(),
                "assignment to field '" + fieldName + "'");
        List<Command> commands = new ArrayList<>(value.commands);
        commands.add(field.isGlobal()
            ? globalStore(field.getType(), value.offset, field.getOffset(), field.getDecimalTypeInfo())
            : move(field.getType(), value.offset, field.getOffset(), field.getDecimalTypeInfo()));
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileArrayAssignment(String name, List<clnParser.ExprContext> indexContexts,
            clnParser.ExprContext valueContext, int line) {
        LocalVariable local = localVariables.get(name);
        String arrayType;
        int baseOffset;
        int length;
        int[] dimensions;
        boolean global;
        DecimalTypeInfo decimalTypeInfo;
        if (local != null) {
            arrayType = local.type;
            baseOffset = local.offset;
            length = local.arrayLength;
            dimensions = local.arrayDimensions;
            global = false;
            decimalTypeInfo = local.decimalTypeInfo;
            if (!local.mutable) {
                throw new IllegalArgumentException("line " + line
                        + ": Cannot assign to immutable variable '" + name + "'.");
            }
        } else {
            CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
            arrayType = offsets.getType(name);
            if (arrayType == null) {
                throw new IllegalArgumentException("line " + line + ": Unknown variable '" + name + "'.");
            }
            if (!isArrayType(arrayType)) {
                throw unsupported(line, "indexed assignment to non-array variable '" + name + "'");
            }
            if (!offsets.isMutable(name)) {
                throw new IllegalArgumentException("line " + line
                        + ": Cannot assign to immutable global '" + name + "'.");
            }
            baseOffset = globalOffset(offsets, name, arrayBaseType(arrayType));
            length = offsets.getArrayLength(name);
            dimensions = offsets.getArrayDimensions(name);
            global = true;
            decimalTypeInfo = offsets.getDecimalTypeInfo(name);
        }
        if (length < 0 || dimensions == null || !isArrayType(arrayType)) {
            throw unsupported(line, "indexed assignment to non-array variable '" + name + "'");
        }
        if (indexContexts.size() != dimensions.length) {
            throw unsupported(line, "partial or excessive multidimensional array indexing");
        }

        List<Command> commands = new ArrayList<>();
        int[] indexOffsets = new int[indexContexts.size()];
        for (int i = 0; i < indexContexts.size(); i++) {
            clnParser.ExprContext indexContext = indexContexts.get(i);
            CompiledValue index = compileExpression(indexContext);
            requireSameType(index.type, "int", indexContext.getStart().getLine(), "array index");
            commands.addAll(index.commands);
            indexOffsets[i] = index.offset;
        }
        int linearIndexOffset = allocate("int");
        commands.add(new ArrayIndexOffsets(indexOffsets, dimensions, linearIndexOffset));
        CompiledValue value = compileExpression(valueContext);
        String elementType = arrayBaseType(arrayType);
        requireSameType(value.type, elementType, valueContext.getStart().getLine(), "array element assignment");
        commands.addAll(value.commands);
        commands.add(arrayStore(elementType, value.offset, baseOffset, length, linearIndexOffset,
            global, decimalTypeInfo));
        return CommandSequenceCompiler.compile(commands);
    }

    private Command compileTupleAssignment(clnParser.TupleAssignStmtContext assignment) {
        CompiledValue value = compileExpression(assignment.expr());
        List<Call.Register> tupleValues = value.tupleValues;
        if (tupleValues == null && value.type != null) {
            tupleValues = List.of(new Call.Register(RegisterBank.forType(value.type), value.offset));
        }
        if (tupleValues == null || tupleValues.size() != assignment.tupleBind().size()) {
            throw new IllegalArgumentException("line " + assignment.getStart().getLine()
                    + ": Tuple assignment requires a call returning the same number of values.");
        }
        List<Command> commands = new ArrayList<>(value.commands);
        for (int i = 0; i < assignment.tupleBind().size(); i++) {
            clnParser.TupleBindContext binding = assignment.tupleBind(i);
            String name = binding.ID().getText();
            String type = normalizeType(binding.type().getText());
            validateRegisterType(type, binding.type().getStart().getLine());
            Call.Register result = tupleValues.get(i);
            requireSameType(result.getTypeName(), type, binding.type().getStart().getLine(), "tuple result '" + name + "'");
            if (localVariables.containsKey(name)) {
                throw new IllegalArgumentException("line " + binding.ID().getSymbol().getLine()
                        + ": Duplicate local variable '" + name + "'.");
            }
            DecimalTypeInfo decimalTypeInfo = extractDecimalTypeInfo(binding.type());
            localVariables.put(name, new LocalVariable(type, result.getOffset(), binding.VAR() != null,
                    decimalTypeInfo));
        }
        return CommandSequenceCompiler.compile(commands);
    }

    private List<Command> compileReturn(clnParser.ReturnStmtContext statement, CompiledFunction function) {
        List<CompiledFunction.Slot> results = function.getReturnValues();
        List<Command> commands = new ArrayList<>();
        if (statement.exprList() != null) {
            if (statement.exprList().expr().size() != results.size()) {
                throw unsupported(statement.getStart().getLine(), "return value count mismatch");
            }
            for (int i = 0; i < results.size(); i++) {
                CompiledValue value = compileExpression(statement.exprList().expr(i));
                CompiledFunction.Slot result = results.get(i);
                requireSameType(value.type, result.getType(), statement.getStart().getLine(), "return value");
                commands.addAll(value.commands);
                commands.add(move(result.getType(), value.offset, result.getOffset(), result.getDecimalTypeInfo()));
            }
        } else if (statement.expr() != null) {
            if (results.size() != 1) {
                throw unsupported(statement.getStart().getLine(), "single return expression for multi-value function");
            }
            CompiledValue value = compileExpression(statement.expr());
            CompiledFunction.Slot result = results.get(0);
            requireSameType(value.type, result.getType(), statement.getStart().getLine(), "return value");
            commands.addAll(value.commands);
            commands.add(move(result.getType(), value.offset, result.getOffset(), result.getDecimalTypeInfo()));
        } else if (results.size() == 1 && results.get(0).getName() == null) {
            throw unsupported(statement.getStart().getLine(), "empty return from a function with an unnamed result");
        }
        commands.add(ReturnCommand.INSTANCE);
        return commands;
    }

    private Command compileGlobalVarDecl(GlobalVarDeclContext globalVarDecl, boolean isExposed) {
        clnParser.VarBindingContext binding = globalVarDecl.varBinding();
        if (binding == null || binding.ID() == null || binding.type() == null || binding.expr() == null) {
            return null;
        }

        String type = normalizeType(binding.type().getText());
        String name = binding.ID().getText();
        boolean isMutable = binding.VAR() != null;
        validateType(type, binding.type().getStart().getLine());
        DecimalTypeInfo decimalTypeInfo = extractDecimalTypeInfo(binding.type());

        boolean previousGlobalInitializer = compilingGlobalInitializer;
        compilingGlobalInitializer = true;
        CompiledValue value;
        try {
            value = compileExpression(binding.expr());
        } finally {
            compilingGlobalInitializer = previousGlobalInitializer;
        }
        if (!type.equals(value.type)) {
            throw new IllegalArgumentException("line " + binding.expr().getStart().getLine()
                    + ": Cannot initialize '" + type + "' global '" + name + "' with '" + value.type + "'.");
        }

        CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
        if (isStructType(type)) {
            if (value.structValue == null) {
                throw unsupported(binding.expr().getStart().getLine(), "struct-variable initialization");
            }
            globalStructValues.put(name, value.structValue);
        } else if (isArrayType(type)) {
            validateArrayType(type, binding.type().getStart().getLine());
            if (!value.arrayLiteral) {
                throw unsupported(binding.expr().getStart().getLine(), "array-variable initialization");
            }
            registerGlobalOffset(offsets, name, arrayBaseType(type), value.offset);
            offsets.registerArrayDimensions(name, value.arrayDimensions);
        } else {
            int target = allocate(type);
            registerGlobalOffset(offsets, name, type, target);
        }
        offsets.registerType(name, type);
        offsets.registerGlobalFlags(name, isMutable, isExposed);
        offsets.registerDecimalTypeInfo(name, decimalTypeInfo);

        List<Command> commands = new ArrayList<>(value.commands);
        if (!isArrayType(type) && !isStructType(type)) {
            int target = globalOffset(offsets, name, type);
            commands.add(move(type, value.offset, target, decimalTypeInfo));
        }
        return CommandSequenceCompiler.compile(commands);
    }

    private void registerGlobalOffset(CompileContext.Offsets offsets, String name, String type, int target) {
        switch (type) {
            case "int" -> offsets.registerIntOffset(name, target);
            case "dec" -> offsets.registerDecOffset(name, target);
            case "bool" -> offsets.registerBooleanOffset(name, target);
            case "string" -> offsets.registerStringOffset(name, target);
            default -> throw unsupported(0, "global type '" + type + "'");
        }
    }

    private CompiledValue compileExpression(clnParser.ExprContext ctx) {
        return expressionCompiler.compile(ctx);
    }

    private CompiledFunction resolveFunction(String name, int line, List<String> argumentTypes) {
        CompiledFunction samePackage = functionsByQualifiedName.get(functionKey(packageName, name));
        if (samePackage != null) {
            if (!matchesSignature(samePackage, argumentTypes)) {
                throw signatureMismatch(name, line, argumentTypes);
            }
            return samePackage;
        }

        CompiledFunction found = null;
        for (CompiledFunction candidate : allKnownFunctions()) {
            if (!candidate.getName().equals(name) || !isImported(candidate)) {
                continue;
            }
            if (!candidate.isExposed()) {
                throw new IllegalArgumentException("line " + line + ": Function '" + name
                        + "' in package '" + candidate.getPackageName() + "' is not exposed.");
            }
            if (!matchesSignature(candidate, argumentTypes)) {
                continue;
            }
            if (found != null && found != candidate) {
                throw new IllegalArgumentException("line " + line + ": Ambiguous imported function '" + name + "'.");
            }
            found = candidate;
        }
        if (found == null) {
            throw signatureMismatch(name, line, argumentTypes);
        }
        return found;
    }

    private boolean hasZeroArgumentFunction(String name) {
        CompiledFunction samePackage = functionsByQualifiedName.get(functionKey(packageName, name));
        if (samePackage != null) {
            return samePackage.getParameters().isEmpty();
        }
        return allKnownFunctions().stream().anyMatch(candidate -> candidate.getName().equals(name)
                && candidate.getParameters().isEmpty() && isImported(candidate));
    }

    private boolean matchesSignature(CompiledFunction function, List<String> argumentTypes) {
        List<CompiledFunction.Slot> parameters = function.getParameters();
        if (parameters.size() != argumentTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.get(i).getType().equals(argumentTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private IllegalArgumentException signatureMismatch(String name, int line, List<String> argumentTypes) {
        if (functionsByQualifiedName.containsKey(functionKey(packageName, name))) {
            CompiledFunction function = functionsByQualifiedName.get(functionKey(packageName, name));
            return new IllegalArgumentException("line " + line + ": Function '" + name + "' expects "
                    + function.getParameters().size() + " arguments of types "
                    + function.getParameters().stream().map(CompiledFunction.Slot::getType).toList()
                    + ", got " + argumentTypes + ".");
        }
        return new IllegalArgumentException("line " + line + ": Unknown or unimported function/signature '"
                + name + "' with argument types " + argumentTypes + ".");
    }

    private List<CompiledFunction> allKnownFunctions() {
        List<CompiledFunction> known = new ArrayList<>(externalFunctions);
        known.addAll(functionsByQualifiedName.values());
        return known;
    }

    private boolean isImported(CompiledFunction function) {
        for (ImportInfo importInfo : imports) {
            if (importInfo.imports(function)) {
                return true;
            }
        }
        return false;
    }

    private CompiledValue compileIdentifier(String name, int line) {
        if (localVariables != null && localVariables.containsKey(name)) {
            LocalVariable local = localVariables.get(name);
            if (local.structValue != null) {
                return new CompiledValue(local.type, -1, List.of(), local.structValue);
            }
            if (local.arrayLength >= 0) {
                return new CompiledValue(local.type, local.offset, List.of(), local.arrayDimensions,
                        false, false);
            }
            int target = allocate(local.type);
            return new CompiledValue(local.type, target, List.of(move(local.type, local.offset, target)));
        }
        CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
        String type = offsets.getType(name);
        if (type == null) {
            throw new IllegalArgumentException("line " + line + ": Unknown global variable '" + name + "'.");
        }
        if (isStructType(type)) {
            StructValue structValue = globalStructValues.get(name);
            if (structValue == null) {
                throw new IllegalArgumentException("line " + line + ": Struct global '" + name
                        + "' has no compiled field layout.");
            }
            return new CompiledValue(type, -1, List.of(), structValue);
        }
        if (isArrayType(type)) {
                return new CompiledValue(type, globalOffset(offsets, name, arrayBaseType(type)), List.of(),
                    offsets.getArrayDimensions(name), true, false);
        }
        int source = globalOffset(offsets, name, type);
        int target = allocate(type);
        return new CompiledValue(type, target, List.of(globalLoad(type, source, target)));
    }

    private CompiledValue compileStructLiteral(clnParser.StructLiteralContext literal, int line) {
        String typeName = literal.qualifiedName().getText();
        StructLayout layout = structLayouts.get(typeName);
        if (layout == null) {
            throw unsupported(line, "struct literal for unavailable layout '" + typeName + "'");
        }

        Map<String, CompiledValue> initializers = new LinkedHashMap<>();
        if (literal.fieldInitList() != null) {
            for (clnParser.FieldInitContext fieldInit : literal.fieldInitList().fieldInit()) {
                String name = fieldInit.ID().getText();
                StructLayout.Field field = layout.fields.get(name);
                if (field == null) {
                    throw new IllegalArgumentException("line " + fieldInit.getStart().getLine()
                            + ": Struct " + typeName + " has no field '" + name + "'.");
                }
                if (initializers.containsKey(name)) {
                    throw new IllegalArgumentException("line " + fieldInit.getStart().getLine()
                            + ": Duplicate initializer for struct field '" + name + "'.");
                }
                CompiledValue value = compileExpression(fieldInit.expr());
                requireSameType(value.type, field.getType(), fieldInit.expr().getStart().getLine(),
                        "initializer for field '" + name + "'");
                initializers.put(name, value);
            }
        }
        for (String fieldName : layout.fields.keySet()) {
            if (!initializers.containsKey(fieldName)) {
                throw new IllegalArgumentException("line " + line + ": Missing initializer for struct field '"
                        + fieldName + "'.");
            }
        }

        List<Command> commands = new ArrayList<>();
        for (CompiledValue value : initializers.values()) {
            commands.addAll(value.commands);
        }
        StructValue value = new StructValue(typeName);
        for (StructLayout.Field field : layout.fields.values()) {
                int offset = allocate(field.getType());
                value.addField(new StructValue.FieldSlot(field.getName(), field.getType(), field.isMutable(), offset,
                    compilingGlobalInitializer, field.getDecimalTypeInfo()));
        }
        for (StructLayout.Field field : layout.fields.values()) {
                CompiledValue source = initializers.get(field.getName());
                commands.add(move(field.getType(), source.offset, value.getField(field.getName()).getOffset(),
                    field.getDecimalTypeInfo()));
        }
        return new CompiledValue(typeName, -1, commands, value);
    }

    private CompiledValue compileStructMember(String variableName, List<String> members, int line) {
        if (members.size() != 1) {
            throw unsupported(line, "nested struct field access");
        }
        StructValue structValue = resolveStructValue(variableName, line);
        String fieldName = members.get(0);
        StructValue.FieldSlot field = structValue.getField(fieldName);
        if (field == null) {
                throw new IllegalArgumentException("line " + line + ": Struct " + structValue.getTypeName()
                    + " has no field '" + fieldName + "'.");
        }
            int target = allocate(field.getType());
        Command load = field.isGlobal()
                ? globalLoad(field.getType(), field.getOffset(), target)
                : move(field.getType(), field.getOffset(), target, field.getDecimalTypeInfo());
            return new CompiledValue(field.getType(), target, List.of(load));
    }

    private StructValue resolveStructValue(String variableName, int line) {
        if (localVariables != null) {
            LocalVariable local = localVariables.get(variableName);
            if (local != null) {
                if (local.structValue == null) {
                    throw unsupported(line, "member access on non-struct variable '" + variableName + "'");
                }
                return local.structValue;
            }
        }
        StructValue global = globalStructValues.get(variableName);
        if (global != null) {
            return global;
        }
        throw new IllegalArgumentException("line " + line + ": Unknown struct variable '" + variableName + "'.");
    }

    private CompiledValue compileIncrement(String name, boolean increment, boolean prefix, int line) {
        LocalVariable local = localVariables == null ? null : localVariables.get(name);
        String type;
        int variableOffset;
        boolean global;
        DecimalTypeInfo decimalTypeInfo;
        if (local != null) {
            if (local.arrayLength >= 0) {
                throw unsupported(line, "increment or decrement of an array");
            }
            if (!local.mutable) {
                throw new IllegalArgumentException("line " + line
                        + ": Cannot assign to immutable variable '" + name + "'.");
            }
            type = local.type;
            variableOffset = local.offset;
            global = false;
            decimalTypeInfo = local.decimalTypeInfo;
        } else {
            CompileContext.Offsets offsets = compileContext.getGlobalOffsets();
            type = offsets.getType(name);
            if (type == null) {
                throw new IllegalArgumentException("line " + line + ": Unknown variable '" + name + "'.");
            }
            if (!offsets.isMutable(name)) {
                throw new IllegalArgumentException("line " + line
                        + ": Cannot assign to immutable global '" + name + "'.");
            }
            if (isArrayType(type)) {
                throw unsupported(line, "increment or decrement of an array");
            }
            variableOffset = globalOffset(offsets, name, type);
            global = true;
            decimalTypeInfo = offsets.getDecimalTypeInfo(name);
        }
        if (!type.equals("int") && !type.equals("dec")) {
            throw unsupported(line, "increment or decrement of '" + type + "'");
        }

        int resultOffset = allocate(type);
        Command command = type.equals("int")
            ? new IncrementInt(variableOffset, resultOffset, global, increment, prefix)
            : new IncrementDec(variableOffset, resultOffset, global, increment, prefix, decimalTypeInfo);
        return new CompiledValue(type, resultOffset, List.of(command));
    }

    private List<Call.Register> allocateCallResults(CompiledFunction function) {
        List<Call.Register> results = new ArrayList<>();
        for (CompiledFunction.Slot result : function.getReturnValues()) {
            if (isStructType(result.getType())) {
                results.add(new Call.Register(allocateStructValue(result.getType(), false)));
            } else {
                results.add(new Call.Register(result.getRegisterBank(), allocate(result.getType())));
            }
        }
        return results;
    }

    private StructValue allocateStructValue(String typeName, boolean global) {
        StructLayout layout = structLayouts.get(typeName);
        if (layout == null) {
            throw new IllegalArgumentException("Unknown struct layout '" + typeName + "'.");
        }
        StructValue value = new StructValue(typeName);
        for (StructLayout.Field field : layout.fields.values()) {
            int fieldOffset = allocate(field.getType());
            value.addField(new StructValue.FieldSlot(field.getName(), field.getType(), field.isMutable(),
                    fieldOffset, global, field.getDecimalTypeInfo()));
        }
        return value;
    }

    private int allocate(String type) {
        return switch (type) {
            case "int" -> memory.allocateInt();
            case "dec" -> memory.allocateDec();
            case "bool" -> memory.allocateBool();
            case "string" -> memory.allocateStr();
            default -> throw new IllegalArgumentException("Unsupported register type: " + type);
        };
    }

    private int globalOffset(CompileContext.Offsets offsets, String name, String type) {
        return switch (type) {
            case "int" -> offsets.getIntOffset(name);
            case "dec" -> offsets.getDecOffset(name);
            case "bool" -> offsets.getBooleanOffset(name);
            case "string" -> offsets.getStringOffset(name);
            default -> -1;
        };
    }

    private Command move(String type, int source, int target) {
        return move(type, source, target, DecimalTypeInfo.DEFAULT);
    }

    private Command move(String type, int source, int target, DecimalTypeInfo decimalTypeInfo) {
        return switch (type) {
            case "int" -> new MoveInt(source, target);
            case "dec" -> new MoveDec(source, target, decimalTypeInfo);
            case "bool" -> new MoveBool(source, target);
            case "string" -> new MoveString(source, target);
            default -> throw new IllegalArgumentException("Unsupported register type: " + type);
        };
    }

    private Command globalLoad(String type, int source, int target) {
        return switch (type) {
            case "int" -> new GlobalLoadInt(source, target);
            case "dec" -> new GlobalLoadDec(source, target);
            case "bool" -> new GlobalLoadBool(source, target);
            case "string" -> new GlobalLoadString(source, target);
            default -> throw new IllegalArgumentException("Unsupported global register type: " + type);
        };
    }

    private Command globalStore(String type, int source, int target, DecimalTypeInfo decimalTypeInfo) {
        return switch (type) {
            case "int" -> new GlobalStoreInt(source, target);
            case "dec" -> new GlobalStoreDec(source, target, decimalTypeInfo);
            case "bool" -> new GlobalStoreBool(source, target);
            case "string" -> new GlobalStoreString(source, target);
            default -> throw new IllegalArgumentException("Unsupported global register type: " + type);
        };
    }

    private Command arrayStore(String type, int source, int base, int length, int index,
            boolean global, DecimalTypeInfo decimalTypeInfo) {
        return switch (type) {
            case "int" -> new ArrayStoreInt(source, base, length, index, global);
            case "dec" -> new ArrayStoreDec(source, base, length, index, global, decimalTypeInfo);
            case "bool" -> new ArrayStoreBool(source, base, length, index, global);
            case "string" -> new ArrayStoreString(source, base, length, index, global);
            default -> throw new IllegalArgumentException("Unsupported array element type: " + type);
        };
    }

    private DecimalTypeInfo extractDecimalTypeInfo(clnParser.TypeContext typeCtx) {
        if (typeCtx.baseType() == null || typeCtx.baseType().primitiveType() == null
                || typeCtx.baseType().primitiveType().decimalType() == null) {
            return DecimalTypeInfo.DEFAULT;
        }

        clnParser.DecimalTypeContext decimalType = typeCtx.baseType().primitiveType().decimalType();
        if (decimalType.INT_LIT() == null) {
            return DecimalTypeInfo.DEFAULT;
        }

        int precision;
        try {
            precision = Integer.parseInt(decimalType.INT_LIT().getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("line " + decimalType.INT_LIT().getSymbol().getLine()
                    + ": Decimal precision is outside the supported range.", e);
        }

        if (decimalType.ID() == null) {
            return new DecimalTypeInfo(precision);
        }
        try {
            return new DecimalTypeInfo(precision, RoundingMode.valueOf(decimalType.ID().getText()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("line " + decimalType.ID().getSymbol().getLine()
                    + ": Invalid rounding mode '" + decimalType.ID().getText() + "'. Valid values are: "
                    + "UP, DOWN, CEILING, FLOOR, HALF_UP, HALF_DOWN, HALF_EVEN, UNNECESSARY", e);
        }
    }

    private void validateRegisterType(String type, int line) {
        if (!type.equals("int") && !type.equals("dec") && !type.equals("bool") && !type.equals("string")) {
            throw unsupported(line, "register type '" + type + "'");
        }
    }

    private boolean isStructType(String type) {
        return structLayouts.containsKey(type);
    }

    private void registerStructLayout(clnParser.StructDeclContext declaration) {
        String name = declaration.ID().getText();
        StructLayout layout = new StructLayout();
        for (clnParser.StructFieldDeclContext fieldContext : declaration.structFieldDecl()) {
            String fieldName = fieldContext.ID().getText();
            String fieldType = normalizeType(fieldContext.type().getText());
            if (isArrayType(fieldType)) {
                throw unsupported(fieldContext.getStart().getLine(), "array fields in struct '" + name + "'");
            }
            validateRegisterType(fieldType, fieldContext.type().getStart().getLine());
            if (layout.fields.containsKey(fieldName)) {
                throw new IllegalArgumentException("line " + fieldContext.getStart().getLine()
                        + ": Duplicate field '" + fieldName + "' in struct '" + name + "'.");
            }
            layout.fields.put(fieldName, new StructLayout.Field(fieldName, fieldType,
                    fieldContext.VAR() != null, extractDecimalTypeInfo(fieldContext.type())));
        }
        structLayouts.put(name, layout);
    }

    private void requireSameType(String actual, String expected, int line, String context) {
        if (actual == null || !actual.equals(expected)) {
            throw new IllegalArgumentException("line " + line + ": Type mismatch in " + context
                    + ": expected '" + expected + "', got '" + actual + "'.");
        }
    }

    private String normalizeType(String type) {
        return type.replaceAll("dec\\([^)]*\\)", "dec");
    }

    private boolean isArrayType(String type) {
        return type != null && type.endsWith("[]");
    }

    private String arrayElementType(String type) {
        if (!isArrayType(type)) {
            throw new IllegalArgumentException("Unsupported array type: " + type);
        }
        return type.substring(0, type.length() - 2);
    }

    private String arrayBaseType(String type) {
        while (isArrayType(type)) {
            type = arrayElementType(type);
        }
        return type;
    }

    private void validateArrayType(String type, int line) {
        validateRegisterType(arrayBaseType(type), line);
    }

    private static int dimensionsElementCount(int[] dimensions) {
        int count = 1;
        for (int dimension : dimensions) {
            count = Math.multiplyExact(count, dimension);
        }
        return count;
    }

    private IllegalArgumentException unsupported(int line, String feature) {
        return new IllegalArgumentException("line " + line + ": Register compiler does not support " + feature + ".");
    }

    private void validateType(String typeName, int lineNumber) {
        String baseType = typeName.replaceAll("\\[\\]", "");
        if (baseType.equals("Any")) {
            return;
        }
        if (!isPrimitiveType(baseType) && !definedTypes.contains(baseType)) {
            throw new IllegalArgumentException("line " + lineNumber + ": Unknown type '" + baseType + "'. "
                    + "Type must be one of the primitive types (int, bool, string, dec) or a declared struct/union.");
        }
    }

    private boolean isPrimitiveType(String typeName) {
        String baseType = typeName.replaceAll("\\[\\]", "");
        return baseType.equals("int") || baseType.equals("bool") || baseType.equals("string") || baseType.equals("dec");
    }
}
