package com.tennistime.bff.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested service is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Service not found")
public class ServiceNotFoundException extends RuntimeException {

    /**
     * Default constructor.
     */
    public ServiceNotFoundException() {
        super();
    }

    /**
     * Constructor with a custom error message.
     *
     * @param message the custom error message.
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     *
     * @param message the custom error message.
     * @param cause   the cause of the exception.
     */
    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause the cause of the exception.
     */
    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }
}
