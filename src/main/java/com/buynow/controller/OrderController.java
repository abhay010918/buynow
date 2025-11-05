package com.buynow.controller;

import com.buynow.entity.Order;
import com.buynow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Order createOrder(@RequestParam Long userId) {
        return orderService.createOrder(userId);
    }

    @GetMapping
    public List<Order> getUserOrders(@RequestParam Long userId) {
        return orderService.getUserOrders(userId);
    }
}
