package org.clnlang.compreg.commands;

import org.clnlang.compreg.Memory;

public final class ArrayIndexOffsets implements Command {

    private final int[] indexOffsets;
    private final int[] dimensions;
    private final int targetOffset;

    public ArrayIndexOffsets(int[] indexOffsets, int[] dimensions, int targetOffset) {
        if (indexOffsets.length != dimensions.length) {
            throw new IllegalArgumentException("An index is required for every array dimension.");
        }
        this.indexOffsets = indexOffsets.clone();
        this.dimensions = dimensions.clone();
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        long flatOffset = 0;
        for (int dimension = 0; dimension < dimensions.length; dimension++) {
            long index = memory.getInt(indexOffsets[dimension]);
            int dimensionLength = dimensions[dimension];
            if (index < 0 || index >= dimensionLength) {
                if (dimensions.length == 1) {
                    throw new IndexOutOfBoundsException("Array index " + index
                            + " out of bounds for length " + dimensionLength + ".");
                }
                throw new IndexOutOfBoundsException("Array index " + index + " out of bounds for dimension "
                        + dimension + " with length " + dimensionLength + ".");
            }
            flatOffset = flatOffset * dimensionLength + index;
        }
        memory.setInt(targetOffset, flatOffset);
    }
}