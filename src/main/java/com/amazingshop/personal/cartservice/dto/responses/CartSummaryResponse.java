package com.amazingshop.personal.cartservice.util.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartSummaryResponse {
    private int uniqueItemsCount;
    private int totalQuantity;
    private BigDecimal totalPrice;
}