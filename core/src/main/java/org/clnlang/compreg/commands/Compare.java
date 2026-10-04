package org.clnlang.compreg.commands;

import java.math.BigDecimal;

import org.clnlang.compreg.Memory;

public final class Compare implements Command {

    public enum Operator {
        EQ, NEQ, LT, LTE, GT, GTE
    }

    private final Operator operator;
    private final String leftType;
    private final String rightType;
    private final int leftOffset;
    private final int rightOffset;
    private final int targetOffset;

    public Compare(Operator operator, String operandType, int leftOffset, int rightOffset, int targetOffset) {
        this(operator, operandType, operandType, leftOffset, rightOffset, targetOffset);
    }

    public Compare(Operator operator, String leftType, String rightType,
            int leftOffset, int rightOffset, int targetOffset) {
        this.operator = operator;
        this.leftType = leftType;
        this.rightType = rightType;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public void execute(Memory memory) {
        int comparison;
        if (isNumeric(leftType) && isNumeric(rightType)) {
            BigDecimal leftValue = leftType.equals("dec") ? memory.getDec(leftOffset)
                : BigDecimal.valueOf(memory.getInt(leftOffset));
            BigDecimal rightValue = rightType.equals("dec") ? memory.getDec(rightOffset)
                : BigDecimal.valueOf(memory.getInt(rightOffset));
            comparison = leftValue.compareTo(rightValue);
        } else if (leftType.equals(rightType) && leftType.equals("bool")) {
            comparison = Boolean.compare(memory.getBool(leftOffset), memory.getBool(rightOffset));
        } else if (leftType.equals(rightType) && leftType.equals("string")) {
            comparison = memory.getStr(leftOffset).compareTo(memory.getStr(rightOffset));
        } else {
            throw new IllegalStateException("Unsupported comparison types: " + leftType + ", " + rightType);
        }
        boolean result = switch (operator) {
            case EQ -> comparison == 0;
            case NEQ -> comparison != 0;
            case LT -> comparison < 0;
            case LTE -> comparison <= 0;
            case GT -> comparison > 0;
            case GTE -> comparison >= 0;
        };
        memory.setBool(targetOffset, result);
    }

    private static boolean isNumeric(String type) {
        return type.equals("int") || type.equals("dec");
    }
}
