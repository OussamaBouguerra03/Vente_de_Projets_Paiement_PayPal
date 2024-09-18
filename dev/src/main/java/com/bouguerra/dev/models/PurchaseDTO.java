package com.bouguerra.dev.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private Long id;
    private Long customerId;
    private Long projectId;
    private LocalDateTime purchaseDate;
    private Double amount;
    private String paymentId;
}
