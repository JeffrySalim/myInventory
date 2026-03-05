package com.project.myinventory.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponseDTO {

    private Long orderItemId;
    private Long productId;
    private String sku;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
