package com.project.myinventory.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {

    @NotNull(message = "Produk ID hasus diisi")
    private Long productId;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 1, message = "Minimal pembelian 1 barang")
    private Integer quantity;
}
