package com.amazingshop.personal.cartservice.services;

import com.amazingshop.personal.cartservice.dto.CartRequest;
import com.amazingshop.personal.cartservice.models.Cart;
import com.amazingshop.personal.cartservice.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CartService(CartRepository cartRepository, RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Cart addToCart(CartRequest request) {

        // Проверка пользователя
        ResponseEntity<String> userResponse = restTemplate.getForEntity(
                "http://auth-service/users" + request.getUserId(), String.class);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        // Проверка товара
        ResponseEntity<>


    }

    @Transactional
    public void clearCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public Cart getCart(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);

        return cart.orElseThrow(); // todo what exception should be throw
    }

    // Метод 1: Добавить товар в корзину
    @Transactional
    public CartResponse addTfoCart(CartRequest request) {
        // Проверка пользователя
        ResponseEntity<String> userResponse = restTemplate.getForEntity(
                "http://auth-service/users/" + request.getUserId(), String.class);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        // Проверка товара
        ResponseEntity<ProductDto> productResponse = restTemplate.getForEntity(
                "http://product-service/products/" + request.getProductId(), ProductDto.class);
        if (productResponse.getStatusCode() != HttpStatus.OK || productResponse.getBody() == null) {
            throw new IllegalArgumentException("Товар не найден");
        }
        ProductDto product = productResponse.getBody();
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        // Поиск или создание корзины
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(request.getUserId());
                    return newCart;
                });

        // Проверка существующего CartItem
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProductId(request.getProductId());
                    newItem.setUnitPrice(product.getPrice());
                    return newItem;
                });
        cartItem.setQuantity(request.getQuantity());

        // Добавление или обновление CartItem
        if (cartItem.getId() == null) {
            cart.getItems().add(cartItem);
        }
        cart = cartRepository.save(cart);

        return toCartResponse(cart);
    }

}


