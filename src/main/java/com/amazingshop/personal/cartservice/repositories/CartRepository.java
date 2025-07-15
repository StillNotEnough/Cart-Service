package com.amazingshop.personal.cartservice.repositories;

import com.amazingshop.personal.cartservice.models.Cart;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(@NotNull(message = "User ID cannot be null") Long userId);
}
