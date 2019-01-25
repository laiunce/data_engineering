package com.dna.exception;

/**
 * @author Cristian Laiun

 */
public class PipelineNotFoundException extends RuntimeException {
    public PipelineNotFoundException() {
    }

    public PipelineNotFoundException(String message) {
        super(message);
    }

    public PipelineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineNotFoundException(Throwable cause) {
        super(cause);
    }

    public PipelineNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}