package org.clnlang.compreg.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.clnlang.compreg.commands.ArrayIndexOffsets;
import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.commands.Command;
import org.clnlang.compreg.commands.JumpIfFalse;
import org.clnlang.compreg.commands.JumpIfTrue;
import org.clnlang.compreg.commands.Label;
import org.clnlang.compreg.commands.NotBool;
import org.clnlang.compreg.commands.add.AddDecDec;
import org.clnlang.compreg.commands.add.AddDecInt;
import org.clnlang.compreg.commands.add.AddIntInt;
import org.clnlang.compreg.commands.array.ArrayLoadBool;
import org.clnlang.compreg.commands.array.ArrayLoadDec;
import org.clnlang.compreg.commands.array.ArrayLoadInt;
import org.clnlang.compreg.commands.array.ArrayLoadString;
import org.clnlang.compreg.commands.compare.Compare;
import org.clnlang.compreg.commands.compare.CompareBool;
import org.clnlang.compreg.commands.compare.CompareDecDec;
import org.clnlang.compreg.commands.compare.CompareDecInt;
import org.clnlang.compreg.commands.compare.CompareIntDec;
import org.clnlang.compreg.commands.compare.CompareIntInt;
import org.clnlang.compreg.commands.compare.CompareString;
import org.clnlang.compreg.commands.concat.BoolStringifier;
import org.clnlang.compreg.commands.concat.ConcatString;
import org.clnlang.compreg.commands.concat.DecStringifier;
import org.clnlang.compreg.commands.concat.IntStringifier;
import org.clnlang.compreg.commands.concat.StringRegisterStringifier;
import org.clnlang.compreg.commands.concat.Stringifier;
import org.clnlang.compreg.commands.div.DivDecDec;
import org.clnlang.compreg.commands.div.DivDecInt;
import org.clnlang.compreg.commands.div.DivIntDec;
import org.clnlang.compreg.commands.div.DivIntInt;
import org.clnlang.compreg.commands.global.load.GlobalLoadBool;
import org.clnlang.compreg.commands.global.load.GlobalLoadDec;
import org.clnlang.compreg.commands.global.load.GlobalLoadInt;
import org.clnlang.compreg.commands.global.load.GlobalLoadString;
import org.clnlang.compreg.commands.modulo.ModuloDecDec;
import org.clnlang.compreg.commands.modulo.ModuloDecInt;
import org.clnlang.compreg.commands.modulo.ModuloIntDec;
import org.clnlang.compreg.commands.modulo.ModuloIntInt;
import org.clnlang.compreg.commands.move.MoveBool;
import org.clnlang.compreg.commands.move.MoveDec;
import org.clnlang.compreg.commands.move.MoveInt;
import org.clnlang.compreg.commands.move.MoveString;
import org.clnlang.compreg.commands.mul.MulDecDec;
import org.clnlang.compreg.commands.mul.MulDecInt;
import org.clnlang.compreg.commands.mul.MulIntInt;
import org.clnlang.compreg.commands.set.SetBool;
import org.clnlang.compreg.commands.set.SetDec;
import org.clnlang.compreg.commands.set.SetInt;
import org.clnlang.compreg.commands.set.SetString;
import org.clnlang.compreg.commands.sub.SubDecDec;
import org.clnlang.compreg.commands.sub.SubDecInt;
import org.clnlang.compreg.commands.sub.SubIntDec;
import org.clnlang.compreg.commands.sub.SubIntInt;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.compreg.runtime.RegisterBank;
import org.clnlang.parser.clnParser;

final class RegisterExpressionCompiler {

    private static final Stringifier INT_STRINGIFIER = new IntStringifier();
    private static final Stringifier DEC_STRINGIFIER = new DecStringifier();
    private static final Stringifier BOOL_STRINGIFIER = new BoolStringifier();
    private static final Stringifier STRING_STRINGIFIER = new StringRegisterStringifier();

