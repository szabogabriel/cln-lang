package org.clnlang.interpreted.compile.expression;

import org.clnlang.interpreted.compile.CompiledExpr;
import org.clnlang.interpreted.runtime.context.ExecutionContext;

import java.util.List;

/**
 * Compiled representation of an index access expression.
 * Supports accessing elements in arrays and strings.
 * Example: arr[0], str[2]
 */
public class IndexAccessExprImpl implements CompiledExpr {
    private CompiledExpr array;
    private CompiledExpr index;

    public IndexAccessExprImpl(CompiledExpr array, CompiledExpr index) {
        this.array = array;
        this.index = index;
    }

    public CompiledExpr getArray() {
        return array;
    }

    public CompiledExpr getIndex() {
        return index;
    }

    @Override
    public Object evaluate(ExecutionContext context) throws Exception {
        Object arrayObj = array.evaluate(context);
        Object indexObj = index.evaluate(context);
        
        // Index must be an integer
        if (!(indexObj instanceof Long)) {
            throw new RuntimeException("Array index must be an integer, got: " + 
                (indexObj == null ? "null" : indexObj.getClass().getSimpleName()));
        }
        
        long indexValue = (Long) indexObj;
        
        // Handle array access
        if (arrayObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) arrayObj;
            
            // Check bounds
            if (indexValue < 0 || indexValue >= list.size()) {
                throw new RuntimeException("Array index out of bounds: " + indexValue + 
                    " (array size: " + list.size() + ")");
            }
            
            return list.get((int) indexValue);
        }
        
        // Handle string access (treat string as array of characters)
        if (arrayObj instanceof String) {
            String str = (String) arrayObj;
            
            // Check bounds
            if (indexValue < 0 || indexValue >= str.length()) {
                throw new RuntimeException("String index out of bounds: " + indexValue + 
                    " (string length: " + str.length() + ")");
            }
            
            // Return single character as a string
            return String.valueOf(str.charAt((int) indexValue));
        }
        
        throw new RuntimeException("Cannot index into non-array type: " + 
            (arrayObj == null ? "null" : arrayObj.getClass().getSimpleName()));
    }
}
