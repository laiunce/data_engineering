package com.dna.valueprovider;

import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class ChannelValueProvider implements ValueProvider {

    private String site;
    private String campaign;
    private String placement;

    public ChannelValueProvider(String site, String campaign, String placement) {
        this.site = StringUtil.trim(site).toUpperCase();
        this.campaign = StringUtil.trim(campaign).toUpperCase();
        this.placement = StringUtil.trim(placement).toUpperCase();
    }

    @Override
    public String get() {
        if (placement.startsWith("CONNEXITY") || placement.startsWith("CHANNEL ADVISOR")) {
            return "Shopping";
        }
        if (!site.contains("DART SEARCH")) return "Display";
        if (campaign.contains("SHOPPING")) {
            return "Shopping";
        }
        return "Display";
    }

}
