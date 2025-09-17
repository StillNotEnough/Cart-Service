package com.amazingshop.personal.cartservice.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotNull(message = "Unit price cannot be null")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
}
