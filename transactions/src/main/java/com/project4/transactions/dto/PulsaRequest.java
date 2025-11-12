package com.project4.transactions.dto;

import lombok.Data;

@Data
public class PulsaRequest {
    private Long userId;
    private String phoneNumber;
    private Double amount;
}
