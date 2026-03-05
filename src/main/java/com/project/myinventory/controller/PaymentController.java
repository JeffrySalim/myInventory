package com.project.myinventory.controller;

import com.project.myinventory.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/payments/{orderId}/upload-proof")
    public ResponseEntity<Map<String, String>> uploadPaymentProof(
            @PathVariable Long orderId,
            @RequestParam("file") MultipartFile file,
            Principal principal) throws IOException {

        paymentService.uploadProof(orderId, file, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Bukti pembayaran berhasil diunggah. Menunggu verifikasi admin."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/payments/{id}/approve")
    public ResponseEntity<Map<String, String>> approvePayment(@PathVariable Long id) {
        paymentService.approvePayment(id);
        return ResponseEntity.ok(Map.of("message", "Pembayaran telah berhasil di-APPROVE"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/payments/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectPayment(@PathVariable Long id) {
        paymentService.rejectPayment(id);
        return ResponseEntity.ok(Map.of("message", "Pembayaran telah di-REJECT dan stok dikembalikan"));
    }

}
