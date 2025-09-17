package com.amazingshop.personal.cartservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartsResponse {
    private List<CartResponse> carts;
    private Integer totalCarts;
}
