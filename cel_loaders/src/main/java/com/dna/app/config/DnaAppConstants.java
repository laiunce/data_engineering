package com.dna.app.config;

import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * @author Cristian Laiun

 */
public class DnaAppConstants {

    
    public static final String DEACTIVATED = "DEACTIVATED_";
    public static final String EXTRACT_DAILY_SUMMARY = "dna_daily_summary";
    public static final String EXTRACT_WEEKLY_SUMMARY_PREFIX = "dna_weekly";
    public static final String SEASON_FALL = "Fall";
    public static final String SEASON_SPRING = "Spring";
    public static final String CONTENT_TYPE_CSV = "text/csv";
    public static final String FILE_EXTENSION_CSV = "csv";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String FILE_EXTENSION_JSON = "json";
    public static final String EMPTY_CHAR = "";
    public static final String CLEAN_VALUE_SEARCH_LIST = "$-�NULLnullN/An/aN-An-aNAna";
    private static AppConfigProps CONFIG_PROPS= new AppConfigProps("falabella-config.properties");

    public static final void setAppConfigProps(AppConfigProps appConfigProps) {
        DnaAppConstants.CONFIG_PROPS = appConfigProps;
    }

    public static final AppConfigProps getAppConfigProps() {
        return DnaAppConstants.CONFIG_PROPS;
    }

    public static String getProjectId() {
        // PROJECT_ID
        return CONFIG_PROPS.get("project.id");
    }

    public static String getProjectBucketName() {
        return CONFIG_PROPS.get("project.bucketName");
    }

    public static String getProjectAppName() {
        // APP_NAME
        return CONFIG_PROPS.get("project.appName");
    }

    public static String getDnaBucketLocation() {
        // DNA_BUCKET_LOCATION
        return CONFIG_PROPS.get("gcs.dna.bucket.location");
    }

    public static String getDnaStagingLocation() {
        // DNA_STAGING_LOCATION
        return CONFIG_PROPS.get("gcs.dna.staging.location");
    }

    public static String getDataSet() {
        // DATA_SET
        return CONFIG_PROPS.get("bq.dataset");
    }

    public static String getDcmDataSet() {
        // DCM_DATASET
        return CONFIG_PROPS.get("doubleclickcampaignmanager.dataset");
    }

    public static List<String> getDcmActivityIds() {
        // DCM_ACTIVITY_IDs
        return CONFIG_PROPS.getStrings("doubleclickcampaignmanager.activityId");
    }

    public static List<String> getDcmAdvertiserIds() {
        // DCM_ADVERTISER_IDs
        return CONFIG_PROPS.getStrings("doubleclickcampaignmanager.advertiserId");
    }

    public static List<String> getDcmCampaignIds() {
        // DCM_CAMPAIGN_IDS
        return CONFIG_PROPS.getStrings("doubleclickcampaignmanager.campaignIds");
    }

    public static Long getDoubleClickSearchAgencyId() {
        // DOUBLECLICK_SEARCH_AGENCY_ID
        return CONFIG_PROPS.getLong("doubleclicksearch.agencyId");
    }

    public static Long getDoubleClickSearchAdvertiserId() {
        // DOUBLECLICK_SEARCH_ADVERTISER_ID
        return CONFIG_PROPS.getLong("doubleclicksearch.advertiserId");
    }

    public static String getAdNetDir() {
        // AD_NET_DIR
        return CONFIG_PROPS.get("gcs.dna.adnet.dir");
    }

    public static String getAdMarketPlaceDir() {
        // AD_MARKET_PLACE_DIR
        return CONFIG_PROPS.get("gcs.dna.admarketplace.dir");
    }

    public static String getChannelAdvisorDir() {
        // CHANNEL_ADVISOR_DIR
        return CONFIG_PROPS.get("gcs.dna.channeladvisor.dir");
    }

    public static String getCommissionJunctionDir() {
        // COMIMISSIONJUNCTION_DIR
        return CONFIG_PROPS.get("gcs.dna.commissionjunction.dir");
    }

    public static String getWhpReportingJunGroupDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.jungroup.dir");
    }

    public static String getWhpReportingPinterestDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.pinterest.dir");
    }

    public static String getWhpReportingApexDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.apex.dir");
    }

    public static String getWhpReportingPandoraDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.pandora.dir");
    }

    public static String getWhpAmazonDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.amazon.dir");
    }

    public static String getReportingCluepDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.cluep.dir");
    }

    public static String getReportingGooglePreferredDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.googlepreferred.dir");
    }

    public static String getReportingSharethroughDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.sharethrough.dir");
    }

    public static String getReportingTrueViewDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.trueview.dir");
    }


    public static String getReportingMtgCondenastDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.mtgcondenast.dir");
    }

    public static String getReportingRakutenDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.rakuten.dir");
    }

    public static String getReportingMtgTwitterDir() {
        return CONFIG_PROPS.get("gcs.dna.reporting.mtgtwitter.dir");
    }

    public static String getEmailDir() {
        // COMIMISSIONJUNCTION_DIR
        return CONFIG_PROPS.get("gcs.dna.email.dir");
    }

    public static String getConversantDir() {
        // CONVERSANT_DIR
        return CONFIG_PROPS.get("gcs.dna.conversant.dir");
    }

    public static String getCriteoDir() {
        // CRITEO_DIR
        return CONFIG_PROPS.get("gcs.dna.criteo.dir");
    }

    public static String getDoubleClickSearchGdnDir() {
        // DOUBLECLICK_SEARCH_GDN_DIR
        return CONFIG_PROPS.get("gcs.dna.gdn.dir");
    }

    public static String getDoubleClickSearchDir() {
        // DOUBLECLICK_SEARCH_DIR
        return CONFIG_PROPS.get("gcs.dna.doubleclicksearch.dir");
    }

    public static String getDoubleClickBidDir() {
        // DOUBLECLICK_BID_DIR
        return CONFIG_PROPS.get("gcs.dna.doubleclickbid.dir");
    }

    public static String getDoubleClickManagerDir() {
        // DOUBLECLICK_MANAGER_DIR
        return CONFIG_PROPS.get("gcs.dna.doubleclickmanager.dir");
    }

    public static String getLbReportsDir() {
        // LB_REPORTS_DIR
        return CONFIG_PROPS.get("gcs.dna.lbreports.dir");
    }

    public static String getLbOutputDir() {
        // LB_OUTPUT_DIR
        return CONFIG_PROPS.get("gcs.dna.lboutput.dir");
    }

    public static String getSundaySkyDir() {
        // SUNDAY_SKY_DIR
        return CONFIG_PROPS.get("gcs.dna.sundaysky.dir");
    }

    public static String getYahooNative() {
        // YAHOO_NATIVE
        return CONFIG_PROPS.get("gcs.dna.yahoonative.dir");
    }

    public static Boolean getImportOverwrite() {
        // IMPORT_OVERWRITE
        return CONFIG_PROPS.getBoolean("import.overwrite");
    }

    public static String getInnovidDir() {
        return CONFIG_PROPS.get("gcs.dna.innovid.dir");
    }

    public static String getPinterestDir() {
        return CONFIG_PROPS.get("gcs.dna.pinterest.dir");
    }

    public static String getJungroupDir() {
        return CONFIG_PROPS.get("gcs.dna.jungroup.dir");
    }

    public static String getApexDir() {
        return CONFIG_PROPS.get("gcs.dna.apex.dir");
    }

    public static String getPandoraDir() {
        return CONFIG_PROPS.get("gcs.dna.pandora.dir");
    }

    public static String getAmazonDir() {
        return CONFIG_PROPS.get("gcs.dna.amazon.dir");
    }

    public static String getTechTargetDir() {
        return CONFIG_PROPS.get("gcs.dna.techtarget.dir");
    }

    public static String getIasDir() {
        return CONFIG_PROPS.get("gcs.dna.ias.dir");
    }

    public static String getDataXuDir() {
        return CONFIG_PROPS.get("gcs.dna.dataxu.dir");
    }

    public static String getYouTubeDir() {
        return CONFIG_PROPS.get("gcs.dna.youtube.dir");
    }

    public static String getBidTellecDir() {
        return CONFIG_PROPS.get("gcs.dna.bidtellec.dir");
    }

    public static String getCbsiDir() {
        return CONFIG_PROPS.get("gcs.dna.cbsi.dir");
    }

    public static String getEconomistDir() {
        return CONFIG_PROPS.get("gcs.dna.economist.dir");
    }

    public static String getHuluDir() {
        return CONFIG_PROPS.get("gcs.dna.hulu.dir");
    }

    public static String getSpiceworksDir() {
        return CONFIG_PROPS.get("gcs.dna.spiceworks.dir");
    }

    public static String getZemantaDir() {
        return CONFIG_PROPS.get("gcs.dna.zemanta.dir");
    }

    public static String getAtlanticDir() {
        return CONFIG_PROPS.get("gcs.dna.atlantic.dir");
    }

    public static String getTubeMogulDir() {
        return CONFIG_PROPS.get("gcs.dna.tubemogul.dir");
    }

    public static String getTubeMogulUserId() {
        return CONFIG_PROPS.get("tubemogul.userId");
    }

    public static String getTubeMogulApiSecret() {
        return CONFIG_PROPS.get("tubemogul.ApiSecret");
    }

    public static String getDnaInnovidApiClientId() {
        return CONFIG_PROPS.get("dna.innovid.api.clientId");
    }

    public static String getDnaInnovidApiAdvertiserId() {
        return CONFIG_PROPS.get("dna.innovid.api.advertiserId");
    }

    public static String getDnaInnovidCredentialsUser() {
        return CONFIG_PROPS.get("dna.innovid.api.credentials.user");
    }

    public static String getDnaInnovidCredentialsPassword() {
        return CONFIG_PROPS.get("dna.innovid.api.credentials.password");
    }

    public static String getGcDcsClientId() {
        return CONFIG_PROPS.get("gc.doubleclicksearch.clientId");
    }

    public static String getGcDcsclientSecret() {
        return CONFIG_PROPS.get("gc.doubleclicksearch.clientSecret");
    }

    public static String getGcDcsRefreshToken() {
        return CONFIG_PROPS.get("gc.doubleclicksearch.refreshToken");
    }

    public static String getGcDbmClientId() {
        return CONFIG_PROPS.get("gc.doubleclickbid.clientId");
    }

    public static String getGcDbmclientSecret() {
        return CONFIG_PROPS.get("gc.doubleclickbid.clientSecret");
    }

    public static String getGcDbmRefreshToken() {
        return CONFIG_PROPS.get("gc.doubleclickbid.refreshToken");
    }

    public static long getGcDbmQueryId() {
        return CONFIG_PROPS.getLong("gcs.dna.doubleclickbid.queryId");
    }

    public static String getGcDcmClientId() {
        return CONFIG_PROPS.get("gc.doubleclickmanager.clientId");
    }

    public static String getGcDcmclientSecret() {
        return CONFIG_PROPS.get("gc.doubleclickmanager.clientSecret");
    }

    public static String getGcDcmRefreshToken() {
        return CONFIG_PROPS.get("gc.doubleclickmanager.refreshToken");
    }

    public static String getGcDcmProfileIds() {
        return (CONFIG_PROPS.get("gcs.dna.doubleclickmanager.profileIds") != null) ? CONFIG_PROPS.get("gcs.dna.doubleclickmanager.profileIds") : "";
    }

    public static String getGcDcmRegions() {
        return (CONFIG_PROPS.get("gcs.dna.doubleclickmanager.regions") != null) ? CONFIG_PROPS.get("gcs.dna.doubleclickmanager.regions") : "";
    }

    public static boolean getGcDcmApplyFilters() {
        return (CONFIG_PROPS.getBoolean("gcs.dna.doubleclickmanager.applyFilters"));
    }

    public static String getGcDcmAdvertiserIdFilters() {
        return (CONFIG_PROPS.get("gcs.dna.doubleclickmanager.advertiserIdFilters") != null) ? CONFIG_PROPS.get("gcs.dna.doubleclickmanager.advertiserIdFilters") : "0";
    }

    public static String getGcDcmFloodlightIdFilters() {
        return (CONFIG_PROPS.get("gcs.dna.doubleclickmanager.floodlightIdFilters") != null) ? CONFIG_PROPS.get("gcs.dna.doubleclickmanager.floodlightIdFilters") : "0";
    }

    public static String getDcsAgencyIdAdvertiserIdPairsFileName() {
        return CONFIG_PROPS.get("doubleclicksearch.agencyIdAdvertiserIdPairsFileName");
    }

    public static String getTwitterAdsAccountIds() {
        return CONFIG_PROPS.get("twitterAds.accountIds");
    }

    public static String getTwitterAdsAccountTimeZones() {
        return (CONFIG_PROPS.get("twitterAds.accountTimeZones") != null) ? CONFIG_PROPS.get("twitterAds.accountTimeZones") : "";
    }

    public static String getTwitterRegions() {
        return (CONFIG_PROPS.get("twitterAds.regions") != null) ? CONFIG_PROPS.get("twitterAds.regions") : "";
    }

    public static String getTwitterCountries() {
        return (CONFIG_PROPS.get("twitterAds.countries") != null) ? CONFIG_PROPS.get("twitterAds.countries") : "";
    }

    public static String getTwitterAdsConsumerKey() {
        return CONFIG_PROPS.get("twitterAds.consumerKey");
    }

    public static String getTwitterAdsConsumerSecret() {
        return CONFIG_PROPS.get("twitterAds.consumerSecret");
    }

    public static String getTwitterAdsAccessToken() {
        return CONFIG_PROPS.get("twitterAds.accessToken");
    }

    public static String getTwitterAdsTokenSecret() {
        return CONFIG_PROPS.get("twitterAds.tokenSecret");
    }

    public static boolean getTwitterServableCampaigns() {
        return (CONFIG_PROPS.get("twitterAds.servableCampaigns") == null) || CONFIG_PROPS.getBoolean("twitterAds.servableCampaigns");
    }

    public static String getLinkedInCampaignsAccountIds() {
        return CONFIG_PROPS.get("linkedInCampaigns.accountIds");
    }

    public static String getLinkedInCampaignsAccessToken() {
        return CONFIG_PROPS.get("linkedInCampaigns.accessToken");
    }

    public static String getFacebookAdAccessToken() {
        return CONFIG_PROPS.get("fb.ad.accessToken");
    }

    public static String getFacebookAdAppSecret() {
        return CONFIG_PROPS.get("fb.ad.appSecret");
    }

    public static String getFacebookAdAccountId() {
        return CONFIG_PROPS.get("fb.ad.accountId");
    }

    public static String getFacebookUserId() {
        return CONFIG_PROPS.get("fb.ad.userId");
    }

    public static Boolean hasDynamicAccountIds() {
        String value = CONFIG_PROPS.get("fb.ad.hasDynamicAccountIds");
        return (value != null ? Boolean.valueOf(value) : Boolean.FALSE);
    }

    public static Boolean getOnlyActiveCampaigns() {
        String value = CONFIG_PROPS.get("fb.ad.getOnlyActiveCampaigns");
        return (value != null ? Boolean.valueOf(value) : Boolean.TRUE);
    }

    public static String getClientSecretsJsonFileName() throws Exception {

        String clientSecretsJsonFilename = CONFIG_PROPS.get("gcs.client.secrets.json.filename");

        if (StringUtils.isBlank(clientSecretsJsonFilename)) {
            throw new Exception("Client Secrets JSON filename is empty in " + AppConfigProps.getConfigFilename());
        }

        return clientSecretsJsonFilename;
    }

    public static String getGcsScheduledQueriesDir() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.dir");
    }

    public static String getGcsScheduledQueriesTableSchemasDir() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.tableSchemas.dir");
    }

    public static String getGcsScheduledQueriesQueryScriptsDir() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.queryScripts.dir");
    }

    public static String getGcsScheduledQueriesDailyConfig() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.daily");
    }

    public static String getGcsScheduledQueriesWeeklyConfig() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.weekly");
    }

    public static String getGcsScheduledQueriesMonthlyConfig() {
        return CONFIG_PROPS.get("gcs.scheduled.queries.monthly");
    }

    public static String getInstagramBusinessAccountId() {
        return CONFIG_PROPS.get("instagram.business.account.id");
    }

    public static String getInstagramBusinessAccountAccessToken() {
        return CONFIG_PROPS.get("instagram.business.account.access.token");
    }

    public static Integer getInstagramMediaDaysOld() {
        return ((Long) CONFIG_PROPS.getLong("instagram.media.days.old")).intValue();
    }

    public static String getPostgresPassword() {
        return CONFIG_PROPS.get("postgres.password") == null ? "" : CONFIG_PROPS.get("postgres.password");
    }

    public static String getPostgresDBName() {
        return CONFIG_PROPS.get("postgres.dbName") == null ? "" : CONFIG_PROPS.get("postgres.dbName");
    }

    public static String getPostgresJdbcUrl() {
        return CONFIG_PROPS.get("postgres.jdbcUrl") == null ? "" : CONFIG_PROPS.get("postgres.jdbcUrl");
    }

    public static String getGcsTransferFile() {
        return CONFIG_PROPS.get("gcs.transfer.json.location");
    }

    public static String getGcsTransferQaPath() {
        return CONFIG_PROPS.get("gcs.transfer.qa.location");
    }

    public static String getGcsTransferQaQueriesPath() {
        return CONFIG_PROPS.get("gcs.transfer.qa.query.location");
    }

    public static String getInstanceConnectionName() {
        return CONFIG_PROPS.get("postgres.instanceConnectionName") == null ? "" : CONFIG_PROPS.get("postgres.instanceConnectionName");
    }

    public static String getPostgresUser() {
        return CONFIG_PROPS.get("postgres.user") == null ? "" : CONFIG_PROPS.get("postgres.user");
    }

    public static String getPrismaJdbcUrl() {
        return CONFIG_PROPS.get("prisma.jdbcUrl") == null ? "" : CONFIG_PROPS.get("prisma.jdbcUrl");
    }

    public static String getPrismaHost() {
        return CONFIG_PROPS.get("prisma.host") == null ? "" : CONFIG_PROPS.get("prisma.host");
    }

    public static String getPrismaUser() {
        return CONFIG_PROPS.get("prisma.user") == null ? "" : CONFIG_PROPS.get("prisma.user");
    }

    public static String getPrismaPassword() {
        return CONFIG_PROPS.get("prisma.password") == null ? "" : CONFIG_PROPS.get("prisma.password");
    }

    public static String getPrismaDbName() {
        return CONFIG_PROPS.get("prisma.dbName") == null ? "" : CONFIG_PROPS.get("prisma.dbName");
    }

    public static String getPrismaTableName() {
        return CONFIG_PROPS.get("prisma.tableName");
    }

    public static String getCleanValueSearchList() {
        return CLEAN_VALUE_SEARCH_LIST;
        //CONFIG_PROPS.get("cleanvalue.search.list"); This code is commented because of a NullPointerException on dataflow into google cloud platform
    }

    public static String getPlacedDir() {
        return CONFIG_PROPS.get("gcs.placed.dir");
    }

    public static String getPlacedApiBasicAuthToken() {
        return CONFIG_PROPS.get("placed.api.basicAuthToken");
    }

    public static String getPlacedReportIdsBigQueryTableName() {
        return CONFIG_PROPS.get("placed.report.ids.bq.tablename");
    }

    public static String getGcsQaPath() {
        return CONFIG_PROPS.get("gcs.qa.location");
    }

    public static String getGcsQaOutputPath() {
        return CONFIG_PROPS.get("gcs.qa.output.location");
    }

    public static String getReportingEmailUserName() {
        return CONFIG_PROPS.get("reporting.email.userName");
    }

    public static String getReportingEmailPassword() {
        return CONFIG_PROPS.get("reporting.email.password");
    }

    public static String getEmailTo() {
        return CONFIG_PROPS.get("reporting.email.emailsTo");
    }

    public static String getEmailSubject() {
        return CONFIG_PROPS.get("reporting.email.emailSubject");
    }

    public static String getEmailGcsLogDir() {
        return CONFIG_PROPS.get("reporting.email.gcs.log.dir");
    }

    public static String getEmailBigQuery() {
        return CONFIG_PROPS.get("reporting.email.bigquery");
    }
}
