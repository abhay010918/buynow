package com.buynow.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private double price;

}
