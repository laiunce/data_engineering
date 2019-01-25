package com.dna.util;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps the table schema to get a table field schema from a field name.
 * E.g.: tableSchemaWapper.get("mycolumn")
 *
 * @author Cristian Laiun

 */
public class TableSchemaWrapper {

    private Map<String, TableFieldSchema> tableFieldSchemaMap;
    private TableSchema tableSchema;

    public TableSchemaWrapper(TableSchema tableSchema) {
        this.tableSchema = tableSchema;

        this.tableFieldSchemaMap = new HashMap<>();
        List<TableFieldSchema> fieldSchemaArray = tableSchema.getFields();
        for (int i = 0; i < fieldSchemaArray.size(); i++) {
            TableFieldSchema tableFieldSchema = fieldSchemaArray.get(i);
            tableFieldSchemaMap.put(tableFieldSchema.getName().toUpperCase(), tableFieldSchema);
        }
    }

    public TableFieldSchema get(String name) {
        return tableFieldSchemaMap.get(name.toUpperCase());
    }

 /*
    public TableSchemaWrapper() {
        tableSchema = new TableFieldSchema();
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    public TableSchemaWrapper set(String name, BqDataType type) {
        TableFieldSchema tableFieldSchema = new TableFieldSchema();
        tableFieldSchema.setName(name).setType(type.getString());
        tableSchema.getFields().add(tableFieldSchema);

        tableFieldSchemaMap.put(name.toUpperCase(), tableFieldSchema);

        return this;
    }
 */

}
