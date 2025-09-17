package com.amazingshop.personal.cartservice.controllers;

import com.amazingshop.personal.cartservice.dto.requests.CartRequest;
import com.amazingshop.personal.cartservice.dto.requests.UpdateQuantityRequest;
import com.amazingshop.personal.cartservice.services.CartService;
import com.amazingshop.personal.cartservice.dto.responses.CartResponse;
import com.amazingshop.personal.cartservice.dto.responses.CartSummaryResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Validated
@Slf4j
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartRequest request){
        log.info("Adding item to cart for user: {}, product: {}", request.getUserId(), request.getProductId());
        CartResponse response = cartService.convertToCartResponse(cartService.addToCart(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable @Positive Long userId){
        log.info("Getting cart for user: {}", userId);
        CartResponse response = cartService.convertToCartResponse(cartService.getCartByUserId(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable @Positive Long cartId){
        log.info("Getting cart by ID: {}", cartId);
        CartResponse response = cartService.convertToCartResponse(cartService.getCartById(cartId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable @Positive Long userId,
                                                           @PathVariable @Positive Long productId,
                                                           @Valid @RequestBody UpdateQuantityRequest request){
        log.info("Updating quantity for user: {}, product: {}, quantity: {}",
                userId, productId, request.getQuantity());
        CartResponse response = cartService.convertToCartResponse(
                cartService.updateCartItemQuantity(userId, productId, request.getQuantity()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable @Positive Long userId,
                                                   @PathVariable @Positive Long productId){
        log.info("Removing item from cart for user: {}, product: {}", userId, productId);
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<CartResponse> clearCart(@PathVariable @Positive Long userId){
        log.info("Clearing cart for user: {}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<CartSummaryResponse> getCartSummary(@PathVariable @Positive Long userId) {
        log.info("Getting cart item count for user: {}", userId);
        CartSummaryResponse response = cartService.getCartSummary(userId);
        return ResponseEntity.ok(response);
    }
}