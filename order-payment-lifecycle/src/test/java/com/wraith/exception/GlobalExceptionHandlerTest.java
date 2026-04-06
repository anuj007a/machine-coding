package com.wraith.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        NotFoundException ex = new NotFoundException("Order not found", "ORDER_NOT_FOUND");
        
        ResponseEntity<?> response = handler.handleNotFound(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Order not found", body.get("error"));
        assertEquals("ORDER_NOT_FOUND", body.get("errorCode"));
    }

    @Test
    void handleBusiness_returns400() {
        BusinessException ex = new BusinessException("Invalid state", "INVALID_STATE");
        
        ResponseEntity<?> response = handler.handleBusiness(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid state", body.get("error"));
        assertEquals("INVALID_STATE", body.get("errorCode"));
    }

    @Test
    void handleValidation_returns400_withDetails() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError = new FieldError("objectName", "amount", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        ResponseEntity<?> response = handler.handleValidation(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Validation failed", body.get("error"));
        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) body.get("details");
        assertEquals("must not be null", details.get("amount"));
    }

    @Test
    void handleOther_returns500() {
        RuntimeException ex = new RuntimeException("Unexpected db error");
        
        ResponseEntity<?> response = handler.handleOther(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Internal server error. Please contact support.", body.get("error"));
        assertEquals("java.lang.RuntimeException", body.get("exception"));
        assertEquals("Unexpected db error", body.get("message"));
    }
}