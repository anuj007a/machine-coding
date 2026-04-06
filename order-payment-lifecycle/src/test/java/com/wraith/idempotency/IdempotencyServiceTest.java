package com.wraith.idempotency;

import com.wraith.domain.model.IdempotencyRecord;
import com.wraith.repository.IdempotencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdempotencyServiceTest {

    @Mock
    private IdempotencyRepository idempotencyRepository;

    @InjectMocks
    private IdempotencyService idempotencyService;

    private final String testKey = "test-key-123";
    private final String testResponse = "{\"status\":\"SUCCESS\"}";

    @Test
    void get_returnsResponse_whenKeyExists() {
        IdempotencyRecord record = new IdempotencyRecord(testKey, null, testResponse, null);
        when(idempotencyRepository.findById(testKey)).thenReturn(Optional.of(record));

        Optional<String> response = idempotencyService.get(testKey);

        assertTrue(response.isPresent());
        assertEquals(testResponse, response.get());
        verify(idempotencyRepository, times(1)).findById(testKey);
    }

    @Test
    void get_returnsEmpty_whenKeyDoesNotExist() {
        when(idempotencyRepository.findById(testKey)).thenReturn(Optional.empty());

        Optional<String> response = idempotencyService.get(testKey);

        assertFalse(response.isPresent());
        verify(idempotencyRepository, times(1)).findById(testKey);
    }

    @Test
    void save_persistsRecord() {
        idempotencyService.save(testKey, testResponse);

        verify(idempotencyRepository, times(1)).save(argThat(record -> 
            record.getKey().equals(testKey) && 
            record.getResponse().equals(testResponse) &&
            record.getCreatedAt() != null
        ));
    }
}