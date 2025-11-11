package com.buynow.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class createPaymentRequest {

    private Long amount;
    private String currency;
    private String orderId;
}
