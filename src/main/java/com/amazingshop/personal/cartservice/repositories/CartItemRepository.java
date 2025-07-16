package com.amazingshop.personal.cartservice.repositories;

import com.amazingshop.personal.cartservice.models.CartItem;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByCartIdAndProductId
            (Long cartId, @NotNull(message = "Product ID cannot be null") Long productId);

}
