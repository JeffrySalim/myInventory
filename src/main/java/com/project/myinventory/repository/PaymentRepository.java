package com.project.myinventory.repository;

import com.project.myinventory.entity.Order;
import com.project.myinventory.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderOrderId(Long orderId);

    Optional<Payment> findByPaymentNumber(String paymentNumber);
}
