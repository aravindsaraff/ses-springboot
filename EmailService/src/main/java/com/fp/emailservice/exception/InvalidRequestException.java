package com.fp.emailservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception
 */
public class InvalidRequestException extends Exception {

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(final String message) {
        super(message);
    }
}
