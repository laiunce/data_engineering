package com.dna.util;

import com.dna.exception.ServiceException;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author Cristian Laiun

 */
public class RunUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RunUtil.class);

    private RunUtil() {
    }

    public static <T> T doAndRetry(Callable<T> callback, int retries) {
        int currentTry = 0;
        ServiceException sex = null;
        while (currentTry < retries) {
            currentTry++;
            try {
                return callback.call();
            } catch (Exception ex) {
                sex = new ServiceException(ex);
            }
        }
        throw Preconditions.checkNotNull(sex);
    }

    public static <T> T doAndRetry(Callable<T> callback) {
        return doAndRetry(callback, 3);
    }

    public static void doAndRetry(Runnable runnable, int retries) {
        int currentTry = 0;
        ServiceException sex = null;
        while (currentTry < retries) {
            currentTry++;
            try {
                runnable.run();
                return;
            } catch (Exception ex) {
                sex = new ServiceException(ex);
                LOG.warn("Could not run process. Will retry.", ex);
            }
        }
        throw Preconditions.checkNotNull(sex);
    }

    public static void doAndRetry(Runnable runnable) {
        doAndRetry(runnable, 3);
    }

    public static <T> T justDo(Callable<T> callback) {
        return doAndRetry(callback, 1);
    }


}
