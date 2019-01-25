package com.dna.util;

import com.google.cloud.hadoop.gcsio.GoogleCloudStorage;

public class CommissionJunctionCredentials {
	private String userName = "";
	private String password = "";
	private String host = "";
	private String port = "";
	private String sslEnabled = "";
	private String folder = "";
	
	private GoogleCloudStorage storageService;

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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(String sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public GoogleCloudStorage getGoogleCloudStorage() {
		return storageService;
	}

	public void setGoogleCloudStorage(GoogleCloudStorage storageService) {
		this.storageService = storageService;
	}
}