    private static final class ArrayLiteralContents {
        private final String elementType;
        private final int[] dimensions;
        private final List<CompiledValue> elements;

        private ArrayLiteralContents(String elementType, int[] dimensions, List<CompiledValue> elements) {
            this.elementType = elementType;
            this.dimensions = dimensions;
            this.elements = elements;
        }
    }

    interface Context {
        int allocate(String type);
        CompiledValue compileIdentifier(String name, int line);
        CompiledValue compileStructLiteral(clnParser.StructLiteralContext literal, int line);
        CompiledValue compileStructMember(String variable, List<String> members, int line);
        CompiledValue compileIncrement(String name, boolean increment, boolean prefix, int line);
        CompiledFunction resolveFunction(String name, int line, List<String> argumentTypes);
        boolean hasZeroArgumentFunction(String name);
        List<Call.Register> allocateCallResults(CompiledFunction function);
        IllegalArgumentException unsupported(int line, String feature);
    }

    private final Context context;

    RegisterExpressionCompiler(Context context) {
        this.context = context;
    }

    CompiledValue compile(clnParser.ExprContext context) {
        clnParser.OrExprContext orExpr = context.orExpr();
        if (orExpr.andExpr().size() == 1) {
            return compileAndExpr(orExpr.andExpr(0));
        }
        int line = context.getStart().getLine();
        CompiledValue first = compileAndExpr(orExpr.andExpr(0));
        requireBoolean(first, line, "logical OR");
        int target = contextAllocate("bool");
        Label end = new Label();
        List<Command> commands = new ArrayList<>(first.commands);
        commands.add(new MoveBool(first.offset, target));
        for (int i = 1; i < orExpr.andExpr().size(); i++) {
            commands.add(new JumpIfTrue(target, end));
            CompiledValue next = compileAndExpr(orExpr.andExpr(i));
            requireBoolean(next, line, "logical OR");
            commands.addAll(next.commands);
            commands.add(new MoveBool(next.offset, target));
        }
        commands.add(end);
        return new CompiledValue("bool", target, commands);
    }

    private CompiledValue compileAndExpr(clnParser.AndExprContext context) {
        if (context.equalityExpr().size() == 1) {
            return compileEqualityExpr(context.equalityExpr(0));
        }
        int line = context.getStart().getLine();
        CompiledValue first = compileEqualityExpr(context.equalityExpr(0));
        requireBoolean(first, line, "logical AND");
        int target = contextAllocate("bool");
        Label end = new Label();
        List<Command> commands = new ArrayList<>(first.commands);
        commands.add(new MoveBool(first.offset, target));
        for (int i = 1; i < context.equalityExpr().size(); i++) {
            commands.add(new JumpIfFalse(target, end));
            CompiledValue next = compileEqualityExpr(context.equalityExpr(i));
            requireBoolean(next, line, "logical AND");
            commands.addAll(next.commands);
            commands.add(new MoveBool(next.offset, target));
        }
        commands.add(end);
        return new CompiledValue("bool", target, commands);
    }

    private CompiledValue compileEqualityExpr(clnParser.EqualityExprContext context) {
        CompiledValue value = compileRelExpr(context.relExpr(0));
        for (int i = 1; i < context.relExpr().size(); i++) {
            value = compileComparison(context.getChild(i * 2 - 1).getText(), value,
                    compileRelExpr(context.relExpr(i)), context.getStart().getLine());
        }
        return value;
    }

    private CompiledValue compileRelExpr(clnParser.RelExprContext context) {
        CompiledValue value = compileAddExpr(context.addExpr(0));
        for (int i = 1; i < context.addExpr().size(); i++) {
            value = compileComparison(context.getChild(i * 2 - 1).getText(), value,
                    compileAddExpr(context.addExpr(i)), context.getStart().getLine());
        }
        return value;
    }

