package com.amazingshop.personal.cartservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CartResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Items cannot be null")
    private List<CartItemResponse> items;

    @CreatedDate
    @NotNull(message = "Created date cannot be null")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotNull(message = "Updated date cannot be null")
    private LocalDateTime updatedAt;
}
