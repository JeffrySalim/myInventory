package com.project.myinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price", precision = 15, scale = 0)
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "subtotal", precision = 15, scale = 0)
    private BigDecimal subtotal;

}
