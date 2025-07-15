package com.amazingshop.personal.cartservice.util;

import com.amazingshop.personal.cartservice.dto.CartResponse;
import lombok.Data;

import java.util.List;

@Data
public class CartsResponse {
    private List<CartResponse> carts;

    public CartsResponse(List<CartResponse> carts) {
        this.carts = carts;
    }
}
