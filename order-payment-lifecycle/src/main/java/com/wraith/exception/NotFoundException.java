package com.wraith.exception;

/**
 * NotFoundException - Exception for missing resources.
 *
 * This exception is thrown when a requested resource does not exist, such as:
 * - Attempting to fetch a non-existent order
 * - Attempting to operate on a non-existent payment
 * - Resource lookup failures
 *
 * Production Considerations:
 * - Returns HTTP 404 NOT_FOUND to the client
 * - Used for expected, predictable resource not found scenarios
 * - Should always be caught and handled gracefully
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
    public NotFoundException(String message, String errorCode, Object details) {
        super(message, errorCode, details);
    }
}
