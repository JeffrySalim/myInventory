package com.project.myinventory.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "Nama Ketegori harus diisi")
    private String categoryName;

    @NotBlank(message = "Deskripsi harus diisi")
    private String description;
}
