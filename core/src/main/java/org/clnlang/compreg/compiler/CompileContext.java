package org.clnlang.compreg.compiler;

import java.util.HashMap;
import java.util.Map;

import org.clnlang.compile.types.DecimalTypeInfo;

public class CompileContext {

    public static class Offsets {

        private final Map<String, Integer> intOffsets = new HashMap<>();

        private final Map<String, Integer> decOffsets = new HashMap<>();

        private final Map<String, Integer> stringOffsets = new HashMap<>();

        private final Map<String, Integer> booleanOffsets = new HashMap<>();
        private final Map<String, String> types = new HashMap<>();
        private final Map<String, Boolean> mutability = new HashMap<>();
        private final Map<String, Boolean> exposure = new HashMap<>();
        private final Map<String, DecimalTypeInfo> decimalTypeInfos = new HashMap<>();
        private final Map<String, int[]> arrayDimensions = new HashMap<>();

        public void registerType(String name, String type) {
            types.put(name, type);
        }

        public String getType(String name) {
            return types.get(name);
        }

        public void registerGlobalFlags(String name, boolean mutable, boolean exposed) {
            mutability.put(name, mutable);
            exposure.put(name, exposed);
        }

        public boolean isMutable(String name) {
            return mutability.getOrDefault(name, false);
        }

        public boolean isExposed(String name) {
            return exposure.getOrDefault(name, false);
        }

        public void registerDecimalTypeInfo(String name, DecimalTypeInfo decimalTypeInfo) {
            decimalTypeInfos.put(name, decimalTypeInfo);
        }

        public DecimalTypeInfo getDecimalTypeInfo(String name) {
            return decimalTypeInfos.getOrDefault(name, DecimalTypeInfo.DEFAULT);
        }

        public void registerArrayDimensions(String name, int[] dimensions) {
            arrayDimensions.put(name, dimensions.clone());
        }

        public int getArrayLength(String name) {
            int[] dimensions = arrayDimensions.get(name);
            if (dimensions == null) {
                return -1;
            }
            int length = 1;
            for (int dimension : dimensions) {
                length = Math.multiplyExact(length, dimension);
            }
            return length;
        }

        public int[] getArrayDimensions(String name) {
            int[] dimensions = arrayDimensions.get(name);
            return dimensions == null ? null : dimensions.clone();
        }

        public void registerIntOffset(String name, int offset) {
            intOffsets.put(name, offset);
        }

        public void registerDecOffset(String name, int offset) {
            decOffsets.put(name, offset);
        }

        public void registerStringOffset(String name, int offset) {
            stringOffsets.put(name, offset);
        }

        public void registerBooleanOffset(String name, int offset) {
            booleanOffsets.put(name, offset);
        }

        public int getIntOffset(String name) {
            return intOffsets.getOrDefault(name, -1);
        }

        public int getDecOffset(String name) {
            return decOffsets.getOrDefault(name, -1);
        }

        public int getStringOffset(String name) {
            return stringOffsets.getOrDefault(name, -1);
        }

        public int getBooleanOffset(String name) {
            return booleanOffsets.getOrDefault(name, -1);
        }
    }

    private final Offsets globalOffsets = new Offsets();

    private final Map<String, Offsets> functionOffsets = new HashMap<>();

    public Offsets getGlobalOffsets() {
        return globalOffsets;
    }

    public Offsets getFunctionOffsets(String packageName, String functionName) {
        return functionOffsets.computeIfAbsent(((packageName != null) ? packageName + "." : "") + functionName,
                k -> new Offsets());
    }

}
