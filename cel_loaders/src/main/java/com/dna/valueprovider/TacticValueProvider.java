package com.dna.valueprovider;

import com.dna.app.config.DnaAppConstants;
import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class TacticValueProvider implements ValueProvider {

    private static final long serialVersionUID = -1730765298244278781L;
    private String site;
    private String campaign;
    private String placement;

    public TacticValueProvider(String site, String campaign, String placement) {
        this.site = StringUtil.trim(site).toUpperCase();
        this.campaign = StringUtil.trim(campaign).toUpperCase();
        // remove "DEACTIVATED_" from any placement name
        this.placement = StringUtil.trim(placement).replaceAll(DnaAppConstants.DEACTIVATED, "").toUpperCase();
    }

    @Override
    public String get() {

        if (placement.startsWith("CONNEXITY") || placement.startsWith("CHANNEL ADVISOR")) {
            return "CSE";
        }

        if (campaign.contains("SHOPPING")) {
            return "CSE";
        }

        if (campaign.contains("BRAND+PRODUCT")) {
            return "Search";
        }

        if (campaign.contains("BRAND")) {
            return "Search";
        }

        if (campaign.contains("PRODUCT")) {
            return "Product Search";
        }

        if (!site.contains("DART SEARCH")) {

            if (site.contains("KINETIC SOCIAL") || site.contains("FACEBOOK")) {

                if (placement.contains("LOOKALIKE") || placement.contains("LAL")) {
                    return "Acquisition";
                }

                if (placement.contains("0TO6MONTHACH") || placement.contains("6TO12MONTHACH")) {
                    return "Angel Card Test";
                }

                if (placement.contains("WCA")) {
                    return "Remarketing (offer)";
                }

                if (placement.contains("DPA")) {
                    return "Remarketing (product)";
                }

                if (placement.contains("CUSTOMAUD") || placement.contains("BUYERS")) {
                    return "Retention";
                }
            }

            if (site.contains("GOOGLE DISPLAY NETWORK")) {
                return "Remarketing";
            }
        }

        if (placement.contains("REMARKETING")) {
            return "Remarketing";
        }

        if (placement.contains("PROSPECTING")) {
            return "Prospecting";
        }

        if (placement.contains("SPOTIFY")) {
            return "Prospecting";
        }

        if (campaign.contains("DYNAMIC")) {
            return "Product Search";
        }

        if (campaign.contains("DSA")) {
            return "Product Search";
        }

        if (campaign.contains("RM - CONTENT")) {
            return "Product Search";
        }

        if (campaign.contains("RM - TRENDS")) {
            return "Product Search";
        }

        if (placement.startsWith("CONVERSANT")) {

            if (placement.contains("ACQ_")) {
                return "Prospecting";
            } else {
                return "Retention";
            }
        }

        if (placement.startsWith("CJ_")) {
            return "Affiliate";
        }

        if (placement.startsWith("POLYVORE")) {
            return "Shopping";
        }

        if (placement.startsWith("SUNDAYSKY")) {
            return "Remarketing";
        }

        return null;
    }
}
