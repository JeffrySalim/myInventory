package com.project.myinventory.repository;

import com.project.myinventory.entity.Order;
import com.project.myinventory.entity.User;
import com.project.myinventory.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByDeletedAtIsNull(Pageable pageable);

    // Admin
    Page<Order> findAllByOrderStatusAndDeletedAtIsNull(OrderStatus status, Pageable pageable);

    // User
    Page<Order> findAllByUserUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);

    Optional<Order> findByOrderIdAndDeletedAtIsNull(Long orderId);

}