    private CompiledValue compileAddExpr(clnParser.AddExprContext context) {
        CompiledValue value = compileMulExpr(context.mulExpr(0));
        for (int i = 1; i < context.mulExpr().size(); i++) {
            value = compileBinary(context.getChild(i * 2 - 1).getText(), value,
                    compileMulExpr(context.mulExpr(i)), context.getStart().getLine());
        }
        return value;
    }

    private CompiledValue compileMulExpr(clnParser.MulExprContext context) {
        CompiledValue value = compileUnaryExpr(context.unaryExpr(0));
        for (int i = 1; i < context.unaryExpr().size(); i++) {
            value = compileBinary(context.getChild(i * 2 - 1).getText(), value,
                    compileUnaryExpr(context.unaryExpr(i)), context.getStart().getLine());
        }
        return value;
    }

    private CompiledValue compileUnaryExpr(clnParser.UnaryExprContext context) {
        if (context.postfixExpr() != null) {
            return compilePostfixExpr(context.postfixExpr());
        }
        if (context.NOT() != null) {
            CompiledValue operand = compileUnaryExpr(context.unaryExpr());
            requireBoolean(operand, context.getStart().getLine(), "logical negation");
            int target = contextAllocate("bool");
            List<Command> commands = new ArrayList<>(operand.commands);
            commands.add(new NotBool(operand.offset, target));
            return new CompiledValue("bool", target, commands);
        }
        if (context.INC() != null || context.DEC() != null) {
            clnParser.UnaryExprContext operand = context.unaryExpr();
            if (operand == null || operand.postfixExpr() == null || operand.postfixExpr().primaryExpr().ID() == null
                    || !operand.postfixExpr().postfixOp().isEmpty()) {
                throw unsupported(context.getStart().getLine(), "increment or decrement of a non-variable expression");
            }
            return this.context.compileIncrement(operand.postfixExpr().primaryExpr().ID().getText(),
                    context.INC() != null, true, context.getStart().getLine());
        }
        if (context.MINUS() == null) {
            throw unsupported(context.getStart().getLine(), "unary operator '" + context.getChild(0).getText() + "'");
        }
        CompiledValue operand = compileUnaryExpr(context.unaryExpr());
        if (!isNumeric(operand.type)) {
            throw unsupported(context.getStart().getLine(), "unary minus for '" + operand.type + "'");
        }
        int zero = contextAllocate(operand.type);
        Command setZero = operand.type.equals("int") ? new SetInt(zero, 0) : new SetDec(zero, BigDecimal.ZERO);
        List<Command> commands = new ArrayList<>();
        commands.add(setZero);
        commands.addAll(operand.commands);
        return compileBinary("-", new CompiledValue(operand.type, zero, List.of()), operand,
                context.getStart().getLine(), commands);
    }

    private CompiledValue compilePostfixExpr(clnParser.PostfixExprContext context) {
        if (context.postfixOp().isEmpty()) {
            return compilePrimaryExpr(context.primaryExpr());
        }
        if (context.primaryExpr().ID() != null && context.postfixOp().size() == 1
                && context.postfixOp(0).LPAREN() != null) {
            return compileFunctionCall(context.primaryExpr().ID().getText(), context.postfixOp(0).argList(),
                    context.getStart().getLine());
        }
        if (context.primaryExpr().ID() != null && context.postfixOp().size() == 1
            && (context.postfixOp(0).INC() != null || context.postfixOp(0).DEC() != null)) {
            return this.context.compileIncrement(context.primaryExpr().ID().getText(),
                context.postfixOp(0).INC() != null, false, context.getStart().getLine());
        }
        if (context.primaryExpr().ID() != null && !context.postfixOp().isEmpty()
            && context.postfixOp().stream().allMatch(postfix -> postfix.DOT() != null)) {
            List<String> members = context.postfixOp().stream()
                .map(postfix -> postfix.ID().getText()).toList();
            return this.context.compileStructMember(context.primaryExpr().ID().getText(), members,
                context.getStart().getLine());
        }
        if (context.primaryExpr().ID() != null && context.postfixOp().stream()
            .allMatch(postfix -> postfix.LBRACK() != null)) {
            List<clnParser.ExprContext> indices = context.postfixOp().stream()
                .map(clnParser.PostfixOpContext::expr).toList();
            return compileArrayRead(context.primaryExpr().ID().getText(), indices, context.getStart().getLine());
        }
        if (context.primaryExpr().ID() == null || context.postfixOp().size() != 1) {
            throw unsupported(context.getStart().getLine(), "member, index, and chained postfix expressions");
        }
        throw unsupported(context.getStart().getLine(), "postfix operator");
    }

