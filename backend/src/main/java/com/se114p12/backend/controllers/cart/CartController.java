package com.se114p12.backend.controllers.cart;

import com.se114p12.backend.annotations.ErrorResponse;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.services.cart.CartService;
import com.se114p12.backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart Module", description = "APIs for managing user carts")
@RequestMapping("/api/v1/carts")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Get current user's cart",
            description = "Retrieve the cart that belongs to the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found for current user")
    })
    @ErrorResponse
    @GetMapping("/me")
    public ResponseEntity<Cart> getMyCart() {
        Long userId = jwtUtil.getCurrentUserId();
        return cartService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get cart by ID",
            description = "Retrieve a cart by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart", content = @Content(schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @ErrorResponse
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(
            @Parameter(description = "ID of the cart to be retrieved")
            @PathVariable("id") Long id) {
        return cartRepository.existsById(id)
                ? ResponseEntity.ok(cartService.getCartById(id))
                : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Create a new cart",
            description = "Create a new cart for the current user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully created cart", content = @Content(schema = @Schema(implementation = Cart.class)))
    @ErrorResponse
    @PostMapping
    public ResponseEntity<Cart> createNewCart(
            @Parameter(description = "Cart object to be created")
            @Valid @RequestBody Cart cart) {
        return ResponseEntity.ok().body(cartService.create(cart));
    }

    @Operation(
            summary = "Delete a cart by ID",
            description = "Delete a cart belonging to the current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found or does not belong to current user")
    })
    @ErrorResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @Parameter(description = "ID of the cart to be deleted")
            @Valid @PathVariable("id") Long id) {
        if (!cartService.existsByIdAndUserId(id, jwtUtil.getCurrentUserId())) {
            return ResponseEntity.notFound().build();
        }
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}