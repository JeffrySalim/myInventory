package com.project.myinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sku", unique = true)
    @NotBlank(message = "SKU harus diisi")
    private String sku;

    @NotBlank(message = "Nama Product harus diisi")
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_image")
    private String productImage;

    @NotBlank(message = "Deskripsi Product harus diisi")
    @Column(name = "product_description")
    private String productDescription;

    @NotNull(message = "Harga Product harus diisi")
    @Positive(message = "Harga harus lebih dari nol")
    @Column(name = "price", precision = 15, scale = 0)
    private BigDecimal price;

    @Min(value = 0, message = "Stock tidak boleh minus")
    @NotNull(message = "Stock harus diisi")
    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Kategori tidak boleh kosong")
    private Category category;

}
