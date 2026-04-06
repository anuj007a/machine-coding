package com.wraith.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wraith.domain.model.IdempotencyRecord;

public interface IdempotencyRepository extends JpaRepository<IdempotencyRecord, String> {
}