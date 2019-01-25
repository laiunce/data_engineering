package com.dna.service;

import com.dna.exception.ServiceException;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdsInsights;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.Campaign;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Laiun
 */
public class FacebookAdService {

    private static final Logger LOG = LoggerFactory.getLogger(FacebookAdAccountService.class);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final int AD_ACCOUNT_ADMIN_ERROR_CODE = 273;
    private static final int REDUCE_AMOUNT_OF_DATA_YOURE_ASKING_FOR = 1;
    private final String SERVICE_NAME = "Executing Service com.dna.service.FacebookAdService";
    private final HashMap<LocalDate, List<AdsInsights>> adsInsightsMap = new HashMap<>();
    private final HashMap<LocalDate, List<AdSet>> adSetMap = new HashMap<>();
    private final List<Campaign> adCampaignMap = new ArrayList<Campaign>();
    private static final Random RANDOM = new Random();
    private static final int LIMIT = 100;
    private static final int ACCUMULATED_CALLS_LIMIT = 30;
    private static final int WAIT_SOME_TIME_TOP_MILLISECONDS = 6000;
    private static final int ACCUMULATED_CALLS_WAIT_TIME_MILLISECONDS = 60000;
    private int ACCUMULATED_CALLS = 0;

    private final Long accountId;
    private final String accessToken;
    private final String appSecret;
    private final boolean debug;
    private final AdAccount adAccount;
    private final boolean validateCampaignStatus;

    public FacebookAdService(Long accountId, String accessToken, String appSecret, boolean debug, boolean validateCampaignStatus) {

        this.accountId = accountId;
        this.accessToken = accessToken;
        this.appSecret = appSecret;
        this.debug = debug;

        this.adAccount = new AdAccount(
                this.accountId,
                new APIContext(
                        this.accessToken,
                        this.appSecret)
                        .enableDebug(this.debug)
                        .setLogger(System.out)
        );

        this.validateCampaignStatus = validateCampaignStatus;
    }

    public List<AdsInsights> getAds(LocalDate date) {

        if (adsInsightsMap.get(date) == null) {
            adsInsightsMap.put(date, getListInsights(date, date));
        }
        return adsInsightsMap.get(date);
    }

    public List<AdSet> getAdSet(LocalDate date) {

        if (adSetMap.get(date) == null) {
            adSetMap.put(date, getListAdSet(date, date));
        }
        return adSetMap.get(date);
    }

    public List<Campaign> getCampaigns() {

        if (adCampaignMap.isEmpty()) {
            List<Campaign> campaigns = getListAdCampaign();
            adCampaignMap.addAll(campaigns);
        }
        return adCampaignMap;
    }

    private List<AdsInsights> getListInsights(LocalDate from, LocalDate to) {

        List<com.facebook.ads.sdk.Campaign> campaignResults = new ArrayList<>();
        List<AdsInsights> insightResults = new ArrayList<>();
        String dateFrom = from.format(DATE_FORMATTER);
        String dateTo = to.format(DATE_FORMATTER);

        try {

            // first we get the campaigns for the Ad Account
            campaignResults = getCampaigns();

            for (com.facebook.ads.sdk.Campaign campaign : campaignResults) {

                if (this.validateCampaignStatus && (campaign.getFieldEffectiveStatus() != Campaign.EnumEffectiveStatus.VALUE_ACTIVE)) {

                    LOG.info(String.format("Skipping campaign %s, effective status is %s", campaign.getFieldId(), campaign.getFieldEffectiveStatus()));
                    continue;
                }

                LOG.info(String.format("Querying ad insights for campaign %s...", campaign.getFieldId()));

                // then for each campaign we have, we get the ads insights for it
                APINodeList<AdsInsights> getInsightsResults = this.adAccount.getInsights()
                        .setParam("limit", LIMIT)
                        .setTimeRange(String.format("{'since':'%s','until':'%s'}", dateFrom, dateTo))
                        .setFiltering("[ { 'field': 'campaign.id', 'operator': 'EQUAL', 'value': '" + campaign.getId() + "' } ]")
                        .setLevel(AdsInsights.EnumLevel.VALUE_AD)
                        .setBreakdowns("[]")
                        .requestField("account_id")
                        .requestField("account_name")
                        .requestField("actions")
                        .requestField("ad_id")
                        .requestField("ad_name")
                        .requestField("clicks")
                        .requestField("cost_per_action_type")
                        .requestField("ctr")
                        .requestField("cpc")
                        .requestField("cpm")
                        .requestField("cpp")
                        .requestField("adset_name")
                        .requestField("buying_type")
                        //.requestField("call_to_action_clicks") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        .requestField("campaign_id")
                        .requestField("campaign_name")
                        .requestField("canvas_avg_view_percent")
                        .requestField("canvas_avg_view_time")
                        //.requestField("canvas_component_avg_pct_view") // removed per upgrade to API client v3.0.0
                        .requestField("date_start")
                        .requestField("date_stop")
                        .requestField("frequency")
                        .requestField("impressions")
                        .requestField("objective")
                        .requestField("reach")
                        .requestField("spend")
                        //.requestField("total_actions") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        //.requestField("total_unique_actions") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        .requestField("unique_actions")
                        .requestField("unique_clicks")
                        .requestField("video_10_sec_watched_actions")
                        .requestField("video_30_sec_watched_actions")
                        .requestField("video_avg_percent_watched_actions")
                        .requestField("video_avg_time_watched_actions")
                        .requestField("video_p100_watched_actions")
                        .requestField("video_p25_watched_actions")
                        .requestField("video_p50_watched_actions")
                        .requestField("video_p75_watched_actions")
                        .requestField("video_p95_watched_actions")
                        .requestField("inline_link_click_ctr")
                        .requestField("inline_link_clicks")
                        .requestField("inline_post_engagement")
                        //.requestField("social_clicks") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        //.requestField("social_impressions") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        //.requestField("social_reach") // removed per https://developers.facebook.com/docs/graph-api/changelog/breaking-changes/#feb2018
                        .requestField("social_spend")
                        .execute();

                // loop for the rest of pages
                while (getInsightsResults != null) {

                    insightResults.addAll(getInsightsResults);

                    // this validation is to avoid calling the API just to get a null/empty page
                    if (getInsightsResults.size() == LIMIT) {

                        waitSomeTime();
                        getInsightsResults = getInsightsResults.nextPage();

                    } else {
                        getInsightsResults = null;
                    }
                }

                waitSomeTime();
            }

            LOG.info(String.format("Got %s ad insights for ad account %s", insightResults.size(), this.adAccount.getFieldId()));
            return insightResults;

        } catch (APIException e) {

            LOG.error(e.getMessage());
            int errorCode = e.getRawResponseAsJsonObject().get("error").getAsJsonObject().get("code").getAsInt();

            switch (errorCode) {
                case AD_ACCOUNT_ADMIN_ERROR_CODE:
                case REDUCE_AMOUNT_OF_DATA_YOURE_ASKING_FOR:
                    LOG.info(String.format("Email"));
                    return insightResults;
                default:
                    throw new ServiceException(e);
            }
        }
    }

