package com.project.myinventory.service;

import com.project.myinventory.dto.category.CategoryResponseDTO;
import com.project.myinventory.dto.product.ProductRequestDTO;
import com.project.myinventory.dto.product.ProductResponseDTO;
import com.project.myinventory.entity.Category;
import com.project.myinventory.entity.Product;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.repository.CategoryRepository;
import com.project.myinventory.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO, MultipartFile file) throws IOException {
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(requestDTO.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Kategori tidak ditemukan"));
        String filename = fileStorageService.storeFile(file, "product_image");
        Product product = new Product();
        product.setSku(requestDTO.getSku());
        product.setProductName(requestDTO.getProductName());
        product.setProductDescription(requestDTO.getProductDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setProductImage(filename);
        product.setCategory(category);

        return mapToDTO(productRepository.save(product));
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable, boolean isAdmin) {
        Page<Product> productPage;

        if (isAdmin) {
            productPage = productRepository.findAllByDeletedAtIsNull(pageable);
        } else {
            productPage = productRepository.findAllByDeletedAtIsNullAndStockGreaterThan(0, pageable);
        }
        return productPage.map(this::mapToDTO);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findByProductIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));

        return mapToDTO(product);
    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findByProductIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produk tidak ditemukan"));
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    private ProductResponseDTO mapToDTO(Product product){
        String imageUrl = product.getProductImage() != null ? baseUrl + "/uploads/product_image/" + product.getProductImage() : null;

        CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getCategoryName())
                .description(product.getCategory().getDescription())
                .build();

        return ProductResponseDTO.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(imageUrl)
                .categoryResponseDTO(categoryResponseDTO)
                .build();
    }

}
