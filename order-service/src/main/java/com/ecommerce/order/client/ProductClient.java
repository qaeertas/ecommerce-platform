package com.ecommerce.order.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PutExchange;

public interface ProductClient {
    @PutExchange("/api/products/{id}/reduce-stock")
    void reduceStock(@PathVariable Long id, @RequestParam Integer quantity);
}
