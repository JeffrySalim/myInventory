package com.project.myinventory.controller;

import com.project.myinventory.dto.order.OrderRequestDTO;
import com.project.myinventory.dto.order.OrderResponseDTO;
import com.project.myinventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/orders")
    public ResponseEntity<Map<String, String>> checkout(@Valid @RequestBody OrderRequestDTO request, Principal principal) {
        orderService.checkout(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Pesanan berhasil dibuat, silakan lanjutkan pembayaran."));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getMyOrders(@PageableDefault Pageable pageable, Principal principal) {
        return ResponseEntity.ok(orderService.getUserOrders(principal.getName(), pageable));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getMyOrderDetail(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(orderService.getUserOrderDetail(id, principal.getName()));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelOrder(@PathVariable Long id, Principal principal) {
        orderService.cancelOrder(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Pesanan berhasil dibatalkan. Stok telah dikembalikan."));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrdersAdmin(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderDetailAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDetailAdmin(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/orders/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelOrderAdmin(@PathVariable Long id) {
        orderService.cancelOrderAdmin(id);
        return ResponseEntity.ok(Map.of("message", "Pesanan berhasil dibatalkan secara manual oleh Admin. Stok telah dikembalikan."));
    }

}
