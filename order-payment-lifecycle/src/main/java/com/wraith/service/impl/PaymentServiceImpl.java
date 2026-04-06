package com.wraith.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wraith.domain.enums.OrderStatus;
import com.wraith.domain.enums.PaymentStatus;
import com.wraith.domain.enums.PaymentType;
import com.wraith.domain.model.Order;
import com.wraith.domain.model.Payment;
import com.wraith.repository.OrderRepository;
import com.wraith.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wraith.idempotency.IdempotencyService;
import com.wraith.service.PaymentService;


import java.time.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PaymentServiceImpl - Business logic implementation for order and payment operations.
 *
 * This service implements the core business rules for the payment lifecycle including:
 * - Order creation and status management
 * - Idempotent payment authorization, capture, and refund operations
 * - Prevention of double capture and over-refunds
 * - Timezone-aware reconciliation reporting
 *
 * Production Considerations:
 * - All state-changing operations are transactional (@Transactional)
 * - Idempotency is enforced using idempotency keys stored in cache/database
 * - Pessimistic locking is used on orders to prevent race conditions
 * - All monetary amounts are stored as Long (cents/paise) to avoid float precision issues
 * - Comprehensive logging for audit trail and debugging
 * - Exception handling with custom exceptions for better error reporting
 *
 * @author Payment Services Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final IdempotencyService idempotencyService;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public Order createOrder(Long amount, String currency) {
        log.info("Creating order: amount={}, currency={}", amount, currency);
        try {
            Order order = orderRepository.save(Order.builder()
                    .amount(amount)
                    .currency(currency)
                    .status(OrderStatus.CREATED)
                    .createdAt(Instant.now())
                    .build());
            log.info("Order created successfully: orderId={}, amount={}", order.getId(), amount);
            return order;
        } catch (Exception e) {
            log.error("Failed to create order: amount={}, currency={}", amount, currency, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Payment authorize(String orderId, Long amount, String key) {
        log.info("Authorizing payment: orderId={}, amount={}, idempotencyKey={}", orderId, amount, key);
        try {
            Optional<String> cached = idempotencyService.get(key);
            if (cached.isPresent()) {
                log.info("Idempotent request detected: returning cached response for key={}", key);
                return deserialize(cached.get(), Payment.class);
            }

            Order order = orderRepository.findByIdForUpdate(orderId).orElseThrow();

            if (order.getStatus() != OrderStatus.CREATED) {
                log.warn("Invalid order state for authorization: orderId={}, currentStatus={}", orderId, order.getStatus());
                throw new RuntimeException("Invalid state for authorization");
            }

            Payment payment = savePayment(orderId, amount, PaymentType.AUTH, key);
            order.setStatus(OrderStatus.AUTHORIZED);

            idempotencyService.save(key, serialize(payment));
            log.info("Payment authorized successfully: paymentId={}, orderId={}", payment.getId(), orderId);
            return payment;
        } catch (Exception e) {
            log.error("Authorization failed: orderId={}, amount={}", orderId, amount, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Payment capture(String orderId, Long amount, String key) {
        log.info("Capturing payment: orderId={}, amount={}, idempotencyKey={}", orderId, amount, key);
        try {
            Optional<String> cached = idempotencyService.get(key);
            if (cached.isPresent()) {
                log.info("Idempotent request detected: returning cached response for key={}", key);
                return deserialize(cached.get(), Payment.class);
            }

            Order order = orderRepository.findByIdForUpdate(orderId).orElseThrow();

            if (order.getStatus() != OrderStatus.AUTHORIZED) {
                log.warn("Capture not allowed: orderId={}, currentStatus={}", orderId, order.getStatus());
                throw new RuntimeException("Capture allowed only after authorization");
            }

            Long captured = paymentRepository.totalCaptured(orderId);
            if (captured > 0) {
                log.warn("Double capture attempt detected: orderId={}, previousCapturedAmount={}", orderId, captured);
                throw new RuntimeException("Already captured");
            }

            Payment payment = savePayment(orderId, amount, PaymentType.CAPTURE, key);
            order.setStatus(OrderStatus.CAPTURED);

            idempotencyService.save(key, serialize(payment));
            log.info("Payment captured successfully: paymentId={}, orderId={}", payment.getId(), orderId);
            return payment;
        } catch (Exception e) {
            log.error("Capture failed: orderId={}, amount={}", orderId, amount, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Payment refund(String orderId, Long amount, String key) {
        log.info("Processing refund: orderId={}, refundAmount={}, idempotencyKey={}", orderId, amount, key);
        try {
            Optional<String> cached = idempotencyService.get(key);
            if (cached.isPresent()) {
                log.info("Idempotent request detected: returning cached response for key={}", key);
                return deserialize(cached.get(), Payment.class);
            }

            orderRepository.findByIdForUpdate(orderId).orElseThrow();

            Long captured = paymentRepository.totalCaptured(orderId);
            Long refunded = paymentRepository.totalRefunded(orderId);
            Long availableForRefund = captured - refunded;

            if (amount > availableForRefund) {
                log.warn("Refund exceeds available amount: orderId={}, refundAmount={}, availableAmount={}",
                    orderId, amount, availableForRefund);
                throw new RuntimeException("Refund exceeds captured amount");
            }

            Payment refund = savePayment(orderId, amount, PaymentType.REFUND, key);
            idempotencyService.save(key, serialize(refund));
            log.info("Refund processed successfully: paymentId={}, orderId={}, refundAmount={}",
                refund.getId(), orderId, amount);
            return refund;
        } catch (Exception e) {
            log.error("Refund failed: orderId={}, amount={}", orderId, amount, e);
            throw e;
        }
    }

    @Override
    public List<Payment> getPayments(String orderId) {
        log.debug("Fetching payment history for orderId={}", orderId);
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public Order getOrder(String orderId) {
        log.debug("Fetching order for orderId={}", orderId);
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public Map<String, Object> reconciliation(String timezone) {
        log.info("Generating reconciliation report: timezone={}", timezone);
        try {
            ZoneId zone = ZoneId.of(timezone);
            List<Payment> payments = paymentRepository.findAll();
            Map<LocalDate, Map<String, Long>> report = new HashMap<>();

            for (Payment p : payments) {
                LocalDate date = p.getCreatedAt().atZone(zone).toLocalDate();
                report.putIfAbsent(date, new HashMap<>());
                Map<String, Long> daily = report.get(date);

                if (p.getType() == PaymentType.CAPTURE) {
                    daily.merge("capture", p.getAmount(), Long::sum);
                }
                if (p.getType() == PaymentType.REFUND) {
                    daily.merge("refund", p.getAmount(), Long::sum);
                }
            }

            log.info("Reconciliation report generated successfully: timezone={}, daysIncluded={}",
                timezone, report.size());
            return Map.of("report", report);
        } catch (Exception e) {
            log.error("Reconciliation report generation failed: timezone={}", timezone, e);
            throw e;
        }
    }

    private Payment savePayment(String orderId, Long amount, PaymentType type, String key) {
        return paymentRepository.save(Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .type(type)
                .status(PaymentStatus.SUCCESS)
                .idempotencyKey(key)
                .createdAt(Instant.now())
                .build());
    }

    private String serialize(Object obj) {
        try { return mapper.writeValueAsString(obj); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    private <T> T deserialize(String str, Class<T> cls) {
        try { return mapper.readValue(str, cls); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

}

