package com.project.myinventory.repository;

import com.project.myinventory.dto.report.ProductSellProjection;
import com.project.myinventory.dto.report.ReportProductSellResponseDTO;
import com.project.myinventory.entity.OrderItem;
import com.project.myinventory.entity.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT p.productId AS productId, p.productName AS productName, p.sku AS sku, SUM(oi.quantity) AS totalQuantitySold " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "JOIN oi.product p " +
            "WHERE o.orderStatus = :status AND o.deletedAt IS NULL AND p.deletedAt IS NULL " +
            "GROUP BY p.productId, p.productName, p.sku " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<ProductSellProjection> findTopSellingProducts(@Param("status") OrderStatus status, Pageable pageable);
}
