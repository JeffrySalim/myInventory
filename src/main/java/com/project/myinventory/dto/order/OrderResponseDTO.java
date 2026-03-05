package com.project.myinventory.dto.order;

import com.project.myinventory.dto.payment.PaymentResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private String invoiceNumber;
    private String username;
    private BigDecimal orderTotal;
    private LocalDateTime orderDate;
    private String orderStatus;
    private boolean isOverdue;
    private String overdueMessage;
    private List<OrderItemResponseDTO> itemResponseDTO;
    private PaymentResponseDTO paymentResponseDTO;
}