    private List<AdSet> getListAdSet(LocalDate from, LocalDate to) {

        List<AdSet> adSetResults = new ArrayList<>();
        String dateFrom = from.format(DATE_FORMATTER);
        String dateTo = to.format(DATE_FORMATTER);
        try {

            LOG.info(String.format("Querying adSets ..."));

            // then for each campaign we have, we get the ads insights for it
            APINodeList<AdSet> getAdSetResults = this.adAccount.getAdSets()
                    .setParam("limit", LIMIT)
                    .setTimeRange(String.format("{'since':'%s','until':'%s'}", dateFrom, dateTo))
                    .requestField("account_id")
                    .requestField("campaign_id")
                    .requestField("id")
                    .requestField("effective_status")
                    .requestField("daily_budget")
                    .requestField("lifetime_budget")
                    .requestField("budget_remaining")
                    .execute();

            // loop for the rest of pages
            while (getAdSetResults != null) {

                adSetResults.addAll(getAdSetResults);

                // this validation is to avoid calling the API just to get a null/empty page
                if (getAdSetResults.size() == LIMIT) {

                    waitSomeTime();
                    getAdSetResults = getAdSetResults.nextPage();

                } else {
                    getAdSetResults = null;
                }
            }

            LOG.info(String.format("Got %s adSets for ad account %s", adSetResults.size(), this.adAccount.getFieldId()));
            return adSetResults;

        } catch (APIException e) {

            LOG.error(e.getMessage());
            int errorCode = e.getRawResponseAsJsonObject().get("error").getAsJsonObject().get("code").getAsInt();

            switch (errorCode) {
                case AD_ACCOUNT_ADMIN_ERROR_CODE:
                case REDUCE_AMOUNT_OF_DATA_YOURE_ASKING_FOR:
                    LOG.info(String.format("Email"));
                    return adSetResults;
                default:
                    throw new ServiceException(e);
            }
        }

    }

    private List<Campaign> getListAdCampaign() {

        List<Campaign> campaignResults = new ArrayList<>();

        try {

            // first we get the campaigns for the Ad Account
            APINodeList<Campaign> getCampaignResults = this.adAccount.getCampaigns()
                    .setParam("limit", LIMIT)
                    .setDatePreset("lifetime")
                    .requestField("id")
                    .requestField("account_id")
                    .requestField("effective_status")
                    .execute();
            // loop for the rest of pages
            while (getCampaignResults != null) {

                campaignResults.addAll(getCampaignResults);

                // this validation is to avoid calling the API just to get a null/empty page
                if (getCampaignResults.size() == LIMIT) {

                    waitSomeTime();
                    getCampaignResults = getCampaignResults.nextPage();

                } else {
                    getCampaignResults = null;
                }
            }

            LOG.info(String.format("Got %s ad Campaigns for ad account %s", campaignResults.size(), this.adAccount.getFieldId()));
            return campaignResults;

        } catch (APIException e) {

            LOG.error(e.getMessage());
            int errorCode = e.getRawResponseAsJsonObject().get("error").getAsJsonObject().get("code").getAsInt();

            switch (errorCode) {
                case AD_ACCOUNT_ADMIN_ERROR_CODE:
                case REDUCE_AMOUNT_OF_DATA_YOURE_ASKING_FOR:
                    LOG.info(String.format("Email"));
                    return campaignResults;
                default:
                    throw new ServiceException(e);

            }
        }
    }

    private void waitSomeTime() {

        try {

            // wait to avoid "User request limit reached" error
            if (ACCUMULATED_CALLS < ACCUMULATED_CALLS_LIMIT) {

                Thread.sleep(RANDOM.nextInt(WAIT_SOME_TIME_TOP_MILLISECONDS));

            } else {

                LOG.info(String.format(
                        "Got to %s calls to the API, will wait %s seconds before continuing...",
                        ACCUMULATED_CALLS_LIMIT,
                        (ACCUMULATED_CALLS_WAIT_TIME_MILLISECONDS / 1000))
                );
                Thread.sleep(ACCUMULATED_CALLS_WAIT_TIME_MILLISECONDS);
                ACCUMULATED_CALLS = 0;
            }

            ACCUMULATED_CALLS++;

        } catch (InterruptedException ex) {

            LOG.error(ex.getMessage());
            throw new ServiceException(ex);
        }
    }
}
