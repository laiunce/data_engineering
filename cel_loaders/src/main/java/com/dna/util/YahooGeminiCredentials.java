package com.dna.util;

import com.google.cloud.hadoop.gcsio.GoogleCloudStorage;

public class YahooGeminiCredentials {

	private String advertId = "";
	private String campId = "";
	private String endpoint = "";

	private String accountId = "";
	private String refreshToken = "";
	private String appSecret = "";

	GoogleCloudStorage googleCloudStorage;

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getCampId() {
		return campId;
	}

	public void setCampId(String campId) {
		this.campId = campId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public GoogleCloudStorage getGoogleCloudStorage() {
		return googleCloudStorage;
	}

	public void setGoogleCloudStorage(GoogleCloudStorage googleCloudStorage) {
		this.googleCloudStorage = googleCloudStorage;
	}
}
