package com.amazingshop.personal.cartservice.util.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(Long cartId) {
        super("Cart not found with ID: " + cartId);
    }

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}