package com.ecommerce.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal price,
        BigDecimal subTotal,
        Integer quantity
) { }
