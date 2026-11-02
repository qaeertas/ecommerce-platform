package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        String name,
        @Size(max = 1000)
        String description,
        @Positive
        @NotNull
        BigDecimal price,
        @PositiveOrZero
        @NotNull
        Integer stockQuantity
) { }
