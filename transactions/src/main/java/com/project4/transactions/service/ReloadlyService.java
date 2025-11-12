package com.project4.transactions.service;

import com.project4.transactions.dto.PulsaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

@Service
@Slf4j
public class ReloadlyService {

  private static final String TOKEN_URL = "https://auth.reloadly.com/oauth/token";
  private static final String TOPUP_URL = "https://topups.reloadly.com/topups";

  private final RestTemplate restTemplate = new RestTemplate();

  private String getAccessToken(String clientId, String clientSecret) {
      try {
          JSONObject requestBody = new JSONObject();
          requestBody.put("client_id", clientId);
          requestBody.put("client_secret", clientSecret);
          requestBody.put("grant_type", "client_credentials");
          requestBody.put("audience", "https://topups.reloadly.com");

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

          ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, entity, String.class);
          JSONObject json = new JSONObject(response.getBody());

          String token = json.getString("access_token");
          log.info("✅ Reloadly access token retrieved successfully.");
          return token;
      } catch (Exception e) {
          log.error("❌ Failed to get Reloadly token: {}", e.getMessage());
          throw new RuntimeException("Reloadly token request failed", e);
      }
  }

  public JSONObject topupPulsa(PulsaRequest request, String clientId, String clientSecret) {
      try {
          String token = getAccessToken(clientId, clientSecret);

          JSONObject payload = new JSONObject();
          payload.put("recipientPhone", request.getPhoneNumber());
          payload.put("operatorId", 174); // contoh operator ID
          payload.put("amount", request.getAmount());
          payload.put("useLocalAmount", true);

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          headers.setBearerAuth(token);

          HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);
          ResponseEntity<String> response = restTemplate.postForEntity(TOPUP_URL, entity, String.class);

          log.info("💰 Reloadly transaction success for phone {} amount {}", request.getPhoneNumber(), request.getAmount());
          return new JSONObject(response.getBody());

      } catch (Exception e) {
          log.error("❌ Reloadly transaction failed: {}", e.getMessage());
          JSONObject error = new JSONObject();
          error.put("status", "failed");
          error.put("message", e.getMessage());
          return error;
      }
  }
}
