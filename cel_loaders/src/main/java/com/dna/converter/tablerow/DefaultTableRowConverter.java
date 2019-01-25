package com.dna.converter.tablerow;

import com.dna.converter.BqStringConverterHandler;
import com.dna.exception.BqParseException;
import com.dna.util.TableColumnSchema;
import com.dna.util.TableSchemaMap;
import com.dna.valueprovider.ValueProvider;
import com.google.api.services.bigquery.model.TableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun

 */
public abstract class DefaultTableRowConverter<I> implements TableRowConverter<I> {

    private static final long serialVersionUID = -2331448588341394931L;

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTableRowConverter.class);

    protected final TableSchemaMap tableSchemaMap;

    protected final BqStringConverterHandler bqStringConverterHandler;

    public DefaultTableRowConverter(TableSchemaMap tableSchemaMap, BqStringConverterHandler bqStringConverterHandler) {
        this.tableSchemaMap = tableSchemaMap;
        this.bqStringConverterHandler = bqStringConverterHandler != null ? bqStringConverterHandler : new BqStringConverterHandler.Builder().build();
    }

    @Override
    public abstract TableRow convert(I input) throws BqParseException;

    protected final void setTableRowValue(TableRow tableRow, TableColumnSchema tableColumnSchema, Object value) throws BqParseException {
        try {
            tableRow.set(tableColumnSchema.getName(), value);
        } catch (Exception e) {
            throw new BqParseException(value, tableColumnSchema);
        }
    }

    protected final void setTableRowValue(TableRow tableRow, TableColumnSchema tableColumnSchema, String value) throws BqParseException {
        try {
            tableRow.set(tableColumnSchema.getName(), bqStringConverterHandler.parseValue(value, tableColumnSchema.getType()));
        } catch (Exception e) {
            throw new BqParseException(value, tableColumnSchema);
        }
    }

    public class TableRowSetter {
        TableRow tableRow;

        public TableRowSetter(TableRow tableRow) {
            this.tableRow = tableRow;
        }

        public TableRowSetter set(String columnName, ValueProvider valueProvider) throws BqParseException {
            return set(columnName, valueProvider.get());
        }

        public TableRowSetter set(String columnName, Object value) throws BqParseException {
            TableColumnSchema tableColumnSchema = tableSchemaMap.get(columnName.toUpperCase());
            if (tableColumnSchema == null) {
                throw new IllegalArgumentException("Column name doesn't exist in schema " + columnName);
            }
            setTableRowValue(tableRow, tableColumnSchema, value);

            return this;
        }

        public TableRowSetter set(String columnName, String value) throws BqParseException {
            TableColumnSchema tableColumnSchema = tableSchemaMap.get(columnName.toUpperCase());
            if (tableColumnSchema == null) {
                throw new IllegalArgumentException("Column name doesn't exist in schema " + columnName);
            }
            setTableRowValue(tableRow, tableColumnSchema, value);

            return this;
        }
    }
}