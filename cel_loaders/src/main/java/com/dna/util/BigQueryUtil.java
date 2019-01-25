package com.dna.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.Job;

/**
 * @author Cristian Laiun

 */
public class BigQueryUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BigQueryUtil.class);

	public static Job pollJob(Bigquery.Jobs.Get request, long interval) throws IOException, InterruptedException {
		Job job = request.execute();
		while (!job.getStatus().getState().equals("DONE")) {
			LOG.debug("Job is {}, waiting {} milliseconds...", job.getStatus().getState(), interval);
			Thread.sleep(interval);
			job = request.execute();
		}
		return job;
	}

    public static String sanitizeFieldName(String s) {
        return s.replaceAll("[-| |\\x00]", "_").toUpperCase().trim();
    }

}
