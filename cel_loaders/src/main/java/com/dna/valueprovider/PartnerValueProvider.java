package com.dna.valueprovider;

import com.dna.app.config.DnaAppConstants;
import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class PartnerValueProvider implements ValueProvider {

    private static final long serialVersionUID = 2193107380376705088L;
    private String site;
    private String placement;

    public PartnerValueProvider(String site, String placement) {
        // uppercase all values
        this.site = StringUtil.trim(site).toUpperCase();
        // remove "DEACTIVATED_" from any placement name
        this.placement = StringUtil.trim(placement).replaceAll(DnaAppConstants.DEACTIVATED, "").toUpperCase();
    }

    @Override
    public String get() {
        if (site.contains("DART SEARCH : MSN")) return "Bing";
        if (site.contains("DART SEARCH : GOOGLE")) return "Google";
        if (site.contains("YAHOO")) return "Yahoo";
        if (site.contains("OTHER")) return "Google";
        if (site.contains("GOOGLE DISPLAY NET")) return "GDN";
        if (site.contains("CRITEO")) return "Criteo";
        if (site.contains("FACEBOOK")) return "Facebook";
        if (site.contains("SOCIAL")) return "Facebook";
        if (site.contains("SPOT")) return "Spotify";

        if (placement.startsWith("CHANNEL ADVISOR")) {
            return "Channel Advisor";
        }

        if (placement.startsWith("CJ_")) {
            return "Commission Junction";
        }

        if (placement.startsWith("CONNEXITY")) {
            return "Connexity";
        }

        if (placement.startsWith("CONVERSANT")) {
            return "Conversant";
        }

        if (placement.startsWith("POLYVORE")) {
            return "Polyvore";
        }

        if (placement.startsWith("SUNDAYSKY")) {
            return "SundaySky";
        }

        return null;
    }

}
