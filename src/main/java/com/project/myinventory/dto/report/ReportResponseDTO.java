package com.project.myinventory.dto.report;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReportResponseDTO {

    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;

    private List<ReportStockResponseDTO> lowStockProduct;
    private List<ReportProductSellResponseDTO> topSellingProduct;
}
