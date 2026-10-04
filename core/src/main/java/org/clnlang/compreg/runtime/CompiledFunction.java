package org.clnlang.compreg.runtime;

import java.util.List;

import org.clnlang.compile.types.DecimalTypeInfo;
import org.clnlang.compreg.Memory;
import org.clnlang.compreg.commands.Call;
import org.clnlang.compreg.commands.Command;

public final class CompiledFunction {

    @FunctionalInterface
    public interface StructuredLibraryBody {
        void execute(Memory memory, List<Call.Register> arguments, List<Call.Register> results);
    }

    public static final class Slot {
        private final String name;
        private final String type;
        private final RegisterBank registerBank;
        private final boolean mutable;
        private final DecimalTypeInfo decimalTypeInfo;
        private int offset = -1;

        public Slot(String name, String type, boolean mutable) {
            this(name, type, mutable, DecimalTypeInfo.DEFAULT);
        }

        public Slot(String name, String type, boolean mutable, DecimalTypeInfo decimalTypeInfo) {
            this.name = name;
            this.type = type;
            this.registerBank = RegisterBank.forType(type);
            this.mutable = mutable;
            this.decimalTypeInfo = decimalTypeInfo == null ? DecimalTypeInfo.DEFAULT : decimalTypeInfo;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public RegisterBank getRegisterBank() { return registerBank; }

        public boolean isMutable() {
            return mutable;
        }

        public DecimalTypeInfo getDecimalTypeInfo() {
            return decimalTypeInfo;
        }

        public int getOffset() {
            return offset;
        }

        void setOffset(int offset) {
            this.offset = offset;
        }
    }

    private final String packageName;
    private final String name;
    private final boolean exposed;
    private final List<Slot> parameters;
    private final List<Slot> returnValues;
    private Memory.FrameLayout frameLayout;
    private Command initializers;
    private Command body;
    private StructuredLibraryBody structuredLibraryBody;

    public CompiledFunction(String packageName, String name, boolean exposed,
            List<Slot> parameters, List<Slot> returnValues) {
        this.packageName = packageName == null ? "" : packageName;
        this.name = name;
        this.exposed = exposed;
        this.parameters = List.copyOf(parameters);
        this.returnValues = List.copyOf(returnValues);
        this.frameLayout = Memory.FrameLayout.empty();
        this.initializers = memory -> {
        };
        this.body = memory -> {
        };
        this.structuredLibraryBody = null;
    }

    public static CompiledFunction createLibraryFunction(String packageName, String name, boolean exposed,
            List<Slot> parameters, List<Slot> returnValues, int[] slotsByType, Command implementation) {
        if (slotsByType == null || slotsByType.length != 4) {
            throw new IllegalArgumentException("Library frame requires four typed slot counts.");
        }
        CompiledFunction function = new CompiledFunction(packageName, name, exposed, parameters, returnValues);
        int[] nextOffset = new int[4];
        assignLibraryOffsets(parameters, nextOffset);
        assignLibraryOffsets(returnValues, nextOffset);
        for (int i = 0; i < slotsByType.length; i++) {
            if (slotsByType[i] != nextOffset[i]) {
                throw new IllegalArgumentException("Library frame slot count does not match its signature.");
            }
        }
        function.setImplementation(new Memory.FrameLayout(slotsByType[0], slotsByType[1],
                slotsByType[2], slotsByType[3]), memory -> { }, implementation);
        return function;
    }

    public static CompiledFunction createStructuredLibraryFunction(String packageName, String name,
            boolean exposed, List<Slot> parameters, List<Slot> returnValues, StructuredLibraryBody body) {
        CompiledFunction function = new CompiledFunction(packageName, name, exposed, parameters, returnValues);
        function.structuredLibraryBody = body;
        return function;
    }

    private static void assignLibraryOffsets(List<Slot> slots, int[] nextOffset) {
        for (Slot slot : slots) {
            RegisterBank registerBank = slot.getRegisterBank();
            if (registerBank == null) {
                throw new IllegalArgumentException("Unsupported library slot type: " + slot.getType());
            }
            int bank = registerBank.getIndex();
            slot.setOffset(nextOffset[bank]++);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public boolean isExposed() {
        return exposed;
    }

    public List<Slot> getParameters() {
        return parameters;
    }

    public List<Slot> getReturnValues() {
        return returnValues;
    }

    public void setParameterOffset(int index, int offset) {
        parameters.get(index).setOffset(offset);
    }

    public void setReturnOffset(int index, int offset) {
        returnValues.get(index).setOffset(offset);
    }

    public void setImplementation(Memory.FrameLayout frameLayout, Command initializers, Command body) {
        this.frameLayout = frameLayout;
        this.initializers = initializers;
        this.body = body;
    }

    public Memory.FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public Command getInitializers() {
        return initializers;
    }

    public Command getBody() {
        return body;
    }

    public StructuredLibraryBody getStructuredLibraryBody() {
        return structuredLibraryBody;
    }
}
