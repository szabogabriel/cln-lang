package org.clnlang.compreg.compiler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.clnlang.compile.types.DecimalTypeInfo;

final class StructLayout {

    static final class Field extends org.clnlang.compreg.runtime.StructValue.FieldLayout {

        Field(String name, String type, boolean mutable, DecimalTypeInfo decimalTypeInfo) {
            super(name, type, mutable, decimalTypeInfo);
        }
    }

    final Map<String, Field> fields = new LinkedHashMap<>();
}
