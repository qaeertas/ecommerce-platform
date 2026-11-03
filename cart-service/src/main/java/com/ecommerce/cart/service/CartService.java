package com.ecommerce.cart.service;

import com.ecommerce.cart.client.ProductClient;
import com.ecommerce.cart.dto.CartItemRequest;
import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.ProductResponse;
import com.ecommerce.cart.exception.InsufficientStockException;
import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductClient productClient;

    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return newCart;
        });
        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        ProductResponse product = productClient.getProductById(request.productId());

        if (product.stockQuantity() < request.quantity()) {
            throw new InsufficientStockException("Not enough stock for product: " + product.name());
        }

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return newCart;
        });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.productId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();

            Integer totalQuantity = item.getQuantity() + request.quantity();
            if (totalQuantity > product.stockQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.name());
            }

            item.setQuantity(totalQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.productId());
            newItem.setQuantity(request.quantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item: cart.getItems()) {
            ProductResponse product = productClient.getProductById(item.getProductId());

            BigDecimal subTotal = product.price()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalPrice = totalPrice.add(subTotal);

            itemResponses.add(new CartItemResponse(
                    item.getProductId(),
                    product.name(),
                    product.price(),
                    item.getQuantity(),
                    subTotal
            ));
        }
        return new CartResponse(cart.getId(), cart.getUserId(), itemResponses, totalPrice);
    }
}
