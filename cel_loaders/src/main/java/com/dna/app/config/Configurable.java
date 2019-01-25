package com.dna.app.config;

/**
 * @author Cristian Laiun

 */
public interface Configurable {
    void setAppConfig(DnaAppConfig appConfig);
    DnaAppConfig getAppConfig();
}
