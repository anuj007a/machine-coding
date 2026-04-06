package com.wraith.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.wraith.domain.model.IdempotencyRecord;
import com.wraith.repository.IdempotencyRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repo;

    public Optional<String> get(String key) {
        return repo.findById(key).map(IdempotencyRecord::getResponse);
    }

    public void save(String key, String response) {
        repo.save(IdempotencyRecord.builder()
                .key(key)
                .response(response)
                .createdAt(Instant.now())
                .build());
    }
}