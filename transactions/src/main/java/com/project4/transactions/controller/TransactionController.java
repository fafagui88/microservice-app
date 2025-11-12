package com.project4.transactions.controller;

import com.project4.transactions.dto.PulsaRequest;
import com.project4.transactions.dto.TransactionResponse;
import com.project4.transactions.dto.UsersDto;
import com.project4.transactions.client.UsersClient;
import com.project4.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Map;

// Jika kamu menggunakan RestTemplate ke Users service, inject client untuk ambil saldo
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final UsersClient usersClient; // contoh: Feign client atau implementasi manual

  @PostMapping("/pulsa")
  public ResponseEntity<?> topupPulsa(@RequestBody PulsaRequest request) {
      // 1. Ambil user (via UsersClient)
      UsersDto user = usersClient.getUser(request.getUserId()); // implementasikan UsersClient
      Double userSaldo = user.getSaldo();

      try {
          TransactionResponse resp = transactionService.topupPulsa(request, userSaldo);

          // Setelah sukses, harus kurangi saldo user via Users service (panggil endpoint update saldo)
          usersClient.updateSaldo(user.getId(), userSaldo - request.getAmount());

          return ResponseEntity.ok(resp);
      } catch (IllegalArgumentException e) {
          return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
      } catch (HttpClientErrorException e) {
          return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getResponseBodyAsString()));
      } catch (Exception e) {
          return ResponseEntity.status(500).body(Map.of("error", "Server error: " + e.getMessage()));
      }
  }
}
