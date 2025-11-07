package com.buynow.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private double totalPrice;
    private String orderStatus;
    private List<OrderItemResponse> items;

}
