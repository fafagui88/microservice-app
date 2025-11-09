package com.project4.transactions.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import java.util.*;
import com.project4.transactions.repository.TransactionsRepository;
import com.project4.transactions.model.Transactions;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsRepository repo;
    @LoadBalanced
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transactions tx) {
        // GET user
        var userResp = restTemplate.getForObject("http://users:8081/api/users/" + tx.getUserId(), Map.class);
        if (userResp == null || userResp.get("saldo") == null)
            return ResponseEntity.badRequest().body("User not found");

        Double saldo = Double.valueOf(userResp.get("saldo").toString());
        if (saldo < tx.getPrice())
            return ResponseEntity.badRequest().body("Saldo tidak cukup!");

        Double remaining = saldo - tx.getPrice();

        // update saldo
        restTemplate.put("http://users:8081/api/users/" + tx.getUserId() + "/saldo?saldo=" + remaining, null);

        // simpan transaksi
        Transactions saved = repo.save(tx);

        // catat ke histories
        Map<String, Object> history = new HashMap<>();
        history.put("userId", tx.getUserId());
        history.put("nameUser", userResp.get("name"));
        history.put("product", tx.getProduct());
        history.put("price", tx.getPrice());
        history.put("saldo", saldo);
        history.put("remainingBalance", remaining);

        restTemplate.postForObject("http://histories:8087/api/histories", history, Map.class);

        return ResponseEntity.ok(saved);
    }
}
