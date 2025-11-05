package com.buynow.service.impl;

import com.buynow.entity.CartItem;
import com.buynow.entity.Order;
import com.buynow.entity.OrderItem;
import com.buynow.entity.User;
import com.buynow.enums.OrderStatus;
import com.buynow.exeptions.ResourceNotFoundException;
import com.buynow.repository.CartItemRepository;
import com.buynow.repository.OrderRepository;
import com.buynow.repository.UserRepository;
import com.buynow.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public Order createOrder(Long userId) {

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if(cartItems.isEmpty()){
            throw  new RuntimeException("cart is empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        List<OrderItem> orderItems = cartItems.stream().map(
                cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                     orderItem.setPrice(cartItem.getProduct().getPrice());
                     return orderItem;
                }
        ).toList();

        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);
        order.setOrderStatus(OrderStatus.CREATED);

        cartItemRepository.deleteByUserId(userId); // clear cart
        return orderRepository.save(order);

    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
