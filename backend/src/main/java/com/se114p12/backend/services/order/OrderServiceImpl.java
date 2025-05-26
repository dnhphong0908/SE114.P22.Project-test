package com.se114p12.backend.services.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.entities.order.OrderDetail;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.dtos.order.OrderRequestDTO;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.exceptions.BadRequestException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.order.OrderMapper;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.cart.CartItemRepository;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(specification, pageable);
        return PageVO.<OrderResponseDTO>builder()
                .content(orders.getContent().stream().map(orderMapper::entityToResponseDTO).toList())
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .numberOfElements(orders.getNumberOfElements())
                .page(orders.getNumber())
                .size(orders.getSize())
                .build();
    }

    @Override
    public OrderResponseDTO getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.entityToResponseDTO(order);
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Long currentUserId = jwtUtil.getCurrentUserId();
        Cart cart = cartRepository.findByUserId(currentUserId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Order order = new Order();
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setNote(orderRequestDTO.getNote());
        order.setUser(userRepository.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("User not found")));
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderDetails(new ArrayList<>());
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> orderDetails;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductImage(product.getImageUrl());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setVariationInfo(extractVariationInfo(cartItem));
            orderDetail.setPrice(cartItem.getPrice());
            totalPrice = totalPrice.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            order.getOrderDetails().add(orderDetail);

            // delete cart item
            cartItemRepository.delete(cartItem);
        }

        order.setTotalPrice(totalPrice);
        return orderMapper.entityToResponseDTO(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (orderRequestDTO.getShippingAddress() != null) {
            order.setShippingAddress(orderRequestDTO.getShippingAddress());
        }
        if (orderRequestDTO.getNote() != null) {
            order.setNote(orderRequestDTO.getNote());
        }
        if (orderRequestDTO.getPaymentMethod() != null) {
            order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        }

        return orderMapper.entityToResponseDTO(orderRepository.save(order));
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getOrderStatus().getCode() > 1) {
            throw new BadRequestException("Can't cancel order because order is confirm");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    private String extractVariationInfo(CartItem cartItem) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> variations = cartItem.getVariationOptions().stream()
                .map(variationOption -> Map.of(variationOption.getVariation().getName(), variationOption.getValue()))
                .toList();
        try {
            return objectMapper.writeValueAsString(variations);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing variation info", e);
        }
    }
}
