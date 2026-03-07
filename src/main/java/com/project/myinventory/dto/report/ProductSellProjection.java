package com.project.myinventory.dto.report;

public interface ProductSellProjection {
    Long getProductId();
    String getProductName();
    String getSku();
    Long getTotalQuantitySold();
}
