package com.dna.app.etl;

import com.dna.app.config.Configurable;
import com.dna.app.config.DnaAppConfig;
import com.dna.app.config.DnaAppConstants;
import com.dna.converter.tablerow.CsvEmptyColValidator;
import com.dna.converter.tablerow.TableRowConverter;
import com.dna.exception.BqParseException;
import com.dna.util.DateRange;
import com.dna.util.PipelineImportOptions;
import com.dna.util.Range;
import com.dna.util.TableSchemaMap;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.bigquery.model.TableRow;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import static org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun

 */
public abstract class BigQueryPipeline<T> implements Runnable, Configurable, Serializable {

    private static final long serialVersionUID = 5628786517218921392L;

    private static final Logger LOG = LoggerFactory.getLogger(BigQueryPipeline.class);

    public static final String DATE_REPLACE_HOLDER = "{date}";

    public abstract TableRowConverter<T> createTableRowConverter(TableSchemaMap tableSchemaMap);

    public abstract PipelineImportOptions getPipelineOptions();

    public abstract String getDateColumn();

    public boolean mustClean() {
        return false;
    }

    public boolean injectJobDateValue() {
        return false;
    }

    public boolean mustValidateValuesAndSchemaLength() {
        return true;
    }

    public CsvEmptyColValidator valEmptyCols() {
        return new CsvEmptyColValidator(false, null);
    }

    private DnaAppConfig appConfig;

    protected Boolean IMPORT_OVERRIDE = null;
    protected String DATASET_NAME_OVERRIDE = null;
    protected Boolean VALIDATE_DATE_COLUMN = true;

