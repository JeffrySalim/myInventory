package com.project.myinventory.service;

import com.project.myinventory.dto.category.CategoryRequestDTO;
import com.project.myinventory.dto.category.CategoryResponseDTO;
import com.project.myinventory.entity.Category;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO){

        Category category = new Category();
        category.setCategoryName(requestDTO.getCategoryName());
        category.setDescription(requestDTO.getDescription());

        Category saved = categoryRepository.save(category);
        return mapToDTO(saved);
    }

    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable){
        return categoryRepository.findAllByDeletedAtIsNull(pageable).map(this::mapToDTO);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO){
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Kategori tidak ditemukan"));

        category.setCategoryName(requestDTO.getCategoryName());
        category.setDescription(requestDTO.getDescription());

        return mapToDTO(categoryRepository.save(category));
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findByCategoryIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new ResourceNotFoundException("Kategori tidak ditemukan"));
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    private CategoryResponseDTO mapToDTO(Category category){
        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}
