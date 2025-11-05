package com.buynow.repository;

import com.buynow.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

   List<Order> findByUserId(Long userID);
}