package com.project.myinventory.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {

    private Long categoryId;
    private String categoryName;
    private String description;
}
