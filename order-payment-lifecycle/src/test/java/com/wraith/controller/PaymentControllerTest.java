package com.wraith.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wraith.domain.enums.OrderStatus;
import com.wraith.domain.enums.PaymentStatus;
import com.wraith.domain.enums.PaymentType;
import com.wraith.domain.model.Order;
import com.wraith.domain.model.Payment;
import com.wraith.dto.AmountRequest;
import com.wraith.dto.CreateOrderRequest;
import com.wraith.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order testOrder;
    private Payment testPayment;
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

        testPayment = Payment.builder()
                .id("pay-123")
                .orderId(orderId)
                .amount(1000L)
                .type(PaymentType.AUTH)
                .status(PaymentStatus.SUCCESS)
                .idempotencyKey(idempotencyKey)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void createOrder_validRequest_returns201() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(1000L, "INR");

        when(paymentService.createOrder(1000L, "INR")).thenReturn(testOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void createOrder_invalidRequest_returns400() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(null, "INR"); // amount is required

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authorize_validRequest_returns200() throws Exception {
        AmountRequest request = new AmountRequest(1000L);

        when(paymentService.authorize(eq(orderId), eq(1000L), eq(idempotencyKey))).thenReturn(testPayment);

        mockMvc.perform(post("/orders/{id}/authorize", orderId)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pay-123"))
                .andExpect(jsonPath("$.type").value("AUTH"));
    }

    @Test
    void capture_validRequest_returns200() throws Exception {
        AmountRequest request = new AmountRequest(1000L);
        testPayment.setType(PaymentType.CAPTURE);

        when(paymentService.capture(eq(orderId), eq(1000L), eq(idempotencyKey))).thenReturn(testPayment);

        mockMvc.perform(post("/orders/{id}/capture", orderId)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pay-123"))
                .andExpect(jsonPath("$.type").value("CAPTURE"));
    }

    @Test
    void refund_validRequest_returns200() throws Exception {
        AmountRequest request = new AmountRequest(500L);
        testPayment.setType(PaymentType.REFUND);
        testPayment.setAmount(500L);

        when(paymentService.refund(eq(orderId), eq(500L), eq(idempotencyKey))).thenReturn(testPayment);

        mockMvc.perform(post("/orders/{id}/refund", orderId)
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pay-123"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.type").value("REFUND"));
    }

    @Test
    void getOrder_returns200() throws Exception {
        when(paymentService.getOrder(orderId)).thenReturn(testOrder);
        when(paymentService.getPayments(orderId)).thenReturn(List.of(testPayment));

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.payments[0].id").value("pay-123"));
    }

    @Test
    void reconciliation_returns200() throws Exception {
        Map<String, Object> mockReport = Map.of("report", Map.of());
        when(paymentService.reconciliation("Asia/Kolkata")).thenReturn(mockReport);

        mockMvc.perform(get("/orders/reconciliation")
                .param("timezone", "Asia/Kolkata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.report").exists());
    }
}