    @Override
    public void setAppConfig(DnaAppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public DnaAppConfig getAppConfig() {
        return appConfig;
    }

    @Override
    public void run() {

        PipelineImportOptions options = getPipelineOptions();
        LOG.info("Running etl to create {}", options.getTableName());

        DateRange savedDateRange = null;
        boolean importOverwrite = appConfig.getImportOverwrite();

        if (!importOverwrite) {
            savedDateRange = getSavedDateRange(options);
        }

        DateRange dateRange = appConfig.getDateRange();
        List<LocalDate> datesToImport = dateRange.getDifference(savedDateRange);

        String dateColumn = getDateColumn();
        if (appConfig.getReplaceData() && this.VALIDATE_DATE_COLUMN && StringUtils.isNotBlank(dateColumn)) {
            try {
                // delete any data first before importing
                String dataSet
                        = StringUtils.isBlank(this.DATASET_NAME_OVERRIDE)
                        ? options.getDataSet()
                        : this.DATASET_NAME_OVERRIDE;

                appConfig.getBigQueryService().deleteDataForDateRange(dataSet, options.getTableName(), dateColumn, dateRange.from(), dateRange.to());
                // import all dates
                datesToImport = dateRange.getAllDates();
            } catch (GoogleJsonResponseException ge) {
                if (ge.getDetails().getCode() == 404) {
                    LOG.warn("Error deleting. " + ge.getMessage());
                } else {
                    LOG.error("Error deleting from table [{}] on column [{}] with date range: [{} - {}]", options.getTableName(), dateColumn, dateRange.from(), dateRange.to(), ge);
                }
            } catch (Exception e) {
                LOG.error("Error deleting from table [{}] on column [{}] with date range: [{} - {}]", options.getTableName(), dateColumn, dateRange.from(), dateRange.to(), e);
            }
        } else {
            if (datesToImport.size() == 0) {
                LOG.info("Files have been already processed for table [{}] for dates between [{}] to [{}]", options.getTableName(), dateRange.from(), dateRange.to());

                if (this.VALIDATE_DATE_COLUMN) {
                    return;
                }
            }
        }

        TableSchemaMap tableSchemaMap = TableSchemaMap.from(options.getSchemaString());

        TableRowConverter<T> tableRowConverter = createTableRowConverter(tableSchemaMap);

        PCollection<T> pCollection = createReadDataPCollection(datesToImport);

        PCollection<TableRow> tableRowPCollection
                = pCollection.apply(
                        "Extract Cell Data",
                        ParDo.of(new TableRowDoFn<>(tableRowConverter))
                );

        if (this.IMPORT_OVERRIDE != null) {
            importOverwrite = this.IMPORT_OVERRIDE; // likely "true"
        }

        writeToBQ(tableRowPCollection, options, tableSchemaMap, importOverwrite);

        //RunUtil.doAndRetry(tableRowPCollection.getPipeline()::run);
        tableRowPCollection.getPipeline().run().waitUntilFinish();

        saveDatesView(savedDateRange, datesToImport, options);

        validateInputRecordCountVsBigQueryRecordCount(datesToImport);
    }

    public abstract PCollection<T> createReadDataPCollection(List<LocalDate> datesToImport);

    public void writeToBQ(PCollection<TableRow> tableRowPCollection, PipelineImportOptions options, TableSchemaMap tableSchemaMap, boolean importOverwrite) {

        String dataSet
                = StringUtils.isBlank(this.DATASET_NAME_OVERRIDE)
                ? options.getDataSet()
                : this.DATASET_NAME_OVERRIDE;

        String fullTableName = dataSet + "." + options.getTableName();

        LOG.info(
                importOverwrite
                        ? "WriteDisposition WRITE_TRUNCATE"
                        : "WriteDisposition WRITE_APPEND"
        );

        tableRowPCollection.apply(
                "Write to table",
                BigQueryIO.writeTableRows().to(fullTableName)
                        .withCreateDisposition(CREATE_IF_NEEDED).withSchema(tableSchemaMap.toTableSchema()).
                        withWriteDisposition(importOverwrite
                                ? BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE
                                : BigQueryIO.Write.WriteDisposition.WRITE_APPEND).withoutValidation());
    }

    public void saveDatesView(Range<LocalDate> dateRange, List<LocalDate> datesToImport, final PipelineImportOptions options) {

        String dataSet
                = StringUtils.isBlank(this.DATASET_NAME_OVERRIDE)
                ? options.getDataSet()
                : this.DATASET_NAME_OVERRIDE;

        try {
            LocalDate maxDate = datesToImport.stream().max(LocalDate::compareTo).get();
            LocalDate minDate = datesToImport.stream().min(LocalDate::compareTo).get();
            if (dateRange == null || minDate.isBefore(dateRange.from()) || maxDate.isAfter(dateRange.to())) {
                appConfig.getBigQueryService().createViewForTableWithDateRange(dataSet, options.getTableName(), minDate, maxDate);
            }
        } catch (Exception e) {
            LOG.warn("Could not save dates view for: " + dataSet + "." + options.getTableName());
        }
    }

    public DateRange getSavedDateRange(PipelineImportOptions options) {

        String dataSet
                = StringUtils.isBlank(this.DATASET_NAME_OVERRIDE)
                ? options.getDataSet()
                : this.DATASET_NAME_OVERRIDE;

        return appConfig.getBigQueryService().getDateRangeFromView(dataSet, options.getTableName());
    }

    public PipelineImportOptions createDefaultImportOptions(String tableName, String schemaString) {

        PipelineImportOptions options = PipelineOptionsFactory.create().as(PipelineImportOptions.class);
        options.setRunner(DataflowRunner.class); // run in the cloud
        options.setProject(DnaAppConstants.getProjectId());
        options.setStagingLocation(DnaAppConstants.getDnaStagingLocation());
        options.setTempLocation(DnaAppConstants.getDnaStagingLocation());
        options.setGcpTempLocation(DnaAppConstants.getDnaStagingLocation());

        if (StringUtils.isBlank(this.DATASET_NAME_OVERRIDE)) {
            // normal flow getting it from app configuration
            options.setDataSet(DnaAppConstants.getDataSet());

        } else {
            // special case, overridden by child implementation
            options.setDataSet(this.DATASET_NAME_OVERRIDE);
        }

        options.setTableName(tableName);
        options.setAppName(DnaAppConstants.getProjectAppName());
        options.setSchemaString(schemaString);
        String jobName = String.format("%s-%s", tableName.replaceAll("_", "-"), options.getJobName());
        options.setJobName(jobName.toLowerCase());
        return options;
    }

    public static class TableRowDoFn<T> extends DoFn<T, TableRow> {

        private static final long serialVersionUID = -788096107081650201L;

        private final TableRowConverter<T> tableRowConverter;

        public TableRowDoFn(TableRowConverter<T> tableRowConverter) {
            this.tableRowConverter = tableRowConverter;
        }

        @ProcessElement
        public void processElement(ProcessContext c) {
            T element = c.element();
            try {
                TableRow tableRow = tableRowConverter.convert(element);
                if (!tableRow.isEmpty()) {
                    c.output(tableRow);
                }
            } catch (BqParseException e) {
                LOG.error("Value [" + e.getValue() + "] in column " + e.getColumnName() + " could not be parsed for " + e.getType()
                        + " in element:\n " + element);
            } catch (Exception e) {
                LOG.error("Error in element " + element, e);
            }
        }
    }

    /**
     * This method should be implemented by processing child class such as
     * "CsvToBigQueryPipeline" or any other that needs to validate the data it
     * just processed. If it is not implemented by children classes then nothing
     * that affects the pipeline is executed, just a simple LOG entry
     *
     * @param datesToImport
     */
    public void validateInputRecordCountVsBigQueryRecordCount(List<LocalDate> datesToImport) {
        LOG.info("Nothing to validate here");
    }

    /**
     * patch methods to avoid NPE when values are null in TableRow records
     * please refer to the following:
     * https://github.com/google/google-http-java-client/issues/384
     * https://github.com/google/google-http-java-client/pull/385
     * https://github.com/google/google-http-java-client/pull/385/files/73e28db3d1ffd196149295c02f2876bd5c30f3c0
     *
     * @param tableRow
     * @param fieldName
     * @param fieldValue
     */
    public static void addToRow(TableRow tableRow, String fieldName, String fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL String fieldValue. Will return default \"\". Depending on the expected data type this might cause an Exception.");
            fieldValue = "";
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, Integer fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL Integer fieldValue. Will return default 0.");
            fieldValue = 0;
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, Long fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL Long fieldValue. Will return default 0L.");
            fieldValue = 0L;
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, Float fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL Float fieldValue. Will return default 0F.");
            fieldValue = 0F;
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, Date fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL Date fieldValue. Will keep it as NULL which will likely cause an Exception.");
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, DateTime fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL DateTime fieldValue. Will keep it as NULL which will likely cause an Exception.");
        }

        tableRow.set(fieldName, fieldValue);
    }

    public static void addToRow(TableRow tableRow, String fieldName, LocalDateTime fieldValue) {

        if (fieldValue == null) {
            LOG.warn("Got NULL LocalDateTime fieldValue. Will keep it as NULL which will likely cause an Exception.");
        }

        tableRow.set(fieldName, fieldValue);
    }

    /**
     * end of patch methods to avoid NPE
     */
}
