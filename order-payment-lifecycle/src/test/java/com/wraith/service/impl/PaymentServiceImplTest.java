package com.wraith.service.impl;

import com.wraith.domain.enums.OrderStatus;
import com.wraith.domain.enums.PaymentStatus;
import com.wraith.domain.enums.PaymentType;
import com.wraith.domain.model.Order;
import com.wraith.domain.model.Payment;
import com.wraith.idempotency.IdempotencyService;
import com.wraith.repository.OrderRepository;
import com.wraith.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order testOrder;
    private final String orderId = "order-123";
    private final String idempotencyKey = "key-123";

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(orderId)
                .amount(1000L)
                .currency("INR")
                .status(OrderStatus.CREATED)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void createOrder_success() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order createdOrder = paymentService.createOrder(1000L, "INR");

        assertNotNull(createdOrder);
        assertEquals(1000L, createdOrder.getAmount());
        assertEquals("INR", createdOrder.getCurrency());
        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void authorize_success() {
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));
        
        Payment savedPayment = Payment.builder()
                .id("pay-123")
                .orderId(orderId)
                .amount(1000L)
                .type(PaymentType.AUTH)
                .status(PaymentStatus.SUCCESS)
                .build();
                
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        Payment payment = paymentService.authorize(orderId, 1000L, idempotencyKey);

        assertNotNull(payment);
        assertEquals(PaymentType.AUTH, payment.getType());
        assertEquals(OrderStatus.AUTHORIZED, testOrder.getStatus());
        verify(idempotencyService, times(1)).save(eq(idempotencyKey), anyString());
    }

    @Test
    void authorize_invalidState_throwsException() {
        testOrder.setStatus(OrderStatus.AUTHORIZED);
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.authorize(orderId, 1000L, idempotencyKey);
        });

        assertEquals("Invalid state for authorization", exception.getMessage());
    }

    @Test
    void capture_success() {
        testOrder.setStatus(OrderStatus.AUTHORIZED);
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.totalCaptured(orderId)).thenReturn(0L);

        Payment savedPayment = Payment.builder()
                .id("pay-456")
                .orderId(orderId)
                .amount(1000L)
                .type(PaymentType.CAPTURE)
                .status(PaymentStatus.SUCCESS)
                .build();
                
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        Payment payment = paymentService.capture(orderId, 1000L, idempotencyKey);

        assertNotNull(payment);
        assertEquals(PaymentType.CAPTURE, payment.getType());
        assertEquals(OrderStatus.CAPTURED, testOrder.getStatus());
        verify(idempotencyService, times(1)).save(eq(idempotencyKey), anyString());
    }

    @Test
    void capture_alreadyCaptured_throwsException() {
        testOrder.setStatus(OrderStatus.AUTHORIZED);
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.totalCaptured(orderId)).thenReturn(1000L); // Already captured

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.capture(orderId, 1000L, idempotencyKey);
        });

        assertEquals("Already captured", exception.getMessage());
    }

    @Test
    void refund_success() {
        testOrder.setStatus(OrderStatus.CAPTURED);
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.totalCaptured(orderId)).thenReturn(1000L);
        when(paymentRepository.totalRefunded(orderId)).thenReturn(0L);

        Payment savedRefund = Payment.builder()
                .id("pay-789")
                .orderId(orderId)
                .amount(500L)
                .type(PaymentType.REFUND)
                .status(PaymentStatus.SUCCESS)
                .build();
                
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedRefund);

        Payment refund = paymentService.refund(orderId, 500L, idempotencyKey);

        assertNotNull(refund);
        assertEquals(PaymentType.REFUND, refund.getType());
        assertEquals(500L, refund.getAmount());
    }

    @Test
    void refund_exceedsCapturedAmount_throwsException() {
        testOrder.setStatus(OrderStatus.CAPTURED);
        when(idempotencyService.get(idempotencyKey)).thenReturn(Optional.empty());
        when(orderRepository.findByIdForUpdate(orderId)).thenReturn(Optional.of(testOrder));
        when(paymentRepository.totalCaptured(orderId)).thenReturn(1000L);
        when(paymentRepository.totalRefunded(orderId)).thenReturn(600L); // 400 available

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentService.refund(orderId, 500L, idempotencyKey); // Trying to refund 500
        });

        assertEquals("Refund exceeds captured amount", exception.getMessage());
    }

    @Test
    void reconciliation_success() {
        Instant now = Instant.now();
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        LocalDate today = now.atZone(zone).toLocalDate();

        Payment capturePayment = Payment.builder()
                .amount(1000L)
                .type(PaymentType.CAPTURE)
                .createdAt(now)
                .build();

        Payment refundPayment = Payment.builder()
                .amount(200L)
                .type(PaymentType.REFUND)
                .createdAt(now)
                .build();

        when(paymentRepository.findAll()).thenReturn(List.of(capturePayment, refundPayment));

        Map<String, Object> result = paymentService.reconciliation("Asia/Kolkata");

        assertNotNull(result);
        assertTrue(result.containsKey("report"));
        
        @SuppressWarnings("unchecked")
        Map<LocalDate, Map<String, Long>> report = (Map<LocalDate, Map<String, Long>>) result.get("report");
        
        assertTrue(report.containsKey(today));
        assertEquals(1000L, report.get(today).get("capture"));
        assertEquals(200L, report.get(today).get("refund"));
    }

    @Test
    void getOrder_returnsOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));

        Order order = paymentService.getOrder(orderId);

        assertNotNull(order);
        assertEquals(orderId, order.getId());
    }

    @Test
    void getOrder_throwsNoSuchElementException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            paymentService.getOrder(orderId);
        });
    }

    @Test
    void getPayments_returnsListOfPayments() {
        Payment payment = Payment.builder().id("pay-1").build();
        when(paymentRepository.findByOrderId(orderId)).thenReturn(List.of(payment));

        List<Payment> payments = paymentService.getPayments(orderId);

        assertNotNull(payments);
        assertEquals(1, payments.size());
        assertEquals("pay-1", payments.get(0).getId());
    }
}