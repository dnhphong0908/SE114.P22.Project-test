package com.se114p12.backend.controllers.cart;

import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.services.cart.CartServiceImpl;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart Module")
@RequestMapping("/api/v1/carts")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final CartServiceImpl cartService;
    private final JwtUtil jwtUtil;

    //Use this method to test APIs
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id) {
        return cartRepository.existsById(id)
                ? ResponseEntity.ok(cartService.getCartById(id))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cart> createNewCart(@Valid @RequestBody Cart cart) {
        return ResponseEntity.ok().body(cartService.create(cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@Valid @PathVariable("id") Long id) {
        if (!cartService.existsByIdAndUserId(id, jwtUtil.getCurrentUserId())) {
            return ResponseEntity.notFound().build();
        }
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
