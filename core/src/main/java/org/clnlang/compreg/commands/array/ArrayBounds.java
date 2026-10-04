package org.clnlang.compreg.commands.array;

final class ArrayBounds {

    private ArrayBounds() { }

    static int checkedIndex(long index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Array index " + index
                    + " out of bounds for length " + length + ".");
        }
        return (int) index;
    }
}