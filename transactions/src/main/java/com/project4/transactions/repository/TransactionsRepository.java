package com.project4.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project4.transactions.model.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> { }