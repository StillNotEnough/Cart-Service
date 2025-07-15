package com.amazingshop.personal.cartservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created date cannot be null")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @NotNull(message = "Updated date cannot be null")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    public Cart(){

    }

    public Cart(Long userId, LocalDateTime createdAt, LocalDateTime updatedAt, List<CartItem> cartItems) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cartItems = cartItems;
    }
}
