package com.fp.emailservice.exception;

/**
 * A custom Exception class to wrap other exceptions
 */
public class FPGenericException extends RuntimeException {

    public FPGenericException() {
        super();
    }

    public FPGenericException(String message) {
        super(message);
    }

    public FPGenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
