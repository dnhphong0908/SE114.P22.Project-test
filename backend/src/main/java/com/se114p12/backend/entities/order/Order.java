package com.se114p12.backend.entities.order;

import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.enums.PaymentMethod;
import com.se114p12.backend.enums.PaymentStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @NotNull
    private Double destinationLatitude;

    @NotNull
    private Double destinationLongitude;

    @NotNull
    private String shippingAddress;

    private BigDecimal totalPrice;

    private String note;

    private Instant expectedDeliveryTime;

    @Nullable
    private Instant actualDeliveryTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @NotNull
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Shipper shipper;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}