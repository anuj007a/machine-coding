package com.wraith.service;

import com.wraith.domain.model.Order;
import com.wraith.domain.model.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    Order createOrder(Long amount, String currency);
    Payment authorize(String orderId, Long amount, String key);
    Payment capture(String orderId, Long amount, String key);
    Payment refund(String orderId, Long amount, String key);
    List<Payment> getPayments(String orderId);
    Order getOrder(String orderId);
    Map<String, Object> reconciliation(String timezone);
}