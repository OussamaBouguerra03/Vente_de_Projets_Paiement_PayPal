package com.bouguerra.dev.dto;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long amount;
    private String email;
}
