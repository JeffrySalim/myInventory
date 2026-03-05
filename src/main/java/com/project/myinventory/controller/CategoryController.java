package com.project.myinventory.controller;

import com.project.myinventory.dto.category.CategoryRequestDTO;
import com.project.myinventory.dto.category.CategoryResponseDTO;
import com.project.myinventory.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
