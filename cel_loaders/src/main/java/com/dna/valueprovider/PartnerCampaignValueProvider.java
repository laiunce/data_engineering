package com.dna.valueprovider;

import com.dna.app.config.DnaAppConstants;
import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class PartnerCampaignValueProvider implements ValueProvider {

    private static final long serialVersionUID = -6353053233746340203L;

    private String partner;
    private String campaign;
    private String campaignOriginal;
    private String placement;

    public PartnerCampaignValueProvider(String partner, String campaign, String placement) {
        this.partner = StringUtil.trim(partner).toUpperCase();
        this.campaign = StringUtil.trim(campaign).toUpperCase();
        this.campaignOriginal = StringUtil.trim(campaign);
        // remove "DEACTIVATED_" from any placement name
        this.placement = StringUtil.trim(placement).replaceAll(DnaAppConstants.DEACTIVATED, "").toUpperCase();
    }

    @Override
    public String get() {

        /*
        Criteo campaign names:
            Web - Mid Funnel (30+ Lookback) - Pink
            Web - Mid Funnel (30+ Lookback) - Victoria Sport
            Web - Mid Funnel (30+ Lookback) - VSL
            Web - Lower Funnel - Pink
            Web - Lower Funnel - Victoria Sport
            Web - Lower Funnel - VSL
         */
        
        // Criteo logic
        if (partner.contains("CRITEO")) {
            
            StringBuilder sb = new StringBuilder();
            
            if (placement.contains("LOOKBACK")) {
                sb.append("Lookback");
            } else {
                sb.append("Lower Funnel");
            }
            
            if (placement.contains("PINK")) {
                sb.append("%PINK");
                return sb.toString();
            } else if (placement.contains("SPORT")) {
                sb.append("%Victoria Sport");
                return sb.toString();
            } else {
                sb.append("%VSL");
                return sb.toString();
            }
        }
        
        return null;
    }

}
