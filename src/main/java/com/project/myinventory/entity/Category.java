package com.project.myinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name="categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NotBlank(message = "Nama Kategori harus diisi")
    @Column(name = "category_name")
    private String categoryName;

    @NotBlank(message = "Deskripsi harus diisi")
    @Column(name = "description")
    private String description;

}
