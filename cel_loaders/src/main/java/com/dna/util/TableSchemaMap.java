package com.dna.util;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Similar to TableSchema, but preserves the order in which is values are added, also values are
 * of TableColumnSchema tyoe, to be able to get the BQType,
 * E.g.: tableSchemaWapper.get("mycolumn")
 *
 * @author Cristian Laiun

 */
public class TableSchemaMap extends LinkedHashMap<String, TableColumnSchema> implements Serializable {

    private static final long serialVersionUID = 5191533729443258874L;

    @Override
    public List<TableColumnSchema> values() {
        return new ArrayList<>(super.values());
    }

    public TableSchema toTableSchema() {
        TableSchema tableSchema = new TableSchema();
        List<TableFieldSchema> tableFieldSchemaList = new ArrayList<>();
        for (TableColumnSchema tableColumnSchema : super.values()) {
            tableFieldSchemaList.add(new TableFieldSchema().
                    setName(tableColumnSchema.getName()).
                    setType(tableColumnSchema.getType().getString()));
        }
        return tableSchema.setFields(tableFieldSchemaList);
    }

    public static TableSchemaMap from(String schemaString) {
        TableSchemaMap tableSchema = new TableSchemaMap();
        String[] schemaStringArray = schemaString.split(",");

        for (String fieldSchemaString : schemaStringArray) {
            try {
                String[] fieldSchemaStringArray = fieldSchemaString.split(":");
                String name = BigQueryUtil.sanitizeFieldName(StringUtil.trim(fieldSchemaStringArray[0]));
                String type = StringUtil.trim(fieldSchemaStringArray[1]);
                if (StringUtil.isEmpty(name) || StringUtil.isEmpty(type)) {
                    throw new IllegalArgumentException("Name or type is empty");
                }
                if (BqDataType.from(type) == null) {
                    throw new IllegalArgumentException("Invalid type " + type);
                }
                if (fieldSchemaStringArray.length > 2) {
                    throw new IllegalArgumentException("Wrong number of fields");
                }
                tableSchema.put(name, new TableColumnSchema(name, BqDataType.from(type)));
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Missing argument " + fieldSchemaString);
            }
        }

        return tableSchema;
    }

}
