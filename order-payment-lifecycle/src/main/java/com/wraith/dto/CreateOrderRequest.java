package com.wraith.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull
    @Positive
    @Schema(description = "Order amount", example = "1000")
    private Long amount;
    @NotBlank
    @Schema(description = "Order currency", example = "INR")
    private String currency;
}