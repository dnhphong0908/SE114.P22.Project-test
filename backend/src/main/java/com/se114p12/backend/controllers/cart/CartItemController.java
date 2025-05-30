package com.se114p12.backend.controllers.cart;

import com.se114p12.backend.dtos.cart.CartItemRequestDTO;
import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.services.cart.CartItemService;
import com.se114p12.backend.vo.PageVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "cart-item-module")
@RestController
@RequestMapping("/api/v1/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<PageVO<CartItemResponseDTO>> getAllCartItems(Pageable pageable) {
        return ResponseEntity.ok(cartItemService.getAllCartItems(pageable));
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> createCartItem(@Valid @RequestBody CartItemRequestDTO dto) {
        return ResponseEntity.ok(cartItemService.createCartItem(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> updateCartItem(
            @PathVariable Long id,
            @Valid @RequestBody CartItemRequestDTO dto) {
        return ResponseEntity.ok(cartItemService.updateCartItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}