package com.wraith.controller;

import com.wraith.domain.model.Order;
import com.wraith.domain.model.Payment;
import com.wraith.dto.AmountRequest;
import com.wraith.dto.CreateOrderRequest;
import com.wraith.dto.OrderResponse;
import com.wraith.dto.PaymentResponse;
import com.wraith.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * PaymentController - REST API endpoints for Order Payment Lifecycle operations.
 *
 * This controller handles all HTTP requests related to order and payment management.
 * All write operations (authorize, capture, refund) are idempotent and require
 * an "Idempotency-Key" header to prevent duplicate processing.
 *
 * Production Considerations:
 * - All endpoints are logged for audit and debugging purposes
 * - Validation is enforced at the DTO level using Jakarta Validation
 * - Error handling is centralized through GlobalExceptionHandler
 * - API documentation is available via Swagger/OpenAPI endpoints
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Payment Lifecycle", description = "APIs for managing order payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService service;

    // Helper method to map Order entity to OrderResponse DTO
    private OrderResponse toOrderResponse(Order order, List<Payment> payments) {
        return OrderResponse.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .currency(order.getCurrency())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .createdAt(order.getCreatedAt())
                .payments(payments != null ? payments.stream().map(this::toPaymentResponse).toList() : null)
                .build();
    }

    // Helper method to map Payment entity to PaymentResponse DTO
    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .type(payment.getType() != null ? payment.getType().name() : null)
                .amount(payment.getAmount())
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .idempotencyKey(payment.getIdempotencyKey())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest req) {
        log.info("Creating order: amount={}, currency={}", req.getAmount(), req.getCurrency());
        Order order = service.createOrder(req.getAmount(), req.getCurrency());
        log.info("Order created successfully: orderId={}", order.getId());
        // No payments at creation
        return ResponseEntity.status(HttpStatus.CREATED).body(toOrderResponse(order, List.of()));
    }

    @PostMapping("/{id}/authorize")
    @Operation(summary = "Authorize payment for an order (idempotent)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment authorized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order state or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<PaymentResponse> authorize(
            @PathVariable String id,
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody AmountRequest req) {
        log.info("Authorizing payment: orderId={}, amount={}, idempotencyKey={}", id, req.getAmount(), key);
        Payment payment = service.authorize(id, req.getAmount(), key);
        log.info("Payment authorized successfully: paymentId={}, orderId={}", payment.getId(), id);
        return ResponseEntity.ok(toPaymentResponse(payment));
    }

    @PostMapping("/{id}/capture")
    @Operation(summary = "Capture payment for an order (idempotent, prevents double capture)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment captured successfully"),
            @ApiResponse(responseCode = "400", description = "Capture not allowed or already captured"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<PaymentResponse> capture(
            @PathVariable String id,
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody AmountRequest req) {
        log.info("Capturing payment: orderId={}, amount={}, idempotencyKey={}", id, req.getAmount(), key);
        Payment payment = service.capture(id, req.getAmount(), key);
        log.info("Payment captured successfully: paymentId={}, orderId={}", payment.getId(), id);
        return ResponseEntity.ok(toPaymentResponse(payment));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund payment for an order (idempotent, supports partial/full refunds)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refund processed successfully"),
            @ApiResponse(responseCode = "400", description = "Refund amount exceeds captured amount"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<PaymentResponse> refund(
            @PathVariable String id,
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody AmountRequest req) {
        log.info("Processing refund: orderId={}, amount={}, idempotencyKey={}", id, req.getAmount(), key);
        Payment payment = service.refund(id, req.getAmount(), key);
        log.info("Refund processed successfully: paymentId={}, orderId={}", payment.getId(), id);
        return ResponseEntity.ok(toPaymentResponse(payment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch order details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        log.debug("Fetching order details for orderId={}", id);
        Order order = service.getOrder(id);
        List<Payment> payments = service.getPayments(id);
        return ResponseEntity.ok(toOrderResponse(order, payments));
    }

    @GetMapping("/{id}/payments")
    @Operation(summary = "Fetch complete payment history for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment history retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<List<PaymentResponse>> getPayments(@PathVariable String id) {
        log.debug("Fetching payment history for orderId={}", id);
        List<Payment> payments = service.getPayments(id);
        return ResponseEntity.ok(payments.stream().map(this::toPaymentResponse).toList());
    }

    @GetMapping("/reconciliation")
    @Operation(summary = "Generate daily reconciliation report for a given timezone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reconciliation report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid timezone")
    })
    public ResponseEntity<Map<String, Object>> reconciliation(
            @RequestParam(defaultValue = "Asia/Kolkata") String timezone) {
        log.info("Generating reconciliation report: timezone={}", timezone);
        Map<String, Object> report = service.reconciliation(timezone);
        log.info("Reconciliation report generated successfully");
        return ResponseEntity.ok(report);
    }
}