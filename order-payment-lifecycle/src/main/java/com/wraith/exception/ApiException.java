package com.wraith.exception;

/**
 * ApiException - Base exception class for all application-specific exceptions.
 *
 * This exception provides a foundation for custom exception handling with support
 * for error codes and additional details. All application exceptions should extend
 * this class or its subclasses.
 *
 * Production Considerations:
 * - Provides error codes for better error identification
 * - Supports attaching additional context through details
 * - Uses transient keyword for details to avoid serialization issues
 * - Extends RuntimeException for unchecked exception handling
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
public class ApiException extends RuntimeException {
    private final String errorCode;
    private final transient Object details;

    public ApiException(String message) {
        super(message);
        this.errorCode = null;
        this.details = null;
    }

    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(String message, String errorCode, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getDetails() {
        return details;
    }
}
