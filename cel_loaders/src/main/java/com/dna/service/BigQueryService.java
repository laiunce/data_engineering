package com.dna.service;

import com.dna.app.config.DnaAppConstants;
import com.dna.exception.ServiceException;
import com.dna.util.BigQueryUtil;
import com.dna.util.DateRange;
import com.dna.util.RunUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.GetQueryResultsResponse;
import com.google.api.services.bigquery.model.Job;
import com.google.api.services.bigquery.model.JobConfiguration;
import com.google.api.services.bigquery.model.JobConfigurationExtract;
import com.google.api.services.bigquery.model.JobConfigurationQuery;
import com.google.api.services.bigquery.model.JobReference;
import com.google.api.services.bigquery.model.QueryRequest;
import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.Table;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableDataList;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.ViewDefinition;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun
 */
public class BigQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(BigQueryService.class);
    public static final DateTimeFormatter BQ_DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final String DEFAULT_VIEW_PREFIX = "v_";
    private static final boolean USE_LEGACY_SQL = false;
    private Bigquery bigquery;
    private String projectId;
    private String appName;

    public BigQueryService(Credential credential, String projectId, String appName) {
        try {
            this.bigquery = new Bigquery.Builder(
                    new NetHttpTransport(), new JacksonFactory(), credential).
                    setApplicationName(appName).build();

            this.appName = appName;
            this.projectId = projectId;
        } catch (Exception e) {
            throw new ServiceException("Could not create service ", e);
        }
    }

    public Boolean executeQuery(String query) {

        LOG.info(String.format("Executing query: %s", query));
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setQuery(query);
        queryRequest.setUseLegacySql(false);
        QueryResponse response;

        try {

            response = this.bigquery.jobs().query(DnaAppConstants.getProjectId(), queryRequest).execute();
            LOG.info(String.format("Is response for query %s job completed?: %s", query, response.getJobComplete()));
            return response.getJobComplete();

        } catch (IOException ex) {

            LOG.error("An error ocurred when executing query. " + ex.getMessage());
            throw new ServiceException("An error ocurred when executing query.", ex);
        }
    }

    public List<TableRow> executeQueryGetResult(String query) {

        List<TableRow> tableRowResult = new ArrayList();
        LOG.info(String.format("Executing query: %s", query));

        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setQuery(query);
        queryRequest.setUseLegacySql(false);

        QueryResponse queryResponse;
        String jobId;
        String pageToken;
        GetQueryResultsResponse queryResults;
        int pageNumber = 0;

        try {

            queryResponse
                    = this.bigquery
                            .jobs()
                            .query(
                                    DnaAppConstants.getProjectId(),
                                    queryRequest
                            )
                            .execute();

            jobId = queryResponse.getJobReference().getJobId();
            LOG.info(String.format("Is response for query %s with jobId %s completed?: %s", query, jobId, queryResponse.getJobComplete()));
            LOG.info(String.format("Got %s rows from query result.", queryResponse.getTotalRows()));

            if (queryResponse.getJobComplete() && !queryResponse.getTotalRows().equals(BigInteger.ZERO)) {

                tableRowResult.addAll(queryResponse.getRows());
                LOG.info("Got rows for page {}", pageNumber);
                pageToken = queryResponse.getPageToken();

                if (pageToken != null) {
                    LOG.info("Got paged result, will get results for each page...");
                }

                while (pageToken != null) {

                    pageNumber++;
                    queryResults
                            = this.bigquery
                                    .jobs()
                                    .getQueryResults(
                                            DnaAppConstants.getProjectId(),
                                            jobId
                                    )
                                    .setPageToken(
                                            pageToken
                                    )
                                    .execute();

                    tableRowResult.addAll(queryResults.getRows());
                    LOG.info("Got rows for page {}", pageNumber);
                    pageToken = queryResults.getPageToken();
                }
            }

        } catch (IOException ex) {

            LOG.error("An error ocurred when executing query for getting a result. " + ex.getMessage());
            throw new ServiceException("An error ocurred when executing query for getting a result.", ex);
        }

        LOG.info("Returning {} rows as final query result", tableRowResult.size());
        return tableRowResult;
    }

    public Table createView(String dataSet, String viewName, String query) throws Exception {
        Table content = new Table();
        TableReference tableReference = new TableReference();
        tableReference.setTableId(viewName);
        tableReference.setDatasetId(dataSet);
        tableReference.setProjectId(projectId);
        content.setTableReference(tableReference);

        ViewDefinition view = new ViewDefinition();

        view.setQuery(query);
        view.setUseLegacySql(USE_LEGACY_SQL);
        content.setView(view);
        LOG.debug("View to create: " + content);
        try {
            if (tableExists(dataSet, viewName)) {
                deleteView(dataSet, viewName);
            }
        } catch (Exception e) {
            LOG.error("Could not delete view", e);
        }
        return RunUtil.doAndRetry(()
                -> bigquery.tables().insert(projectId, dataSet, content).setProjectId(projectId).execute());
    }

    public void deleteView(String dataSet, String viewName) throws Exception {
        LOG.info("View to delete: " + viewName);
        try {
            if (tableExists(dataSet, viewName)) {
                bigquery.tables().delete(projectId, dataSet, viewName).execute();
            }
        } catch (Exception e) {
            LOG.error("Could not delete view", e);
        }
    }

    public void extract(String dataSet, String tableName, String fileName) throws Exception {
        JobConfigurationExtract extract = new JobConfigurationExtract();

        TableReference tableReference = new TableReference();
        tableReference.setTableId(tableName);
        tableReference.setDatasetId(dataSet);
        tableReference.setProjectId(projectId);

        extract.setSourceTable(tableReference);
        extract.setDestinationUri(fileName);

        LOG.info("About to extract {}.{} to: {}", dataSet, tableName, fileName);

        Job extractJob = bigquery.jobs().insert(tableReference.getProjectId(),
                new Job().setConfiguration(new JobConfiguration().setExtract(extract))).execute();

        LOG.info("Ran extract job status: {}. total bytes: {}, end time: {}", extractJob.getStatus().getState(), extractJob.getStatistics().getTotalBytesProcessed(), extractJob.getStatistics().getEndTime());

        Bigquery.Jobs.Get get_job = bigquery.jobs().get(extractJob.getJobReference().getProjectId(),
                extractJob.getJobReference().getJobId());

        BigQueryUtil.pollJob(get_job, 5 * 1000);

        LOG.info("Ran extract job status: {}. total bytes: {}, end time: {}", extractJob.getStatus().getState(), extractJob.getStatistics().getTotalBytesProcessed(), extractJob.getStatistics().getEndTime());
        LOG.info("Export is Done!");
    }

    public void createViewForTableWithDateRange(String dataSet, String tableName, LocalDate from, LocalDate to) throws Exception {

        String viewName = DEFAULT_VIEW_PREFIX + tableName;

        String query = String.format("SELECT *, DATE('%s') as _FIRST_DATE, DATE('%s') as _LAST_DATE FROM `%s.%s.%s`",
                BQ_DATE_FORMATTER.format(from), BQ_DATE_FORMATTER.format(to), DnaAppConstants.getProjectId(), DnaAppConstants.getDataSet(), tableName);

        createView(dataSet, viewName, query);
    }

    public void deleteDataForDateRange(String dataSet, String tableName, String dateColumn, LocalDate from, LocalDate to) throws Exception {

        if (StringUtils.isBlank(dataSet)) {
            dataSet = DnaAppConstants.getDataSet();
        }

        // only run the delete on a valid date range
        if (from.isEqual(to) || from.isBefore(to)) {
            String query = String.format("DELETE FROM `%s.%s.%s` WHERE `%s` >= DATE('%s') AND `%s` <= DATE('%s')", DnaAppConstants.getProjectId(), dataSet, tableName, dateColumn,
                    BQ_DATE_FORMATTER.format(from), dateColumn, BQ_DATE_FORMATTER.format(to));

            LOG.info(query);
            QueryRequest queryRequst = new QueryRequest();
            queryRequst.setQuery(query);
            queryRequst.setUseLegacySql(USE_LEGACY_SQL);
            QueryResponse response = bigquery.jobs().query(projectId, queryRequst).execute();

            LOG.info("Job complete from deleting table [{}] for dates [{} - {}]? {}", tableName, BQ_DATE_FORMATTER.format(from), BQ_DATE_FORMATTER.format(to), response.getJobComplete());
        } else {
            throw new ServiceException("Ensure from date occurs before to date!");
        }
        return;
    }

    public DateRange getDateRangeFromView(String dataSet, String tableName) {

        String viewName = DEFAULT_VIEW_PREFIX + tableName;
        if (!tableExists(dataSet, viewName)) {
            return null;
        }
        try {
            List<TableRow> tableRows = executeQueryGetResult(String.format("SELECT _FIRST_DATE, _LAST_DATE FROM `%s.%s.%s` LIMIT 1", projectId, dataSet, viewName));
            if (tableRows.size() > 0) {
                TableRow tableRow = tableRows.get(0);
                List<TableCell> tableCells = tableRow.getF();
                return new DateRange(
                        LocalDate.parse((String) tableCells.get(0).getV(), BQ_DATE_FORMATTER),
                        LocalDate.parse((String) tableCells.get(1).getV(), BQ_DATE_FORMATTER));
            }
        } catch (Exception e) {
            LOG.warn("Could not get date range from view");
        }
        return null;
    }

    public boolean tableExists(String dataSet, String tableName) {
        try {
            bigquery.tables().get(projectId, dataSet, tableName).execute();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public List<TableRow> getTableData(String query) {
        QueryJob queryJob = new QueryJob(query);
        return queryJob.getTableData();
    }

    public class QueryJob {

        private Job job;
        private String query;

        public QueryJob(String query) {
            this.query = query;
            beginQuery();
        }

        private void beginQuery() {
            final Job queryJob = makeJob(query);

            job = RunUtil.doAndRetry(
                    () -> bigquery.jobs().insert(projectId, queryJob).execute());

            Preconditions.checkNotNull(job);
        }

        public boolean jobSucceeded() {
            return (job != null && job.getStatus().getErrorResult() == null);
        }

        public String getJobErrorMessage() {
            if (job != null && job.getStatus().getErrorResult() != null) {
                return job.getStatus().getErrorResult().getMessage();
            }
            return "";
        }

        public boolean jobIsDone() {
            String status = getJobStatus();
            return ("DONE").equalsIgnoreCase(status);
        }

        public String getJobStatus() {
            return (job != null) ? job.getStatus().getState() : null;
        }

        public List<TableFieldSchema> getSchemaFieldNames() {
            if (job != null) {
                final TableReference tableReference = job.getConfiguration().getQuery().getDestinationTable();

                Table table = RunUtil.doAndRetry(()
                        -> bigquery.tables().get(tableReference.getProjectId(), tableReference.getDatasetId(),
                                tableReference.getTableId()).execute());
                Preconditions.checkNotNull(table);
                Preconditions.checkNotNull(table.getSchema());
                Preconditions.checkNotNull(table.getSchema().getFields());
                return table.getSchema().getFields();
            }
            return null;
        }

        public List<TableRow> getTableData() {
            if (job != null) {
                final TableReference tableReference = job.getConfiguration().getQuery().getDestinationTable();

                LOG.info(String.format(
                        "Getting table data from %s.%s.%s",
                        tableReference.getProjectId(),
                        tableReference.getDatasetId(),
                        tableReference.getTableId()
                ));

                TableDataList tableDataList = RunUtil.doAndRetry(()
                        -> bigquery.tabledata().list(tableReference.getProjectId(),
                                tableReference.getDatasetId(), tableReference.getTableId()).execute()
                );

                LOG.info(String.format(
                        "Got table data from %s.%s.%s",
                        tableReference.getProjectId(),
                        tableReference.getDatasetId(),
                        tableReference.getTableId()
                ));

                Preconditions.checkNotNull(tableDataList);
                if (tableDataList != null && tableDataList.getRows() != null) {
                    Preconditions.checkNotNull(tableDataList.getRows());
                    return tableDataList.getRows();
                }
            }

            LOG.info("Returning NULL (no data from table)");
            return null;
        }

        /**
         * Instantiates an example job and sets required fields.
         */
        private Job makeJob(String query) {
            JobConfigurationQuery jobconfigurationquery = new JobConfigurationQuery();

            jobconfigurationquery.setQuery(query).setUseLegacySql(USE_LEGACY_SQL);
            jobconfigurationquery.setCreateDisposition("CREATE_IF_NEEDED");

            JobConfiguration jobconfiguration = new JobConfiguration();
            jobconfiguration.setQuery(jobconfigurationquery);

            JobReference jobreference = new JobReference();
            jobreference.setProjectId(projectId);

            Job newJob = new Job();
            newJob.setConfiguration(jobconfiguration);
            newJob.setJobReference(jobreference);

            return newJob;
        }

    }

}
