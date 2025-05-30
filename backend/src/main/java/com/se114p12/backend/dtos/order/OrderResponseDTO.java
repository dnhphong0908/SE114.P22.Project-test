package com.se114p12.backend.dtos.order;

import com.se114p12.backend.dtos.BaseResponseDTO;
import com.se114p12.backend.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponseDTO extends BaseResponseDTO {
  private String shippingAddress;
  private BigDecimal totalPrice;
  private String note;
  private Instant expectedDeliveryTime;
  private Instant actualDeliveryTime;
  private OrderStatus orderStatus;
  private Long userId;
  private Long shipperId;
  private List<OrderDetailResponseDTO> orderDetails;
}
