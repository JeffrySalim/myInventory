package com.project.myinventory.controller;

import com.project.myinventory.dto.product.ProductRequestDTO;
import com.project.myinventory.dto.product.ProductResponseDTO;
import com.project.myinventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDTO>> getAvailableProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable, false));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProductDetail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsAdmin(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable, true));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @ModelAttribute("data") @Valid ProductRequestDTO requestDTO,
            @RequestPart("image") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestDTO, file));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Produk berhasil dihapus"));
    }
}
