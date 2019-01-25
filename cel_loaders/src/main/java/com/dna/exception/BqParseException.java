package com.dna.exception;

import com.dna.util.TableColumnSchema;

import java.text.ParseException;

/**
 * @author Cristian Laiun

 */
public class BqParseException extends ParseException {
        Object value;
        String type;
        String columnName;

        public BqParseException(Object value, String type, String columnName) {
            super("", 0);
            this.columnName = columnName;
            this.value = value;
            this.type = type;
        }

    public BqParseException(Object value, TableColumnSchema tableColumnSchema) {
        this(value, tableColumnSchema.getType().getString(), tableColumnSchema.getName());
    }

        public Object getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public String getColumnName() {
            return columnName;
        }
    }

