package com.buynow.service;


import com.buynow.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userID);

    List<Order> getUserOrders(Long userId);

}
