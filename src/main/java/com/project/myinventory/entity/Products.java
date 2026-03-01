package com.project.myinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Products extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @Column(unique = true)
    @NotBlank(message = "SKU harus diisi")
    private String sku;

    @NotBlank(message = "Nama Product harus diisi")
    private String product_name;

    @NotBlank(message = "Nama Product harus diisi")
    private String product_description;

    @NotBlank(message = "Harga Product harus diisi")
    private double price;

    @Min(value = 0, message = "Stock tidak boleh minus")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categories;

}
