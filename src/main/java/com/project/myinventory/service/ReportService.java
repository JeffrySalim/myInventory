package com.project.myinventory.service;

import com.project.myinventory.dto.report.ReportProductSellResponseDTO;
import com.project.myinventory.dto.report.ReportResponseDTO;
import com.project.myinventory.dto.report.ReportStockResponseDTO;
import com.project.myinventory.entity.Order;
import com.project.myinventory.entity.enums.OrderStatus;
import com.project.myinventory.repository.OrderItemRepository;
import com.project.myinventory.repository.OrderRepository;
import com.project.myinventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ReportResponseDTO getReport() {
        // Total produk aktif
        long totalProducts = productRepository.countByDeletedAtIsNull();

        // Total pesanan
        long totalOrders = orderRepository.countByDeletedAtIsNull();

        // Total pendapatan dari pesanan COMPLETED
        List<Order> completedOrders = orderRepository.findAllByOrderStatusAndDeletedAtIsNull(OrderStatus.COMPLETED);
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getOrderTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Produk dengan Stok di bawah 10
        List<ReportStockResponseDTO> lowStockProducts = productRepository.findByStockLessThanAndDeletedAtIsNull(10)
                .stream()
                .map(p -> ReportStockResponseDTO.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .sku(p.getSku())
                        .currentStock(p.getStock())
                        .build())
                .collect(Collectors.toList());

        // Top Selling 5 Produk sering dibeli
        Pageable topSelling = PageRequest.of(0, 5);
        List<ReportProductSellResponseDTO> topSellingProducts = orderItemRepository.findTopSellingProducts(OrderStatus.COMPLETED, topSelling)
                .stream()
                .map(proj -> ReportProductSellResponseDTO.builder()
                        .productId(proj.getProductId())
                        .productName(proj.getProductName())
                        .sku(proj.getSku())
                        .totalQuantitySold(proj.getTotalQuantitySold())
                        .build())
                .collect(Collectors.toList());

            return ReportResponseDTO.builder()
                    .totalProducts(totalProducts)
                    .totalOrders(totalOrders)
                    .totalRevenue(totalRevenue)
                    .lowStockProduct(lowStockProducts)
                    .topSellingProduct(topSellingProducts)
                    .build();



    }
}
