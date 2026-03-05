package com.project.myinventory.service;

import com.project.myinventory.dto.order.OrderItemRequestDTO;
import com.project.myinventory.dto.order.OrderItemResponseDTO;
import com.project.myinventory.dto.order.OrderRequestDTO;
import com.project.myinventory.dto.order.OrderResponseDTO;
import com.project.myinventory.dto.payment.PaymentResponseDTO;
import com.project.myinventory.entity.*;
import com.project.myinventory.entity.enums.OrderStatus;
import com.project.myinventory.entity.enums.PaymentMethod;
import com.project.myinventory.entity.enums.PaymentStatus;
import com.project.myinventory.exception.BadRequestException;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.exception.UnauthorizedException;
import com.project.myinventory.repository.OrderRepository;
import com.project.myinventory.repository.PaymentRepository;
import com.project.myinventory.repository.ProductRepository;
import com.project.myinventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Transactional
    public void checkout(OrderRequestDTO requestDTO, String username){
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(()-> new ResourceNotFoundException("User tidak ditemukan"));

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Order order = new Order();
        order.setUser(user);
        order.setInvoiceNumber("INV-" + date + "-" + System.currentTimeMillis());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        BigDecimal totalOrder = BigDecimal.ZERO;
        List<OrderItem> itemList = new ArrayList<>();

        for (OrderItemRequestDTO itemRequestDTO : requestDTO.getItemRequestDTO()){
            Product product = productRepository.findByProductIdAndDeletedAtIsNull(itemRequestDTO.getProductId())
                    .orElseThrow(()-> new ResourceNotFoundException("Produk Id " + itemRequestDTO.getProductId()+ " tidak ditemukan"));

            int updated = productRepository.deductStock(product.getProductId(), itemRequestDTO.getQuantity());
            if (updated == 0){
                throw new BadRequestException("Stock tidak cukup untuk produk : " + product.getProductName());
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequestDTO.getQuantity()));
            totalOrder = totalOrder.add(subtotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setSku(product.getSku());
            orderItem.setProductName(product.getProductName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemRequestDTO.getQuantity());
            orderItem.setSubtotal(subtotal);

            itemList.add(orderItem);
        }
        order.setOrderTotal(totalOrder);
        order.setOrderItems(itemList);
        orderRepository.save(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentNumber("PAY-" + date + "-"+ System.currentTimeMillis());
        payment.setPaymentMethod(PaymentMethod.valueOf(requestDTO.getPaymentMethod().toUpperCase()));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        paymentRepository.save(payment);
    }

    @Transactional
    public void cancelOrder(Long orderId, String username){
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order tidak ditemukan"));

        if (!order.getUser().getUsername().equals(username)){
            throw new UnauthorizedException("Bukan pesanan anda");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING){
            throw new BadRequestException("Hanya pesanan PENDING yang bisa dibatalkan");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        Payment payment = paymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Tagihan tidak ditemukan"));
        payment.setPaymentStatus(PaymentStatus.REJECTED);
        payment.setFinishedAt(LocalDateTime.now());

        for (OrderItem orderItem : order.getOrderItems()){
            productRepository.addStock(orderItem.getProduct().getProductId(), orderItem.getQuantity());
        }
    }

    @Transactional
    public void cancelOrderAdmin(Long orderId) {
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan"));

        if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Pesanan sudah selesai atau sudah dibatalkan sebelumnya.");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        paymentRepository.findByOrderOrderId(orderId).ifPresent(payment -> {
            payment.setPaymentStatus(PaymentStatus.REJECTED);
            payment.setFinishedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        });

        for (OrderItem item : order.getOrderItems()) {
            productRepository.addStock(item.getProduct().getProductId(), item.getQuantity());
        }

        orderRepository.save(order);
        log.info("Admin membatalkan pesanan {} secara manual dan stok telah dikembalikan.", order.getInvoiceNumber());
    }

    // Get Order untuk user
    public Page<OrderResponseDTO> getUserOrders(String username, Pageable pageable){
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(()-> new ResourceNotFoundException("User tidak ditemukan"));
        return orderRepository.findAllByUserUserIdAndDeletedAtIsNull(user.getUserId(), pageable)
                .map(this::mapToOrderResponseDTO);

    }

    public OrderResponseDTO getUserOrderDetail(Long orderId, String username){
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order tidak ditemukan"));

        if (!order.getUser().getUsername().equals(username)){
            throw new UnauthorizedException("Akses ditolak, Bukan pesanan anda");
        }
        return mapToOrderResponseDTO(order);
    }

    // Get Order untuk admin
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByDeletedAtIsNull(pageable)
                .map(this::mapToOrderResponseDTO);
    }

    public OrderResponseDTO getOrderDetailAdmin(Long orderId) {
        Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order tidak ditemukan"));

        return mapToOrderResponseDTO(order);
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order){
        List<OrderItemResponseDTO> itemResponseDTOS = order.getOrderItems().stream().map(item->
                OrderItemResponseDTO.builder()
                        .orderItemId(item.getOrderItemId())
                        .productId(item.getProduct().getProductId())
                        .sku(item.getSku())
                        .productName(item.getProductName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build()
        ).collect(Collectors.toList());
        Payment payment = paymentRepository.findByOrderOrderId(order.getOrderId()).orElse(null);
        PaymentResponseDTO paymentDTO = null;

        if (payment != null){
            String proofUrl = payment.getPaymentProof() != null ? baseUrl + "/uploads/payment_proof/" + payment.getPaymentProof() : null;

            paymentDTO = PaymentResponseDTO.builder()
                    .paymentId(payment.getPaymentId())
                    .paymentNumber(payment.getPaymentNumber())
                    .paymentMethod(payment.getPaymentMethod().name())
                    .paymentDate(payment.getPaymentDate())
                    .paymentProof(proofUrl)
                    .paymentStatus(payment.getPaymentStatus().name())
                    .finishedAt(payment.getFinishedAt())
                    .build();
        }

        boolean isOverdue = false;
        String overdueMessage = null;

        if (order.getOrderStatus() == OrderStatus.PENDING &&
                LocalDateTime.now().isAfter(order.getOrderDate().plusHours(24))) {

            isOverdue = true;
            overdueMessage = "Peringatan: Belum dibayar melewati batas 24 Jam!";
        }

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .invoiceNumber(order.getInvoiceNumber())
                .username(order.getUser().getFullname())
                .orderTotal(order.getOrderTotal())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus().name())
                .isOverdue(isOverdue)
                .overdueMessage(overdueMessage)
                .itemResponseDTO(itemResponseDTOS)
                .paymentResponseDTO(paymentDTO)
                .build();
    }
}
