package org.clnlang.compile.types;

import java.math.RoundingMode;

/**
 * Holds precision and rounding mode configuration for decimal types.
 * Enables support for dec(precision) and dec(precision, roundingMode) syntax.
 */
public class DecimalTypeInfo {
    private final int precision;
    private final RoundingMode roundingMode;
    
    /**
     * Default decimal type (no precision constraint)
     */
    public static final DecimalTypeInfo DEFAULT = new DecimalTypeInfo(-1, null);
    
    /**
     * Create a decimal type with precision constraint
     * @param precision Number of decimal places (-1 for unlimited)
     * @param roundingMode Rounding mode (null for default HALF_UP)
     */
    public DecimalTypeInfo(int precision, RoundingMode roundingMode) {
        this.precision = precision;
        this.roundingMode = roundingMode != null ? roundingMode : RoundingMode.HALF_UP;
    }
    
    /**
     * Create a decimal type with only precision constraint (uses HALF_UP)
     */
    public DecimalTypeInfo(int precision) {
        this(precision, RoundingMode.HALF_UP);
    }
    
    public int getPrecision() {
        return precision;
    }
    
    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
    
    public boolean hasPrecision() {
        return precision >= 0;
    }
    
    /**
     * Apply this type's precision and rounding to a BigDecimal value
     */
    public java.math.BigDecimal applyConstraints(java.math.BigDecimal value) {
        if (value == null || !hasPrecision()) {
            return value;
        }
        return value.setScale(precision, roundingMode);
    }
    
    @Override
    public String toString() {
        if (!hasPrecision()) {
            return "dec";
        }
        return "dec(" + precision + ", " + roundingMode + ")";
    }
}
