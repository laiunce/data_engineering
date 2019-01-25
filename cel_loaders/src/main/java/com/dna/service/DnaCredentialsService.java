package com.dna.service;

import com.dna.exception.ServiceException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.services.dataflow.DataflowScopes;
import com.google.api.services.doubleclicksearch.Doubleclicksearch;
import com.google.api.services.doubleclicksearch.DoubleclicksearchScopes;
import com.google.api.services.storage.StorageScopes;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author Cristian Laiun
 */
public class DnaCredentialsService {

    private static String[] SCOPES = new String[]{
            BigqueryScopes.BIGQUERY,
            DataflowScopes.CLOUD_PLATFORM,
            DoubleclicksearchScopes.DOUBLECLICKSEARCH,
            StorageScopes.DEVSTORAGE_READ_WRITE,
    };

    /* File for storing user credentials.*/
    private static final File DATA_STORE_FILE = new File(System.getProperty("user.home"), ".credentials/googlecloud");

    /**
     * Generates a credentials by reading client id, client secret and refresh
     * token from the command line. You could update this method to read the
     * data from a database or other secure location.
     */
    public Credential getCredentials(String clientId, String clientSecret, String refreshToken) {
        try {
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = new JacksonFactory();

            return new GoogleCredential.Builder()
                    .setJsonFactory(jsonFactory)
                    .setTransport(transport)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * This method is an alternative to {@link #getCredentials}. It reads
     * client secrets from a {@code client_secrets.json} file, interactively creates
     * the necessary authorization tokens on first run, and stores the tokens in the
     * {@code FileDataStore}. Subsequent runs will no longer require interactivity
     * as long as the file is not removed.
     * You can download the json file in .credentials from the Google API Console.
     * Note that setting the {@link GoogleAuthorizationCodeFlow} access type
     * to {@code offline} is what causes {@code GoogleAuthorizationCodeFlow} to obtain
     * and store refresh tokens.
     */
    
    public Credential generateCredentialInteractively() throws Exception {

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_FILE);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                jsonFactory, new InputStreamReader(
                        Doubleclicksearch.class.getResourceAsStream("/client_secrets.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, jsonFactory, clientSecrets, Arrays.asList(SCOPES))
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
