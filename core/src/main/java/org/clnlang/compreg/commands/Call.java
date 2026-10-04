package org.clnlang.compreg.commands;

import java.util.List;

import org.clnlang.compreg.Memory;
import org.clnlang.compreg.runtime.CompiledFunction;
import org.clnlang.compreg.runtime.RegisterBank;
import org.clnlang.compreg.runtime.StructValue;

public final class Call implements Command {

    public static final class Register {
        private final RegisterBank registerBank;
        private final int offset;
        private final StructValue structValue;

        public Register(RegisterBank registerBank, int offset) {
            if (registerBank == null) {
                throw new IllegalArgumentException("Scalar register bank is required.");
            }
            this.registerBank = registerBank;
            this.offset = offset;
            this.structValue = null;
        }

        public Register(StructValue structValue) {
            this.registerBank = null;
            this.offset = -1;
            this.structValue = structValue;
        }

        public RegisterBank getRegisterBank() { return registerBank; }

        public String getTypeName() { return structValue == null ? registerBankTypeName() : structValue.getTypeName(); }

        private String registerBankTypeName() { return registerBank.name().toLowerCase(); }

        public int getOffset() {
            return offset;
        }

        public StructValue getStructValue() {
            return structValue;
        }

        public boolean isStruct() {
            return structValue != null;
        }
    }

    private final CompiledFunction function;
    private final List<Register> arguments;
    private final List<Register> results;

    public Call(CompiledFunction function, List<Register> arguments, List<Register> results) {
        this.function = function;
        this.arguments = List.copyOf(arguments);
        this.results = List.copyOf(results);
    }

    @Override
    public void execute(Memory memory) {
        if (function.getStructuredLibraryBody() != null) {
            validateStructuredCall();
            function.getStructuredLibraryBody().execute(memory, arguments, results);
            return;
        }
        List<CompiledFunction.Slot> parameters = function.getParameters();
        List<CompiledFunction.Slot> returnValues = function.getReturnValues();
        if (arguments.size() != parameters.size()) {
            throw new IllegalArgumentException("Function '" + function.getName() + "' expects "
                    + parameters.size() + " arguments, got " + arguments.size() + ".");
        }
        if (!results.isEmpty() && results.size() != returnValues.size()) {
            throw new IllegalArgumentException("Function '" + function.getName() + "' returns "
                    + returnValues.size() + " values, got " + results.size() + " result registers.");
        }

        int callerIntBase = RegisterBank.INT.baseOffset(memory);
        int callerDecBase = RegisterBank.DEC.baseOffset(memory);
        int callerBoolBase = RegisterBank.BOOL.baseOffset(memory);
        int callerStringBase = RegisterBank.STRING.baseOffset(memory);
        for (int i = 0; i < arguments.size(); i++) {
            Register argument = arguments.get(i);
            CompiledFunction.Slot parameter = parameters.get(i);
            if (argument.getRegisterBank() != parameter.getRegisterBank()) {
                throw new IllegalArgumentException("Argument " + (i + 1) + " of '" + function.getName()
                        + "' expects '" + parameter.getType() + "', got '" + argument.getTypeName() + "'.");
            }
        }

        for (int i = 0; i < results.size(); i++) {
            Register destination = results.get(i);
            CompiledFunction.Slot result = returnValues.get(i);
            if (destination.getRegisterBank() != result.getRegisterBank()) {
                throw new IllegalArgumentException("Result " + (i + 1) + " of '" + function.getName()
                        + "' has type '" + result.getType() + "', destination has type '"
                        + destination.getTypeName() + "'.");
            }
        }

        memory.pushFrame(function.getFrameLayout());
        try {
            for (int i = 0; i < parameters.size(); i++) {
                Register argument = arguments.get(i);
                CompiledFunction.Slot parameter = parameters.get(i);
                int callerBase = switch (argument.getRegisterBank()) {
                    case INT -> callerIntBase;
                    case DEC -> callerDecBase;
                    case BOOL -> callerBoolBase;
                    case STRING -> callerStringBase;
                };
                parameter.getRegisterBank().copyAbsolute(memory,
                    argument.getRegisterBank().absoluteOffset(callerBase, argument.getOffset()),
                    parameter.getRegisterBank().absoluteOffset(memory, parameter.getOffset()),
                    parameter.getDecimalTypeInfo());
            }
            function.getInitializers().execute(memory);
            if (!memory.isReturnRequested()) {
                function.getBody().execute(memory);
            }
            if (!results.isEmpty()) {
                for (int i = 0; i < returnValues.size(); i++) {
                    CompiledFunction.Slot result = returnValues.get(i);
                    Register destination = results.get(i);
                    int callerBase = switch (destination.getRegisterBank()) {
                        case INT -> callerIntBase;
                        case DEC -> callerDecBase;
                        case BOOL -> callerBoolBase;
                        case STRING -> callerStringBase;
                    };
                    result.getRegisterBank().copyAbsolute(memory,
                            result.getRegisterBank().absoluteOffset(memory, result.getOffset()),
                            destination.getRegisterBank().absoluteOffset(callerBase, destination.getOffset()),
                            result.getDecimalTypeInfo());
                }
            }
        } finally {
            memory.popOffset();
        }
    }

    private void validateStructuredCall() {
        if (arguments.size() != function.getParameters().size()) {
            throw new IllegalArgumentException("Function '" + function.getName() + "' expects "
                    + function.getParameters().size() + " arguments, got " + arguments.size() + ".");
        }
        if (results.size() != function.getReturnValues().size()) {
            throw new IllegalArgumentException("Function '" + function.getName() + "' returns "
                    + function.getReturnValues().size() + " values, got " + results.size() + " result registers.");
        }
        validateStructuredValues(arguments, function.getParameters(), "argument");
        validateStructuredValues(results, function.getReturnValues(), "result");
    }

    private void validateStructuredValues(List<Register> values, List<CompiledFunction.Slot> slots, String role) {
        for (int i = 0; i < values.size(); i++) {
            Register value = values.get(i);
            CompiledFunction.Slot expected = slots.get(i);
            String expectedType = expected.getType();
                if (expected.getRegisterBank() != value.getRegisterBank()
                    || (expected.getRegisterBank() == null
                        && !expectedType.equals(value.getStructValue().getTypeName()))) {
                throw new IllegalArgumentException("Function '" + function.getName() + "' " + role + " "
                        + (i + 1) + " expects '" + expectedType + "', got '" + value.getTypeName() + "'.");
            }
            if (value.isStruct() != (expected.getRegisterBank() == null)) {
                throw new IllegalArgumentException("Function '" + function.getName() + "' " + role + " "
                        + (i + 1) + " has incompatible register representation for '" + expectedType + "'.");
            }
            if (value.isStruct() && !value.getStructValue().getTypeName().equals(expectedType)) {
                throw new IllegalArgumentException("Function '" + function.getName() + "' " + role + " "
                        + (i + 1) + " expects struct '" + expectedType + "'.");
            }
        }
    }

}
