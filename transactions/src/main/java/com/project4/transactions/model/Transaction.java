package com.project4.transactions.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String phoneNumber;
    private Double amount;
    private String transactionId; // id dari Reloadly
    private String status;
    @Column(columnDefinition = "TEXT")
    private String apiResponse;
    private LocalDateTime createdAt = LocalDateTime.now();
}
