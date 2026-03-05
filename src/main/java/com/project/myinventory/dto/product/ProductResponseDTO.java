package com.project.myinventory.dto.product;

import com.project.myinventory.dto.category.CategoryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponseDTO {
    private Long productId;
    private String sku;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private CategoryResponseDTO categoryResponseDTO;
}
