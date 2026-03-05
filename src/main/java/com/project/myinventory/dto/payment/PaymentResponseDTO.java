package com.project.myinventory.dto.payment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponseDTO {

    private Long paymentId;
    private String paymentNumber;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String paymentProof;
    private String paymentStatus;
    private LocalDateTime finishedAt;
}
