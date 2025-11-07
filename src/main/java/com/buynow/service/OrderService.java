package com.buynow.service;


import com.buynow.entity.Order;
import com.buynow.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(Long userID);

    List<Order> getUserOrders(Long userId);

}
