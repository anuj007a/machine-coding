package com.wraith.exception;

/**
 * BusinessException - Exception for business logic violations.
 *
 * This exception is thrown when a business rule is violated, such as:
 * - Attempting to capture before authorization
 * - Attempting to refund more than the captured amount
 * - Invalid order state transitions
 *
 * Production Considerations:
 * - Returns HTTP 400 BAD_REQUEST to the client
 * - Used for expected, predictable business constraint violations
 * - Should always be caught and handled gracefully
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }
    public BusinessException(String message, String errorCode, Object details) {
        super(message, errorCode, details);
    }
}
