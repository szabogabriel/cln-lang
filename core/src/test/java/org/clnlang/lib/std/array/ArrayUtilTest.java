package org.clnlang.lib.std.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ArrayUtil standard library class.
 */
class ArrayUtilTest {

    // ========== newArray ==========

    @Test
    void testNewArray_zero() {
        List<Object> arr = ArrayUtil.newArray(0);
        assertNotNull(arr);
        assertEquals(0, arr.size());
    }

    @Test
    void testNewArray_positive() {
        List<Object> arr = ArrayUtil.newArray(5);
        assertEquals(5, arr.size());
        for (Object element : arr) {
            assertNull(element);
        }
    }

    @Test
    void testNewArray_negative() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray(-1));
    }

    // ========== newArray2D ==========

    @Test
    void testNewArray2D_basic() {
        List<Object> matrix = ArrayUtil.newArray2D(3, 4);
        assertNotNull(matrix);
        assertEquals(3, matrix.size());
        for (Object row : matrix) {
            assertTrue(row instanceof List);
            @SuppressWarnings("unchecked")
            List<Object> rowList = (List<Object>) row;
            assertEquals(4, rowList.size());
            for (Object cell : rowList) {
                assertNull(cell);
            }
        }
    }

    @Test
    void testNewArray2D_zero_rows() {
        List<Object> matrix = ArrayUtil.newArray2D(0, 5);
        assertEquals(0, matrix.size());
    }

    @Test
    void testNewArray2D_zero_cols() {
        List<Object> matrix = ArrayUtil.newArray2D(3, 0);
        assertEquals(3, matrix.size());
        for (Object row : matrix) {
            @SuppressWarnings("unchecked")
            List<Object> rowList = (List<Object>) row;
            assertEquals(0, rowList.size());
        }
    }

    @Test
    void testNewArray2D_rows_are_independent() {
        List<Object> matrix = ArrayUtil.newArray2D(2, 3);
        // Modifying one row should not affect another
        @SuppressWarnings("unchecked")
        List<Object> row0 = (List<Object>) matrix.get(0);
        @SuppressWarnings("unchecked")
        List<Object> row1 = (List<Object>) matrix.get(1);
        row0.set(0, 42L);
        assertNull(row1.get(0));
    }

    @Test
    void testNewArray2D_negative_dimensions() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray2D(-1, 3));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray2D(3, -1));
    }

    // ========== newArray3D ==========

    @Test
    void testNewArray3D_basic() {
        List<Object> cube = ArrayUtil.newArray3D(2, 3, 4);
        assertNotNull(cube);
        assertEquals(2, cube.size());
        for (Object plane : cube) {
            assertTrue(plane instanceof List);
            @SuppressWarnings("unchecked")
            List<Object> planeList = (List<Object>) plane;
            assertEquals(3, planeList.size());
            for (Object row : planeList) {
                assertTrue(row instanceof List);
                @SuppressWarnings("unchecked")
                List<Object> rowList = (List<Object>) row;
                assertEquals(4, rowList.size());
            }
        }
    }

    @Test
    void testNewArray3D_cells_are_null() {
        List<Object> cube = ArrayUtil.newArray3D(2, 2, 2);
        @SuppressWarnings("unchecked")
        List<Object> plane = (List<Object>) cube.get(0);
        @SuppressWarnings("unchecked")
        List<Object> row = (List<Object>) plane.get(0);
        assertNull(row.get(0));
    }

    @Test
    void testNewArray3D_planes_are_independent() {
        List<Object> cube = ArrayUtil.newArray3D(2, 2, 2);
        @SuppressWarnings("unchecked")
        List<Object> plane0 = (List<Object>) cube.get(0);
        @SuppressWarnings("unchecked")
        List<Object> plane1 = (List<Object>) cube.get(1);
        @SuppressWarnings("unchecked")
        List<Object> row0_0 = (List<Object>) plane0.get(0);
        @SuppressWarnings("unchecked")
        List<Object> row1_0 = (List<Object>) plane1.get(0);
        row0_0.set(0, 99L);
        assertNull(row1_0.get(0));
    }

    @Test
    void testNewArray3D_negative_dimensions() {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray3D(-1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray3D(2, -1, 2));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtil.newArray3D(2, 2, -1));
    }

    // ========== deepCopy ==========

    @Test
    void testDeepCopy_flat_array() {
        List<Object> original = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<Object> copy = ArrayUtil.deepCopy(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
        // Modifying copy should not affect original
        copy.set(0, 99L);
        assertEquals(1L, original.get(0));
    }

    @Test
    void testDeepCopy_2d_array() {
        List<Object> row0 = new ArrayList<>(Arrays.asList(1L, 2L));
        List<Object> row1 = new ArrayList<>(Arrays.asList(3L, 4L));
        List<Object> original = new ArrayList<>(Arrays.asList(row0, row1));

        List<Object> copy = ArrayUtil.deepCopy(original);

        // Outer list is a different object
        assertNotSame(original, copy);
        // Inner lists are different objects (deep copy)
        @SuppressWarnings("unchecked")
        List<Object> copyRow0 = (List<Object>) copy.get(0);
        assertNotSame(row0, copyRow0);

        // Modifying inner element of copy should NOT affect original
        copyRow0.set(0, 99L);
        assertEquals(1L, row0.get(0));
    }

    @Test
    void testDeepCopy_3d_array() {
        List<Object> cell = new ArrayList<>(Arrays.asList(1L, 2L));
        List<Object> row = new ArrayList<>(Arrays.asList(cell));
        List<Object> plane = new ArrayList<>(Arrays.asList(row));
        List<Object> original = new ArrayList<>(Arrays.asList(plane));

        List<Object> copy = ArrayUtil.deepCopy(original);

        // Navigate to innermost cell in the copy
        @SuppressWarnings("unchecked")
        List<Object> copyPlane = (List<Object>) copy.get(0);
        @SuppressWarnings("unchecked")
        List<Object> copyRow = (List<Object>) copyPlane.get(0);
        @SuppressWarnings("unchecked")
        List<Object> copyCell = (List<Object>) copyRow.get(0);

        // All levels are different objects
        assertNotSame(plane, copyPlane);
        assertNotSame(row, copyRow);
        assertNotSame(cell, copyCell);

        // Mutating the deepest level does not affect the original
        copyCell.set(0, 99L);
        assertEquals(1L, cell.get(0));
    }

    @Test
    void testDeepCopy_null() {
        List<Object> copy = ArrayUtil.deepCopy(null);
        assertNotNull(copy);
        assertEquals(0, copy.size());
    }

    @Test
    void testDeepCopy_empty() {
        List<Object> copy = ArrayUtil.deepCopy(new ArrayList<>());
        assertNotNull(copy);
        assertEquals(0, copy.size());
    }

    /** Build a minimal struct Map the same way StructLiteralExprImpl does. */
    private static Map<String, Object> makeStruct(String type, Object... keyValues) {
        Map<String, Object> m = new HashMap<>();
        m.put("__type__", type);
        for (int i = 0; i < keyValues.length; i += 2) {
            m.put((String) keyValues[i], keyValues[i + 1]);
        }
        return m;
    }

    @Test
    void testDeepCopy_struct_array() {
        Map<String, Object> s0 = makeStruct("Point", "x", 1L, "y", 2L);
        Map<String, Object> s1 = makeStruct("Point", "x", 3L, "y", 4L);
        List<Object> original = new ArrayList<>(Arrays.asList(s0, s1));

        List<Object> copy = ArrayUtil.deepCopy(original);

        // Outer list is new
        assertNotSame(original, copy);
        // Each struct Map is a new object
        @SuppressWarnings("unchecked")
        Map<String, Object> copyS0 = (Map<String, Object>) copy.get(0);
        assertNotSame(s0, copyS0);

        // Mutating the copy's struct field must NOT affect the original
        copyS0.put("x", 99L);
        assertEquals(1L, s0.get("x"));
        assertEquals(99L, copyS0.get("x"));
    }

    @Test
    void testDeepCopy_struct_preserves_type_field() {
        Map<String, Object> s = makeStruct("MyStruct", "val", 42L);
        List<Object> original = new ArrayList<>(Arrays.asList(s));

        List<Object> copy = ArrayUtil.deepCopy(original);

        @SuppressWarnings("unchecked")
        Map<String, Object> copiedStruct = (Map<String, Object>) copy.get(0);
        assertEquals("MyStruct", copiedStruct.get("__type__"));
        assertEquals(42L, copiedStruct.get("val"));
    }

    @Test
    void testDeepCopy_struct_with_array_field() {
        // A struct whose field is itself an array: { __type__: "S", arr: [1, 2] }
        List<Object> innerArr = new ArrayList<>(Arrays.asList(10L, 20L));
        Map<String, Object> s = makeStruct("S", "arr", innerArr);
        List<Object> original = new ArrayList<>(Arrays.asList(s));

        List<Object> copy = ArrayUtil.deepCopy(original);
        @SuppressWarnings("unchecked")
        Map<String, Object> copiedS = (Map<String, Object>) copy.get(0);
        @SuppressWarnings("unchecked")
        List<Object> copiedInner = (List<Object>) copiedS.get("arr");

        assertNotSame(innerArr, copiedInner);
        copiedInner.set(0, 99L);
        assertEquals(10L, innerArr.get(0));
    }

    @Test
    void testDeepCopy_2d_struct_array() {
        Map<String, Object> s00 = makeStruct("Point", "x", 1L, "y", 2L);
        List<Object> row0 = new ArrayList<>(Arrays.asList(s00));
        List<Object> original = new ArrayList<>(Arrays.asList(row0));

        List<Object> copy = ArrayUtil.deepCopy(original);

        @SuppressWarnings("unchecked")
        List<Object> copyRow0 = (List<Object>) copy.get(0);
        @SuppressWarnings("unchecked")
        Map<String, Object> copyS00 = (Map<String, Object>) copyRow0.get(0);

        assertNotSame(row0, copyRow0);
        assertNotSame(s00, copyS00);

        copyS00.put("x", 99L);
        assertEquals(1L, s00.get("x"));
    }

    // ========== copy (shallow) ==========

    @Test
    void testCopy_shallow_behavior_with_2d() {
        List<Object> row0 = new ArrayList<>(Arrays.asList(1L, 2L));
        List<Object> original = new ArrayList<>(Arrays.asList(row0));

        List<Object> shallow = ArrayUtil.copy(original);
        assertNotSame(original, shallow);

        // Inner list is the SAME object (shallow copy)
        @SuppressWarnings("unchecked")
        List<Object> shallowRow0 = (List<Object>) shallow.get(0);
        // Modifying via copy affects original (shallow semantics)
        shallowRow0.set(0, 99L);
        assertEquals(99L, row0.get(0));
    }

    // ========== length ==========

    @Test
    void testLength() {
        assertEquals(0, ArrayUtil.length(null));
        assertEquals(0, ArrayUtil.length(new ArrayList<>()));
        assertEquals(3, ArrayUtil.length(new ArrayList<>(Arrays.asList(1L, 2L, 3L))));
    }

    // ========== isEmpty ==========

    @Test
    void testIsEmpty() {
        assertTrue(ArrayUtil.isEmpty(null));
        assertTrue(ArrayUtil.isEmpty(new ArrayList<>()));
        assertFalse(ArrayUtil.isEmpty(new ArrayList<>(Arrays.asList(1L))));
    }

    // ========== indexOf / lastIndexOf / contains ==========

    @Test
    void testIndexOf() {
        List<Object> arr = new ArrayList<>(Arrays.asList(10L, 20L, 30L, 20L));
        assertEquals(1, ArrayUtil.indexOf(arr, 20L));
        assertEquals(-1, ArrayUtil.indexOf(arr, 99L));
        assertEquals(-1, ArrayUtil.indexOf(null, 10L));
    }

    @Test
    void testLastIndexOf() {
        List<Object> arr = new ArrayList<>(Arrays.asList(10L, 20L, 30L, 20L));
        assertEquals(3, ArrayUtil.lastIndexOf(arr, 20L));
        assertEquals(-1, ArrayUtil.lastIndexOf(arr, 99L));
    }

    @Test
    void testContains() {
        List<Object> arr = new ArrayList<>(Arrays.asList(10L, 20L, 30L));
        assertTrue(ArrayUtil.contains(arr, 20L));
        assertFalse(ArrayUtil.contains(arr, 99L));
    }

    // ========== fill / reverse ==========

    @Test
    void testFill() {
        List<Object> arr = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        ArrayUtil.fill(arr, 0L);
        for (Object e : arr) {
            assertEquals(0L, e);
        }
    }

    @Test
    void testReverse() {
        List<Object> arr = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        ArrayUtil.reverse(arr);
        assertEquals(Arrays.asList(3L, 2L, 1L), arr);
    }

    // ========== equals ==========

    @Test
    void testEquals_1d() {
        List<Object> a = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<Object> b = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        assertTrue(ArrayUtil.equals(a, b));
    }

    @Test
    void testEquals_2d() {
        List<Object> a = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(1L, 2L)),
            new ArrayList<>(Arrays.asList(3L, 4L))
        ));
        List<Object> b = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(1L, 2L)),
            new ArrayList<>(Arrays.asList(3L, 4L))
        ));
        assertTrue(ArrayUtil.equals(a, b));

        // Modify b's inner array and check it becomes unequal
        @SuppressWarnings("unchecked")
        List<Object> bRow0 = (List<Object>) b.get(0);
        bRow0.set(0, 99L);
        assertFalse(ArrayUtil.equals(a, b));
    }

    // ========== concat ==========

    @Test
    void testConcat() {
        List<Object> a = new ArrayList<>(Arrays.asList(1L, 2L));
        List<Object> b = new ArrayList<>(Arrays.asList(3L, 4L));
        List<Object> result = ArrayUtil.concat(a, b);
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L), result);
    }

    // ========== slice ==========

    @Test
    void testSlice() {
        List<Object> arr = new ArrayList<>(Arrays.asList(10L, 20L, 30L, 40L, 50L));
        List<Object> result = ArrayUtil.slice(arr, 1, 3);
        assertEquals(Arrays.asList(20L, 30L, 40L), result);
    }
}
