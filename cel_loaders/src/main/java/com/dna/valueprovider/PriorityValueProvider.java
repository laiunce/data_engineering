package com.dna.valueprovider;

import com.dna.util.StringUtil;

/**
 * @author Cristian Laiun

 */
public class PriorityValueProvider implements ValueProvider {

    private String paidSearchCampaign;

    public PriorityValueProvider(String paidSearchCampaign) {
        this.paidSearchCampaign = StringUtil.trim(paidSearchCampaign).toUpperCase();
    }

    @Override
    public String get() {
        return Boolean.toString(paidSearchCampaign.contains("BRAND"));
    }

}
