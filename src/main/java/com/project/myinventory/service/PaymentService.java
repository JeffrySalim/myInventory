package com.project.myinventory.service;

import com.project.myinventory.entity.Order;
import com.project.myinventory.entity.OrderItem;
import com.project.myinventory.entity.Payment;
import com.project.myinventory.entity.enums.OrderStatus;
import com.project.myinventory.entity.enums.PaymentStatus;
import com.project.myinventory.exception.BadRequestException;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.exception.UnauthorizedException;
import com.project.myinventory.repository.OrderRepository;
import com.project.myinventory.repository.PaymentRepository;
import com.project.myinventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void uploadProof(Long orderId, MultipartFile file, String username) throws IOException {
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Akses ditolak");
        }

        Payment payment = paymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment tidak ditemukan"));

        if (payment.getPaymentStatus() != PaymentStatus.UNPAID) {
            throw new BadRequestException("Tagihan sudah dibayar atau sedang diverifikasi");
        }

        String fileName = fileStorageService.storeFile(file, "payment_proof");
        payment.setPaymentProof(fileName);
        payment.setPaymentStatus(PaymentStatus.NEED_VERIFY);

        order.setOrderStatus(OrderStatus.PROCESSING);
    }

    @Transactional
    public void approvePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment tidak ditemukan"));
        Order order = payment.getOrder();

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setFinishedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETED);
    }

    @Transactional
    public void rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment tidak ditemukan"));
        Order order = payment.getOrder();

        payment.setPaymentStatus(PaymentStatus.REJECTED);
        payment.setFinishedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getOrderItems()) {
            productRepository.addStock(item.getProduct().getProductId(), item.getQuantity());
        }
    }
}
