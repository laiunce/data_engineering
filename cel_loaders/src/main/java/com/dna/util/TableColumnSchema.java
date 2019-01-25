package com.dna.util;

import java.io.Serializable;

/**
 * Similar to TableFieldSchema, but it uses a BqDataType rather than a string
 *
 * @author Cristian Laiun

 */
public class TableColumnSchema implements Serializable {

    private final String name;
    private final BqDataType type;

    public TableColumnSchema(String name, BqDataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public BqDataType getType() {
        return type;
    }
}
