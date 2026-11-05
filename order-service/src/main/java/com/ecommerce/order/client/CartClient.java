package com.ecommerce.order.client;

import com.ecommerce.order.dto.CartResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;

public interface CartClient {
    @GetExchange("/api/carts/{userId}")
    CartResponse getCartByUserId(@PathVariable Long userId);

    @DeleteExchange("/api/carts/{userId}")
    void clearCart(@PathVariable Long userId);
}