    private CompiledValue compileArrayRead(String name, List<clnParser.ExprContext> indexContexts, int line) {
        CompiledValue array = context.compileIdentifier(name, line);
        if (array.arrayLength < 0 || array.type == null || array.arrayDimensions == null) {
            throw unsupported(line, "indexed access to non-array variable '" + name + "'");
        }
        if (indexContexts.size() != array.arrayDimensions.length) {
            throw unsupported(line, "partial or excessive multidimensional array indexing");
        }
        String elementType = arrayBaseType(array.type);
        int[] indexOffsets = new int[indexContexts.size()];
        List<Command> commands = new ArrayList<>(array.commands);
        for (int i = 0; i < indexContexts.size(); i++) {
            clnParser.ExprContext indexContext = indexContexts.get(i);
            CompiledValue index = compile(indexContext);
            requireSameType(index.type, "int", indexContext.getStart().getLine(), "array index");
            commands.addAll(index.commands);
            indexOffsets[i] = index.offset;
        }
        int linearIndex = contextAllocate("int");
        commands.add(new ArrayIndexOffsets(indexOffsets, array.arrayDimensions, linearIndex));
        int target = contextAllocate(elementType);
        commands.add(arrayLoad(elementType, array.offset, array.arrayLength, linearIndex, target,
            array.globalArray));
        return new CompiledValue(elementType, target, commands);
    }

    private String arrayBaseType(String type) {
        while (type.endsWith("[]")) {
            type = type.substring(0, type.length() - 2);
        }
        return type;
    }

    private CompiledValue compileFunctionCall(String name, clnParser.ArgListContext argList, int line) {
        List<clnParser.ExprContext> argumentExpressions = argList == null ? List.of() : argList.expr();
        if (name.equals("copy")) {
            return compileArrayCopy(argumentExpressions, line);
        }

        List<Command> commands = new ArrayList<>();
        List<CompiledValue> argumentValues = new ArrayList<>();
        for (clnParser.ExprContext argumentExpression : argumentExpressions) {
            CompiledValue value = compile(argumentExpression);
            argumentValues.add(value);
            commands.addAll(value.commands);
        }
        List<String> argumentTypes = argumentValues.stream().map(value -> value.type).toList();
        CompiledFunction function = context.resolveFunction(name, line, argumentTypes);
        List<CompiledFunction.Slot> parameters = function.getParameters();
        List<Call.Register> arguments = new ArrayList<>();
        for (int i = 0; i < argumentValues.size(); i++) {
            CompiledValue value = argumentValues.get(i);
            CompiledFunction.Slot parameter = parameters.get(i);
            requireSameType(value.type, parameter.getType(), line,
                    "argument " + (i + 1) + " to '" + name + "'");
            if (value.structValue != null) {
                arguments.add(new Call.Register(value.structValue));
            } else {
                arguments.add(new Call.Register(RegisterBank.forType(value.type), value.offset));
            }
        }

        List<Call.Register> results = context.allocateCallResults(function);
        commands.add(new Call(function, arguments, results));
        if (results.isEmpty()) {
            return new CompiledValue("void", -1, commands);
        }
        if (results.size() == 1) {
            Call.Register result = results.get(0);
            if (result.getStructValue() != null) {
                return new CompiledValue(result.getTypeName(), -1, commands, result.getStructValue());
            }
            return new CompiledValue(result.getTypeName(), result.getOffset(), commands);
        }
        return new CompiledValue(null, -1, commands, results);
    }

