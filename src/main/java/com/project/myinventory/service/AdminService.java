package com.project.myinventory.service;

import com.project.myinventory.entity.Categories;
import com.project.myinventory.entity.Products;
import com.project.myinventory.exception.BadRequestException;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.repository.CategoryRepository;
import com.project.myinventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    public Products addProduct(Products product) {
        Categories category = categoryRepository.findById(product.getCategories().getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori tidak ditemukan"));
        product.setCategories(category);
        return productRepository.save(product);
    }

    public Products updateProduct(Long id,Products productDetails) {
        Products product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produk tidak ditemukan"));

        if (productDetails.getCategories() != null){
            Categories categories = categoryRepository.findById(productDetails.getCategories().getCategory_id())
                    .orElseThrow(()-> new ResourceNotFoundException("Kategori tidak ditemukan"));
            product.setCategories(categories);
        }

        if (product.getDeletedAt()!=null){
            throw new BadRequestException("Produk sudah dihapus");
        }

        product.setSku(productDetails.getSku());
        product.setProduct_name(productDetails.getProduct_name());
        product.setProduct_description(productDetails.getProduct_description());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Products products = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Produk tidak ditemukan"));

        if (products.getDeletedAt()!=null){
            throw new BadRequestException("Produk sudah dihapus");
        }

        products.setDeletedAt(LocalDateTime.now());

        productRepository.deleteById(id);
    }


}
