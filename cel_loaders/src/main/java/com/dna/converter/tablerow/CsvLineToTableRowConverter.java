package com.dna.converter.tablerow;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.converter.BqStringConverterHandler;
import com.dna.converter.StringConverter;
import com.dna.exception.BqParseException;
import com.dna.util.CsvParserSerializable;
import com.dna.util.StringUtil;
import com.dna.util.TableColumnSchema;
import com.dna.util.TableSchemaMap;
import com.google.api.services.bigquery.model.TableRow;

/**
 * @author Cristian Laiun

 */
public class CsvLineToTableRowConverter extends DefaultTableRowConverter<String> {

    private static final long serialVersionUID = -2331448588341394931L;

    private static final Logger LOG = LoggerFactory.getLogger(CsvLineToTableRowConverter.class);

    private StringConverter<String[]> lineConverter;

    public CsvLineToTableRowConverter(TableSchemaMap tableSchemaMap, BqStringConverterHandler bqStringConverterHandler, StringConverter<String[]> lineConverter) {
        super(tableSchemaMap, bqStringConverterHandler);
        this.lineConverter = lineConverter != null ? lineConverter : CsvParserSerializable.CSV_PARSER::parseLine;
    }

    @Override
    public TableRow convert(String input) throws BqParseException {

        String values[] = parse(input);

        List<TableColumnSchema> tableSchemaValues = tableSchemaMap.values();
        if (values.length < tableSchemaValues.size()) {
            LOG.warn("There are " + tableSchemaValues.size() + " columns, but " + values.length + " values in [" + input + "]");
        }
        int size = Math.min(tableSchemaValues.size(), values.length);

        TableRow tableRow = new TableRow();

        for (int i = 0; i < size; i++) {
            TableColumnSchema tableColumnSchema = tableSchemaValues.get(i);
            String value = StringUtil.toEmptyIfNull(values[i]);
            setTableRowValue(tableRow, tableColumnSchema, value);
        }

        return tableRow;
    }

    protected String[] parse(String line) {
        try {
            return lineConverter.convert(line);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}