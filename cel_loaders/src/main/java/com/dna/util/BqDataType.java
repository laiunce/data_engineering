package com.dna.util;

/**
 * @author Cristian Laiun

 */
public enum BqDataType {
    STRING("string"),
    INTEGER("integer"),
    FLOAT("float"),
    DATE("date"),
    BOOLEAN("boolean");

    private String name;

    BqDataType(String name) {
        this.name = name;
    }

    public String getString() {
        return name;
    }

    public static final BqDataType from(String type) {
        for (BqDataType bqDataType : BqDataType.values()) {
            if (bqDataType.name.equalsIgnoreCase(type)) {
                return bqDataType;
            }
        }

        return null;
    }
}
