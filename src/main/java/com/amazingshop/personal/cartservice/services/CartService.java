package com.amazingshop.personal.cartservice.services;

import com.amazingshop.personal.cartservice.dto.CartRequest;
import com.amazingshop.personal.cartservice.dto.ProductDTO;
import com.amazingshop.personal.cartservice.models.Cart;
import com.amazingshop.personal.cartservice.models.CartItem;
import com.amazingshop.personal.cartservice.repositories.CartItemRepository;
import com.amazingshop.personal.cartservice.repositories.CartRepository;
import com.amazingshop.personal.cartservice.util.responses.CartResponse;
import com.amazingshop.personal.cartservice.util.responses.CartSummaryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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
                "http://auth-service/users/" + request.getUserId(), String.class);
        if (userResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        // Проверка товара
        ResponseEntity<ProductDTO> productResponse = restTemplate.getForEntity(
                "http://product-service/products/", ProductDTO.class);
        if (productResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Товар не найден");
        }

        ProductDTO product = productResponse.getBody();

        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Недостаточно товара на складе");
        }

        // Ищем или создаем новую корзину
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart(request.getUserId());
                    return cartRepository.save(newCart); // Сохраняем сразу для получения ID
                });

        // Чтобы явно указать что это финальная версия cart
        Cart finalCart = cart;

        // Проверяем или создаем CartItem
        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(
                cart.getId(), request.getProductId()).orElse(null);

        if (cartItem != null) {
            // Товар уже есть - обновляем количество
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            // Новый товар - создаем CartItem
            cartItem = new CartItem(
                    request.getProductId(), request.getQuantity(), product.getPrice(), cart);

            cart.getCartItems().add(cartItem);
        }
        // Сохраняем корзину (cascade сохранит и CartItem)
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItemQuantity(Long userId, Long productId, Integer quantity){
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(
                        cart.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден в корзине"));

        if (quantity <= 0){
            // Удаляем товар из корзины
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }else {
            // Обновляем количество
            cartItem.setQuantity(quantity);
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(Long userId, Long productId){
        Cart cart = getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(
                        cart.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден в корзине"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("Корзина не найдена"));
    }

    public Cart getCartById(Long cartId){
        return cartRepository.findById(cartId).orElseThrow(() ->
                new IllegalArgumentException("Корзина не найдена"));
    }

    private BigDecimal calculateTotalPrice(Cart cart){
        return cart.getCartItems().stream()
                .map(item -> item.getUnitPrice()
                                .multiply(BigDecimal.
                                        valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Integer calculateTotalQuantity(Cart cart){
        return cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    private Integer getUniqueItemsCount(Cart cart) {
        return cart.getCartItems().size();
    }

    public BigDecimal calculateTotalPrice(Long userId) {
        Cart cart = getCartByUserId(userId);
        return calculateTotalPrice(cart);
    }

    public Integer calculateTotalQuantity(Long userId) {
        Cart cart = getCartByUserId(userId);
        return calculateTotalQuantity(cart);
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

    public CartSummaryResponse getCartSummary(Long userId) {
        Cart cart = getCartByUserId(userId);

        return new CartSummaryResponse(getUniqueItemsCount(cart),
                calculateTotalQuantity(cart),
                calculateTotalPrice(cart));
    }

}