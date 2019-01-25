package com.dna.util;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;

public interface PipelineImportOptions extends DataflowPipelineOptions {
    @Description("Table Schema String")
    String getSchemaString();

    void setSchemaString(String value);

    @Description("Data Set")
    String getDataSet();

    void setDataSet(String value);

    @Description("Table Name")
    String getTableName();

    void setTableName(String value);

}
