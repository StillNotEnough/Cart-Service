package com.amazingshop.personal.cartservice.controllers;

import com.amazingshop.personal.cartservice.dto.CartRequest;
import com.amazingshop.personal.cartservice.dto.CartResponse;
import com.amazingshop.personal.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request){
        return ResponseEntity.ok(cartService.convertedToCartResponse(cartService.addToCart(request)));
    }

    @GetMapping("/{cartsId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long cartsId){
        return ResponseEntity.ok(cartService.convertedToCartResponse(cartService.getCart(cartsId)));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}