    private CompiledValue compileArrayCopy(List<clnParser.ExprContext> arguments, int line) {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("line " + line + ": Function 'copy' expects 1 argument, got "
                    + arguments.size() + ".");
        }

        CompiledValue source = compile(arguments.get(0));
        if (source.arrayLength < 0 || source.type == null || !source.type.endsWith("[]")) {
            throw unsupported(line, "copy of a non-array value");
        }
        String elementType = source.type.substring(0, source.type.length() - 2);
        if (elementType.endsWith("[]")) {
            throw unsupported(line, "multidimensional array copy");
        }

        int targetOffset = contextAllocate(elementType);
        for (int i = 1; i < source.arrayLength; i++) {
            contextAllocate(elementType);
        }

        List<Command> commands = new ArrayList<>(source.commands);
        for (int i = 0; i < source.arrayLength; i++) {
            int sourceOffset = source.offset + i;
            int targetElementOffset = targetOffset + i;
            if (source.globalArray) {
                commands.add(globalLoad(elementType, sourceOffset, targetElementOffset));
            } else {
                commands.add(arrayElementMove(elementType, sourceOffset, targetElementOffset));
            }
        }
        return new CompiledValue(source.type, targetOffset, commands, source.arrayDimensions, false, true);
    }

    private CompiledValue compilePrimaryExpr(clnParser.PrimaryExprContext context) {
        int line = context.getStart().getLine();
        if (context.INT_LIT() != null) {
            int target = contextAllocate("int");
            try {
                return value("int", target, new SetInt(target, Integer.parseInt(context.INT_LIT().getText())));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("line " + line
                        + ": Integer literal is outside the supported register range.", e);
            }
        }
        if (context.DEC_LIT() != null) {
            int target = contextAllocate("dec");
            return value("dec", target, new SetDec(target, new BigDecimal(context.DEC_LIT().getText())));
        }
        if (context.BOOL_LIT() != null) {
            int target = contextAllocate("bool");
            return value("bool", target, new SetBool(target, Boolean.parseBoolean(context.BOOL_LIT().getText())));
        }
        if (context.STRING_LIT() != null) {
            String text = context.STRING_LIT().getText();
            String literal = text.substring(1, text.length() - 1)
                    .replace("\\n", "\n").replace("\\t", "\t")
                    .replace("\\\"", "\"").replace("\\\\", "\\");
            int target = contextAllocate("string");
            return value("string", target, new SetString(target, literal));
        }
        if (context.arrayLiteral() != null) {
            return compileArrayLiteral(context.arrayLiteral(), line);
        }
        if (context.structLiteral() != null) {
            clnParser.StructLiteralContext literal = context.structLiteral();
            if (literal.fieldInitList() == null) {
                String name = literal.qualifiedName().getText();
                if (this.context.hasZeroArgumentFunction(name)) {
                    return compileFunctionCall(name, null, line);
                }
            }
            return this.context.compileStructLiteral(context.structLiteral(), line);
        }
        if (context.ID() != null) {
            return this.context.compileIdentifier(context.ID().getText(), line);
        }
        if (context.expr() != null) {
            return compile(context.expr());
        }
        throw unsupported(line, "array or struct literals");
    }

    private CompiledValue compileArrayLiteral(clnParser.ArrayLiteralContext context, int line) {
        if (context.exprList() == null || context.exprList().expr().isEmpty()) {
            throw unsupported(line, "empty arrays without an explicit element type");
        }
        ArrayLiteralContents contents = collectArrayLiteral(context, line);

        List<Command> commands = new ArrayList<>();
        for (CompiledValue element : contents.elements) {
            commands.addAll(element.commands);
        }
        int baseOffset = contextAllocate(contents.elementType);
        for (int i = 1; i < contents.elements.size(); i++) {
            contextAllocate(contents.elementType);
        }
        for (int i = 0; i < contents.elements.size(); i++) {
            commands.add(arrayElementMove(contents.elementType, contents.elements.get(i).offset, baseOffset + i));
        }
        String arrayType = contents.elementType + "[]".repeat(contents.dimensions.length);
        return new CompiledValue(arrayType, baseOffset, commands, contents.dimensions, false, true);
    }

    private ArrayLiteralContents collectArrayLiteral(clnParser.ArrayLiteralContext context, int line) {
        List<CompiledValue> flattenedElements = new ArrayList<>();
        int[] childDimensions = new int[0];
        boolean firstChild = true;
        String elementType = null;
        for (clnParser.ExprContext expression : context.exprList().expr()) {
            clnParser.ArrayLiteralContext nestedLiteral = directArrayLiteral(expression);
            int[] currentChildDimensions;
            if (nestedLiteral != null) {
                ArrayLiteralContents nested = collectArrayLiteral(nestedLiteral, expression.getStart().getLine());
                currentChildDimensions = nested.dimensions;
                if (elementType == null) {
                    elementType = nested.elementType;
                } else {
                    requireSameType(nested.elementType, elementType, expression.getStart().getLine(),
                            "array literal element");
                }
                flattenedElements.addAll(nested.elements);
            } else {
                CompiledValue element = compile(expression);
                if (element.type == null || element.type.endsWith("[]")) {
                    throw unsupported(expression.getStart().getLine(), "array values in non-literal array elements");
                }
                currentChildDimensions = new int[0];
                if (elementType == null) {
                    elementType = element.type;
                } else {
                    requireSameType(element.type, elementType, expression.getStart().getLine(),
                            "array literal element");
                }
                flattenedElements.add(element);
            }

            if (firstChild) {
                childDimensions = currentChildDimensions;
                firstChild = false;
            } else if (!Arrays.equals(childDimensions, currentChildDimensions)) {
                throw new IllegalArgumentException("line " + expression.getStart().getLine()
                        + ": Multidimensional array literals must be rectangular.");
            }
        }
        if (elementType == null || flattenedElements.isEmpty()) {
            throw unsupported(line, "empty arrays without an explicit element type");
        }
        int[] dimensions = new int[childDimensions.length + 1];
        dimensions[0] = context.exprList().expr().size();
        System.arraycopy(childDimensions, 0, dimensions, 1, childDimensions.length);
        return new ArrayLiteralContents(elementType, dimensions, flattenedElements);
    }

    private clnParser.ArrayLiteralContext directArrayLiteral(clnParser.ExprContext expression) {
        clnParser.OrExprContext orExpr = expression.orExpr();
        if (orExpr.andExpr().size() != 1) return null;
        clnParser.AndExprContext andExpr = orExpr.andExpr(0);
        if (andExpr.equalityExpr().size() != 1) return null;
        clnParser.EqualityExprContext equalityExpr = andExpr.equalityExpr(0);
        if (equalityExpr.relExpr().size() != 1) return null;
        clnParser.RelExprContext relExpr = equalityExpr.relExpr(0);
        if (relExpr.addExpr().size() != 1) return null;
        clnParser.AddExprContext addExpr = relExpr.addExpr(0);
        if (addExpr.mulExpr().size() != 1) return null;
        clnParser.MulExprContext mulExpr = addExpr.mulExpr(0);
        if (mulExpr.unaryExpr().size() != 1) return null;
        clnParser.UnaryExprContext unaryExpr = mulExpr.unaryExpr(0);
        if (unaryExpr.postfixExpr() == null || !unaryExpr.postfixExpr().postfixOp().isEmpty()) return null;
        clnParser.PrimaryExprContext primaryExpr = unaryExpr.postfixExpr().primaryExpr();
        if (primaryExpr.arrayLiteral() != null) return primaryExpr.arrayLiteral();
        return primaryExpr.expr() == null ? null : directArrayLiteral(primaryExpr.expr());
    }

    private Command arrayElementMove(String elementType, int sourceOffset, int targetOffset) {
        return switch (elementType) {
            case "int" -> new MoveInt(sourceOffset, targetOffset);
            case "dec" -> new MoveDec(sourceOffset, targetOffset);
            case "bool" -> new MoveBool(sourceOffset, targetOffset);
            case "string" -> new MoveString(sourceOffset, targetOffset);
            default -> throw new IllegalArgumentException("Unsupported array element type: " + elementType);
        };
    }

    private CompiledValue compileBinary(String operator, CompiledValue left, CompiledValue right, int line) {
        return compileBinary(operator, left, right, line, List.of());
    }

    private CompiledValue compileBinary(String operator, CompiledValue left, CompiledValue right, int line,
            List<Command> prefix) {
        if (operator.equals("+") && (left.type.equals("string") || right.type.equals("string"))) {
            if (!isConcatenable(left.type) || !isConcatenable(right.type)) {
                throw unsupported(line, "operator '+' with '" + left.type + "' and '" + right.type + "'");
            }
            int target = contextAllocate("string");
            List<Command> commands = new ArrayList<>(prefix);
            if (prefix.isEmpty()) {
                commands.addAll(left.commands);
                commands.addAll(right.commands);
            }
                commands.add(new ConcatString(stringifier(left.type), stringifier(right.type),
                    left.offset, right.offset, target));
            return new CompiledValue("string", target, commands);
        }
        if (!isNumeric(left.type) || !isNumeric(right.type)) {
            throw unsupported(line, "operator '" + operator + "' with '" + left.type + "' and '" + right.type + "'");
        }
        String resultType = left.type.equals("dec") || right.type.equals("dec") ? "dec" : "int";
        int target = contextAllocate(resultType);
        List<Command> commands = new ArrayList<>(prefix);
        if (prefix.isEmpty()) {
            commands.addAll(left.commands);
            commands.addAll(right.commands);
        }
        commands.add(binaryCommand(operator, left, right, target, line));
        return new CompiledValue(resultType, target, commands);
    }

    private CompiledValue compileComparison(String operator, CompiledValue left, CompiledValue right, int line) {
        boolean numeric = isNumeric(left.type) && isNumeric(right.type);
        boolean equality = operator.equals("==") || operator.equals("!=");
        boolean sameComparableType = left.type.equals(right.type)
                && (left.type.equals("bool") || left.type.equals("string"));
        if (!numeric && !(equality && sameComparableType)) {
            throw unsupported(line, "comparison '" + operator + "' between '" + left.type + "' and '"
                    + right.type + "'");
        }
        int target = contextAllocate("bool");
        List<Command> commands = new ArrayList<>(left.commands);
        commands.addAll(right.commands);
        Compare.Operator compareOperator = toCompareOperator(operator);
        if (left.type.equals("int") && right.type.equals("int")) {
            commands.add(new CompareIntInt(compareOperator, left.offset, right.offset, target));
        } else if (left.type.equals("dec") && right.type.equals("dec")) {
            commands.add(new CompareDecDec(compareOperator, left.offset, right.offset, target));
        } else if (left.type.equals("dec")) {
            commands.add(new CompareDecInt(compareOperator, left.offset, right.offset, target));
        } else if (right.type.equals("dec")) {
            commands.add(new CompareIntDec(compareOperator, left.offset, right.offset, target));
        } else if (left.type.equals("bool")) {
            commands.add(new CompareBool(compareOperator, left.offset, right.offset, target));
        } else {
            commands.add(new CompareString(compareOperator, left.offset, right.offset, target));
        }
        return new CompiledValue("bool", target, commands);
    }

    private Compare.Operator toCompareOperator(String operator) {
        return switch (operator) {
            case "==" -> Compare.Operator.EQ;
            case "!=" -> Compare.Operator.NEQ;
            case "<" -> Compare.Operator.LT;
            case "<=" -> Compare.Operator.LTE;
            case ">" -> Compare.Operator.GT;
            case ">=" -> Compare.Operator.GTE;
            default -> throw new IllegalArgumentException("Unknown comparison operator: " + operator);
        };
    }

    private Stringifier stringifier(String type) {
        return switch (type) {
            case "int" -> INT_STRINGIFIER;
            case "dec" -> DEC_STRINGIFIER;
            case "bool" -> BOOL_STRINGIFIER;
            case "string" -> STRING_STRINGIFIER;
            default -> throw new IllegalArgumentException("Cannot concatenate value of type '" + type + "'.");
        };
    }

    private void requireBoolean(CompiledValue value, int line, String operator) {
        if (!"bool".equals(value.type)) {
            throw unsupported(line, operator + " operand with type '" + value.type + "'");
        }
    }

    private Command binaryCommand(String operator, CompiledValue left, CompiledValue right, int target, int line) {
        int a = left.offset;
        int b = right.offset;
        boolean leftDec = left.type.equals("dec");
        boolean rightDec = right.type.equals("dec");
        return switch (operator) {
            case "+" -> {
                if (leftDec && rightDec) yield new AddDecDec(target, a, b);
                if (leftDec) yield new AddDecInt(target, a, b);
                if (rightDec) yield new AddDecInt(target, b, a);
                yield new AddIntInt(target, a, b);
            }
            case "-" -> {
                if (leftDec && rightDec) yield new SubDecDec(target, a, b);
                if (leftDec) yield new SubDecInt(target, a, b);
                if (rightDec) yield new SubIntDec(target, a, b);
                yield new SubIntInt(target, a, b);
            }
            case "*" -> {
                if (leftDec && rightDec) yield new MulDecDec(target, a, b);
                if (leftDec) yield new MulDecInt(target, a, b);
                if (rightDec) yield new MulDecInt(target, b, a);
                yield new MulIntInt(target, a, b);
            }
            case "/" -> {
                if (leftDec && rightDec) yield new DivDecDec(target, a, b);
                if (leftDec) yield new DivDecInt(target, a, b);
                if (rightDec) yield new DivIntDec(target, a, b);
                yield new DivIntInt(target, a, b);
            }
            case "%" -> {
                if (leftDec && rightDec) yield new ModuloDecDec(target, a, b);
                if (leftDec) yield new ModuloDecInt(target, a, b);
                if (rightDec) yield new ModuloIntDec(target, a, b);
                yield new ModuloIntInt(target, a, b);
            }
            default -> throw unsupported(line, "operator '" + operator + "'");
        };
    }

    private CompiledValue value(String type, int offset, Command command) {
        return new CompiledValue(type, offset, List.of(command));
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

    private Command arrayLoad(String type, int base, int length, int index, int target, boolean global) {
        return switch (type) {
            case "int" -> new ArrayLoadInt(base, length, index, target, global);
            case "dec" -> new ArrayLoadDec(base, length, index, target, global);
            case "bool" -> new ArrayLoadBool(base, length, index, target, global);
            case "string" -> new ArrayLoadString(base, length, index, target, global);
            default -> throw new IllegalArgumentException("Unsupported array element type: " + type);
        };
    }

    private int contextAllocate(String type) {
        return context.allocate(type);
    }

    private IllegalArgumentException unsupported(int line, String feature) {
        return context.unsupported(line, feature);
    }

    private void requireSameType(String actual, String expected, int line, String use) {
        if (actual == null || !actual.equals(expected)) {
            throw new IllegalArgumentException("line " + line + ": Type mismatch in " + use
                    + ": expected '" + expected + "', got '" + actual + "'.");
        }
    }

    private boolean isNumeric(String type) {
        return "int".equals(type) || "dec".equals(type);
    }

    private boolean isConcatenable(String type) {
        return isNumeric(type) || "bool".equals(type) || "string".equals(type);
    }
}
