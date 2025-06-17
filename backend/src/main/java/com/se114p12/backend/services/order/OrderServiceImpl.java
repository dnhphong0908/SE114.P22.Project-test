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
import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.exceptions.BadRequestException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.order.OrderMapper;
import com.se114p12.backend.neo4j.entities.BoughtWithRelationship;
import com.se114p12.backend.neo4j.entities.OrderedRelationship;
import com.se114p12.backend.neo4j.entities.ProductNode;
import com.se114p12.backend.neo4j.entities.UserNode;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import com.se114p12.backend.neo4j.repositories.UserNeo4jRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.cart.CartItemRepository;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.repositories.order.OrderDetailRepository;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.repositories.shipper.ShipperRepository;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShipperRepository shipperRepository;
    private final ProductNeo4jRepository productNeo4jRepository;
    private final UserNeo4jRepository userNeo4jRepository;

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
    public PageVO<OrderResponseDTO> getOrdersByUserId(Long userId, Specification<Order> specification, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUser_Id(userId, specification, pageable);
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
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Long currentUserId = jwtUtil.getCurrentUserId();
        Cart cart = cartRepository.findByUserId(currentUserId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setNote(orderRequestDTO.getNote());
        order.setUser(userRepository.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("User not found")));

        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        order.setOrderDetails(new ArrayList<>());
        order.setActualDeliveryTime(null); // sẽ được cập nhật bởi khách

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

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

        List<Shipper> availableShippers = shipperRepository.findByIsAvailableTrue();
        if (!availableShippers.isEmpty()) {
            Shipper selectedShipper = availableShippers.get(new Random().nextInt(availableShippers.size()));
            selectedShipper.setIsAvailable(false);
            order.setShipper(selectedShipper);
        }

        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);
        cartRepository.delete(cart);

        // Neo4j recommendation system

        // 1. User ordered products
        UserNode userNode = userNeo4jRepository.findById(currentUserId)
                .orElseGet(() -> {
                    UserNode newUser = new UserNode();
                    newUser.setId(currentUserId);
                    newUser.setOrderedProducts(new ArrayList<>());
                    return newUser;
                });

        Map<Long, OrderedRelationship> orderedMap = userNode.getOrderedProducts().stream()
                .collect(Collectors.toMap(rel -> rel.getProductNode().getId(), rel -> rel));
        Map<Long, ProductNode> productNodeMap = new HashMap<>();

        for (OrderDetail detail : order.getOrderDetails()) {
            Long productId = detail.getProductId();
            ProductNode productNode = productNeo4jRepository.findById(productId).orElseGet( ()-> {
                    ProductNode newProductNode = new ProductNode();
                    newProductNode.setId(productId);
                    return productNeo4jRepository.save(newProductNode);
                }
            );
            productNodeMap.put(productId, productNode);

            OrderedRelationship rel = orderedMap.get(productId);
            if (rel != null) {
                rel.setCount(rel.getCount() + detail.getQuantity());
            } else {
                OrderedRelationship newRel = new OrderedRelationship();
                newRel.setProductNode(productNode);
                newRel.setCount(detail.getQuantity());
                userNode.getOrderedProducts().add(newRel);
            }
        }
        userNeo4jRepository.save(userNode);

        // 2.bought with relationship
        List<ProductNode> productNodes = new ArrayList<>(productNodeMap.values());
        int n = productNodes.size();
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j<n; j++) {
                ProductNode p1 = productNodes.get(i);
                ProductNode p2 = productNodes.get(j);

                upsertBoughtWith(p1, p2);
                upsertBoughtWith(p2, p1);
            }
        }
        productNeo4jRepository.saveAll(productNodes);
        
        return orderMapper.entityToResponseDTO(order);
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

    @Override
    @Transactional
    public void markOrderAsDelivered(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        if (!order.getOrderStatus().equals(OrderStatus.SHIPPING)) {
            throw new IllegalStateException("Order must be in SHIPPING status to mark as delivered.");
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setActualDeliveryTime(Instant.now());

        // Cập nhật shipper: đánh dấu là available
        if (order.getShipper() != null) {
            Shipper shipper = order.getShipper();
            shipper.setIsAvailable(true);
            shipperRepository.save(shipper);
        }

        orderRepository.save(order);
    }

    private void upsertBoughtWith(ProductNode source, ProductNode target) {
        for (BoughtWithRelationship rel : source.getCoPurchasedProducts()) {
            if (rel.getProduct().getId().equals(target.getId())) {
                rel.setCount(rel.getCount() + 1);
                return;
            }
        }
        BoughtWithRelationship newRel = new BoughtWithRelationship();
        newRel.setProduct(target);
        newRel.setCount(1L);
        source.getCoPurchasedProducts().add(newRel);
    }
}
