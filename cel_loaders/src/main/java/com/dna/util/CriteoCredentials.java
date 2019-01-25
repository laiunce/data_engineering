package com.dna.util;

import com.google.cloud.hadoop.gcsio.GoogleCloudStorage;

public class CriteoCredentials {
	private String userName = "";
	private String password = "";
	private String source = "";
	private String token = "";
	private String gcDir = "";

	GoogleCloudStorage googleCloudStorage;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public GoogleCloudStorage getGoogleCloudStorage() {
		return googleCloudStorage;
	}

	public void setGoogleCloudStorage(GoogleCloudStorage googleCloudStorage) {
		this.googleCloudStorage = googleCloudStorage;
	}

	public String getGcDir() {
		return gcDir;
	}

	public void setGcDir(String gcDir) {
		this.gcDir = gcDir;
	}
}
