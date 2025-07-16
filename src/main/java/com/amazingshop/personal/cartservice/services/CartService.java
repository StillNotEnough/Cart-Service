package com.amazingshop.personal.cartservice.services;

import com.amazingshop.personal.cartservice.dto.CartRequest;
import com.amazingshop.personal.cartservice.dto.CartResponse;
import com.amazingshop.personal.cartservice.dto.ProductDTO;
import com.amazingshop.personal.cartservice.models.Cart;
import com.amazingshop.personal.cartservice.models.CartItem;
import com.amazingshop.personal.cartservice.repositories.CartItemRepository;
import com.amazingshop.personal.cartservice.repositories.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CartService(CartRepository cartRepository, RestTemplate restTemplate, CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
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
        ResponseEntity<ProductDTO> productResponse = restTemplate.getForEntity(
          "http://product-service/products/", ProductDTO.class);
        if (userResponse.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException("Товар не найден");
        }

        ProductDTO product = productResponse.getBody();

        if (product.getStock() < request.getQuantity()){
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        // Ищем или создаем новую корзину
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                   Cart newCart = new Cart();
                   newCart.setUserId(request.getUserId());
                   return newCart;
                });

        // Чтобы явно указать что это финальная версия cart
        Cart finalCart = cart;

        // Проверяем или создаем CartItem
        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getId(),
                request.getProductId()).orElseGet(() -> {
           CartItem newCartItem = new CartItem();
           newCartItem.setCart(finalCart);
           newCartItem.setProductId(request.getProductId());
           newCartItem.setUnitPrice(product.getPrice());
           return newCartItem;
        });
        cartItem.setQuantity(request.getQuantity());

        // Добавить или обновить CartItem
        if (cartItem.getId() == null){
            cart.getCartItems().add(cartItem);
        }
        cart = cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new IllegalArgumentException("Корзина не найдена"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public Cart getCart(Long cartId){
        return cartRepository.findByUserId(cartId).orElseThrow(() ->
                new IllegalArgumentException("Корзина не найдена"));
    }

    public CartResponse convertedToCartResponse(Cart cart){
        return modelMapper.map(cart, CartResponse.class);
    }
}