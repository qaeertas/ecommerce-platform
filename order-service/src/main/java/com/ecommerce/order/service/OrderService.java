package com.ecommerce.order.service;

import com.ecommerce.order.client.CartClient;
import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.CartResponse;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.exception.EmptyCartException;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse placeOrder(Long userId) {
        CartResponse cart = cartClient.getCartByUserId(userId);

        if (cart.items() == null || cart.items().isEmpty()) {
            throw new EmptyCartException("Cart of user: " + userId + " is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(cart.totalPrice());
        order.setStatus(OrderStatus.CREATED);

        for (CartItemResponse cartItem : cart.items()) {
            productClient.reduceStock(cartItem.productId(), cartItem.quantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.productId());
            orderItem.setProductName(cartItem.productName());
            orderItem.setQuantity(cartItem.quantity());
            orderItem.setPrice(cartItem.price());

            order.addItem(orderItem);
        }

        orderRepository.save(order);
        cartClient.clearCart(userId);

        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse getOrderById(Long orderId) {
        return orderMapper.toOrderResponse(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId)));
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }
}
