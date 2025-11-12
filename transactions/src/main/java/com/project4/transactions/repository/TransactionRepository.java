package com.project4.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project4.transactions.model.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}
