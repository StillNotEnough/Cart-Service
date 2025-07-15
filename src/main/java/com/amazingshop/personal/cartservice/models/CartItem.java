package com.amazingshop.personal.cartservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    @NotNull(message = "Unit price cannot be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
    private Cart cart;

    public CartItem(){

    }

    public CartItem(Long productId, Integer quantity, BigDecimal unitPrice, Cart cart) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.cart = cart;
    }
}