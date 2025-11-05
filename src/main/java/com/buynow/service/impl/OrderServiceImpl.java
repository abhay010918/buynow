package com.buynow.service.impl;

import com.buynow.dto.OrderItemResponse;
import com.buynow.entity.*;
import com.buynow.enums.OrderStatus;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.CartItemRepository;
import com.buynow.repository.OrderRepository;
import com.buynow.repository.UserRepository;
import com.buynow.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId) {
        // Step 1: Get user & cart items
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Step 2: Create empty order first
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);

        // Step 3: Convert cart items -> order items and link them
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    orderItem.setOrder(order); // ✅ Important line
                    return orderItem;
                })
                .toList();

        // Step 4: Calculate total price
        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        // Step 5: Clear the cart
        cartItemRepository.deleteByUserId(userId);

        // Step 6: Save order (will cascade orderItems)
        orderRepository.save(order);

        OrderResponse response = OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus().name())
                .items(
                        order.getOrderItems().stream().map(
                                item -> OrderItemResponse.builder()
                                        .productId(item.getProduct().getId())
                                        .productName(item.getProduct().getName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .build()
                        ).toList()
                ).build();

        return response;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
