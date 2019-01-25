package com.dna.app.config;

import com.dna.exception.ServiceException;
import com.dna.service.BigQueryService;
import com.dna.service.DnaCredentialsService;
import com.dna.service.FacebookAdAccountService;
import com.dna.service.FacebookAdService;
import com.dna.util.CommissionJunctionCredentials;
import com.dna.util.CriteoCredentials;
import com.dna.util.DateRange;
//import com.dna.util.EmailCredentials;
import com.dna.util.YahooGeminiCredentials;
import com.facebook.ads.sdk.APIContext;
import com.google.api.client.auth.oauth2.Credential;
import com.google.cloud.hadoop.gcsio.GoogleCloudStorage;
import com.google.cloud.hadoop.gcsio.GoogleCloudStorageImpl;
import com.google.cloud.hadoop.gcsio.GoogleCloudStorageOptions;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun

 */
public class DnaAppConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DnaAppConfig.class);

    private final Credential credentials;
    private final GoogleCloudStorage googleCloudStorage;
    private final BigQueryService bigQueryService;
    private final FacebookAdAccountService facebookAdAccountService;
    private final List<FacebookAdService> facebookAdServices;
    private final YahooGeminiCredentials yahooGeminiCredentials;
    private final CriteoCredentials criteoCredentials;
    private final CommissionJunctionCredentials commissionJunctionCredentials;
    private final Boolean importOverwrite;
    private final Boolean replaceData;
    private final Boolean useIdsFromAPI;
    private final DateRange dateRange;
    private final String facebookAccountIdsOverride;
    private final Boolean facebookValidateCampaignStatus;


    protected DnaAppConfig(Builder builder) {

        credentials = builder.credentials;
        googleCloudStorage = builder.googleCloudStorage;
        bigQueryService = builder.bigQueryService;
        facebookAdAccountService = builder.facebookAdAccountService;
        facebookAdServices = builder.facebookAdServices;
        importOverwrite = builder.importOverwrite;
        replaceData = builder.replaceData;
        useIdsFromAPI = builder.useIdsFromAPI;
        dateRange = builder.dateRange;
        yahooGeminiCredentials = builder.yahooGeminiCredentials;
        criteoCredentials = builder.criteoCredentials;
        commissionJunctionCredentials = builder.commissionJunctionCredentials;
        facebookAccountIdsOverride = builder.facebookAccountIdsOverride;
        facebookValidateCampaignStatus = builder.facebookValidateCampaignStatus;
    }

    public YahooGeminiCredentials getYahooGeminiCredentials() {
        return yahooGeminiCredentials;
    }

    public Credential getCredentials() {
        return credentials;
    }

    public GoogleCloudStorage getGoogleCloudStorage() {
        return googleCloudStorage;
    }


    public FacebookAdAccountService getFacebookAdAccountService() {
        return facebookAdAccountService;
    }

    public List<FacebookAdService> getFacebookAdServices() {
        return facebookAdServices;
    }

    public BigQueryService getBigQueryService() {
        return bigQueryService;
    }

    public Boolean getImportOverwrite() {
        return DnaAppConstants.getImportOverwrite();
    }

    public Boolean getReplaceData() {
        return replaceData;
    }

    public Boolean getUseIdsFromAPI() {
        return useIdsFromAPI;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public CriteoCredentials getCriteoCredentials() {
        return criteoCredentials;
    }

    public CommissionJunctionCredentials getCommissionJunctionCredentials() {
        return commissionJunctionCredentials;
    }


    public String getFacebookAccountIdsOverride() {
        return facebookAccountIdsOverride;
    }

    public Boolean getFacebookValidateCampaignStatus() {
        return facebookValidateCampaignStatus;
    }


    public static class Builder {

        Credential credentials;
        GoogleCloudStorage googleCloudStorage;
        BigQueryService bigQueryService;
        FacebookAdAccountService facebookAdAccountService;
        List<FacebookAdService> facebookAdServices;
        YahooGeminiCredentials yahooGeminiCredentials;
        CriteoCredentials criteoCredentials;
        CommissionJunctionCredentials commissionJunctionCredentials;
        Boolean importOverwrite;
        Boolean replaceData;
        Boolean useIdsFromAPI;
        DateRange dateRange;
        LocalDate reportDate;
        String facebookAccountIdsOverride;
        Boolean facebookValidateCampaignStatus;

        AppConfigProps appConfigProps;

        public Builder(AppConfigProps appConfigProps) {
            this.appConfigProps = appConfigProps;
        }

        public Builder setCommissionJunctionCredentials(CommissionJunctionCredentials commissionJunctionCredentials) {
            this.commissionJunctionCredentials = commissionJunctionCredentials;
            return this;
        }


        public Builder setCriteoCredentials(CriteoCredentials criteoCredentials) {
            this.criteoCredentials = criteoCredentials;
            return this;
        }

        public Builder setYahooGeminiCredentials(YahooGeminiCredentials yahooGeminiCredentials) {
            this.yahooGeminiCredentials = yahooGeminiCredentials;
            return this;
        }

        public Builder setCredentials(Credential credentials) {
            this.credentials = credentials;
            return this;
        }


        public Builder setGoogleCloudStorage(GoogleCloudStorage googleCloudStorage) {
            this.googleCloudStorage = googleCloudStorage;
            return this;
        }


        public Builder setDateRange(DateRange dateRange) {
            this.dateRange = dateRange;
            return this;
        }

        public Builder setFacebookAccountIdsOverride(String facebookAccountIdsOverride) {
            this.facebookAccountIdsOverride = facebookAccountIdsOverride;
            return this;
        }

        public Builder setFacebookValidateCampaignStatus(Boolean facebookValidateCampaignStatus) {
            this.facebookValidateCampaignStatus = facebookValidateCampaignStatus;
            return this;
        }

        public boolean getFacebookValidateCampaignStatus(boolean debug) {

            boolean fbvcs;

            if (this.facebookValidateCampaignStatus != null) {

                if (debug) {

                    LOG.info("Overriden by command line parameter '-fbvcs' with value {}", this.facebookValidateCampaignStatus);
                }

                fbvcs = this.facebookValidateCampaignStatus;

            } else {

                // use properties file configuration
                // this is the usual workflow
                fbvcs = DnaAppConstants.getOnlyActiveCampaigns();
            }

            if (debug) {

                LOG.info(fbvcs ? "Only getting ACTIVE campaigns" : "NOT VALIDATING campaign status");
            }

            return fbvcs;
        }

        public Builder setImportOverwrite(Boolean importOverwrite) {
            this.importOverwrite = importOverwrite;
            return this;
        }

        public Builder setReplaceData(Boolean replaceData) {
            this.replaceData = replaceData;
            return this;
        }

        public Builder setUseIdsFromAPI(Boolean useIdsFromAPI) {
            this.useIdsFromAPI = useIdsFromAPI;
            return this;
        }

        public Builder setBigQueryService(BigQueryService bigQueryService) {
            this.bigQueryService = bigQueryService;
            return this;
        }


        public Builder setReportDate(LocalDate reportDate) {
            this.reportDate = reportDate;
            return this;
        }

        public Credential createDefaultCredentials() {
            DnaCredentialsService credentials = new DnaCredentialsService();

            try {
                return appConfigProps.getBoolean("gc.generateCredentialInteractively")
                        ? credentials.generateCredentialInteractively()
                        : credentials.getCredentials(appConfigProps.get("gc.clientId"),
                                appConfigProps.get("gc.clientSecret"), appConfigProps.get("gc.refreshToken"));
            } catch (Exception e) {
                throw new ServiceException(e);
            }
        }

        public Credential createDoubleClickSearchCredentials() {

            DnaCredentialsService dnaCredentialsService = new DnaCredentialsService();

            try {

                return dnaCredentialsService.getCredentials(
                        DnaAppConstants.getGcDcsClientId(),
                        DnaAppConstants.getGcDcsclientSecret(),
                        DnaAppConstants.getGcDcsRefreshToken());

            } catch (Exception e) {

                throw new ServiceException(e);
            }
        }

        public Credential createDoubleClickBidCredentials() {

            DnaCredentialsService dnaCredentialsService = new DnaCredentialsService();

            try {

                return dnaCredentialsService.getCredentials(
                        DnaAppConstants.getGcDbmClientId(),
                        DnaAppConstants.getGcDbmclientSecret(),
                        DnaAppConstants.getGcDbmRefreshToken());

            } catch (Exception e) {

                throw new ServiceException(e);
            }
        }

        public Credential createDoubleClickManagerCredentials() {

            DnaCredentialsService dnaCredentialsService = new DnaCredentialsService();

            try {

                return dnaCredentialsService.getCredentials(
                        DnaAppConstants.getGcDcmClientId(),
                        DnaAppConstants.getGcDcmclientSecret(),
                        DnaAppConstants.getGcDcmRefreshToken());

            } catch (Exception e) {

                throw new ServiceException(e);
            }
        }

        public YahooGeminiCredentials createDefaultYahooGeminiCredentials() {
            YahooGeminiCredentials defaultCredentials = new YahooGeminiCredentials();

            try {
                defaultCredentials.setAdvertId(appConfigProps.get("yg.advertId"));
                defaultCredentials.setCampId(appConfigProps.get("yg.campId"));
                defaultCredentials.setEndpoint(appConfigProps.get("yg.endpoint"));
                defaultCredentials.setAccountId(appConfigProps.get("yg.accountId"));
                defaultCredentials.setRefreshToken(appConfigProps.get("yg.refreshToken"));
                defaultCredentials.setAppSecret(appConfigProps.get("yg.appSecret"));
                defaultCredentials.setGoogleCloudStorage(googleCloudStorage);
            } catch (Exception e) {
                throw new ServiceException(e);
            }
            return defaultCredentials;
        }

        public CriteoCredentials createDefaultCriteoCredentials() {
            CriteoCredentials criteoCredentials = new CriteoCredentials();
            try {
                criteoCredentials.setGoogleCloudStorage(googleCloudStorage);
                criteoCredentials.setPassword(appConfigProps.get("criteo.password"));
                long token = 0L;
                try {
                    token = Long.parseLong(appConfigProps.get("criteo.token"));
                } catch (java.lang.NumberFormatException e) {
                    // What to do here?
                }
                criteoCredentials.setUserName(appConfigProps.get("criteo.userName"));
                criteoCredentials.setToken(appConfigProps.get("criteo.token"));
                criteoCredentials.setSource(appConfigProps.get("criteo.source"));
                criteoCredentials.setGcDir(appConfigProps.get("gcs.dna.criteo.dir"));
            } catch (Exception e) {
                throw new ServiceException(e);
            }
            return criteoCredentials;
        }

        public CommissionJunctionCredentials createDefaultCommissionJunctionCredentials() {
            CommissionJunctionCredentials commissionJunctionCredentials = new CommissionJunctionCredentials();
            try {
                commissionJunctionCredentials.setFolder(appConfigProps.get("cj.email.folder"));
                commissionJunctionCredentials.setHost(appConfigProps.get("cj.email.host"));
                commissionJunctionCredentials.setPassword(appConfigProps.get("cj.email.password"));
                commissionJunctionCredentials.setPort(appConfigProps.get("cj.email.port"));
                commissionJunctionCredentials.setSslEnabled(appConfigProps.get("cj.email.sslEnabled"));
                commissionJunctionCredentials.setUserName(appConfigProps.get("cj.email.userName"));
                commissionJunctionCredentials.setGoogleCloudStorage(googleCloudStorage);
            } catch (Exception e) {
                throw new ServiceException(e);
            }
            return commissionJunctionCredentials;
        }


        public BigQueryService createDefaultBigQueryService() {

            return new BigQueryService(credentials, appConfigProps.get("project.id"),
                    appConfigProps.get("project.appName"));
        }


        public FacebookAdAccountService createDefaultFacebookAdAccountService() {

            if (DnaAppConstants.getFacebookUserId() != null) {
                return new FacebookAdAccountService(
                        new APIContext(
                                DnaAppConstants.getFacebookAdAccessToken(),
                                DnaAppConstants.getFacebookAdAppSecret()).enableDebug(true).setLogger(System.out),
                        DnaAppConstants.getFacebookUserId()
                );
            }

            return null;
        }

        public List<FacebookAdService> createDefaultFacebookAdServices() {

            List<FacebookAdService> fbAdServices = new ArrayList<>();
            int limit;
            String[] accountIds;
            String[] accessTokens;
            String[] appSecrets;

            if (StringUtils.isBlank(this.facebookAccountIdsOverride)) {

                if (DnaAppConstants.hasDynamicAccountIds()) {
                    accountIds = facebookAdAccountService.getAccountList();
                } else {
                    accountIds = DnaAppConstants.getFacebookAdAccountId().split(",");
                }

            } else {

                accountIds = this.facebookAccountIdsOverride.split(",");
                LOG.info("FB using account ids given by parameters: {}", this.facebookAccountIdsOverride);
            }

            // check if there is only one accessToken
            accessTokens = DnaAppConstants.getFacebookAdAccessToken().split(",");
            if (accessTokens.length == 1) {
                // if there is only one accessToken then we
                // are going to copy it for each accountId since
                // we are going to share it among all of them
                String singleAccessToken = accessTokens[0];
                accessTokens = new String[accountIds.length];
                limit = accessTokens.length;
                for (int i = 0; i < limit; i++) {
                    accessTokens[i] = singleAccessToken;
                }
            }

            // check if there is only one appSecret
            appSecrets = DnaAppConstants.getFacebookAdAppSecret().split(",");
            if (appSecrets.length == 1) {
                // if there is only one appSecret then we
                // are going to copy it for each accountId since
                // we are going to share it among all of them
                String singleAppSecret = appSecrets[0];
                appSecrets = new String[accountIds.length];
                limit = appSecrets.length;
                for (int i = 0; i < limit; i++) {
                    appSecrets[i] = singleAppSecret;
                }
            }

            // now create all FB ad account services using their respective
            // credentials (ad account id, access token, etc...)
            this.getFacebookValidateCampaignStatus(true);
            limit = accountIds.length;
            for (int i = 0; i < limit; i++) {

                fbAdServices.add(
                        new FacebookAdService(
                                Long.parseLong(accountIds[i]),
                                accessTokens[i],
                                appSecrets[i],
                                true,
                                this.getFacebookValidateCampaignStatus(false)
                        )
                );
            }

            return fbAdServices;
        }




        public GoogleCloudStorage createDefaultGoogleCloudStorageService() {

            GoogleCloudStorageOptions googleCloudStorageOptions = GoogleCloudStorageOptions.newBuilder()
                    .setProjectId(appConfigProps.get("project.id")).setAppName(appConfigProps.get("project.appName"))
                    .build();

            try {

                return new GoogleCloudStorageImpl(googleCloudStorageOptions, credentials);
            } catch (Exception e) {

                throw new ServiceException(e);
            }
        }

        public DateRange createDefaultDateRange() {
            LocalDate from = appConfigProps.getLocalDate("import.date.from");
            LocalDate to = appConfigProps.getLocalDate("import.date.to");
            return new DateRange(from, to);
        }


        public DnaAppConfig build() {

            if (credentials == null) {
                credentials = createDefaultCredentials();
            }
            if (googleCloudStorage == null) {
                googleCloudStorage = createDefaultGoogleCloudStorageService();
            }
            if (bigQueryService == null) {
                bigQueryService = createDefaultBigQueryService();
            }
            if (facebookAdAccountService == null) {
                facebookAdAccountService = createDefaultFacebookAdAccountService();
            }
            if (facebookAdServices == null) {
                facebookAdServices = createDefaultFacebookAdServices();
            }

            if (importOverwrite == null) {
                importOverwrite = appConfigProps.getBoolean("import.overwrite");
            }
            if (replaceData == null) {
                replaceData = appConfigProps.getBoolean("import.replace");
            }
            if (useIdsFromAPI == null) {
                useIdsFromAPI = appConfigProps.getBoolean("import.useidsfromapi");
            }
            if (dateRange == null || dateRange.from() == null || dateRange.to() == null) {
                dateRange = createDefaultDateRange();
            }
            if (yahooGeminiCredentials == null) {
                yahooGeminiCredentials = createDefaultYahooGeminiCredentials();
            }
            if (criteoCredentials == null) {
                criteoCredentials = createDefaultCriteoCredentials();
            }
            if (commissionJunctionCredentials == null) {
                commissionJunctionCredentials = createDefaultCommissionJunctionCredentials();
            }

            return new DnaAppConfig(this);
        }
    }
}
