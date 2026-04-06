package com.wraith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

/**
 * Order Payment Lifecycle Service - Spring Boot Application Entry Point.
 * This service manages the complete lifecycle of order payments with support for:
 * - Order creation and management
 * - Payment authorization, capture, and refund operations
 * - Idempotent API operations
 * - Daily reconciliation reports with timezone support
 */
@SpringBootApplication
public class OrderPaymentLifecycleApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderPaymentLifecycleApplication.class);

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        log.info("Starting Order Payment Lifecycle Service...");
        SpringApplication app = new SpringApplication(OrderPaymentLifecycleApplication.class);

        // Set custom banner
        app.setBanner((environment, sourceClass, out) -> {
            out.println("╔════════════════════════════════════════════════════════╗");
            out.println("║   Order Payment Lifecycle Management Service v1.0.0    ║");
            out.println("║            ================================            ║");
            out.println("╚════════════════════════════════════════════════════════╝");
        });
        // Run the application
        try {
            app.run(args);
        } catch (Exception e) {
            log.error("Failed to start Order Payment Lifecycle Service", e);
            System.exit(1);
        }
    }

    /**
     * Logs when the application has started successfully.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String[] profiles = environment.getActiveProfiles();
        String activeProfile = profiles.length > 0 ? String.join(", ", profiles) : "default";

        log.info("========== Application Startup Summary ==========");
        log.info("Service Name: Order Payment Lifecycle Management");
        log.info("Version: 1.0.0");
        log.info("Active Profile(s): {}", activeProfile);
        log.info("Server Port: {}", environment.getProperty("server.port"));
        log.info("Database URL: {}", maskSensitiveUrl(environment.getProperty("spring.datasource.url")));
        log.info("JPA Dialect: {}", environment.getProperty("spring.jpa.database-platform"));
        log.info("Hibernate DDL: {}", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        log.info("H2 Console: {}", environment.getProperty("spring.h2.console.enabled"));
        log.info("Swagger/API Docs: http://localhost:{}/swagger-ui.html", environment.getProperty("server.port"));
        log.info("H2 Database Console: http://localhost:{}/h2-console", environment.getProperty("server.port"));
        log.info("====================================================");
        log.info("✓ Order Payment Lifecycle Service is READY for requests!");
    }

    /**
     * Logs when the application fails to start.
     */
    @EventListener(ApplicationFailedEvent.class)
    public void onApplicationFailed(ApplicationFailedEvent event) {
        log.error("✗ Application failed to start", event.getException());
    }

    /**
     * Masks sensitive database URL information for logging.
     */
    private String maskSensitiveUrl(String url) {
        if (url == null) return "N/A";
        if (url.contains("password")) {
            return url.replaceAll("password=.*", "password=****");
        }
        return url;
    }
}