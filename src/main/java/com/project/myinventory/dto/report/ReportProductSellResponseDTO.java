package com.project.myinventory.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportProductSellResponseDTO {

    private Long productId;
    private String productName;
    private String sku;
    private Long totalQuantitySold;
}
