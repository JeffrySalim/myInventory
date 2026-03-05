package com.project.myinventory.entity;

import com.project.myinventory.entity.enums.PaymentMethod;
import com.project.myinventory.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="payment_id")
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name ="payment_number", unique = true)
    private String paymentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name ="payment_method")
    private PaymentMethod paymentMethod;

    @Column(name ="payment_date")
    private LocalDateTime paymentDate;

    @Column(name ="payment_proof")
    private String paymentProof;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

}
