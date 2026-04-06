package com.wraith.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.wraith.domain.model.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByOrderId(String orderId);

    @Query("SELECT COALESCE(SUM(p.amount),0) FROM Payment p WHERE p.orderId = :orderId AND p.type = 'CAPTURE'")
    Long totalCaptured(@Param("orderId") String orderId);

    @Query("SELECT COALESCE(SUM(p.amount),0) FROM Payment p WHERE p.orderId = :orderId AND p.type = 'REFUND'")
    Long totalRefunded(@Param("orderId") String orderId);

    Payment findByIdempotencyKey(String idempotencyKey);
}