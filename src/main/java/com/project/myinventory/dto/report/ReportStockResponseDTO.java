package com.project.myinventory.dto.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportStockResponseDTO {

    private Long productId;
    private String productName;
    private String sku;
    private Integer currentStock;
}
