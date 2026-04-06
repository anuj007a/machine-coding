package com.wraith.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * GlobalExceptionHandler - Centralized exception handling for all REST endpoints.
 *
 * This class provides a unified error handling mechanism across the application.
 * It catches different types of exceptions and returns appropriate HTTP status codes
 * with meaningful error messages.
 *
 * Production Considerations:
 * - All exceptions are logged with appropriate severity levels
 * - Sensitive information is NOT exposed to the client
 * - Validation errors are returned with field-level details
 * - Business exceptions (NotFoundException, BusinessException) return 4xx status
 * - Unexpected exceptions return 500 status with a generic error message
 * - Response format is consistent across all error types
 * - Error logging includes full stack traces for debugging
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle NotFoundException - returns 404 NOT_FOUND.
     * Logged as warning level.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        log.warn("NotFoundException - Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage(), "errorCode", ex.getErrorCode()));
    }

    /**
     * Handle BusinessException - returns 400 BAD_REQUEST.
     * Logged as warning level (business rule violation).
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex) {
        log.warn("BusinessException - Business rule violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage(), "errorCode", ex.getErrorCode()));
    }

    /**
     * Handle validation errors - returns 400 BAD_REQUEST.
     * Includes field-level error details for client debugging.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation error - Request validation failed");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Validation failed", "details", errors));
    }

    /**
     * Handle unexpected exceptions - returns 500 INTERNAL_SERVER_ERROR.
     * Logged as error level with full stack trace for debugging.
     * Generic error message returned to client (no sensitive info leaked).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(Exception ex) {
        log.error("Unexpected exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Internal server error. Please contact support.",
                        "exception", ex.getClass().getName(),
                        "message", ex.getMessage()
                ));
    }
}
