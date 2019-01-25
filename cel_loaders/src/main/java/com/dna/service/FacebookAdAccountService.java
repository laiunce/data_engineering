package com.dna.service;

import com.dna.exception.ServiceException;
import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.User;

/**
 * @author Cristian Laiun
 */
public class FacebookAdAccountService {
    
    private final User user;
    
    public FacebookAdAccountService(APIContext apiContext, String userId) {
        this.user = new User(userId,apiContext);
    }
         
    public String[] getAccountList() {
        // set up array to hold results
        String[] adAccountResults;
        try {
            // get initial resultsRef
            APINodeList<AdAccount> result = this.user.getAdAccounts()
                    .requestField("account_id")
                    .execute();
            
            // loop to set the result on a List
            adAccountResults = new String[result.size()];
            
            for (int index = 0; index < result.size(); index++){
                adAccountResults[index] = result.get(index).getFieldAccountId();
            }
            
            return adAccountResults;
        } catch (APIException e) {
            throw new ServiceException(e);
        }
        
    }
    
}
