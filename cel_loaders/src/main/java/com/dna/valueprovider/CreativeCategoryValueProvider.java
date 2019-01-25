package com.dna.valueprovider;

import com.dna.app.config.DnaAppConstants;
import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class CreativeCategoryValueProvider implements ValueProvider {

    private String site;
    private String campaign;
    private String placement;
    private String creative;

    public CreativeCategoryValueProvider(String site, String campaign, String placement, String creative) {
        this.site = StringUtil.trim(site).toUpperCase();
        this.campaign = StringUtil.trim(campaign).toUpperCase();
        // remove "DEACTIVATED_" from any placement name
        this.placement = StringUtil.trim(placement).replaceAll(DnaAppConstants.DEACTIVATED, "").toUpperCase();
        this.creative = StringUtil.trim(creative).toUpperCase();
    }

    @Override
    public String get() {

        if (site.contains("CRITEO")) return "Dynamic";
        if (site.contains("SPOTIFY")) return "Bra";

        if (site.contains("KINETIC SOCIAL") || site.contains("FACEBOOK")) {
            String[] placementArray = placement.split("_");
            if (placement.contains("_DPA_")) {
                if (placement.contains("LOVE")) return "Beauty";
                if (placement.contains("PINK")) return "PINK";
                if (placement.contains("SPORT")) return "Sport";
                if (placement.contains("VSL")) return "VSL";
                if (placement.contains("BEAUTY")) return "Beauty";
                return "Dynamic";
            }
            if (placementArray.length > 2) return placementArray[2].replaceAll("MOB", "");
        }

        if (site.contains("GOOGLE DISPLAY NETWORK")) {
            String[] creativeArray = creative.split("_");
            if (creativeArray.length > 1) return creativeArray[1];
        }

        if (placement.contains("BRAND+PRODUCT")) return "Brand+Product";
        if (placement.contains("AFFILIATED BRANDS")) return "Brand+Product";

        if (placement.contains("BRAND")) return "Brand";
        if (placement.contains("PINK")) return "Pink";
        if (placement.contains("SPORT")) return "Sport";

        if (placement.contains("BRAS")) return "Bra";
        if (placement.contains("BRALETTES")) return "Bra";

        if (placement.contains("PANT")) return "Panty";
        if (placement.contains("SLEEP")) return "Panty";

        if (placement.contains("BEAUTY")) return "Beauty";
        if (placement.contains("SWIM")) return "Swim";
        if (placement.contains("CLOTHING")) return "Clothing";
        if (placement.contains("LOUNGE")) return "Lounge";

        if (placement.contains("OFFER")) return "TotalBox";
        if (placement.contains("TOTAL BOX")) return "TotalBox";

        if (placement.contains("DYNAMIC")) return "Dynamic";
        if (placement.contains("ALL PRODUCT")) return "Dynamic";

        if (placement.contains("LEGACY")) return "Legacy International";
        if (placement.contains("LINGER")) return "Lingerie";
        if (placement.contains("PAJAMA")) return "Sleep";

        if (placement.contains("BEACHWEAR")) return "Clothing";
        if (placement.contains("COVER")) return "Clothing";
        if (placement.contains("TOPS")) return "Clothing";
        if (placement.contains("YOGA")) return "Clothing";
        if (placement.contains("LEGGING")) return "Clothing";

        if (placement.contains("CONTENT IMAGE")) return "Other";
        if (placement.contains("HOSIERY")) return "Panty";

        return "TotalBox";
    }

}
