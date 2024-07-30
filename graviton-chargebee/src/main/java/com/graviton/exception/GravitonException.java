package com.graviton.exception;

/**
 * Custom exception class for the Graviton application.
 * It extends RuntimeException to provide unchecked exceptions
 * for various error conditions in the application.
 */
public class GravitonException extends RuntimeException {

    /**
     * Constructor for GravitonException.
     *
     * @param message The error message to be associated with the exception.
     */
    public GravitonException(String message) {
        super(message);
    }

    /**
     * Exception thrown when a customer is not found.
     */
    public static class CustomerNotFoundException extends GravitonException {
        public CustomerNotFoundException(String customerId) {
            super("Customer not found: " + customerId);
        }
    }

    /**
     * Exception thrown when a package is not found.
     */
    public static class PackageNotFoundException extends GravitonException {
        public PackageNotFoundException(String packageName) {
            super("Package not found: " + packageName);
        }
    }

    /**
     * Exception thrown when a service is not found.
     */
    public static class ServiceNotFoundException extends GravitonException {
        public ServiceNotFoundException(String serviceName) {
            super("Service not found: " + serviceName);
        }
    }

    /**
     * Exception thrown when there are insufficient credits.
     */
    public static class InsufficientCreditsException extends GravitonException {
        public InsufficientCreditsException(double requiredCredits, double availableCredits) {
            super("Insufficient credits: Required " + requiredCredits + ", but available is " + availableCredits);
        }
    }
}
