package com.buynow.controller;

import com.buynow.entity.Order;
import com.buynow.dto.OrderResponse;
import com.buynow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderResponse createOrder(@RequestParam Long userId) {
        return orderService.createOrder(userId);
    }

    @GetMapping
    public List<Order> getUserOrders(@RequestParam Long userId) {
        return orderService.getUserOrders(userId);
    }
}
