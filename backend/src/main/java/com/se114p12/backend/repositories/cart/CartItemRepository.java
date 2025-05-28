package com.se114p12.backend.repositories.cart;

import com.se114p12.backend.entities.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>,
        JpaSpecificationExecutor<CartItem> {
    List<CartItem> findAllByCartIdAndProductId(Long cartId, Long productId);
}
