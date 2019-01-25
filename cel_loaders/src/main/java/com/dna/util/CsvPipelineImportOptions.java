package com.dna.util;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

public interface CsvPipelineImportOptions extends PipelineImportOptions {
    @Description("Path of the files to read from")
    String getSourceUris();

    void setSourceUris(String value);

    @Description("Leading rows to skip")
    @Default.Integer(0) //ignore heading row by default
    Integer getSkipLeadingRows();

    void setSkipLeadingRows(Integer value);

}
