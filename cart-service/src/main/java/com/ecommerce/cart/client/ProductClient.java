package com.ecommerce.cart.client;

import com.ecommerce.cart.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductClient {
    @GetExchange("/api/products/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}
