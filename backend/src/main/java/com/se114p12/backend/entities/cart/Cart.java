package com.se114p12.backend.entities.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}