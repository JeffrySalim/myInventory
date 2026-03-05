package com.project.myinventory.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Metode pembayaran CASH atau TRANSFER")
    private String paymentMethod;

    @NotEmpty(message = "Order tidak boleh kosong")
    @Valid
    private List<OrderItemRequestDTO> itemRequestDTO;
}
