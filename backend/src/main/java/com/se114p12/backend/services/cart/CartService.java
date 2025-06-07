package com.se114p12.backend.services.cart;

import com.se114p12.backend.entities.cart.Cart;

public interface CartService {
    Cart getCartById(Long id);

    Cart create(Cart cart);

    void delete(Long id);

    boolean existsByIdAndUserId(Long id, Long userId);
}