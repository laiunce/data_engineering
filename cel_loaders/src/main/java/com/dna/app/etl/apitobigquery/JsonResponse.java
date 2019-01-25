
package com.dna.app.etl.apitobigquery;

/**
 *
 * @author Cristian Laiun

 */
public abstract class JsonResponse {

    private transient String jsonResponse;

    public String getJsonResponse() {
        return this.jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }
}
