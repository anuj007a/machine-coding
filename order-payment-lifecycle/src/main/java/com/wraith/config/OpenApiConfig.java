package com.wraith.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration - Provides custom OpenAPI documentation.
 *
 * This configuration class sets up OpenAPI/Swagger documentation for the REST API.
 * It includes service information, contact details, licensing, and server configurations.
 *
 * Accessible at:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 *
 * Production Considerations:
 * - Customized API documentation with service details
 * - Server endpoints configured for dev and prod
 * - Contact and license information for API consumers
 * - Clear API title and description
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Payment Lifecycle Service API")
                        .version("1.0.0")
                        .description("""
                                REST API for managing the complete lifecycle of order payments.
                                
                                **Key Features:**
                                - Order creation and management
                                - Idempotent payment authorization, capture, and refund
                                - Prevention of double capture and over-refunds
                                - Daily reconciliation reports with timezone support
                                - Comprehensive audit logging
                                
                                **Important Notes:**
                                - All write operations (authorize, capture, refund) require an "Idempotency-Key" header
                                - Amounts are specified in the smallest currency unit (e.g., cents/paise)
                                - All timestamps are in UTC
                                """));
    }
}

