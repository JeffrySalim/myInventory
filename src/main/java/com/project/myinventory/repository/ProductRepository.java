package com.project.myinventory.repository;

import com.project.myinventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Admin
    Page<Product> findAllByDeletedAtIsNull(Pageable pageable);

    // User
    Page<Product> findAllByDeletedAtIsNullAndStockGreaterThan(Integer stock, Pageable pageable);

//    Page<Product> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Product> findByProductIdAndDeletedAtIsNull(Long productId);

    // Query kurangin stock
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :qty WHERE p.productId = :id AND p.stock >= :qty AND p.deletedAt IS NULL")
    int deductStock(@Param("id") Long productId, @Param("qty") Integer quantity);

    // Query kembali stock jika order dibatalkan
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.productId = :productId")
    void addStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    long countByDeletedAtIsNull();
    List<Product> findByStockLessThanAndDeletedAtIsNull(Integer stockThreshold);


}
