package com.amazingshop.personal.cartservice.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CartItemResponse {

    private Long id;
    private Long productId;
    private Integer quantity;
    private String productName;
    private String productImage;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
