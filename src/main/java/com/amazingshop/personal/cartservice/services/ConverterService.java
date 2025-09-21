package com.amazingshop.personal.cartservice.services;

import com.amazingshop.personal.cartservice.dto.responses.CartResponse;
import com.amazingshop.personal.cartservice.models.Cart;
import com.amazingshop.personal.cartservice.models.CartItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConverterService {

    private final ModelMapper modelMapper;

    @Autowired
    public ConverterService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CartResponse convertToCartResponse(Cart cart) {

        CartResponse response = modelMapper.map(cart, CartResponse.class);

        // Добавляем вычисляемые поля
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantity = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        response.setTotalPrice(totalPrice);
        response.setTotalQuantity(totalQuantity);

        return response;
    }
}