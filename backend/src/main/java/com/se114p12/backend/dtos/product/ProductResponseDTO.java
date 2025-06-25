package com.se114p12.backend.dtos.product;

import com.se114p12.backend.dtos.BaseResponseDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO extends BaseResponseDTO {
    private String name;
    private String shortDescription;
    private String detailDescription;
    private BigDecimal originalPrice;
    private String imageUrl;
    private Boolean isAvailable;
    private Long categoryId;
    private String categoryName;
}