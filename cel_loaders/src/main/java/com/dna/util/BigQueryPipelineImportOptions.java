package com.dna.util;

import org.apache.beam.sdk.options.Description;

public interface BigQueryPipelineImportOptions extends PipelineImportOptions {
    @Description("Table Query")
    String getQuery();

    void setQuery(String value);
}
