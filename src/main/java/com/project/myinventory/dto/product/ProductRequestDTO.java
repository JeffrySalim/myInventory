package com.project.myinventory.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "SKU harus diisi")
    private String sku;

    @NotBlank(message = "Nama Product harus diisi")
    private String productName;

    @NotBlank(message = "Deskripsi Product harus diisi")
    private String productDescription;

    @NotNull(message = "Harga Product harus diisi")
    @Positive(message = "Harga harus lebih dari nol")
    private BigDecimal price;

    @NotNull(message = "Stock harus diisi")
    @Min(value = 0, message = "Stock tidak boleh minus")
    private Integer stock;

    @NotNull(message = "Kategori tidak boleh kosong")
    private Long categoryId;
}
