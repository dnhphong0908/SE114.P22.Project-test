package com.se114p12.backend.mappers.cart;

import com.se114p12.backend.dtos.cart.CartItemRequestDTO;
import com.se114p12.backend.dtos.cart.CartItemResponseDTO;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.variation.VariationOption;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.repositories.product.ProductRepository;
import com.se114p12.backend.repositories.variation.VariationOptionRepository;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cart", source = "cartId", qualifiedByName = "mapCart")
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapProduct")
    @Mapping(target = "variationOptions", source = "variationOptionIds", qualifiedByName = "mapVariationOptions")
    CartItem toEntity(CartItemRequestDTO dto,
                      @Context CartRepository cartRepo,
                      @Context ProductRepository productRepo,
                      @Context VariationOptionRepository variationRepo);

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "variationOptions", target = "variationOptionIds", qualifiedByName = "mapVariationOptionIds")
    CartItemResponseDTO toDTO(CartItem entity);

    @Named("mapCart")
    static Cart mapCart(Long cartId, @Context CartRepository cartRepo) {
        return cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found: " + cartId));
    }

    @Named("mapProduct")
    static Product mapProduct(Long productId, @Context ProductRepository productRepo) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }

    @Named("mapVariationOptions")
    static Set<VariationOption> mapVariationOptions(Set<Long> ids, @Context VariationOptionRepository repo) {
        return ids.stream()
                .map(id -> repo.findById(id)
                        .orElseThrow(() -> new RuntimeException("VariationOption not found: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("mapVariationOptionIds")
    static Set<Long> mapVariationOptionIds(Set<VariationOption> options) {
        return options.stream()
                .map(VariationOption::getId)
                .collect(Collectors.toSet());
    }
}