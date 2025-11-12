package com.project4.transactions.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project4.transactions.dto.PulsaRequest;
import com.project4.transactions.dto.TransactionResponse;
import com.project4.transactions.model.Transaction;
import com.project4.transactions.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${reloadly.client-id}")
    private String clientId;

    @Value("${reloadly.client-secret}")
    private String clientSecret;

    @Value("${reloadly.auth-url:https://auth.reloadly.com/oauth/token}")
    private String authUrl;

    @Value("${reloadly.topups-url:https://topups.reloadly.com/topups}")
    private String topupsUrl;

    // =========================================================
    // 🔑 1. Ambil Access Token dari Reloadly API
    // =========================================================
    private String getAccessToken() {
        log.info("[RELOADLY] Requesting new access token...");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "grant_type", "client_credentials",
                "audience", "https://topups.reloadly.com"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> resp = restTemplate.postForEntity(authUrl, request, Map.class);
            Map<String, Object> map = resp.getBody();

            if (map != null && map.containsKey("access_token")) {
                String token = (String) map.get("access_token");
                log.info("[RELOADLY] Token retrieved successfully.");
                return token;
            } else {
                log.error("[RELOADLY] Invalid response while getting token: {}", map);
                throw new RestClientException("No access_token in Reloadly response");
            }
        } catch (RestClientException ex) {
            log.error("[RELOADLY] Failed to get token: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // =========================================================
    // ⚡ 2. Proses Top-Up Pulsa
    // =========================================================
    public TransactionResponse topupPulsa(PulsaRequest req, Double userSaldo) {
        log.info("[TOPUP] Starting transaction for user {} with phone {}", req.getUserId(), req.getPhoneNumber());

        // ✅ Step 1: Validasi saldo user
        if (userSaldo == null || userSaldo < req.getAmount()) {
            log.warn("[TOPUP] Saldo tidak cukup untuk user {}. Diperlukan: {}", req.getUserId(), req.getAmount());
            throw new IllegalArgumentException("Saldo tidak cukup");
        }

        // ✅ Step 2: Ambil token dari Reloadly
        String token = getAccessToken();

        if (token == null || token.isEmpty()) {
            log.error("[TOPUP] Gagal mendapatkan token dari Reloadly");
            throw new RuntimeException("Gagal mendapatkan token dari Reloadly");
        }

        // ✅ Step 3: Siapkan payload untuk API Reloadly
        Map<String, Object> recipientPhone = Map.of("countryCode", "ID", "number", req.getPhoneNumber());
        String customIdentifier = "TX-" + UUID.randomUUID();

        Map<String, Object> payload = Map.of(
                "recipientPhone", recipientPhone,
                "amount", req.getAmount(),
                "customIdentifier", customIdentifier
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        log.info("[TOPUP] Sending top-up request to Reloadly... {}", payload);

        // ✅ Step 4: Kirim ke API Reloadly dan tangani response
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(topupsUrl, requestEntity, Map.class);
            Map<String, Object> body = response.getBody();

            String status = body != null && body.get("status") != null ? body.get("status").toString() : "UNKNOWN";
            String txId = body != null && body.get("transactionId") != null ? body.get("transactionId").toString() : customIdentifier;
            String apiResp = body != null ? body.toString() : "";

            // ✅ Step 5: Simpan transaksi sukses ke database
            Transaction trx = new Transaction();
            trx.setUserId(req.getUserId());
            trx.setPhoneNumber(req.getPhoneNumber());
            trx.setAmount(req.getAmount());
            trx.setTransactionId(txId);
            trx.setStatus(status);
            trx.setApiResponse(apiResp);
            trx.setCreatedAt(LocalDateTime.now());
            repo.save(trx);

            log.info("[TOPUP] Transaction success: id={} status={}", txId, status);

            return new TransactionResponse(txId, status, "Transaksi berhasil diproses");

        } catch (HttpClientErrorException e) {
            log.error("[TOPUP] Reloadly API returned error: {}", e.getResponseBodyAsString());

            // ❌ Simpan error API ke database
            Transaction trxErr = new Transaction();
            trxErr.setUserId(req.getUserId());
            trxErr.setPhoneNumber(req.getPhoneNumber());
            trxErr.setAmount(req.getAmount());
            trxErr.setStatus("FAILED_API");
            trxErr.setApiResponse(e.getResponseBodyAsString());
            trxErr.setCreatedAt(LocalDateTime.now());
            repo.save(trxErr);

            throw e;

        } catch (ResourceAccessException e) {
            log.error("[TOPUP] Connection error to Reloadly: {}", e.getMessage(), e);
            throw new RuntimeException("Koneksi ke Reloadly gagal: " + e.getMessage(), e);

        } catch (RestClientException e) {
            log.error("[TOPUP] General REST client error: {}", e.getMessage(), e);
            throw new RuntimeException("Kesalahan umum pada REST client: " + e.getMessage(), e);

        } catch (Exception e) {
            log.error("[TOPUP] Unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Terjadi kesalahan tidak terduga: " + e.getMessage(), e);
        }
    }
}
